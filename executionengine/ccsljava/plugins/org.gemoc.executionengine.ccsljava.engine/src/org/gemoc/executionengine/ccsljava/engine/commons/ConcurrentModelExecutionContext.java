package org.gemoc.executionengine.ccsljava.engine.commons;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.gemoc.execution.engine.commons.EngineContextException;
import org.gemoc.execution.engine.commons.ModelExecutionContext;
import org.gemoc.executionengine.ccsljava.api.core.IConcurrentExecutionContext;
import org.gemoc.executionengine.ccsljava.api.core.IConcurrentExecutionPlatform;
import org.gemoc.executionengine.ccsljava.api.core.IConcurrentRunConfiguration;
import org.gemoc.executionengine.ccsljava.api.core.ILogicalStepDecider;
import org.gemoc.executionengine.ccsljava.api.extensions.languages.ConcurrentLanguageDefinitionExtension;
import org.gemoc.executionengine.ccsljava.api.extensions.languages.ConcurrentLanguageDefinitionExtensionPoint;
import org.gemoc.executionengine.ccsljava.engine.ui.LogicalStepDeciderFactory;
import org.gemoc.gemoc_language_workbench.api.core.ExecutionMode;
import org.gemoc.gemoc_language_workbench.api.core.IExecutionPlatform;
import org.gemoc.gemoc_language_workbench.api.extensions.languages.LanguageDefinitionExtension;

import fr.inria.aoste.timesquare.ecl.feedback.feedback.ActionModel;

public class ConcurrentModelExecutionContext extends ModelExecutionContext implements IConcurrentExecutionContext
{

	
	public ConcurrentModelExecutionContext(IConcurrentRunConfiguration runConfiguration, ExecutionMode executionMode)
			throws EngineContextException
	{
		super(runConfiguration, executionMode);
		try
		{

			setUpFeedbackModel();
			
			_logicalStepDecider = LogicalStepDeciderFactory.createDecider(runConfiguration.getDeciderName(),
					executionMode);
			
		} catch (CoreException e)
		{
			EngineContextException exception = new EngineContextException(
					"Cannot initialize the execution context, see inner exception.", e);
			throw exception;
		}
	}

	protected IExecutionPlatform createExecutionPlatform() throws CoreException{
		if(_languageDefinition instanceof  ConcurrentLanguageDefinitionExtension ){
			return new DefaultConcurrentExecutionPlatform((ConcurrentLanguageDefinitionExtension)_languageDefinition, _runConfiguration);
		} else {
			return super.createExecutionPlatform();
		}
	}
	
	@Override
	protected LanguageDefinitionExtension getLanguageDefinition(String languageName) throws EngineContextException
	{
		ConcurrentLanguageDefinitionExtension languageDefinition = ConcurrentLanguageDefinitionExtensionPoint
				.findDefinition(_runConfiguration.getLanguageName());
		if (languageDefinition == null)
		{
			String message = "Cannot find concurrent xdsml definition for the language " + _runConfiguration.getLanguageName()
					+ ", please verify that is is correctly deployed.";
			EngineContextException exception = new EngineContextException(message);
			throw exception;
		}
		return languageDefinition;
	}

	private void setUpFeedbackModel()
	{
		URI feedbackPlatformURI = URI.createPlatformResourceURI(getWorkspace().getMSEModelPath().removeFileExtension().addFileExtension("feedback").toString(),
				true);
		try
		{
			Resource resource = getResourceSet().getResource(feedbackPlatformURI, true);
			_feedbackModel = (ActionModel) resource.getContents().get(0);
		} catch (Exception e)
		{
			// file will be created later
		}
	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		_logicalStepDecider.dispose();
	}

	
	protected ActionModel _feedbackModel;

	@Override
	public ActionModel getFeedbackModel()
	{
		return _feedbackModel;
	}
	

	protected ILogicalStepDecider _logicalStepDecider;

	@Override
	public ILogicalStepDecider getLogicalStepDecider() {
		return _logicalStepDecider;
	}

	@Override
	public IConcurrentExecutionPlatform getConcurrentExecutionPlatform() {
		if(getExecutionPlatform() instanceof IConcurrentExecutionPlatform) return (IConcurrentExecutionPlatform) getExecutionPlatform();
		else return null;
	}

	@Override
	public ConcurrentLanguageDefinitionExtension getConcurrentLanguageDefinitionExtension() {
		if(getLanguageDefinitionExtension() instanceof ConcurrentLanguageDefinitionExtension) return (ConcurrentLanguageDefinitionExtension) getLanguageDefinitionExtension();
		return null;
	}




}
