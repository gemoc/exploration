/*******************************************************************************
 * Copyright (c) 2013 Obeo. All Rights Reserved.
 *
 * This software and the attached documentation are the exclusive ownership
 * of its authors and was conceded to the profit of Obeo SARL.
 * This software and the attached documentation are protected under the rights
 * of intellectual ownership, including the section "Titre II  Droits des auteurs (Articles L121-1 L123-12)"
 * By installing this software, you acknowledge being aware of this rights and
 * accept them, and as a consequence you must:
 * - be in possession of a valid license of use conceded by Obeo only.
 * - agree that you have read, understood, and will comply with the license terms and conditions.
 * - agree not to do anything that could conflict with intellectual ownership owned by Obeo or its beneficiaries
 * or the authors of this software
 *
 * Should you not agree with these terms, you must stop to use this software and give it back to its legitimate owner.
 *
 *******************************************************************************/
package org.gemoc.gemoc_language_workbench.process.specific;

import fr.obeo.dsl.process.ActionTask;
import fr.obeo.dsl.process.Activator;
import fr.obeo.dsl.process.IllegalVariableAccessException;
import fr.obeo.dsl.process.ProcessUtils;
import fr.obeo.dsl.process.ProcessVariable;
import fr.obeo.dsl.process.impl.ProcessContextImpl;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.gemoc.gemoc_language_workbench.conf.DomainModelProject;
import org.gemoc.gemoc_language_workbench.conf.LanguageDefinition;
import org.gemoc.gemoc_language_workbench.process.utils.EMFResource;
import org.gemoc.gemoc_language_workbench.process.utils.EclipseResource;

public class GemocLanguageProcessContext extends ProcessContextImpl {

	// convenient data that are maintained by some of the task in order to ease the search from other tasks
	// this
	// private GemocLanguageWorkbenchConfiguration _xdsmlModel;

	// private URI _xdsmlURI = null;

	/** String used to retreive an GemocLanguageWorkbenchConfiguration model element */
	public static final String XDSML_MODEL_VAR = "XDSML_MODEL";

	/** String used to retreive the URI of the xsdml */
	public static final String XDSML_FILE_URI_VAR = "XDSML_FILE_URI";

	/**
	 * this data is indirectly retreived from the xdsml (via the reference to the genmodel that in turn
	 * reference the ecore file)
	 */
	/** String used to retreive the IFile of the ecore of the domain */
	public static final String ECORE_IFILE_VAR = "ECORE_IFILE";

	public GemocLanguageProcessContext() {
		super();
	}

	public void initialize(URI newUri) {
		if (newUri != null) {
			setName("Gemoc process: " + newUri.toPlatformString(true));
			//CHECKSTYLE:OFF
			try {
				ActionTask initialTask = ProcessUtils.getInitialActionTasks(getDefinition()).get(0);
				setXdsmlConfigURI(newUri, initialTask);
			} catch (IllegalVariableAccessException e) {
				Activator.getDefault().error(e);
			//CHECKSTYLE:OFF	
			} catch (Exception e) {
				// load of the xdsml model may fail if the file is ill-formed
				Activator.getDefault().error(e);
			}
			//CHECKSTYLE:ON
		}
	}

	public void setXdsmlConfigURI(URI newUri, ActionTask writterTask) throws IllegalVariableAccessException {
		if (newUri != null) {
			if (!newUri.equals(getVariableValue(XDSML_FILE_URI_VAR, writterTask))) {
				this.setVariableValue(XDSML_FILE_URI_VAR, newUri, writterTask);
				loadXdsmlConfigURI(writterTask);
			}
		}
	}

	private Object getVariableValue(String varName, ActionTask task) throws IllegalVariableAccessException {
		ProcessVariable processVar = getProcessVariable(varName);
		if (processVar == null) {
			throw new IllegalVariableAccessException("No ProcessVariable " + varName + " in current ProcessContext");
		}
		return getVariableValue(processVar, task);
	}

	public void loadXdsmlConfigURI(ActionTask writterTask) throws IllegalVariableAccessException {
		URI xdsmlURI = (URI)getVariableValue(XDSML_FILE_URI_VAR, writterTask);
		if (xdsmlURI != null) {
			this.setVariableValue(XDSML_MODEL_VAR, EMFResource.getFirstContent(xdsmlURI), writterTask);
		} else {
			this.setVariableValue(XDSML_MODEL_VAR, null, writterTask);
		}
	}

	// getter and setters
	// ------------------

	public LanguageDefinition getXdsmlModel(ActionTask task) {
		try {
			return (LanguageDefinition)this.getVariableValue(XDSML_MODEL_VAR, task);
		} catch (IllegalVariableAccessException e) {
			Activator.getDefault().error(e);
		}
		return null;
	}

	public URI getXdsmlURI(ActionTask task) {
		try {
			return (URI)getVariableValue(XDSML_FILE_URI_VAR, task);
		} catch (IllegalVariableAccessException e) {

			Activator.getDefault().error(e);
		}
		return null;
	}

	public IFile getXdsmlFile(ActionTask task) {
		IFile file = null;
		URI xdsmlURI;
		try {
			xdsmlURI = (URI)getVariableValue(XDSML_FILE_URI_VAR, task);
			if (xdsmlURI != null) {
				file = EclipseResource.getFile(xdsmlURI);
			}
		} catch (IllegalVariableAccessException e) {
			Activator.getDefault().error(e);
		}
		return file;
	}

	public IFile getEcoreIFile(ActionTask task) {
		IFile ecoreIFile = null;
		try {
			ecoreIFile = (IFile)getVariableValue(ECORE_IFILE_VAR, task);
		} catch (IllegalVariableAccessException e) {
			Activator.getDefault().error(e);
		}
		return ecoreIFile;
	}

	public void setEcoreIFile(IFile ecoreIFile, ActionTask writterTask) throws IllegalVariableAccessException {
		setVariableValue(ECORE_IFILE_VAR, ecoreIFile, writterTask);
	}

	private void setVariableValue(String varName, Object variableValue, ActionTask writterTask) throws IllegalVariableAccessException {
		ProcessVariable processVar = getProcessVariable(varName);
		if (processVar == null) {
			throw new IllegalVariableAccessException("No ProcessVariable " + varName + " in current ProcessContext");
		}
		setVariableValue(processVar, variableValue, writterTask);
	}

	public DomainModelProject getEcoreProject(ActionTask task) {
		return getXdsmlModel(task).getDomainModelProject();
	}
	
	public String getEcoreProjectName(ActionTask task) {
		return getXdsmlModel(task).getDomainModelProject().getProjectName();
	}

}