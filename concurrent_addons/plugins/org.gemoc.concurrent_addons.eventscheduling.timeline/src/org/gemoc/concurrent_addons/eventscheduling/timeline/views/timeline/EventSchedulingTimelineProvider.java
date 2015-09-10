package org.gemoc.concurrent_addons.eventscheduling.timeline.views.timeline;

import java.util.Collection;

import org.gemoc.execution.engine.trace.gemoc_execution_trace.Branch;
import org.gemoc.execution.engine.trace.gemoc_execution_trace.Choice;
import org.gemoc.execution.engine.trace.gemoc_execution_trace.ExecutionTraceModel;
import org.gemoc.execution.engine.trace.gemoc_execution_trace.Gemoc_execution_traceFactory;
import org.gemoc.execution.engine.trace.gemoc_execution_trace.LogicalStep;
import org.gemoc.execution.engine.trace.gemoc_execution_trace.MSEOccurrence;
import org.gemoc.executionengine.ccsljava.engine.eventscheduling.trace.EventSchedulingModelExecutionTracingAddon;
import org.gemoc.executionframework.ui.utils.ViewUtils;
import org.gemoc.gemoc_language_workbench.api.core.EngineStatus.RunStatus;
import org.gemoc.gemoc_language_workbench.api.core.IDisposable;
import org.gemoc.gemoc_language_workbench.api.core.IBasicExecutionEngine;
import org.gemoc.gemoc_language_workbench.api.engine_addon.IEngineAddon;

import fr.obeo.timeline.view.AbstractTimelineProvider;

public class EventSchedulingTimelineProvider extends AbstractTimelineProvider implements IEngineAddon, IDisposable {

	private IBasicExecutionEngine _engine;
	private EventSchedulingModelExecutionTracingAddon _tracingAddon;
	
	public EventSchedulingTimelineProvider(IBasicExecutionEngine engine) {
		_engine = engine;
		_engine.getExecutionContext().getExecutionPlatform().addEngineAddon(this);
	}
	
	private ExecutionTraceModel getExecutionTrace() {
		ExecutionTraceModel traceModel = null;;
		if (_engine.hasAddon(EventSchedulingModelExecutionTracingAddon.class))
		{
			_tracingAddon = _engine.getAddon(EventSchedulingModelExecutionTracingAddon.class);
			traceModel = _tracingAddon.getExecutionTrace();			
		}
		else
		{
			traceModel = Gemoc_execution_traceFactory.eINSTANCE.createExecutionTraceModel();
		}
		return traceModel;
	}

	private Branch getBranchAt(int branchIndex)
	{
		Branch result = null;
		if (getExecutionTrace() != null
			&& getExecutionTrace().getBranches().size() >= branchIndex)
		{
			result = getExecutionTrace().getBranches().get(branchIndex);			
		}
		return result;
	}
	
	public Choice getChoiceAt(int branchIndex, int executionStepIndex) 
	{
		Choice result = null;
		Branch branch = getBranchAt(branchIndex);
		if (branch != null
			&& (branch.getStartIndex() + branch.getChoices().size()) >= executionStepIndex)
		{
			int choiceIndex = executionStepIndex - branch.getStartIndex();
			if (choiceIndex >= 0)
			{
				result = branch.getChoices().get(choiceIndex);
			}
		}
		return result;
	}
	
	@Override
	public int getNumberOfBranches() 
	{
		int result = 0;
		if (getExecutionTrace() != null)
		{
			result = getExecutionTrace().getBranches().size();			
		}
		return result;
	}
	
	@Override
	public int getStart(int branchIndex) 
	{
		int result = 0;
		Branch branch = getBranchAt(branchIndex);
		if (branch != null)
		{
			result = branch.getStartIndex();
		}
		return result;
	}

	@Override
	public int getEnd(int branchIndex) 
	{
		int result = 0;
		Branch branch = getBranchAt(branchIndex);
		if (branch != null)
		{
			result = branch.getStartIndex() + branch.getChoices().size();
		}
		return result;
	}

	@Override
	public int getNumberOfPossibleStepsAt(int branchIndex, int executionStepIndex) {
		int numberOfPossibleSteps = 0;
		Choice choice = getChoiceAt(branchIndex, executionStepIndex);
		if (choice != null)
		{
			numberOfPossibleSteps = choice.getPossibleLogicalSteps().size();				
		}
		return numberOfPossibleSteps;
	}

	@Override
	public String getTextAt(int branchIndex) {
		return "Current execution";
	}
	
	@Override
	public String getTextAt(int branchIndex, int index) {
		return String.valueOf(index);
	}

	@Override
	public Object getAt(int branchIndex, int executionStepIndex, int logicalStepIndex) 
	{
		Object result = null;
		Choice choice = getChoiceAt(branchIndex, executionStepIndex);
		if (choice != null)
		{
			if (choice.getPossibleLogicalSteps().size() >= logicalStepIndex)
			{
				result = choice.getPossibleLogicalSteps().get(logicalStepIndex);
			}
		}
		return result;
	}

	@Override
	public Object getAt(int branchIndex, int executionStepIndex) 
	{
		Choice choice = getChoiceAt(branchIndex, executionStepIndex);
		return choice;
	}

	@Override
	public String getTextAt(int branchIndex, int choiceIndex, int logicalStepIndex) 
	{
		StringBuilder builder = new StringBuilder();
		LogicalStep ls = (LogicalStep)getAt(branchIndex, choiceIndex, logicalStepIndex);
		for(MSEOccurrence mseOccurrence : ls.getMseOccurrences())
		{
			appendToolTipTextToBuilder(builder, mseOccurrence);
			builder.append(System.getProperty("line.separator"));
		}
		return builder.toString();
	}

	private void appendToolTipTextToBuilder(StringBuilder builder, MSEOccurrence mseOccurrence) {
		String s = "";
		if (mseOccurrence.getMse() != null)
			s = String.format("%-50s%s", mseOccurrence.getMse().getName(), ViewUtils.eventToString(mseOccurrence.getMse()));
		builder.append(s);
	}
	
	@Override
	public int[][] getFollowings(int branchIndex, int executionStepIndex, int logicalStepIndex) {
		int[][] res = {{branchIndex, -1}};
		Choice choice = getChoiceAt(branchIndex, executionStepIndex);
		if (choice != null
			&& !choice.getNextChoices().isEmpty())
		{
			res = new int[choice.getNextChoices().size()][1];
			for (int i=0; i<choice.getNextChoices().size(); i++)
			{
				Choice next = choice.getNextChoices().get(i);
				Branch nextBranch = next.getBranch();
				int nextBranchNumber = getExecutionTrace().getBranches().indexOf(nextBranch);
				if (next.getChosenLogicalStep() != null)
				{
					int nextLogicalStepindex = next.getPossibleLogicalSteps().indexOf(next.getChosenLogicalStep());
					int content[] = {nextBranchNumber, nextLogicalStepindex};
					res[i] = content;						
				}
				else
				{
					int content[] = {nextBranchNumber, -1};
					res[i] = content;												
				}
			}			
		}	
		return res;
	}

	@Override
	public int[][] getPrecedings(int branchIndex, int executionStepIndex, int logicalStepIndex) {
		int[][] res = {{branchIndex, -1}};
		Choice choice = getChoiceAt(branchIndex, executionStepIndex);
		if (choice != null
			&& choice.getPreviousChoice() != null)
		{
			Choice previous = choice.getPreviousChoice();
			Branch previousBranch = previous.getBranch();
			int previousBranchNumber = getExecutionTrace().getBranches().indexOf(previousBranch);

			if (previous.getChosenLogicalStep() != null)
			{
				int previousLogicalStepindex = previous.getPossibleLogicalSteps().indexOf(previous.getChosenLogicalStep());
				int content[] = {previousBranchNumber, previousLogicalStepindex};
				res[0] = content;						
			}
			else
			{
				int content[] = {previousBranchNumber, -1};
				res[0] = content;												
			}
		}	
		return res;
	}


	private int _numberOfChoices = 0;
	private int _numberOfSteps = 0;
	
	private void update(IBasicExecutionEngine engine) 
	{
		if (engine == _engine
			&& getExecutionTrace() != null
			&& _tracingAddon != null
			&& _tracingAddon.getCurrentBranch() != null)		
		{
			Branch branch = _tracingAddon.getCurrentBranch();
			if (branch.getChoices().size() > 0)
			{
				int branchIndex = getExecutionTrace().getBranches().indexOf(branch);
				boolean mustNotify = false;

				Choice gemocChoice = branch.getChoices().get(branch.getChoices().size() - 1);
				if (gemocChoice.getPossibleLogicalSteps().size() != _numberOfSteps)
				{
					_numberOfSteps = gemocChoice.getPossibleLogicalSteps().size();
					mustNotify = true;
				}
				
				if (branch.getChoices().size() != _numberOfChoices)
				{
					_numberOfChoices = branch.getChoices().size();
					mustNotify = true;
				}
				
				mustNotify = true;
				
				if (mustNotify)
				{
					int stepIndex = branch.getStartIndex() + branch.getChoices().size();
					boolean isSelected = gemocChoice.getChosenLogicalStep() != null;
					notifyIsSelectedChanged(branchIndex,
							stepIndex,
							gemocChoice.getPossibleLogicalSteps().indexOf(gemocChoice.getChosenLogicalStep()), 
							isSelected);				
					notifyEndChanged(branchIndex, stepIndex);
					notifyStartChanged(branchIndex, branch.getStartIndex());					
				}
			}
		}
	}

	@Override
	public int getSelectedPossibleStep(int branchIndex, int executionStepIndex) 
	{
		int result = -1;
		Branch branch = getBranchAt(branchIndex);
		if (branch != null)
		{
			int choiceIndex = executionStepIndex - branch.getStartIndex();
			if (branch.getChoices().size() >= choiceIndex)
			{
				Choice choice = branch.getChoices().get(choiceIndex);
				if (choice.getSelectedNextChoice() != null)
				{
					result = choice.getPossibleLogicalSteps().indexOf(choice.getChosenLogicalStep());
				}
			}
		}
		return result;
	}

	@Override
	public void dispose() 
	{
		if (_engine != null)
		{
			_engine.getExecutionContext().getExecutionPlatform().removeEngineAddon(this);
		}
	}

	@Override
	public void engineAboutToStart(IBasicExecutionEngine engine) 
	{
	}

	@Override
	public void engineStarted(IBasicExecutionEngine executionEngine) 
	{
	}


	@Override
	public void aboutToExecuteLogicalStep(IBasicExecutionEngine executionEngine, LogicalStep logicalStepToApply) 
	{
	}

	@Override
	public void aboutToExecuteMSEOccurrence(IBasicExecutionEngine executionEngine, MSEOccurrence mseOccurrence) 
	{
	}

	@Override
	public void engineAboutToStop(IBasicExecutionEngine engine) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void engineStopped(IBasicExecutionEngine engine) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void aboutToSelectLogicalStep(IBasicExecutionEngine engine, Collection<LogicalStep> logicalSteps) 
	{
		update(engine);
	}

	@Override
	public void logicalStepSelected(IBasicExecutionEngine engine, LogicalStep selectedLogicalStep) 
	{
		update(engine);
	}

	@Override
	public void logicalStepExecuted(IBasicExecutionEngine engine,
			LogicalStep logicalStepExecuted) {
	}

	@Override
	public void mseOccurrenceExecuted(IBasicExecutionEngine engine, MSEOccurrence mseOccurrence) {
	}

	@Override
	public void engineStatusChanged(IBasicExecutionEngine engine, RunStatus newStatus) {
	}

	protected void setSelectedLogicalStep(LogicalStep ls) {
	}

	@Override
	public void proposedLogicalStepsChanged(IBasicExecutionEngine engine,
			Collection<LogicalStep> logicalSteps) {
		update(engine);
	}

	@Override
	public void engineAboutToDispose(IBasicExecutionEngine engine) {
	}



}