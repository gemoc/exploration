package org.eclipse.gemoc.execution.concurrent.ccsljavaengine.ui.launcher;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.gemoc.execution.concurrent.ccsljavaxdsml.api.core.IConcurrentRunConfiguration;
import org.eclipse.gemoc.executionframework.engine.ui.commons.RunConfiguration;

public class ConcurrentRunConfiguration extends RunConfiguration implements IConcurrentRunConfiguration{


	public static final String LAUNCH_SELECTED_DECIDER = "GEMOC_LAUNCH_SELECTED_DECIDER";
	public static final String DECIDER_SOLVER = "Solver decider";
	public static final String DECIDER_ASKUSER_STEP_BY_STEP = "Step by step user decider";
	public static final String EXTRA_TIMEMODEL_PATH = "TIMEMODEL_PATH";
	
	public ConcurrentRunConfiguration(ILaunchConfiguration launchConfiguration)
			throws CoreException {
		super(launchConfiguration);
		// TODO Auto-generated constructor stub
	}
	
	protected void extractInformation() throws CoreException {
		super.extractInformation();
		
		_deciderName = getAttribute(LAUNCH_SELECTED_DECIDER, "");
		_executionModelPath = getAttribute(EXTRA_TIMEMODEL_PATH, "");
	}
	
	protected String _deciderName;
	protected String _executionModelPath;
	
	@Override
	public String getDeciderName() {
		return _deciderName;
	}
	
	@Override
	public String getExecutionModelPath() {
		return _executionModelPath;
	}

}
