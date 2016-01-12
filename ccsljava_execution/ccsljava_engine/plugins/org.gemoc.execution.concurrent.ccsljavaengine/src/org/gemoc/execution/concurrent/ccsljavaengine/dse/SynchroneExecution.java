package org.gemoc.execution.concurrent.ccsljavaengine.dse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.gemoc.execution.concurrent.ccsljavaxdsml.api.core.IConcurrentExecutionEngine;
import org.gemoc.execution.concurrent.ccsljavaxdsml.api.dsa.executors.CodeExecutionException;
import org.gemoc.executionframework.engine.Activator;
import org.gemoc.executionframework.engine.core.CommandExecution;
import org.gemoc.executionframework.engine.mse.MSEOccurrence;
import org.gemoc.xdsmlframework.api.engine_addon.IEngineAddon;

public class SynchroneExecution extends OperationExecution 
{

	protected SynchroneExecution(MSEOccurrence mseOccurrence, IConcurrentExecutionEngine engine) 
	{
		super(mseOccurrence, engine);
	}

	@Override
	public void run() 
	{
		for (IEngineAddon addon : getEngine().getExecutionContext().getExecutionPlatform().getEngineAddons()) 
		{
			addon.aboutToExecuteMSEOccurrence(getEngine(), getMSEOccurrence());
		}
		Object res = callExecutor();
		setResult(res);
		try {
			applyAnimationTime();
		} catch (InterruptedException e) {
			Activator.getDefault().error("Exception received " + e.getMessage(), e);
		}
		for (IEngineAddon addon : getEngine().getExecutionContext().getExecutionPlatform().getEngineAddons()) 
		{
			addon.mseOccurrenceExecuted(getEngine(), getMSEOccurrence());
		}
	}
	
	/**
	 * Calls the {@link EventExecutor} for the given
	 * {@link EngineEventOccurence}.
	 * 
	 * @param mse
	 *            the {@link EngineEventOccurence} to execute
	 * @return the {@link FeedbackData} if any, <code>null</code> other wise
	 */
	private Object callExecutor() 
	{
		final TransactionalEditingDomain editingDomain = TransactionalEditingDomain.Factory.INSTANCE.getEditingDomain(getExecutionContext().getResourceModel().getResourceSet());
		Object res = null;
		if (editingDomain != null) {
			final RecordingCommand command = new RecordingCommand(editingDomain, "execute engine event occurence " + getMSEOccurrence()) {
				private List<Object> result = new ArrayList<Object>();

				@Override
				protected void doExecute() {
					try {
						result.add(getExecutionContext().getConcurrentExecutionPlatform().getCodeExecutor().execute(getMSEOccurrence()));
					} catch (CodeExecutionException e) {
						Activator.getDefault().error("Exception received " + e.getMessage(), e);
					}
				}

				@Override
				public Collection<?> getResult() {
					return result;
				}
			};
			res = CommandExecution.execute(editingDomain, command);
		} else {
			try {
				res = getExecutionContext().getConcurrentExecutionPlatform().getCodeExecutor().execute(getMSEOccurrence());
			} catch (CodeExecutionException e) { 
				Activator.getDefault().error("Exception received " + e.getMessage(), e);
			}
		}
		return res;
	}
	
	private void applyAnimationTime() throws InterruptedException {
		int animationDelay = getEngine().getExecutionContext().getRunConfiguration().getAnimationDelay();								
		if (animationDelay != 0) 
		{
			Thread.sleep(animationDelay);
		}
	}

}
