package org.gemoc.gemoc_modeling_workbench.concurrent.ui.views.step;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.gemoc.execution.engine.mse.engine_mse.LogicalStep;
import org.gemoc.executionengine.ccsljava.api.core.IConcurrentExecutionEngine;
import org.gemoc.xdsmlframework.api.core.EngineStatus.RunStatus;

public class LogicalStepsViewContentProvider implements ITreeContentProvider {

	@Override
	public void dispose() 
	{
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) 
	{
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof IConcurrentExecutionEngine)
		{
			IConcurrentExecutionEngine engine = (IConcurrentExecutionEngine)inputElement;
			if (engine.getRunningStatus().equals(RunStatus.Stopped))
			{
				String message = "Engine is not running";
				return new Object[] {
					message
				};				
			}
			else
			{
				if (engine.getPossibleLogicalSteps() != null)
				{
					return engine.getPossibleLogicalSteps().toArray();				
				}
				else
				{
					return new Object[] {};
				}
			}
		}
		else if (inputElement instanceof LogicalStep)
		{
			LogicalStep ls = (LogicalStep)inputElement;
			return ls.getMseOccurrences().toArray();
		}
		return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IConcurrentExecutionEngine)
		{
			IConcurrentExecutionEngine engine = (IConcurrentExecutionEngine)parentElement;
			return engine.getPossibleLogicalSteps().toArray();
		}
		else if (parentElement instanceof LogicalStep)
		{
			LogicalStep ls = (LogicalStep)parentElement;
			return ls.getMseOccurrences().toArray();
		}
		return new Object[0];	
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) 
	{
		if (element instanceof IConcurrentExecutionEngine)
		{
			IConcurrentExecutionEngine engine = (IConcurrentExecutionEngine)element;
			return engine.getPossibleLogicalSteps().size() > 0;
		}
		else if (element instanceof LogicalStep)
		{
			LogicalStep ls = (LogicalStep)element;
			return ls.getMseOccurrences().size() > 0;
		}
		return false;	
	}



}
