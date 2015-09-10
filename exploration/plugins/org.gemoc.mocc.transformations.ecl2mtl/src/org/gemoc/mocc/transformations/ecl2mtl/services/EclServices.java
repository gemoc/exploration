/**
 * Copyright (c) 2012-2016 GEMOC consortium.
 * 
 * http://www.gemoc.org
 * 
 * Contributors:
 *   Stephen Creff - ENSTA Bretagne [stephen.creff@ensta-bretagne.fr]
 *   Papa Issa Diallo - ENSTA Bretagne [papa_issa.diallo@ensta-bretagne.fr]
 *   
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *   
 * $Id$
 */
package org.gemoc.mocc.transformations.ecl2mtl.services;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ocl.examples.pivot.Operation;
import org.eclipse.ocl.examples.pivot.Package;
import org.eclipse.ocl.examples.pivot.Property;
import org.eclipse.ocl.examples.pivot.Type;
import org.eclipse.ocl.examples.xtext.base.basecs.ConstraintCS;
import org.eclipse.ocl.examples.xtext.base.basecs.PrimitiveTypeRefCS;
import org.eclipse.ocl.examples.xtext.completeocl.completeoclcs.ClassifierContextDeclCS;
import org.eclipse.ocl.examples.xtext.completeocl.completeoclcs.ContextDeclCS;
import org.eclipse.ocl.examples.xtext.completeocl.completeoclcs.DefCS;
import org.eclipse.ocl.examples.xtext.completeocl.completeoclcs.PackageDeclarationCS;
import org.eclipse.ocl.examples.xtext.essentialocl.essentialoclcs.ExpCS;
import org.eclipse.ocl.examples.xtext.essentialocl.essentialoclcs.ExpSpecificationCS;
import org.eclipse.ocl.examples.xtext.essentialocl.essentialoclcs.InfixExpCS;
import org.eclipse.ocl.examples.xtext.essentialocl.essentialoclcs.LetExpCS;
import org.eclipse.ocl.examples.xtext.essentialocl.essentialoclcs.LetVariableCS;
import org.gemoc.mocc.ccslmoc.model.moccml.StateMachineRelationDefinition;
import org.gemoc.mocc.ccslmoc.model.moccml.StateRelationBasedLibrary;
import org.gemoc.mocc.transformations.ecl2mtl.libLoader.LibLoader;

import fr.inria.aoste.timesquare.ECL.ECLDocument;
import fr.inria.aoste.timesquare.ECL.ECLExpression;
import fr.inria.aoste.timesquare.ECL.ECLRelation;
import fr.inria.aoste.timesquare.ECL.EventType;
import fr.inria.aoste.timesquare.ccslkernel.model.TimeModel.BasicType.IntegerElement;
import fr.inria.aoste.timesquare.ccslkernel.model.TimeModel.CCSLModel.ClockExpressionAndRelation.ConcreteEntity;
import fr.inria.aoste.timesquare.ccslkernel.model.TimeModel.CCSLModel.ClockExpressionAndRelation.ExpressionLibrary;
import fr.inria.aoste.timesquare.ccslkernel.model.TimeModel.CCSLModel.ClockExpressionAndRelation.Library;
import fr.inria.aoste.timesquare.ccslkernel.model.TimeModel.CCSLModel.ClockExpressionAndRelation.RelationDeclaration;
import fr.inria.aoste.timesquare.ccslkernel.model.TimeModel.CCSLModel.ClockExpressionAndRelation.RelationDefinition;
import fr.inria.aoste.timesquare.ccslkernel.model.TimeModel.CCSLModel.ClockExpressionAndRelation.RelationLibrary;

/**
 * Services to go through the ECL models
 * <br/>FIXME: this is a first version of the services
 * <br/>add comments,
 * <br/>test more,
 * @author Stfun
 *
 */
public class EclServices {
	
	public String getMoCIDFromFirstLib(ECLDocument document){
		
		StateRelationBasedLibrary lib = null;
		
		for (fr.inria.aoste.timesquare.ECL.ImportStatement st : document.getImports())
		{
			if(st.toString().contains("moccml"))
			{
				//lib = LibLoader.loadMoCMLLibrary(document.getImports().get(0));
				lib = LibLoader.loadMoCMLLibrary(st);
			}
		}
		return lib.getName();
	}
	
	public EList<String> getContextFullLabels(ECLDocument document){
		EList<String> result = new BasicEList<String>();	
		for (Iterator<ContextDeclCS> iterator = getContexts(document).iterator(); iterator.hasNext();) {
			ContextDeclCS c = iterator.next();
			String cLabel = ((Package)((PackageDeclarationCS)c.eContainer()).getPivot()).getName()+"::"+((org.eclipse.ocl.examples.pivot.Class)c.getPivot()).getName();
			if (!result.contains(cLabel)) {
				result.add(cLabel);
			}
		}
		return result;
	}
	
	public EList<ContextDeclCS> getContexts(ECLDocument document){
		EList<ContextDeclCS> result = new BasicEList<>();
		for (Iterator<ContextDeclCS> iterator = getAllContextOccurences(document).iterator(); iterator.hasNext();) {
			ContextDeclCS contextDeclCS = iterator.next();
			if (!result.contains(contextDeclCS)) {
				result.add(contextDeclCS);
			}	
		}	
		return result;
	}
	
//	public EList<String> getContextLabels(ECLDocument document){
//		EList<String> result = new BasicEList<>();
//		for (Iterator<ContextDeclCS> iterator = getAllContextOccurences(document).iterator(); iterator.hasNext();) {
//			ContextDeclCS contextDeclCS = iterator.next();
//			String name = ((org.eclipse.ocl.examples.pivot.Class)contextDeclCS.getPivot()).getName();
//			if (!result.contains(name)) {
//			result.add(name);
//			}	
//	}	
//		return result;
//	}
	
	public EList<String> getContextLabels(ECLDocument document, String root){
		EList<String> result = new BasicEList<>();
		String [] tab = new String[3];
		tab = root.split("::");
		String name = "";
		for (Iterator<ContextDeclCS> iterator = getAllContextOccurences(document).iterator(); iterator.hasNext();) {
			ContextDeclCS contextDeclCS = iterator.next();
			ClassifierContextDeclCS _contextDeclCS = (ClassifierContextDeclCS) contextDeclCS;
			if(tab.length>1)
				name = tab[0] + "::"+_contextDeclCS.getClassifier().getName();
			else
				name = ((org.eclipse.ocl.examples.pivot.Class)contextDeclCS.getPivot()).getName();
			if (!result.contains(name)) {
				result.add(name);
			}	
		}	
		return result;
	}
	
	public EList<ContextDeclCS> getAllContextOccurences(ECLDocument document){
		EList<ContextDeclCS> result = new BasicEList<>();
		for (Iterator<PackageDeclarationCS> iterator = document.getPackages().iterator(); iterator.hasNext();) {
			PackageDeclarationCS p = iterator.next();
			for (Iterator<ContextDeclCS> iterator2 = p.getContexts().iterator(); iterator2.hasNext();) {
				ContextDeclCS c = iterator2.next();
				result.add(c);
			}
		}	
		return result;
	}
	
	public EList<String> getAllEvents(ECLDocument document){
		EList<String> result = new BasicEList<>();
		for (Iterator<ContextDeclCS> iterator = getAllContextOccurences(document).iterator(); iterator.hasNext();) {
			ContextDeclCS c = iterator.next();
			result.addAll(getEvents(c));
		}
		return result;
	}
	
	public String getLabel(ContextDeclCS context){
		return ((org.eclipse.ocl.examples.pivot.Class)context.getPivot()).getName();
	}
	
	public EList<String> getEvents(ContextDeclCS context){
		EList<String> result = new BasicEList<>();
		if (context instanceof ClassifierContextDeclCS) {
			ClassifierContextDeclCS cCls = (ClassifierContextDeclCS) context;
			for (Iterator<DefCS> iterator2 = cCls.getDefinitions().iterator(); iterator2
					.hasNext();) {
				DefCS def = iterator2.next();
				if (def.getOwnedType() instanceof EventType) {
					result.add(def.getName());
				}				
			}	
		}
		return result;
	}
	
	public EList<String> getEventsWithDSA(ContextDeclCS context){
		EList<String> result = new BasicEList<>();
		if (context instanceof ClassifierContextDeclCS) {
			ClassifierContextDeclCS cCls = (ClassifierContextDeclCS) context;
			for (Iterator<DefCS> iterator2 = cCls.getDefinitions().iterator(); iterator2
					.hasNext();) {
				DefCS def = iterator2.next();
				if (def.getOwnedType() instanceof EventType) {
					if(def.getSpecification().toString().equals("self")==false)
					result.add(def.getName());
				}				
			}	
		}
		return result;
	}
	
	public EList<String> getEventsWithoutDSA(ContextDeclCS context){
		EList<String> result = new BasicEList<>();
		if (context instanceof ClassifierContextDeclCS) {
			ClassifierContextDeclCS cCls = (ClassifierContextDeclCS) context;
			for (Iterator<DefCS> iterator2 = cCls.getDefinitions().iterator(); iterator2
					.hasNext();) {
				DefCS def = iterator2.next();
				if (def.getOwnedType() instanceof EventType) {
					if(def.getSpecification().toString().equals("self")==true)
					result.add(def.getName());
				}				
			}	
		}
		return result;
	}
	
	
	public EList<String> getAllEvents(ECLDocument document, String contextName){
		EList<String> result = new BasicEList<>();
		result.addAll(getEventsDSA(document, contextName)); // TODO: Supposedly the events for which Execution Functions are defined, see method getEventsDSA
		return result;
	}
	
	public String getExpressionName(ECLDocument document, String contextName, String eventName){
		for (ContextDeclCS contextDeclCS : getAllContextOccurences(document)) {
			if (((org.eclipse.ocl.examples.pivot.Class)contextDeclCS.getPivot()).getName().equals(contextName)) {
				EList<ConstraintCS> invList = getInvariants(contextDeclCS);
				for (ConstraintCS constraintCS : invList) {
					EList<LetExpCS> lst = new BasicEList<>();
					getLetRelation(((ExpSpecificationCS)constraintCS.getSpecification()).getOwnedExpression(), lst);
					for (LetExpCS letExpCS : lst) {
						for(LetVariableCS letVariableCS :letExpCS.getVariable()){
							if (letVariableCS.getOwnedType() instanceof EventType) {
								if (letVariableCS.getName().equalsIgnoreCase(eventName)) {
									return ((ECLExpression)letVariableCS.getInitExpression()).getType().getName();
								}
							}
						}
					}
				}
			}
		}
		return "error in getExpressionName";
	}
	
	
	public String printListedClockParameters(ECLDocument document, String contextName, String eventName){
		for (ContextDeclCS contextDeclCS : getAllContextOccurences(document)) {
			if (((org.eclipse.ocl.examples.pivot.Class)contextDeclCS.getPivot()).getName().equals(contextName)) {
				EList<ConstraintCS> invList = getInvariants(contextDeclCS);
				for (ConstraintCS constraintCS : invList) {
					EList<LetExpCS> lst = new BasicEList<>();
					getLetRelation(((ExpSpecificationCS)constraintCS.getSpecification()).getOwnedExpression(), lst);
					for (LetExpCS letExpCS : lst) {
						for(LetVariableCS letVariableCS :letExpCS.getVariable()){
							if (letVariableCS.getOwnedType() instanceof EventType) {
								if (letVariableCS.getName().equalsIgnoreCase(eventName)) {
									return getClockNamesListedAndSepBySpace(letVariableCS);
								}
							}
						}
					}
				}
			}
		}
		return "error in printListedClockParameters";
	}
	
	public String printListedClockParametersSepByDot(ECLDocument document, String contextName, String eventName){
		for (ContextDeclCS contextDeclCS : getAllContextOccurences(document)) {
			if (((org.eclipse.ocl.examples.pivot.Class)contextDeclCS.getPivot()).getName().equals(contextName)) {
				EList<ConstraintCS> invList = getInvariants(contextDeclCS);
				for (ConstraintCS constraintCS : invList) {
					EList<LetExpCS> lst = new BasicEList<>();
					getLetRelation(((ExpSpecificationCS)constraintCS.getSpecification()).getOwnedExpression(), lst);
					for (LetExpCS letExpCS : lst) {
						for(LetVariableCS letVariableCS :letExpCS.getVariable()){
							if (letVariableCS.getOwnedType() instanceof EventType) {
								if (letVariableCS.getName().equalsIgnoreCase(eventName)) {
									return getClockNamesListedAndSepByDot(letVariableCS);
								}
							}
						}
					}
				}
			}
		}
		return "error in printListedClockParametersSepByDot";
	}
	
	public EList<String> getAllInternalEvents(ECLDocument document, String contextName){
		EList<String> result = new BasicEList<>();
		//add internal events from let expression
		for (ContextDeclCS contextDeclCS : getAllContextOccurences(document)) {
			if (((org.eclipse.ocl.examples.pivot.Class)contextDeclCS.getPivot()).getName().equals(contextName)) {
				EList<ConstraintCS> invList = getInvariants(contextDeclCS);
				for (ConstraintCS constraintCS : invList) {
					EList<LetExpCS> lst = new BasicEList<>();
					getLetRelation(((ExpSpecificationCS)constraintCS.getSpecification()).getOwnedExpression(), lst);
					for (LetExpCS letExpCS : lst) {
						for(LetVariableCS letVariableCS :letExpCS.getVariable()){
							if (letVariableCS.getOwnedType() instanceof EventType) {
								result.add(letVariableCS.getName());
							}
						}
					}
				}
			}
			
		}
		
		// add all events that does not have a DSA function attached
		result.addAll(getEventsWithoutDSA(document, contextName)); // Only the events without Execution Functions defined
		
		return result;
	}
	
	
	public boolean hasInternalEvents(ECLDocument document, String contextName){
		//add internal events from let expression
		for (ContextDeclCS contextDeclCS : getAllContextOccurences(document)) {
			if (((org.eclipse.ocl.examples.pivot.Class)contextDeclCS.getPivot()).getName().equals(contextName)) {
				EList<ConstraintCS> invList = getInvariants(contextDeclCS);
				for (ConstraintCS constraintCS : invList) {
					EList<LetExpCS> lst = new BasicEList<>();
					getLetRelation(((ExpSpecificationCS)constraintCS.getSpecification()).getOwnedExpression(), lst);
					for (LetExpCS letExpCS : lst) {
						for(LetVariableCS letVariableCS :letExpCS.getVariable()){
							if (letVariableCS.getOwnedType() instanceof EventType) {
								return true;
							}
						}
					}
				}
			}
			
		}
		return false;
	}		
	
	public EList<String> getEvents(ECLDocument document, ContextDeclCS context){
		EList<String> result = new BasicEList<>();
		
		for (Iterator<ContextDeclCS> iterator = getAllContextOccurences(document).iterator(); iterator.hasNext();) {
			ContextDeclCS c = iterator.next();
			if (((org.eclipse.ocl.examples.pivot.Class)c.getPivot()).getName().equals(((org.eclipse.ocl.examples.pivot.Class)context.getPivot()).getName())) {
				result.addAll(getEvents(c));
			}
		}
		return result;
	}
	
	public EList<String> getEvents(ECLDocument document, String contextName){
		EList<String> result = new BasicEList<>();
		
		for (Iterator<ContextDeclCS> iterator = getAllContextOccurences(document).iterator(); iterator.hasNext();) {
			ContextDeclCS c = iterator.next();
			if (((org.eclipse.ocl.examples.pivot.Class)c.getPivot()).getName().equals(contextName)) {
				result.addAll(getEvents(c)); 
				//result.addAll(getEventsWithDSA(c)); 
			}
		}
		return result;
	}
	
	public EList<String> getEventsDSA(ECLDocument document, String contextName){
		EList<String> result = new BasicEList<>();
		
		for (Iterator<ContextDeclCS> iterator = getAllContextOccurences(document).iterator(); iterator.hasNext();) {
			ContextDeclCS c = iterator.next();
			if (((org.eclipse.ocl.examples.pivot.Class)c.getPivot()).getName().equals(contextName)) {
				result.addAll(getEvents(c)); 
				//result.addAll(getEventsWithDSA(c)); // Used when we want to have only the events with DSA 
			}
		}
		return result;
	}
	
	public EList<String> getEventsWithoutDSA(ECLDocument document, String contextName){
		EList<String> result = new BasicEList<>();
		
		for (Iterator<ContextDeclCS> iterator = getAllContextOccurences(document).iterator(); iterator.hasNext();) {
			ContextDeclCS c = iterator.next();
			if (((org.eclipse.ocl.examples.pivot.Class)c.getPivot()).getName().equals(contextName)) { 
				result.addAll(getEventsWithoutDSA(c)); 
			}
		}
		return result;
	}
	
	public String getListedEventsSepByDot(ECLDocument document, String contextName){
		StringBuilder sb = new StringBuilder(16);
		for (Iterator<String> iterator = getEvents(document, contextName).iterator(); iterator.hasNext();) {
			String e = iterator.next();
			sb.append(e).append("[element.name/]");
			if (iterator.hasNext()) {
				sb.append(". ");
			}
		}
		return sb.toString();
	}
	
	public EList<ConstraintCS> getInvariants(ContextDeclCS context){
		if (context instanceof ClassifierContextDeclCS) {
			ClassifierContextDeclCS cCls = (ClassifierContextDeclCS) context;
			return cCls.getInvariants();	
		}
		return null;
	}
	
	public EList<ConstraintCS> getInvariants(ECLDocument document, String contextName){
		EList<ConstraintCS> result = new BasicEList<>();
		String [] splitContexName = new String [3];
		String cname = "";
		splitContexName = contextName.split("::");
		if(splitContexName.length>1)
			cname = splitContexName[1];
		else
			cname = contextName;
		for (Iterator<ContextDeclCS> iterator = getAllContextOccurences(document).iterator(); iterator.hasNext();) {
			ContextDeclCS c = iterator.next();
			if (((org.eclipse.ocl.examples.pivot.Class)c.getPivot()).getName().equals(cname)) {
				result.addAll(getInvariants(c));
			}
		}
		return result;
	}
	
	public String getLibraryName(ConstraintCS inv){
		
		if (inv.getSpecification()!=null) {
			if (!(inv.getSpecification() instanceof ExpSpecificationCS)) {
				return "TODO";
			}
			ECLRelation rel = getECLRelation(((ExpSpecificationCS)inv.getSpecification()).getOwnedExpression());
			
			if (rel!=null) {
				return ((Library)((RelationLibrary)rel.getType().eContainer()).eContainer()).getName();
			}
			return "TODO: complete EclServices.java, ConstraintCS.getLibraryName() for expression type " +((ExpSpecificationCS)inv.getSpecification()).getOwnedExpression().eClass().getName();

		}
		return "TODO";
	}
	
	public String getLibraryName(LetVariableCS var){
		return ((Library)((ExpressionLibrary)((ECLExpression)var.getInitExpression()).getType().eContainer()).eContainer()).getName();
	}
	
	public String getRelationName(LetVariableCS var){
		return ((ECLExpression)var.getInitExpression()).getType().getName().toString();
	}
	
	public String getClockNamesListedAndSepByDot(LetVariableCS var){
		return getClockNamesListedAndSepBySep(var, ". ");
	}
	
	public String getClockNamesListedAndSepBySpace(LetVariableCS var){
		return getClockNamesListedAndSepBySep(var, " ");
	}
	
	public String getClockNamesListedAndSepBySep(LetVariableCS var, String sep){
		StringBuilder sb = new StringBuilder(16);
		ECLExpression exp = (ECLExpression)var.getInitExpression();
		for (int i = 0; i < exp.getParameters().size(); i++) {
			ExpCS e = exp.getParameters().get(i);
			//e.
			if (e.getPivot().toString().contains("->collect")) {
				//[for (ne : NamedElement | element.allocatedAgents.oclAsType(Agent))] isExecuting[ne.name/] [/for]
				String str = e.toString().replace("self.", "element.");
				String type = str.substring(str.lastIndexOf("oclAsType("), str.length());
				sb.append("[for (ne : ").append(type.substring(10, type.indexOf(")"))).append(" | ").append(str.substring(0, str.lastIndexOf("."))).append(")]").append(str.substring(str.lastIndexOf(".")+1, str.length())).append("[ne.name/]").append(sep).append("[/for]");
			} else {
				if (e.toString().contains("self.")) {
					if (e.toString().replace("self.", "").contains(".")) {
						//complex navigation TODO
						String str = e.toString().replace("self.", "element.");//FIXME
						sb.append(str.substring(str.lastIndexOf(".")+1, str.length())).append("[").append(str.substring(0, str.lastIndexOf("."))).append(".name/]"); 
					}else {
						sb.append(e.toString().replace("self.", "")).append("[element.name/]"); //FIXME UGGLY
					}
				}else {
					//parameter of type integer constant 
				}
				if (i<exp.getParameters().size()-1) {
					if (exp.getParameters().get(i+1).toString().contains("self.")) {
						sb.append(sep);
					}
				}
			}
		}
		return sb.toString();
	}
	
	
	public String getConstantsOrLinkersListedAndSepByDot(LetVariableCS var){
		StringBuilder sb = new StringBuilder(16);
		ECLExpression exp = (ECLExpression)var.getInitExpression();
		for (int i = 0; i < exp.getParameters().size(); i++) {
			ExpCS e = exp.getParameters().get(i);
			if (e.toString().contains("self.")) {
				// clocks
			}else {
				//parameter of type integer constant 
				EObject eo = var.eContainer();
				do {
					eo = eo.eContainer();
				} while (!(eo instanceof ConstraintCS));
				ConstraintCS c = (ConstraintCS)eo;
				EList<LetExpCS> lst = new BasicEList<>();
				getLetRelation(((ExpSpecificationCS)c.getSpecification()).getOwnedExpression(), lst);
				for (LetExpCS letExpCS : lst) {
					for (LetVariableCS letVarCS : letExpCS.getVariable()) {
						if (letVarCS.getName().equals(e.toString())) {
							if (letVarCS.getInitExpression().toString().contains("self.")) {
								if (letVarCS.getInitExpression().toString().replace("self.", "").contains(".")) {
									//complex navigation TODO
									sb.append("["+letVarCS.getInitExpression().toString().replace("self.", "element.")+"/]"); //FIXME
									
								}else {
									sb.append("[element."+letVarCS.getInitExpression().toString().replace("self.", "")+"/]");
								}
							}else {
								sb.append(letVarCS.getInitExpression().toString());
							}
						}
					}
				}
				if (i<exp.getParameters().size()-1) {
					if (!exp.getParameters().get(i+1).toString().contains("self.")) {
						sb.append(". ");
					}
				}
			}
		}
		return sb.toString();
	}
	
	public String getClockNamesListedAndSepBySep(ConstraintCS inv, String sep){
		StringBuilder sb = new StringBuilder();
		if (inv.getSpecification()!=null) {
			if (!(inv.getSpecification() instanceof ExpSpecificationCS)) {
				return "TODO: complete EclServices.java, ConstraintCS.getClockNamesListedAndSepBySep()";
			}
			ECLRelation rel = getECLRelation(((ExpSpecificationCS)inv.getSpecification()).getOwnedExpression());
			
			if (rel!=null) {
				for (int i = 0; i < rel.getParameters().size(); i++) {
					ExpCS e = rel.getParameters().get(i);
					if (e.getPivot().toString().contains("->collect")) {
						String str = e.toString().replace("self.", "element.");
						String type = str.substring(str.lastIndexOf("oclAsType("), str.length());
						sb.append("[for (ne : ").append(type.substring(10, type.indexOf(")"))).append(" | ").append(str.substring(0, str.lastIndexOf("."))).append(")]").append(str.substring(str.lastIndexOf(".")+1, str.length())).append("[ne.name/] [/for]");
					}else {
						if (e.toString().contains("self.")) {
							if (e.toString().replace("self.", "").contains(".")) {
								//complex navigation TODO
								String str = e.toString().replace("self.", "element.");//FIXME
								sb.append(str.substring(str.lastIndexOf(".")+1, str.length())).append("[").append(str.substring(0, str.lastIndexOf("."))).append(".name/]"); 
							}else {
								sb.append(e.toString().replace("self.", "")).append("[element.name/]"); //FIXME UGGLY
							}
						}else {
							if (e.toString().startsWith("(")) { //clock in let
								String letID = e.toString().substring(e.toString().indexOf("(")+1, e.toString().lastIndexOf(")"));
								EList<LetExpCS> lst = new BasicEList<>();
								getLetRelation(((ExpSpecificationCS)inv.getSpecification()).getOwnedExpression(), lst);
								for (LetExpCS letExpCS : lst) {
									for (LetVariableCS letVariableCS : letExpCS.getVariable()) {
										if (letVariableCS.getName().equals(letID)) {
											String str = letVariableCS.getInitExpression().toString().replace("self", "element");//FIXME
											sb.append(e.toString().substring(e.toString().lastIndexOf(".")+1, e.toString().length())).append("[").append(str).append(".name/]");
										}
									}
								}
								
							}else {//only name, either parameter of type integer constant or internal clock
								EObject eo = e.eContainer();
								do {
									eo = eo.eContainer();
								} while (!(eo instanceof ConstraintCS));
								ConstraintCS c = (ConstraintCS)eo;
								EList<LetExpCS> lst = new BasicEList<>();
								getLetRelation(((ExpSpecificationCS)c.getSpecification()).getOwnedExpression(), lst);
								for (LetExpCS letExpCS : lst) {
									for (LetVariableCS letVarCS : letExpCS.getVariable()) {
										if (letVarCS.getName().equals(e.toString())) {
											if (letVarCS.getOwnedType() instanceof EventType) {
												if (e.toString().contains("self.")) {
													if (e.toString().replace("self.", "").contains(".")) {
														//complex navigation TODO
														String str = e.toString().replace("self.", "element.");//FIXME
														sb.append(str.substring(str.lastIndexOf(".")+1, str.length())).append("[").append(str.substring(0, str.lastIndexOf("."))).append(".name/]"); 
													}else {
														sb.append(e.toString().replace("self.", "")).append("[element.name/]"); //FIXME UGGLY
													}
												}else {
													if (sb.length()!=0) {
														sb.append(sep);
													}
													sb.append(e.toString()).append("[element.name/]");
												}
											}
										}
									}
								}
							}
						}
						if (i<rel.getParameters().size()-1) {
							if (rel.getParameters().get(i+1).toString().contains("self.") || rel.getParameters().get(i+1).toString().startsWith("(")) {
								sb.append(sep);
							}
						}
					}
				}
				return sb.toString();
			}
			return "TODO: complete EclServices.java, ConstraintCS.getClockNamesListedAndSepBySep()";
		}
		return "TODO: complete EclServices.java, ConstraintCS.getClockNamesListedAndSepBySep()";
	}
	
	public String getClockNamesListedAndSepBySpace(ConstraintCS inv){
		return getClockNamesListedAndSepBySep(inv, " ");
	}
	
	
	public String getClockNamesListedAndSepByDot(ConstraintCS inv){
		return getClockNamesListedAndSepBySep(inv, ". ");
	}
	
	public String getVariablesListedAndSepByDot(ConstraintCS inv){
		StringBuilder sb = new StringBuilder();
		if (inv.getSpecification()!=null) {
			if (!(inv.getSpecification() instanceof ExpSpecificationCS)) {
				return "TODO";
			}
			ECLRelation rel = getECLRelation(((ExpSpecificationCS)inv.getSpecification()).getOwnedExpression());
			if (rel==null) {
				return "TODO: complete EclServices.java, ConstraintCS.getVariablesListedAndSepByDot() for expression type " +((ExpSpecificationCS)inv.getSpecification()).getOwnedExpression().eClass().getName();
			}
			if (((RelationLibrary)rel.getType().eContainer()).getName().equalsIgnoreCase("kernelRelations")) {
				return "";
			}//FIXME do it for all ccsl lib
			
			RelationDefinition rd = getCorrespondingRelationDefinition(((RelationLibrary)rel.getType().eContainer()), rel.getType());
			if (rd==null) {
				return "TODO: complete EclServices.java, ConstraintCS.getVariablesListedAndSepByDot() for expression type " +((ExpSpecificationCS)inv.getSpecification()).getOwnedExpression().eClass().getName();
			}
			if (rd instanceof StateMachineRelationDefinition) {
				for (ConcreteEntity ce : ((StateMachineRelationDefinition)rd).getDeclarationBlock().getConcreteEntities()) {
					//FIXME do it for other concreteEntity specializations
					if (ce instanceof IntegerElement) {
						if (sb.length()!=0) {
							sb.append(". ");
						}
						sb.append(((IntegerElement)ce).getValue());
					}else {
						return "TODO: complete EclServices.java, ConstraintCS.getVariablesListedAndSepByDot() for expression type " +((ExpSpecificationCS)inv.getSpecification()).getOwnedExpression().eClass().getName();
					}
				}
			}
			return sb.toString();
		}
		return "TODO";
	}
	
	private RelationDefinition getCorrespondingRelationDefinition(RelationLibrary rl, RelationDeclaration rd){
		for (RelationDefinition rdef : rl.getRelationDefinitions()) {
			if (rdef.getDeclaration().equals(rd)) {
				return rdef;
			}
		}
		return null;
	}
	
	
	public String getConstantsOrLinkersListedAndSepByDot(ConstraintCS inv){
		StringBuilder sb = new StringBuilder(16);
		EList<LetExpCS> lst = new BasicEList<>();
		getLetRelation(((ExpSpecificationCS)inv.getSpecification()).getOwnedExpression(), lst);
		for (Iterator<LetExpCS> iterator = lst.iterator(); iterator.hasNext();) {
			LetExpCS expCS = iterator.next();
			EList<LetVariableCS> listVar = expCS.getVariable();
			boolean isIn = false;
			for (int i = 0; i < listVar.size(); i++) {
				LetVariableCS letVariableCS = listVar.get(i);
				if (letVariableCS.getOwnedType() instanceof  PrimitiveTypeRefCS) {
					if (((PrimitiveTypeRefCS)letVariableCS.getOwnedType()).getName().equalsIgnoreCase("Integer")) {
						isIn = false;
						ECLRelation rel = getECLRelation(((ExpSpecificationCS)inv.getSpecification()).getOwnedExpression());
						for (ExpCS e : rel.getParameters()) {
							if (e.toString().contains(expCS.getVariable().get(0).getName())) {
								isIn=true;
							};
						}
						if (isIn) {//if in relation
							if (letVariableCS.getInitExpression().toString().contains("self.")) {
								if (letVariableCS.getInitExpression().toString().replace("self.", "").contains(".")) {
									//complex navigation TODO
									sb.append("["+letVariableCS.getInitExpression().toString().replace("self.", "element.")+"/]"); //FIXME
									
								}else {
									sb.append("[element."+letVariableCS.getInitExpression().toString().replace("self.", "")+"/]");
								}
							}else {
								sb.append(letVariableCS.getInitExpression().toString());
							}
							if (i<listVar.size()-1) {
								sb.append(". ");
							}
						}
					}
				}
				
			}
			if (isIn &&iterator.hasNext()) {
				sb.append(". ");
			}
		}
		return sb.toString();
	}
	
	private ECLRelation getLetRelation(ExpCS exp, EList<LetExpCS> lst){
		if (exp instanceof ECLRelation) {
			return (ECLRelation) exp;
		}
		if (exp instanceof LetExpCS) {
			lst.add((LetExpCS)exp);
			return getLetRelation(((LetExpCS)exp).getIn(),lst);
		}
		if (exp instanceof InfixExpCS) {
			return getLetRelation(((InfixExpCS)exp).getOwnedExpression().get(1),lst);
		}
		return null;
	}
	
	public String getRelationName(ConstraintCS inv){
		if (inv.getSpecification()!=null) {
			if (!(inv.getSpecification() instanceof ExpSpecificationCS)) {
				return "TODO";
			}
			ECLRelation rel = getECLRelation(((ExpSpecificationCS)inv.getSpecification()).getOwnedExpression());
			
			if (rel!=null) {
				RelationDefinition rd = getCorrespondingRelationDefinition((RelationLibrary)rel.getType().eContainer(), rel.getType());
				return rd.getName();
			}
			return "TODO: complete EclServices.java, ConstraintCS.getRelationName() for expression type " +((ExpSpecificationCS)inv.getSpecification()).getOwnedExpression().eClass().getName();

		}
		return "TODO";
	}
	
	public String getRelationDeclName(ConstraintCS inv){
		if (inv.getSpecification()!=null) {
			if (!(inv.getSpecification() instanceof ExpSpecificationCS)) {
				return "TODO";
			}
			ECLRelation rel = getECLRelation(((ExpSpecificationCS)inv.getSpecification()).getOwnedExpression());
			
			if (rel!=null) {
				return rel.getType().getName();
			}
			return "TODO: complete EclServices.java, ConstraintCS.getRelationName() for expression type " +((ExpSpecificationCS)inv.getSpecification()).getOwnedExpression().eClass().getName();

		}
		return "TODO";
	}
	
	private ECLRelation getECLRelation(ExpCS exp){
		if (exp instanceof ECLRelation) {
			return (ECLRelation) exp;
		}
		if (exp instanceof LetExpCS) {
			return getECLRelation(((LetExpCS)exp).getIn());
		}
		if (exp instanceof InfixExpCS) {
			return getECLRelation(((InfixExpCS)exp).getOwnedExpression().get(1));
		}
		return null;
	}
	
	public String getNsURIToDeclare(ECLDocument document){
		StringBuilder sb = new StringBuilder(48);
		for (Iterator<PackageDeclarationCS> iterator = document.getPackages().iterator(); iterator.hasNext();) {
			PackageDeclarationCS p = iterator.next();
			String uri = ((Package)p.getPivot()).getNsURI().toString().replace("/extended", "");
			sb.append("'").append(uri).append("'");
			if (iterator.hasNext()) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
	
	public boolean hasInternalClocks(ECLDocument document){
		EList<ContextDeclCS> contextList = getAllContextOccurences(document);
		for (ContextDeclCS contextDeclCS : contextList) {
			EList<ConstraintCS> invList = getInvariants(contextDeclCS);
			for (ConstraintCS constraintCS : invList) {
				return hasInternalClocks(constraintCS);
			}
		}
		return false;
	}
	
	public boolean hasInternalClocks(ConstraintCS constraintCS){
		EList<LetExpCS> lst = new BasicEList<>();
		getLetRelation(((ExpSpecificationCS)constraintCS.getSpecification()).getOwnedExpression(), lst);
		for (LetExpCS letExpCS : lst) {
			for(LetVariableCS letVariableCS :letExpCS.getVariable()){
				if (letVariableCS.getOwnedType() instanceof EventType) {
					return true;
				}
			}
		}
		return false;
	}
	
	public EList<String> getInternalClocks(ConstraintCS constraintCS){
		EList<String> result = new BasicEList<String>();
		EList<LetExpCS> lst = new BasicEList<>();
		getLetRelation(((ExpSpecificationCS)constraintCS.getSpecification()).getOwnedExpression(), lst);
		for (LetExpCS letExpCS : lst) {
			for(LetVariableCS letVariableCS :letExpCS.getVariable()){
				if (letVariableCS.getOwnedType() instanceof EventType) {
					result.add(letVariableCS.getName());
				}
			}
		}
		return result;
	}
	
	public EList<LetVariableCS> getInternalClockExps(ConstraintCS constraintCS){
		EList<LetVariableCS> result = new BasicEList<LetVariableCS>();
		EList<LetExpCS> lst = new BasicEList<>();
		getLetRelation(((ExpSpecificationCS)constraintCS.getSpecification()).getOwnedExpression(), lst);
		for (LetExpCS letExpCS : lst) {
			for(LetVariableCS letVariableCS :letExpCS.getVariable()){
				if (letVariableCS.getOwnedType() instanceof EventType) {
					result.add(letVariableCS);
				}
			}
		}
		return result;
	}
	
	
	
	
	public EList<String> getInternalClocks(ECLDocument document){
		EList<String> result = new BasicEList<String>();
		EList<ContextDeclCS> contextList = getAllContextOccurences(document);
		for (ContextDeclCS contextDeclCS : contextList) {
			EList<ConstraintCS> invList = getInvariants(contextDeclCS);
			for (ConstraintCS constraintCS : invList) {
				result.addAll(getInternalClocks(constraintCS));
			}
		}
		return result;
	}
	
	/***************************************New Added Rules ********************************************/
	
	public static EList<ContextDeclCS> getContextsFromPack(PackageDeclarationCS pack)
	{
		EList<ContextDeclCS> result = new BasicEList<>();
		EList<ContextDeclCS> tmp = new BasicEList<>();
		EList<String> ctmp = new BasicEList<>();
		tmp = pack.getContexts();
		
		for (ContextDeclCS c : tmp) 
		{			
			ClassifierContextDeclCS clc = (ClassifierContextDeclCS) c;
			if(ctmp.contains(clc.getClassifier().getName())==false)
			{
				ctmp.add(clc.getClassifier().getName());
				result.add(clc);
			}
		}	
		return result;
	}
	
	public String getEventParameters(PackageDeclarationCS pack, String elt, String ct)
	{
		StringBuilder bresult = new StringBuilder();
		EList<ContextDeclCS> contexts = new BasicEList<>();
		contexts = pack.getContexts();
		
		for(ContextDeclCS c: contexts)
		{
			if (c instanceof ClassifierContextDeclCS)
			{
				ClassifierContextDeclCS clc = (ClassifierContextDeclCS) c;
				if(clc.getClassifier().getName().equalsIgnoreCase(ct)==true)
				{
					EList<DefCS> listdef = new BasicEList<>();
					listdef = clc.getDefinitions();
					for (Iterator<DefCS> iterator = listdef.iterator(); iterator
							.hasNext();) {
						DefCS def = (DefCS) iterator.next();
						if (def.getOwnedType() instanceof EventType)
						{
							bresult.append("[" +elt+ "/]_"+def.getName()+":in none");
							if(iterator.hasNext()) bresult.append(",");
						}
					}
					/*for(DefCS def:listdef)
					{
						if (def.getOwnedType() instanceof EventType)
							bresult.append("[" +elt+ "/]_"+def.getName()+":in none" + ",");
					}*/
				}
			}
		}
		//bresult.deleteCharAt(bresult.length()-1);
		return bresult.toString();
	}
	
	public String getEventParametersOUT(PackageDeclarationCS pack, String elt, String ct)
	{
		StringBuilder bresult = new StringBuilder();
		EList<ContextDeclCS> contexts = new BasicEList<>();
		contexts = pack.getContexts();
		
		for(ContextDeclCS c: contexts)
		{
			if (c instanceof ClassifierContextDeclCS)
			{
				ClassifierContextDeclCS clc = (ClassifierContextDeclCS) c;
				if(clc.getClassifier().getName().equalsIgnoreCase(ct)==true)
				{
					EList<DefCS> listdef = new BasicEList<>();
					listdef = clc.getDefinitions();
					for(DefCS def:listdef)
					{
						if (def.getOwnedType() instanceof EventType)
							bresult.append("[" +elt+ "/]_"+def.getName()+":out none" + ",");
					}
				}
			}
		}
		//bresult.deleteCharAt(bresult.length()-1);
		return bresult.toString();
	}
	
	/*public String getEventParameterParDefinition(PackageDeclarationCS pack, String elt, String ct)
	{
		StringBuilder bresult = new StringBuilder();
		EList<ContextDeclCS> contexts = new BasicEList<>();
		String spacing = "   ";
		contexts = pack.getContexts();
		
		for(ContextDeclCS c: contexts)
		{
			if (c instanceof ClassifierContextDeclCS)
			{
				ClassifierContextDeclCS clc = (ClassifierContextDeclCS) c;
				if(clc.getClassifier().getName().equalsIgnoreCase(ct)==true)
				{
					EList<String> lst_evt = new BasicEList<>();
					EList<DefCS> listdef = new BasicEList<>();
					listdef = clc.getDefinitions();
					for(DefCS def:listdef)
					{
						if (def.getOwnedType() instanceof EventType)
						{
							//bresult.append("[" +elt+ "/]_"+def.getName()+",");
							lst_evt.add("[" +elt+ "/]_"+def.getName()+",");
						}
					}
					for (Iterator<String> iterator = lst_evt.iterator(); iterator.hasNext();) {
						String input = iterator.next();
						bresult.append(input);
					}
				}
			}
		}
		//bresult.deleteCharAt(bresult.length()-1);
		return bresult.toString();
	}*/
	
	public String getEventParameterParDefinition(PackageDeclarationCS pack, String elt, String ct)
	{
		StringBuilder bresult = new StringBuilder();
		EList<ContextDeclCS> contexts = new BasicEList<>();
		contexts = pack.getContexts();
		
		for(ContextDeclCS c: contexts)
		{
			if (c instanceof ClassifierContextDeclCS)
			{
				ClassifierContextDeclCS clc = (ClassifierContextDeclCS) c;
				if(clc.getClassifier().getName().equalsIgnoreCase(ct)==true)
				{
					EList<DefCS> listdef = new BasicEList<>();
					listdef = clc.getDefinitions();
					for (Iterator<DefCS> iterator = listdef.iterator(); iterator
							.hasNext();) {
						DefCS def = (DefCS) iterator.next();
						if (def.getOwnedType() instanceof EventType)
						{
							bresult.append("[" +elt+ "/]_"+def.getName());
							if(iterator.hasNext()) bresult.append(",");
						}
					}
					/*for(DefCS def:listdef)
					{
						if (def.getOwnedType() instanceof EventType)
							bresult.append("[" +elt+ "/]_"+def.getName()+":in none" + ",");
					}*/
				}
			}
		}
		//bresult.deleteCharAt(bresult.length()-1);
		return bresult.toString();
	}
	
	/*public String getPortDeclaration(PackageDeclarationCS pack, String elt, String ct)
	{
		StringBuilder bresult = new StringBuilder();
		EList<ContextDeclCS> contexts = new BasicEList<>();
		contexts = pack.getContexts();
		
		for (Iterator<ContextDeclCS> iterator = contexts.iterator(); iterator.hasNext();) 
		{
			ContextDeclCS c = (ContextDeclCS) iterator.next();
			ClassifierContextDeclCS clc = (ClassifierContextDeclCS) c;
				if(clc.getClassifier().getName().equalsIgnoreCase(ct)==true)
				{
					EList<String> lst_port = new BasicEList<>();
					EList<DefCS> listdef = new BasicEList<>();
					listdef = clc.getDefinitions();
			
					if(iterator.hasNext())
					{
						for (Iterator<DefCS> iterator2 = listdef.iterator(); iterator2.hasNext();) 
						{
							DefCS defCS = (DefCS) iterator2.next();
							if (defCS.getOwnedType() instanceof EventType)
							{
								lst_port.add("[" +elt+ "/]_"+defCS.getName()+": none"+"[if (0=0)],[/if]");
								//lst_port.add("[" +elt+ "/]_"+defCS.getName());
							}
						}
						for (Iterator<String> iterator3 = lst_port.iterator(); iterator3.hasNext();) {
							String input = iterator3.next();
							bresult.append(input);
						}
					}
					else {
							for (Iterator<DefCS> iterator2 = listdef.iterator(); iterator2.hasNext();) 
							{
								DefCS defCS = (DefCS) iterator2.next();
								if(iterator2.hasNext())
								{
									if (defCS.getOwnedType() instanceof EventType)
										lst_port.add("[" +elt+ "/]_"+defCS.getName()+": none"+"[if (0=0)],[/if]"); 
								}
								else{
									if (defCS.getOwnedType() instanceof EventType)
										lst_port.add("[" +elt+ "/]_"+defCS.getName()+": none"+"[if (0=1)],[/if]"); 
								}
								
							}
							for (Iterator<String> iterator3 = lst_port.iterator(); iterator3.hasNext();) {
								String input = iterator3.next();
								bresult.append(input);
							}
						}
					
			}
		}
		return bresult.toString();
	}
	*/
	public String getPortDeclaration(PackageDeclarationCS pack, String elt, String ct)
	{
		StringBuilder bresult = new StringBuilder();
		EList<ContextDeclCS> contexts = new BasicEList<>();
		String spacing = "   ";
		contexts = pack.getContexts();
		
		for(ContextDeclCS c: contexts)
		{
			if (c instanceof ClassifierContextDeclCS)
			{
				ClassifierContextDeclCS clc = (ClassifierContextDeclCS) c;
				if(clc.getClassifier().getName().equalsIgnoreCase(ct)==true)
				{
					EList<String> lst_port = new BasicEList<>();
					EList<DefCS> listdef = new BasicEList<>();
					listdef = clc.getDefinitions();
					for(DefCS def:listdef)
					{
						if (def.getOwnedType() instanceof EventType)
							//bresult.append("[" +elt+ "/]_"+def.getName()+": none in" +"['['/]"+"0,0"+"[']'/]" + ",");
							lst_port.add("[" +elt+ "/]_"+def.getName()+": none,");
					}
					
					for (Iterator<String> iterator = lst_port.iterator(); iterator.hasNext();) {
						String input = iterator.next();
						bresult.append(input);
						//if (iterator.hasNext()) {
						//	bresult.append(",\n");
						//}
					}
					bresult.append("\n");
				}
			}
		}
		return bresult.toString();
	}
	
	public String getProcTransitions(PackageDeclarationCS pack, String elt, String ct)
	{
		StringBuilder bresult = new StringBuilder();
		EList<ContextDeclCS> contexts = new BasicEList<>();
		contexts = getContextsFromPack(pack);
		
		for(ContextDeclCS c: contexts)
		{
			if (c instanceof ClassifierContextDeclCS)
			{
				ClassifierContextDeclCS clc = (ClassifierContextDeclCS) c;
				if(clc.getClassifier().getName().equalsIgnoreCase(ct)==true)
				{
					EList<DefCS> listdef = new BasicEList<>();
					listdef = clc.getDefinitions();
					for(DefCS def:listdef)
					{
						if (def.getOwnedType() instanceof EventType)
						{
							if(def.getSpecification().toString().equals("self"))
							{
								bresult.append("    from s0  " +"["+elt+ "/]_"+def.getName()+";" + "  to s0" + "\n");
							}
							else
							{
								String methodname = def.getSpecification().toString().substring(5, def.getSpecification().toString().length()-2);
								for(Operation op: clc.getClassifier().getOwnedOperation())
								{
									if(op.getName().equalsIgnoreCase(methodname))
									{
										String rtype = getReturnType(clc, op);
										if(rtype.equalsIgnoreCase("Y"))
										{
											methodname = methodname +"_"+elt;
										}
									}
								}
								String func = "global:="+clc.getClassifier().getName()+"_"+methodname+"( "+"global , " +"global.refToElement_id)"; 
								bresult.append("    from s0  " +"["+elt+ "/]_"+def.getName()+";" + " " + func +" "+ ";" + "  to s0" + "\n");
							}
						}
							
					}
				}
			}
		}
		bresult.deleteCharAt(bresult.length()-1);
		return bresult.toString();
	}
	
	public String getProcTransitionsOUT(PackageDeclarationCS pack, String elt, String ct)
	{
		StringBuilder bresult = new StringBuilder();
		EList<ContextDeclCS> contexts = new BasicEList<>();
		contexts = getContextsFromPack(pack);
		
		for(ContextDeclCS c: contexts)
		{
			if (c instanceof ClassifierContextDeclCS)
			{
				ClassifierContextDeclCS clc = (ClassifierContextDeclCS) c;
				if(clc.getClassifier().getName().equalsIgnoreCase(ct)==true)
				{
					EList<DefCS> listdef = new BasicEList<>();
					listdef = clc.getDefinitions();
					for(DefCS def:listdef)
					{
						if (def.getOwnedType() instanceof EventType)
						{
							bresult.append("    from s0  " +"["+elt+ "/]_"+def.getName()+";" + "  to s0" + "\n");
						}
							
					}
				}
			}
		}
		//bresult.deleteCharAt(bresult.length()-1);
		return bresult.toString();
	}
	
	
	
	/*public String getGlobalParameters(ClassifierContextDeclCS cdecl)
	{
		StringBuilder bresult = new StringBuilder();
		EList<Property> attrs = new BasicEList<>();
		attrs = (EList<Property>) cdecl.getClassifier().getOwnedAttribute();
		
		for (Property attr:attrs)
		{
			if(attr.getType().getName().contains("EIn"))
			{
				String param = "p"+attr.getName().substring(0, 4);
				bresult.append("&" +param+ ":int"+ ",");
			}
		}
		bresult.append("&rt:int");
		return bresult.toString();
	}*/
	
	/*public String getGlobalParameters(ClassifierContextDeclCS cs)
	{
		StringBuilder bresult = new StringBuilder();
		for(Property attr : cs.getClassifier().getOwnedAttribute())
		{
			if(attr.getName().startsWith("nonED"))
			{
					if(attr.getName().contains("procparam"))
					{
						bresult.append(attr.getDefault().toString());
					}
				
			}
		}
		return bresult.toString();
	}*/
	
	public String getGlobalParameters(ClassifierContextDeclCS cs)
	{
		StringBuilder bresult = new StringBuilder();
		bresult.append("&global : SystemDataRoot, id : read int");
		return bresult.toString();
	}
	
	/*public String getGlobalParametersInit(ClassifierContextDeclCS cdecl)
	{
		StringBuilder bresult = new StringBuilder();
		EList<Property> attrs = new BasicEList<>();
		attrs = (EList<Property>) cdecl.getClassifier().getOwnedAttribute();
		
		for (Property attr:attrs)
		{
			if(attr.getType().getName().contains("EIn"))
			{
				String param ="p"+cdecl.getClassifier().getName().substring(0, 2)+ attr.getName().substring(0, 4);
				bresult.append("&" +param+ ",");
			}
		}
		bresult.append("&rt");
		return bresult.toString();
	}*/
	
	public String getGlobalParametersInit(ClassifierContextDeclCS cdecl)
	{
		StringBuilder bresult = new StringBuilder();
		bresult.append("&global, ID");
		return bresult.toString();
	}
	
	public String getParametersDeclaration(PackageDeclarationCS pack)
	{
		StringBuilder bresult = new StringBuilder();
		EList<Property> attrs = new BasicEList<>();
		EList<ContextDeclCS> decls = new BasicEList<>();
		decls = getContextsFromPack(pack);
		for(ContextDeclCS decl:decls)
		{
			ClassifierContextDeclCS cdecl = (ClassifierContextDeclCS)decl;
			attrs = (EList<Property>) cdecl.getClassifier().getOwnedAttribute();
		
			for (Property attr:attrs)
			{
				if(attr.getType().getName().contains("EIn"))
				{
					String param ="p"+cdecl.getClassifier().getName().substring(0, 2)+ attr.getName().substring(0, 4);
					bresult.append(param+ ":int"+ ",");
				}
			}
		}
		
		bresult.deleteCharAt(bresult.length()-1);
		return bresult.toString();
	}
	
	public String removeLastComma(String inputstr){
		StringBuilder bresult = new StringBuilder();
		bresult.append(inputstr);
		bresult.deleteCharAt(bresult.length()-1);
		return bresult.toString();
	}
	
	public String getXtendFilePath(ECLDocument document)
	{
		String result = "";
		XtendFileChooserBasic fl = new XtendFileChooserBasic("C:\\Workspaces\\runtime-v4LanguageModelingTest\\org.gemoc.sigpml.k3dsa");
		result = fl.getFile().getAbsolutePath();
		return result;
	}
	
	/*public String getBasicTypesDeclaration (PackageDeclarationCS pck, String root)
	{
		StringBuilder sb = new StringBuilder();
		EList<String> listArrayID = new BasicEList<String>();
		//System.out.println("============================= ID Arrays Declaration ===============================================");
		for(ContextDeclCS ct : getContextsFromPack(pck))
		{
			ClassifierContextDeclCS cct = (ClassifierContextDeclCS) ct;
			String leftsideMetaClass = "";
			String rightsideMetaClass = "";
			
			for(Property attr : cct.getClassifier().getOwnedAttribute())
			{	
					if(attr.getName().startsWith("nonED")==false)
					{
						if (attr.getType().getName().equalsIgnoreCase("Enumeration")) 
						{
							sb.append("type " + attr.getName()+ " is " + "int" +"\n");
						}
						if (attr.getType().getName().equalsIgnoreCase("OrderedSet")) 
						{
							if(attr.getType().toString().contains(cct.getClassifier().getPackage().toString())==true)
							{
								leftsideMetaClass = "OrderedSet"+"\\("+cct.getClassifier().getPackage().toString()+"::";
							}
							else
							{
								leftsideMetaClass = "OrderedSet\\(ecore::";
							}
							
							rightsideMetaClass = "\\)";
							String ff = attr.getType().toString();
							String conceptname = ff.replaceAll(leftsideMetaClass, "").replaceAll(rightsideMetaClass, "");
							String arrname = conceptname+"_"+attr.getName() +"Array";
							String arraysize = "[a"+root+".eAllContents("+conceptname+")->size()/]";
						
							//sb.append("type " + arrname+ " is array " + arraysize +" of "+ conceptname +"\n");
							listArrayID.add("type " + arrname+ " is array " + arraysize +" of "+ conceptname +"\n");
						}
						if (attr.getType().getName().equalsIgnoreCase("ArrayList")) 
						{
							if(attr.getType().toString().contains(cct.getClassifier().getPackage().toString())==true)
							{
								leftsideMetaClass = "ArrayList"+"\\("+cct.getClassifier().getPackage().toString()+"::";
							}
							else
							{
								leftsideMetaClass = "ArrayList\\(ecore::";
							}
							rightsideMetaClass = "\\)";
							String ff = attr.getType().toString();
							String conceptname = ff.replaceAll(leftsideMetaClass, "").replaceAll(rightsideMetaClass, "");
							String arraysize =  "[a"+root+".eAllContents("+conceptname+")->size()/]";
							String arrname = conceptname+"_"+attr.getName() +"Array";
							//sb.append("type " + arrname+ " is array " + arraysize +" of "+conceptname +"\n");
							listArrayID.add("type " + arrname+ " is array " + arraysize +" of "+ conceptname +"\n");
						}
						if (attr.getType().getName().equalsIgnoreCase("Bag")) 
						{
							if(attr.getType().toString().contains(cct.getClassifier().getPackage().toString())==true)
							{
								leftsideMetaClass = "Bag"+"\\("+cct.getClassifier().getPackage().toString()+"::";
							}
							else
							{
								leftsideMetaClass = "Bag\\(ecore::";
							}
							rightsideMetaClass = "\\)";
							String ff = attr.getType().toString();
							String conceptname = ff.replaceAll(leftsideMetaClass, "").replaceAll(rightsideMetaClass, "");
							String arraysize =  "[a"+root+".eAllContents("+conceptname+")->size()/]";
							String arrname = conceptname+"_"+attr.getName() +"Array";
							//sb.append("type " + arrname+ " is array " + arraysize +" of "+conceptname +"\n");
							listArrayID.add("type " + arrname+ " is array " + arraysize +" of "+ conceptname +"\n");
						}
					}
			}
			
			for(org.eclipse.ocl.examples.pivot.Type supt: cct.getClassifier().getSuperClass())
			{
				for(Property attr : supt.getOwnedAttribute())
				{
					if(attr.getName().startsWith("nonED")==false)
					{
						if (attr.getType().getName().equalsIgnoreCase("Enumeration")) 
						{
							sb.append("type " + attr.getName()+ " is " + "int" +"\n");
						}
						if (attr.getType().getName().equalsIgnoreCase("OrderedSet")) 
						{
							
							if(attr.getType().toString().contains(supt.getPackage().toString())==true)
							{
								leftsideMetaClass = "OrderedSet"+"\\("+supt.getPackage().toString()+"::";
							}
							else
							{
								leftsideMetaClass = "OrderedSet\\(ecore::";
							}
							rightsideMetaClass = "\\)";
							String ff = attr.getType().toString();
							String conceptname = ff.replaceAll(leftsideMetaClass, "").replaceAll(rightsideMetaClass, "");
							String arraysize =  "[a"+root+".eAllContents("+conceptname+")->size()/]";
							String arrname = conceptname+"_"+attr.getName() +"Array";
							//sb.append("type " + arrname+ " is array " + arraysize +" of "+conceptname +"\n");
							listArrayID.add("type " + arrname+ " is array " + arraysize +" of "+ conceptname +"\n");
						}
						
						if (attr.getType().getName().equalsIgnoreCase("ArrayList")) 
						{
							if(attr.getType().toString().contains(supt.getPackage().toString())==true)
							{
								leftsideMetaClass = "ArrayList"+"\\("+supt.getPackage().toString()+"::";
							}
							else
							{
								leftsideMetaClass = "ArrayList\\(ecore::";
							}
							rightsideMetaClass = "\\)";
							String ff = attr.getType().toString();
							String conceptname = ff.replaceAll(leftsideMetaClass, "").replaceAll(rightsideMetaClass, "");
							String arraysize =  "[a"+root+".eAllContents("+conceptname+")->size()/]";
							String arrname = conceptname+"_"+attr.getName() +"Array";
							//sb.append("type " + arrname+ " is array " + arraysize +" of "+conceptname +"\n");
							listArrayID.add("type " + arrname+ " is array " + arraysize +" of "+ conceptname +"\n");
						}
						if (attr.getType().getName().equalsIgnoreCase("Bag")) 
						{
							if(attr.getType().toString().contains(supt.getPackage().toString())==true)
							{
								leftsideMetaClass = "Bag"+"\\("+supt.getPackage().toString()+"::";
							}
							else
							{
								leftsideMetaClass = "Bag\\(ecore::";
							}
							rightsideMetaClass = "\\)";
							String ff = attr.getType().toString();
							String conceptname = ff.replaceAll(leftsideMetaClass, "").replaceAll(rightsideMetaClass, "");
							String arraysize =  "[a"+root+".eAllContents("+conceptname+")->size()/]";
							String arrname = conceptname+"_"+attr.getName() +"Array";
							//sb.append("type " + arrname+ " is array " + arraysize +" of "+conceptname +"\n");
							listArrayID.add("type " + arrname+ " is array " + arraysize +" of "+ conceptname +"\n");
						}
					}
				}
			}
		}
		
		for (Iterator<String> iterator = listArrayID.iterator(); iterator.hasNext();) {
			String input = iterator.next();
			sb.append(input+"\n");
		}
		//System.out.println("============================================================================");
		return sb.toString();
	}
	*/
	public boolean compareElementLists(EList<String> list1, EList<String> list2)
	{
		boolean isTrue = false;
		
		for (String elt1 : list1) 
		{
			if(list2.contains(elt1))
				isTrue = true;
		}
		return isTrue;
	}
	
	public boolean checkTypeInListContext(PackageDeclarationCS pck, ContextDeclCS ct){
		boolean isTrue = false;
		EList <ContextDeclCS> listContexts = new BasicEList<ContextDeclCS>();
		listContexts = getContextsFromPack(pck);
		EList <String> listContextsNames = new BasicEList<String>();
		
		for (ContextDeclCS c:listContexts)
		{
			ClassifierContextDeclCS cc = (ClassifierContextDeclCS) c;
			listContextsNames.add(cc.getClassifier().getName()); 
		}
		
		// For Each Context <ct>, get its SuperClass that has the same name (inheritance relation btw MM_Extended & MM)
		
		ClassifierContextDeclCS cct = (ClassifierContextDeclCS) ct;
		EList <String> listAttrType = new BasicEList<String>();
		
		for ( org.eclipse.ocl.examples.pivot.Type supt: cct.getClassifier().getSuperClass())
		{
			if(supt.toString().contains(cct.getClassifier().getName()))
			{
				for(Property attr : supt.getOwnedAttribute())
				{	
					listAttrType.add(attr.getType().getName());
				}
					
			}
		}
		
		return isTrue;
	}
	
	
	public org.eclipse.ocl.examples.pivot.Type getParentClass(ClassifierContextDeclCS ccd)
	{
		org.eclipse.ocl.examples.pivot.Type ctr = null;
		EList <org.eclipse.ocl.examples.pivot.Type> typelist = new BasicEList<org.eclipse.ocl.examples.pivot.Type>();
		
		typelist = (EList<Type>) ccd.getClassifier().getSuperClass();
		
		for (Type type : typelist) {
			if(type.getName().equalsIgnoreCase(ccd.getClassifier().getName()))
				{
					ctr = type;
				}
		}
		return ctr;
	}
	
	public EList<org.eclipse.ocl.examples.pivot.Type> getSuperTypesOfParent (org.eclipse.ocl.examples.pivot.Type t, EList<ContextDeclCS> lst){
		
		EList<org.eclipse.ocl.examples.pivot.Type> lst2 =  new BasicEList<Type>();
		
		for (ContextDeclCS ct : lst) 
		{
			ClassifierContextDeclCS cct = (ClassifierContextDeclCS) ct;
			
			for (Type type : t.getSuperClass()) {
				if(type.getName().equalsIgnoreCase(cct.getClassifier().getName()))
				{
					lst2.add(cct.getClassifier());
					lst2.add(getParentClass(cct));
				}
			}
			//System.out.println("################### = " + t.getName() + " : " + t.getSuperClass() + " / " + cct.getClassifier().getName());
		}
		return lst2;
	}
	
	public void getListofAllOwnedAttributes(EList <org.eclipse.ocl.examples.pivot.Type> lst, EList <Property> listAtrr)
	{
		for (org.eclipse.ocl.examples.pivot.Type t : lst) {
			for (Property property : t.getOwnedAttribute()) 
			{
				if(!(property.getName().equals(t.getName())))
					listAtrr.add(property);
			}
		}
	}
	
	public void getListofOwnedAttributes(org.eclipse.ocl.examples.pivot.Type t, EList <Property> listAtrr)
	{
		for (Property property : t.getOwnedAttribute()) 
		{
			if(!(property.getName().equals(t.getName())))
			listAtrr.add(property);
		}
	}
	
	// Building the different Array and ID arrays
	public String getBasicTypesDeclaration (PackageDeclarationCS pck, String root)
	{
		StringBuilder sb = new StringBuilder();
		EList <String> listIDArray = new BasicEList<String>();
		
		ClassifierContextDeclCS cct = (ClassifierContextDeclCS) getContextsFromPack(pck).get(0);
		String leftsideMetaClass = "";
		String rightsideMetaClass = "";
		String attrname = "";
			
		EList <Type> ltype = new BasicEList<Type>();
			
		for (EObject eo : cct.getClassifier().getPackage().eContents()) {
				ltype.add((org.eclipse.ocl.examples.pivot.Type)eo);
		}
		
		EList <Property> list_attr = new BasicEList<Property>();
		getListofAllOwnedAttributes(ltype,list_attr); // Get the List of attributes at the MM Level
			
		System.out.println("################### = " + cct.getClassifier().getName() + " --> " + list_attr);
		for (Property property : list_attr) {
				System.out.println("                  ################### = " + property.getName() + " : " + property.getType().getName() + " //  " + property.getType());
		}
			
		for (Property attr : list_attr) 
		{
			if (attr.getType().getName().equalsIgnoreCase("OrderedSet")) 
				{
					attrname = attr.getName();
					if(attr.getName().equalsIgnoreCase("type")) {attrname = "_"+attr.getName();}

					if(attr.getType().toString().contains(cct.getClassifier().getPackage().toString())==true)
					{
						leftsideMetaClass = "OrderedSet"+"\\("+cct.getClassifier().getPackage().toString()+"::";
					}
					else
					{
						leftsideMetaClass = "OrderedSet\\(ecore::";
					}
					rightsideMetaClass = "\\)";
					String ff = attr.getType().toString();
					String conceptname = ff.replaceAll(leftsideMetaClass, "").replaceAll(rightsideMetaClass, "");
					String arrname = conceptname.replace("[+]","")+"_"+attr.getName() +"Array";

					String arraysize = "[a"+root+".eAllContents("+conceptname.replace("[+]","")+")->size()/]";
					//listIDArray.add("type " + arrname+ " is array " + arraysize +" of "+ conceptname.replace("[+]","") +"\n");
					listIDArray.add("type " + arrname.replace("::", "_")+ " is array " + arraysize +" of "+ "int" +"\n");
				}
				
				if (attr.getType().getName().equalsIgnoreCase("ArrayList")) 
				{
					attrname = attr.getName();
					if(attr.getName().equalsIgnoreCase("type")) {attrname = "_"+attr.getName();}
					
					if(attr.getType().toString().contains(cct.getClassifier().getPackage().toString())==true)
					{
						leftsideMetaClass = "ArrayList"+"\\("+cct.getClassifier().getPackage().toString()+"::";
					}
					else
					{
						leftsideMetaClass = "ArrayList\\(ecore::";
					}
					rightsideMetaClass = "\\)";
					String ff = attr.getType().toString();
					String conceptname = ff.replaceAll(leftsideMetaClass, "").replaceAll(rightsideMetaClass, "");
					String arrname = conceptname.replace("[+]","")+"_"+attr.getName() +"Array";
					
					String arraysize = "[a"+root+".eAllContents("+conceptname.replace("[+]","")+")->size()/]";
					listIDArray.add("type " + arrname.replace("::", "_")+ " is array " + "100" +" of "+ "int" +"\n");
				}
			}
		
		for (Iterator<String> iterator = listIDArray.iterator(); iterator.hasNext();) {
			String input = iterator.next();
			sb.append(input);
		}
		
		return sb.toString();
	}
		public String getTypesDeclaration (PackageDeclarationCS pck, String root)
		{
			StringBuilder sb = new StringBuilder();
			String typebegindecl = "";
			String subToadd = "";
			String spacing = "    ";
			String typeenddecl = "\nend record"+"\n\n";
			ClassifierContextDeclCS cct = (ClassifierContextDeclCS) getContextsFromPack(pck).get(0);
			
			EList <Type> ltype = new BasicEList<Type>();
			
			for (EObject eo : cct.getClassifier().getPackage().eContents()) {
				ltype.add((org.eclipse.ocl.examples.pivot.Type)eo);
			}
			
			for(org.eclipse.ocl.examples.pivot.Type ct : ltype)
			{
			//	ClassifierContextDeclCS cct = (ClassifierContextDeclCS) ct;
				EList <String> listsubElt = new BasicEList<String>();
				
				typebegindecl = "type " + ct.getName() + " is record";
				sb.append(typebegindecl+"\n");
				String leftsideMetaClass = "";
				String rightsideMetaClass = "";
				String attrname = "";
				
				System.out.println("################### = " + cct.getClassifier().getName() + " --> " + ltype);
				
				EList <Property> list_attr = new BasicEList<Property>();
				getListofOwnedAttributes(ct,list_attr); // Get the List of attributes at the MM Level
				
				for(Property attr : list_attr)
				{
					if (attr.getType().getName().equalsIgnoreCase("Enumeration")) 
					{
						attrname = spacing+ attr.getName();
						if(attr.getName().equalsIgnoreCase("type")) {attrname = "_"+attr.getName();}
						subToadd = attrname + " : Enum";
						listsubElt.add(subToadd);
								
					}
					
					if (attr.getType().getName().equalsIgnoreCase("EInt")) 
					{
						attrname = attr.getName();
						if(attr.getName().equalsIgnoreCase("type")) {attrname = "_"+attr.getName();}
						subToadd = attrname + " : int";
						listsubElt.add(subToadd);
					}
					
					if (attr.getType().getName().equalsIgnoreCase("Boolean")) 
					{
						attrname = attr.getName();
						if(attr.getName().equalsIgnoreCase("type")) {attrname = "_"+attr.getName();}
						subToadd = attrname + " : bool";
						listsubElt.add(subToadd);
					}
					
					if (attr.getType().getName().equalsIgnoreCase("OrderedSet")) 
					{
						attrname = attr.getName();
						if(attr.getName().equalsIgnoreCase("type")) {attrname = "_"+attr.getName();}

						if(attr.getType().toString().contains(cct.getClassifier().getPackage().toString())==true)
						{
							leftsideMetaClass = "OrderedSet"+"\\("+cct.getClassifier().getPackage().toString()+"::";
						}
						else
						{
							leftsideMetaClass = "OrderedSet\\(ecore::";
						}
						rightsideMetaClass = "\\)";
						String ff = attr.getType().toString();
						String conceptname = ff.replaceAll(leftsideMetaClass, "").replaceAll(rightsideMetaClass, "");
						String arrname = conceptname.replace("[+]","")+"_"+attr.getName() +"Array";

						subToadd = attrname+"ID"+ " : " + arrname.replace("::", "_");
						listsubElt.add(subToadd);
					}
					
					if (attr.getType().getName().equalsIgnoreCase("ArrayList")) 
					{
						attrname = attr.getName();
						if(attr.getName().equalsIgnoreCase("type")) {attrname = "_"+attr.getName();}
						
						if(attr.getType().toString().contains(cct.getClassifier().getPackage().toString())==true)
						{
							leftsideMetaClass = "ArrayList"+"\\("+cct.getClassifier().getPackage().toString()+"::";
						}
						else
						{
							leftsideMetaClass = "ArrayList\\(ecore::";
						}
						rightsideMetaClass = "\\)";
						String ff = attr.getType().toString();
						String conceptname = ff.replaceAll(leftsideMetaClass, "").replaceAll(rightsideMetaClass, "");
						String arrname = conceptname.replace("[+]","")+"_"+attr.getName() +"Array";
						//subToadd = attrname+ " : " + arrname;
						//listsubElt.add(subToadd);
						
						subToadd = attrname+"ID"+ " : " + arrname.replace("::", "_");
						listsubElt.add(subToadd);
					}
					
					if (attr.getType().getName().equalsIgnoreCase("Bag")) 
					{
						attrname = attr.getName();
						if(attr.getName().equalsIgnoreCase("type")) {attrname = "_"+attr.getName();}
						
						if(attr.getType().toString().contains(cct.getClassifier().getPackage().toString())==true)
						{
							leftsideMetaClass = "Bag"+"\\("+cct.getClassifier().getPackage().toString()+"::";
						}
						else
						{
							leftsideMetaClass = "Bag\\(ecore::";
						}
						rightsideMetaClass = "\\)";
						String ff = attr.getType().toString();
						String conceptname = ff.replaceAll(leftsideMetaClass, "").replaceAll(rightsideMetaClass, "");
						String arrname = conceptname.replace("[+]","")+"_"+attr.getName() +"Array";
						
						subToadd = attrname+ " : " + arrname.replace("::", "_");
						if(!(attrname.equals(conceptname)))
							listsubElt.add(subToadd);
					}
				}
					
			for (Iterator<String> iterator = listsubElt.iterator(); iterator.hasNext();) {
					String input = iterator.next();
					sb.append(spacing + input);
					if (iterator.hasNext()) {
						sb.append(",\n");
					}
			}
			sb.append(typeenddecl);
			}
			return sb.toString();
		}
	
	// TODO  Here we assume there is only one package in the ECL file, getRootTypeArraysDeclaration can be extended when * package in ECL
		public String getRootTypeArraysDeclaration (PackageDeclarationCS pck,String root){
		
		StringBuilder sb = new StringBuilder();
		String spacing = "    ";
		String ref = "";
		
		ClassifierContextDeclCS cct = (ClassifierContextDeclCS) getContextsFromPack(pck).get(0);
		
		EList <Type> lst_metaclass = new BasicEList<Type>();
		
		for (EObject eo : cct.getClassifier().getPackage().eContents()) {
			lst_metaclass.add((org.eclipse.ocl.examples.pivot.Type)eo);
		}
		
		
		
		EList<FiacreArrayElement> lst_arrays = new BasicEList<FiacreArrayElement>();
		EList<RootRecordElement> lst_recordElt = new BasicEList<RootRecordElement>();
		for (Type ct : lst_metaclass) 
		{
			
				ref = "rootRefTo";
				ref = ref + cct.getClassifier().getName();
				String sprefix = ".eAllContents(" + cct.getClassifier().getName()+")";
				createEntryInRootList(ct,lst_metaclass,ref,root,lst_arrays,lst_recordElt,sprefix);
				parseOwnedAttributes(ct,lst_metaclass,ref,root,lst_arrays,lst_recordElt,sprefix);
		}
		
		for (Iterator<FiacreArrayElement> iterator = lst_arrays.iterator(); iterator.hasNext();) {
			FiacreArrayElement input = iterator.next();
			sb.append("type " + input.arrayname+ " is array " + input.arraysize +" of "+ input.conceptname+"\n");
		}
		
		sb.append("\ntype SystemDataRoot is record \n");
		for (Iterator<RootRecordElement> iterator = lst_recordElt.iterator(); iterator.hasNext();) {
			RootRecordElement input = iterator.next();
			sb.append(spacing + input.refname+ " : " + input.arrayname);
			if(iterator.hasNext())
			{
				sb.append(",\n");
			}
		}
		sb.append("\nend record \n");
		return sb.toString();
	}
	
	public void parseOwnedAttributes(org.eclipse.ocl.examples.pivot.Type t, EList<Type> lst, String refname,String root, EList<FiacreArrayElement> lst1, EList<RootRecordElement> lst2, String cref)
	{
		if(!(classInTypeDeclaration(t,lst).equalsIgnoreCase("none")))
		{
			//String conceptname = classInTypeDeclaration(t,lst);
			
			for(Property attr : t.getOwnedAttribute())
			{
				if(attr.getType().getName().equalsIgnoreCase("OrderedSet"))
				{
					String attrType = "" ;
					if(!(classInTypeDeclaration(attr.getType(),lst).equalsIgnoreCase("none")))
						attrType = classInTypeDeclaration(attr.getType(),lst);
					refname = refname + attr.getName()+attrType;
					// cref = cref +"."+attr.getName()+".eContents(" + attrType+")";
					String sprefix = ".eAllContents(" +attrType +")";
					createEntryInRootList(t,lst,refname,root,lst1,lst2,sprefix); // replace "sprefix" with "cref"
					org.eclipse.ocl.examples.pivot.Type nextT = attr.getType();
					parseOwnedAttributes(nextT,lst,refname,root,lst1,lst2,sprefix);
				}
			}
		}
	}
	
	public void createEntryInRootList(org.eclipse.ocl.examples.pivot.Type t, EList<Type> lst, String refname,String root, EList<FiacreArrayElement> lst1, EList<RootRecordElement> lst2, String sizePrefix)
	{
			
			String conceptname = classInTypeDeclaration(t,lst);
			String recordname = refname;
			String arrname = refname +"Array";
			String arraysize = "[a" + root + sizePrefix + "->size()/]";
			
			lst1.add(new FiacreArrayElement(arrname,arraysize,conceptname));
			lst2.add(new RootRecordElement(arrname, recordname));
	}
	
	public String classInTypeDeclaration(org.eclipse.ocl.examples.pivot.Type t, EList<Type> lst)
	{
		String concept = "none";
		
		for(Type ct : lst)
		{
			if(t.toString().contains(ct.getName()))
			{
				concept = ct.getName();
			}
		}
		return concept;
	}
	
	public String getUnChangingFunctionsDeclarations(PackageDeclarationCS pck){
		StringBuilder sb = new StringBuilder();
		




		for(ContextDeclCS ct : getContextsFromPack(pck))
		{
			ClassifierContextDeclCS cct = (ClassifierContextDeclCS) ct;
			for(Operation op : cct.getClassifier().getOwnedOperation())
			{

				boolean coreChange = getCoreChangesValue(cct, op); 
				if(coreChange==false)
				{
					String returnType = getReturnType(cct,op);
					String params = getParamList(cct, op);
					sb.append("function " + cct.getClassifier().getName()+"_"+op.getName() + " (" + params + ") :" + returnType +" is"+"\n");
					sb.append("begin"+ "\n");
					String coreBody = getExecutionFunctionBody(op);
					sb.append(coreBody + "\n");
					sb.append("end" + "\n\n");
				}
			}
		}






		return sb.toString();
	}
	
	public String getChangingFunctionsDeclarations(PackageDeclarationCS pck, String root){
		StringBuilder sb = new StringBuilder();
		
		for(ContextDeclCS ct : getContextsFromPack(pck))
		{
			ClassifierContextDeclCS cct = (ClassifierContextDeclCS) ct;
			for(Operation op : cct.getClassifier().getOwnedOperation())
			{
				boolean coreChange = getCoreChangesValue(cct, op); 
				if(coreChange==true)
				{
					String returnType = getReturnType(cct,op);
					String params = getParamList(cct, op);
					sb.append("[for (element : "+cct.getClassifier().getName()+"| a"+root+".eAllContents("+cct.getClassifier().getName()+"))]");
					sb.append("function " + cct.getClassifier().getName()+"_"+op.getName()+"_"+"[element.name/]" + " (" + params + ") :" + returnType +" is"+"\n");
					sb.append("begin"+ "\n");
					String coreBody = getExecutionFunctionBody(op);
					sb.append(coreBody + "\n");
					sb.append("end" + "\n\n");
					sb.append("[/for]\n");
				}
			}
		}

		return sb.toString();
	}
	
	public String getExecutionFunctionBody(Operation op){
		StringBuilder sb = new StringBuilder();
		sb.append("TODO");
		return sb.toString();
	}
	
	// Return TYpe by Functions
	public String getReturnType(ClassifierContextDeclCS cs, Operation op)
	{
		StringBuilder sb = new StringBuilder();
		//System.out.println("=======================================");
		for(Property attr : cs.getClassifier().getOwnedAttribute())
		{
			//System.out.println(attr.getName());	
			if(attr.getName().startsWith("nonED"))
			{
				if(attr.getName().contains(op.getName()))
				{
					if(attr.getName().contains("freturn"))
					{
						//System.out.println("============"+attr.getDefaultExpression());
						String value = attr.getDefault().toString();
						sb.append(value);
					}
				}
			}
		}
		//System.out.println("=======================================");
		return sb.toString();
	}
	
	/*public String getReturnType(ClassifierContextDeclCS cs)
	{
		StringBuilder sb = new StringBuilder();
		System.out.println("=======================================");
		for(Property attr : cs.getClassifier().getOwnedAttribute())
		{
			System.out.println(attr.getName());	
			if(attr.getName().startsWith("nonED"))
			{
				if(attr.getName().contains("freturn"))
					{
						System.out.println("============"+attr.getDefaultExpression());
						String value = attr.getDefault().toString();
						sb.append(value);
					}
			}
		}
		System.out.println("=======================================");
		return sb.toString();
	}*/
	
	public String getParamList(ClassifierContextDeclCS cs, Operation op)
	{
		StringBuilder sb = new StringBuilder();
		for(Property attr : cs.getClassifier().getOwnedAttribute())
		{
			if(attr.getName().startsWith("nonED"))
			{
				if(attr.getName().contains(op.getName()))
				{
					if(attr.getName().contains("lparam"))
					{
						String value = attr.getDefault().toString();
						sb.append(value);
					}
				}
			}
		}
		return sb.toString();
	}
	
	public boolean getCoreChangesValue(ClassifierContextDeclCS cs, Operation op)
	{
		boolean change = false;
		for(Property attr : cs.getClassifier().getOwnedAttribute())
		{
			if(attr.getName().startsWith("nonED"))
			{
				if(attr.getName().contains(op.getName()))
				{
					if(attr.getName().contains("fcore"))
					{
						String value = attr.getDefault().toString();
						if(value.equals("Y")) change = true;
					}
				}
			}
		}
		return change;
	}
	
	public String getBodyExpression(PackageDeclarationCS pck, String operatioName, String xtendAbsolutePath)
	{
		String result = "";
		System.out.println("Check Operation : " + operatioName+ " IN -> " +xtendAbsolutePath);
		System.out.println("========================================================================");
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(xtendAbsolutePath));
			String aLine;
			while ((aLine = br.readLine())!=null)
			{
				System.out.println(aLine);
			}
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("========================================================================");
		return result;
	}
	
	public String INITIAL_Configuration (PackageDeclarationCS pck, String root){
		
		// TODO 
		StringBuilder sb = new StringBuilder();
		return sb.toString();
	}
}
