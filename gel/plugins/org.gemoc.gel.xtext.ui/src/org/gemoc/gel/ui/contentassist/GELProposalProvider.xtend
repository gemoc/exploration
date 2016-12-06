/*
 * generated by Xtext
 */
package org.gemoc.gel.ui.contentassist

import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.Assignment
import org.eclipse.xtext.EcoreUtil2
import org.eclipse.xtext.Keyword
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor
import org.gemoc.gel.AtomicDomainSpecificEvent
import org.gemoc.gel.EclEvent
import org.gemoc.gel.ExecutionFunctionResult
import org.gemoc.gel.FeedbackConsequence
import org.gemoc.gel.FeedbackPolicy
import org.gemoc.gel.UnfoldingStrategy
import org.gemoc.gel.utils.GELHelper

/**
 * see http://www.eclipse.org/Xtext/documentation.html#contentAssist on how to customize content assistant
 */
class GELProposalProvider extends AbstractGELProposalProvider {

	/**
	 * Suggests the owning Feedback Policy's owning Execution Function's result as an EObject to start a GExpression
	 */
	override completeGReferenceExpression_ReferencedEObject(EObject model, Assignment assignment,
		ContentAssistContext context, ICompletionProposalAcceptor acceptor) {

		if (EcoreUtil2.getContainerOfType(model, UnfoldingStrategy) != null) {

			// Expression inside an Instantiation Rule for the Unfolding Strategy of a Composite DSE.
			val strategy = EcoreUtil2.getContainerOfType(model, UnfoldingStrategy)
			for (localVariable : strategy.localVariables) {
				acceptor.accept(createCompletionProposal(localVariable.name, context))
			}
		} else if (EcoreUtil2.getContainerOfType(model, FeedbackPolicy) != null) {
			if (EcoreUtil2.getContainerOfType(model, FeedbackConsequence) != null) {

				// Expression to designate the MoccEvent
				val dse = EcoreUtil2.getContainerOfType(model, AtomicDomainSpecificEvent)
				val proposal = GELHelper.parseContextName(
					(dse.uponMoccEvent as EclEvent).eventReference.owningClassifierContextDecl)
				acceptor.accept(createCompletionProposal(proposal, context))
			} else {

				// Expression inside a Feedback Filter.
				val dse = EcoreUtil2.getContainerOfType(model, AtomicDomainSpecificEvent)
				val proposal = dse.executionFunction.result.name
				acceptor.accept(createCompletionProposal(proposal, context))
			}
		} else {

			// Expression to designate the EOperation
			val dse = EcoreUtil2.getContainerOfType(model, AtomicDomainSpecificEvent)
			val proposal = GELHelper.parseContextName(
				(dse.uponMoccEvent as EclEvent).eventReference.owningClassifierContextDecl)
			acceptor.accept(createCompletionProposal(proposal, context))
		}
	}

	/**
	 * Connects the GExpression's proposal provider for EObject with the ExecutionFunctionResult of GEL. 
	 */
	override protected void dealWithEObject(EObject eo, Keyword keyword, ContentAssistContext contentAssistContext,
		ICompletionProposalAcceptor acceptor) {
		if (eo instanceof ExecutionFunctionResult) {
			val result = eo as ExecutionFunctionResult
			dealWithClassifier(GELHelper.getTypeOfExecutionFunctionResult(result), keyword, contentAssistContext,
				acceptor)
		}
	}

}
