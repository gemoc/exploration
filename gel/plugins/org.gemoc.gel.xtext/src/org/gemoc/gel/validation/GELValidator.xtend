/*
 * generated by Xtext
 */
package org.gemoc.gel.validation

import fr.inria.aoste.timesquare.ecl.ecl.ECLDefCS
import java.util.ArrayList
import java.util.Collection
import java.util.List
import org.eclipse.emf.ecore.EClassifier
import org.eclipse.emf.ecore.EOperation
import org.eclipse.xtext.validation.Check
import org.gemoc.gel.CompositeDomainSpecificEvent
import org.gemoc.gel.DomainSpecificEvent
import org.gemoc.gel.DomainSpecificEventReference
import org.gemoc.gel.DomainSpecificEventReferenceWithArguments
import org.gemoc.gel.DomainSpecificEventsSpecification
import org.gemoc.gel.ExecutionFunctionResult
import org.gemoc.gel.FeedbackConsequence
import org.gemoc.gel.FeedbackFilter
import org.gemoc.gel.InstantiationPredicate
import org.gemoc.gel.Kermeta3ExecutionFunction
import org.gemoc.gel.LocalVariable
import org.gemoc.gel.UnaryPattern
import org.gemoc.gel.utils.CompositeDseArgumentCollector
import org.gemoc.gel.utils.CompositeDseDseReferencesCollector
import org.gemoc.gel.utils.GELHelper
import org.gemoc.gel.utils.GELPredicateValidator
import org.gemoc.gel.utils.LocalVariableArgumentsCollector
import org.gemoc.gexpressions.GNavigationExpression

//import org.eclipse.xtext.validation.Check
/**
 * Custom validation rules. 
 *
 * see http://www.eclipse.org/Xtext/documentation.html#validation
 */
class GELValidator extends AbstractGELValidator {

	public static val NAME_SHOULD_START_WITH_CAPITAL = 'name should start with capital'
	public static val NAME_SHOULD_NOT_START_WITH_CAPITAL = 'name should not start with capital'
	public static val MISSING_ECORE_AND_ECL_FILE = 'missing Ecore and ECL files'
	public static val MISSING_ECORE_FILE = 'missing Ecore file'
	public static val MISSING_ECL_FILE = 'missing ECL file'
	public static val FEEDBACK_FILTER_BODY_IS_NOT_PREDICATE = "FeedbackFilter body is not a predicate"
	public static val INSTANTIATION_RULE_BODY_IS_NOT_PREDICATE = "InstantiationRule body is not a predicate"
	public static val PATTERN_OF_COMPOSITE_WITH_UNFOLDING_STRATEGY_MUST_USE_LOCAL_VARIABLE = "Pattern of a Composite DSE with an unfolding strategy must precise the target of the referenced DSE(s)"
	public static val PATTERN_OF_COMPOSITE_WITH_UNFOLDING_STRATEGY_MUST_USE_ALL_LOCAL_VARIABLES = "Pattern of a Composite DSE with an unfolding strategy must use all the local variables declared"
	public static val TOO_MANY_ARGUMENTS = "There are too many arguments"
	public static val TOO_FEW_ARGUMENTS = "There are too few arguments"
	public static val WRONG_TYPES_ARGUMENTS = "The types of the arguments do not match the DSE's"

	@Check
	def checkExecutionFunctionResultStartsWithoutCapital(ExecutionFunctionResult result) {
		if (Character.isUpperCase(result.name.charAt(0))) {
			warning('Name should not start with a capital', result.eClass().getEStructuralFeature("name"),
				NAME_SHOULD_NOT_START_WITH_CAPITAL)
		}
	}

	@Check
	def checkDomainSpecificEventNameStartsWithCapital(DomainSpecificEvent dse) {
		if (Character.isLowerCase(dse.name.charAt(0))) {
			warning('Name of a Domain-Specific Event should start with a capital',
				dse.eClass().getEStructuralFeature("name"), NAME_SHOULD_START_WITH_CAPITAL);
		}
	}

	@Check
	def checkAtLeastOneEcoreAndOneEclFilesAreImported(DomainSpecificEventsSpecification dseSpecification) {
		var hasEcore = false;
		var hasEcl = false;

		for (import : dseSpecification.imports) {
			if (import.importURI.endsWith(".ecore")) {
				hasEcore = true;
			} else if (import.importURI.endsWith(".ecl")) {
				hasEcl = true;
			}
		}

		if (!hasEcore && !hasEcl) {
			warning('There should be at least one Ecore file and one ECL file in the import section',
				dseSpecification.eClass().getEStructuralFeature("imports"), MISSING_ECORE_AND_ECL_FILE);
		} else if (! hasEcore) {
			warning('There should be at least one imported Ecore file',
				dseSpecification.eClass().getEStructuralFeature("imports"), MISSING_ECORE_FILE);
		} else if (! hasEcl) {
			warning('There should be at least one imported ECL file',
				dseSpecification.eClass().getEStructuralFeature("imports"), MISSING_ECL_FILE);
		}
	}

	//	@Check
	//	def checkExecutionFunctionResultHasType(ExecutionFunctionResult result) {
	//		if (result.type == null) {
	//			if (result.eContainer instanceof Kermeta3ExecutionFunction) {
	//				val operation = (result.eContainer as Kermeta3ExecutionFunction).getEOperation
	//				if (operation.EType == null) {
	//					error("Trying to use feedback for an Execution Function, but its return type is Void", result,
	//						result.eClass.getEStructuralFeature("type"))
	//				} else {
	//					error("Could not infer the type of the Kermeta3ExecutionFunction", result,
	//						result.eClass.getEStructuralFeature("type"))
	//				}
	//			} else {
	//				warning("Referenced ExecutionFunction was not a Kermeta3ExecutionFunction", result,
	//					result.eClass.getEStructuralFeature("type"))
	//			}
	//		}
	//	}
	@Check
	def checkCompositeDseDefinitionIsNotCyclic(CompositeDomainSpecificEvent dse) {
		val feature = if (dse.body instanceof UnaryPattern) {
				dse.eClass.getEStructuralFeature("operand")
			} else {
				dse.eClass.getEStructuralFeature("leftOperand")
			}

		val arguments = (new CompositeDseArgumentCollector).doSwitch(dse.getBody())

		// Do not allow composite DSEs like e = e & a
		if (arguments.contains(dse)) {
			error("Cyclic definition of Composite DSE: " + dse.getName() + " is defined using itself", dse, feature)
		}

		// Do not allow composite DSEs like e = a & b with a = e & c
		arguments.filter(CompositeDomainSpecificEvent).forEach [ composite |
			val compositeArguments = (new CompositeDseArgumentCollector).doSwitch(composite.getBody())
			if (compositeArguments.contains(dse)) {
				error("Cyclic definition of Composite Domain-Specific Event detected", dse, feature)
			}
		]
	}

	@Check
	def checkCompositeDsePatternArgumentsAreWithinTheSameContext(CompositeDomainSpecificEvent dse) {
		if (dse.unfoldingStrategy == null) {
			val feature = if (dse.body instanceof UnaryPattern) {
					dse.eClass.getEStructuralFeature("operand")
				} else {
					dse.eClass.getEStructuralFeature("leftOperand")
				}
			val arguments = (new CompositeDseArgumentCollector).doSwitch(dse.getBody())
			val Collection<List<EClassifier>> contexts = new ArrayList<List<EClassifier>>()
			for (argument : arguments) {
				try {
					contexts.add(GELHelper.determineContextOfDomainSpecificEvent(argument))
				} catch (IllegalArgumentException e) {
					error("Cyclic definition of Composite DSE", dse, feature)
					return
				}
			}
			val firstContext = contexts.get(0)
			var areAllContextsTheSame = true
			for (context : contexts) {
				for (classifier : context) {
					areAllContextsTheSame = areAllContextsTheSame && (classifier.equals(
						firstContext.get(context.indexOf(classifier))) ||
						classifier.name.equals(firstContext.get(context.indexOf(classifier)).name))
				}
			}

			if (! areAllContextsTheSame) {
				error("All the arguments of the pattern of a Composite DSE should be defined within the same context",
					dse, feature)
			}

		}
	}

	//	@Check
	//	def checkMoccEventIsInTheSameContextAsExecutionFunction(AtomicDomainSpecificEvent dse) {
	//		val executionFunction = dse.triggersExecutionFunction
	//		val moccEvent = dse.uponMoccEvent
	//
	//		if (executionFunction instanceof Kermeta3ExecutionFunction) {
	//			if (moccEvent instanceof EclEvent) {
	//				val k3ef = executionFunction as Kermeta3ExecutionFunction
	//				val eclEvent = moccEvent as EclEvent
	//				val contextOfExecutionFunction = GELHelper.determineOperationOfExecutionFunction(k3ef).EContainingClass
	//				val nameOfContextOfEclEvent = GELHelper.parseContextName(eclEvent.eventReference.classifierContextDecl)
	//				if (!contextOfExecutionFunction.name.equals(nameOfContextOfEclEvent)) {
	//					error(
	//						"MoccEvent and Execution Function are not defined within the same context: " +
	//							nameOfContextOfEclEvent + " != " + contextOfExecutionFunction.name, dse,
	//						dse.eClass.getEStructuralFeature("uponMoccEvent"))
	//				}
	//			} else {
	//				error("Could not determine concrete type of the referenced MoccEvent", dse,
	//					dse.eClass.getEStructuralFeature("uponMoccEvent"))
	//			}
	//		} else {
	//			error("Could not determine concrete type of the referenced ExecutionFunction", dse,
	//				dse.eClass.getEStructuralFeature("triggersExecutionFunction"))
	//		}
	//	}
	//	@Check
	//	def checkFeedbackConsequenceIsInTheSameContextAsTheDse(AtomicDomainSpecificEvent dse) {
	//		val executionFunction = dse.executionFunction
	//
	//		if (executionFunction instanceof Kermeta3ExecutionFunction) {
	//			val k3ef = executionFunction as Kermeta3ExecutionFunction
	//			val contextOfExecutionFunction = GELHelper.determineOperationOfExecutionFunction(k3ef).EContainingClass
	//			val allRules = new ArrayList<FeedbackRule>()
	//			allRules.add(dse.feedbackPolicy.defaultRule)
	//			allRules.addAll(dse.feedbackPolicy.rules)
	//			for (rule : allRules) {
	//				val moccEvent = rule.consequence.target
	//				if (moccEvent instanceof EclEvent) {
	//					val eclEvent = moccEvent as EclEvent
	//					val nameOfContextOfEclEvent = GELHelper.parseContextName(
	//						eclEvent.eventReference.classifierContextDecl)
	//					if (!nameOfContextOfEclEvent.equals(contextOfExecutionFunction.name)) {
	//						error(
	//							"Target of FeedbackConsequence should be defined within the same context as the Execution Function of the owning DSE: " +
	//								nameOfContextOfEclEvent + " != " + contextOfExecutionFunction.name,
	//							rule.consequence,
	//							rule.consequence.eClass.getEStructuralFeature("target")
	//						)
	//					}
	//				} else {
	//					error("Could not determine concrete type of the referenced MoccEvent", dse,
	//						dse.eClass.getEStructuralFeature("moccEvent"))
	//				}
	//			}
	//		} else {
	//			error("Could not determine concrete type of the referenced ExecutionFunction", dse,
	//				dse.eClass.getEStructuralFeature("executionFunction"))
	//		}
	//	}
	@Check
	def checkFeedbackFilterIsPredicate(FeedbackFilter feedbackFilter) {
		if (!(new GELPredicateValidator()).doSwitch(feedbackFilter.body)) {
			warning("FeedbackFilter expression does not seem to lead to a boolean result", feedbackFilter,
				feedbackFilter.eClass.getEStructuralFeature("body"), FEEDBACK_FILTER_BODY_IS_NOT_PREDICATE)
		}
	}

	@Check
	def checkInstantiationPredicateBodyIsPredicate(InstantiationPredicate predicate) {
		if (!(new GELPredicateValidator()).doSwitch(predicate.body)) {
			warning("InstantiationRule expression does not seem to lead to a boolean result", predicate,
				predicate.eClass.getEStructuralFeature("body"), INSTANTIATION_RULE_BODY_IS_NOT_PREDICATE)
		}
	}

	@Check
	def checkPatternOfCompositeDseWithUnfoldingStrategyUsesLocalVariables(CompositeDomainSpecificEvent dse) {
		if (dse.unfoldingStrategy != null) {
			val Collection<DomainSpecificEventReference> dseReferencesInThePattern = (new CompositeDseDseReferencesCollector()).
				doSwitch(dse.body)
			for (DomainSpecificEventReference dseReference : dseReferencesInThePattern) {
				if (! (dseReference instanceof DomainSpecificEventReferenceWithArguments)) {
					error(
						"The pattern of a Composite DSE with an unfolding strategy should specify for which targets the DSE references must be applied",
						dse, dse.eClass.getEStructuralFeature("body"),
						PATTERN_OF_COMPOSITE_WITH_UNFOLDING_STRATEGY_MUST_USE_LOCAL_VARIABLE)
				}
			}
		}
	}

	@Check
	def checkPatternOfCompositeDseWithUnfoldingStrategyUsesAllLocalVariables(CompositeDomainSpecificEvent dse) {
		if (dse.unfoldingStrategy != null) {
			val localVariables = dse.unfoldingStrategy.localVariables
			val Collection<DomainSpecificEventReference> dseReferencesInThePattern = (new CompositeDseDseReferencesCollector()).
				doSwitch(dse.body)
			val usedLocalVariables = new ArrayList<LocalVariable>()
			for (DomainSpecificEventReference dseReference : dseReferencesInThePattern) {
				if (dseReference instanceof DomainSpecificEventReferenceWithArguments) {
					val dseReferenceWithTarget = dseReference as DomainSpecificEventReferenceWithArguments
					usedLocalVariables.addAll(
						(new LocalVariableArgumentsCollector()).doSwitch(dseReferenceWithTarget.arguments))
				}
			}

			var allLocalVariablesAreUsed = false
			if (localVariables.length <= (usedLocalVariables.length) &&
				localVariables.forall[localVar|usedLocalVariables.contains(localVar)]) {
				allLocalVariablesAreUsed = true;
			}

			if (! allLocalVariablesAreUsed) {
				error(
					"The pattern of a Composite DSE with an unfolding strategy must use all the local variables declared in the strategy",
					dse, dse.eClass.getEStructuralFeature("body"),
					PATTERN_OF_COMPOSITE_WITH_UNFOLDING_STRATEGY_MUST_USE_ALL_LOCAL_VARIABLES)

			}
		}
	}

	@Check
	def checkArgumentsUsedInDseReferenceWithArguments(
		DomainSpecificEventReferenceWithArguments dseReferenceWithArguments) {
		val List<EClassifier> context = GELHelper.
			determineContextOfDomainSpecificEvent(dseReferenceWithArguments.referencedDse)
		val List<LocalVariable> listOfArguments = (new LocalVariableArgumentsCollector()).doSwitch(
			dseReferenceWithArguments.arguments)
		if (context.length < listOfArguments.length) {
			error("There are too many arguments compared to the context of this DSE", dseReferenceWithArguments,
				dseReferenceWithArguments.eClass.getEStructuralFeature("arguments"), TOO_MANY_ARGUMENTS)
		} else if (context.length > listOfArguments.length) {
			error("There are too few arguments compared to the context of this DSE", dseReferenceWithArguments,
				dseReferenceWithArguments.eClass.getEStructuralFeature("arguments"), TOO_FEW_ARGUMENTS)
		} else {
			for (EClassifier classifier : context) {
				val LocalVariable localVariable = listOfArguments.get(context.indexOf(classifier))
				if (! localVariable.type.equals(classifier) || !localVariable.type.name.equals(classifier.name)) {
					error("The types of the arguments do not correspond to the types required by this DSE",
						dseReferenceWithArguments, dseReferenceWithArguments.eClass.getEStructuralFeature("arguments"),
						WRONG_TYPES_ARGUMENTS)

				}
			}
		}
	}

	@Check
	def checkNavigationPathToEOperationIsGNavigationExpressionToAnEOperation(Kermeta3ExecutionFunction k3ef) {
		if (!(k3ef.navigationPathToOperation instanceof GNavigationExpression)) {
			error("This GExpression must be a navigation path to an EOperation.", k3ef,
				k3ef.eClass.getEStructuralFeature("navigationPathToOperation"))
		} else {
			val navExp = k3ef.navigationPathToOperation as GNavigationExpression
			if (! (navExp.referencedEObject instanceof EOperation)) {
				error("This GExpression is a navigation path, but its ultimate element must be an EOperation.", k3ef,
					k3ef.eClass.getEStructuralFeature("navigationPathToOperation"))
			}
		}
	}

	@Check
	def checkNavigationPathToMoccEventIsGNavigationExpressionToAMoccEvent(FeedbackConsequence feedbackConsequence) {
		if (!(feedbackConsequence.navigationPathToMoccEvent instanceof GNavigationExpression)) {
			error("This GExpression must be a navigation path to a MoCC Event.", feedbackConsequence,
				feedbackConsequence.eClass.getEStructuralFeature("navigationPathToMoccEvent"))
		} else {
			val navExp = feedbackConsequence.navigationPathToMoccEvent as GNavigationExpression
			if (! (navExp.referencedEObject instanceof ECLDefCS)) {
				error("This GExpression is a navigation path, but its ultimate element must be an ECL Event.",
					feedbackConsequence,
					feedbackConsequence.eClass.getEStructuralFeature("navigationPathToMoccEvent"))
			}
		}
	}
}
