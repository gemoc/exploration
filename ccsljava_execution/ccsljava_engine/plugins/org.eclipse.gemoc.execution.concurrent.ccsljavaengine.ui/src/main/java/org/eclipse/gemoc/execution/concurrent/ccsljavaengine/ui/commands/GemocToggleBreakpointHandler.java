package org.eclipse.gemoc.execution.concurrent.ccsljavaengine.ui.commands;

import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.gemoc.execution.concurrent.ccsljavaengine.ui.launcher.Launcher;
import org.eclipse.gemoc.execution.concurrent.ccsljavaengine.ui.views.stimulimanager.ModelSpecificEventWrapper;
import org.eclipse.gemoc.executionframework.debugger.GemocBreakpoint;

import fr.inria.aoste.timesquare.ecl.feedback.feedback.ModelSpecificEvent;
import org.eclipse.gemoc.trace.commons.model.trace.MSEOccurrence;
import org.eclipse.gemoc.dsl.debug.ide.DSLBreakpoint;
import org.eclipse.gemoc.dsl.debug.ide.sirius.ui.DSLToggleBreakpointsUtils;

public class GemocToggleBreakpointHandler extends AbstractHandler {

	/**
	 * The {@link DSLToggleBreakpointsUtils}.
	 */
	protected final DSLToggleBreakpointsUtils breakpointUtils;
	 org.eclipse.ui.ide.IGotoMarker f;
	/**
	 * Constructor.
	 */
	public GemocToggleBreakpointHandler() {
		breakpointUtils = new DSLToggleBreakpointsUtils(Launcher.MODEL_ID) {
			
			@Override
			protected EObject getInstruction(Object selected) {
				final EObject res;
				
				if (selected instanceof ModelSpecificEventWrapper) 
				{
					res = ((ModelSpecificEventWrapper) selected).getMSE();
				} 
				else if (selected instanceof MSEOccurrence)
				{
					res = ((MSEOccurrence) selected).getMse();				
				} 
				else 
				{
					res = super.getInstruction(selected);
				}

				return res;
			}
			
			@Override
			protected DSLBreakpoint createBreakpoint(Object selected,
					EObject instruction) throws CoreException {
				return new GemocBreakpoint(identifier, instruction, true);
			}
			
		};
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.commands.AbstractHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		final ISelection selection = HandlerUtil
				.getCurrentSelectionChecked(event);
		try {
			breakpointUtils.toggleBreakpoints(selection);
		} catch (CoreException e) {
			throw new ExecutionException("Error while toggling breakpoint.", e);
		}

		return null;
	}
	
	@Override
	public boolean isEnabled() {
		final boolean res;
		
		ISelectionService service = (ISelectionService) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getService(ISelectionService.class);
		if (service != null) {
			final ISelection selection = service.getSelection();
			if (selection instanceof IStructuredSelection) {
				boolean allMSE = true;
				final Iterator<?> it = ((IStructuredSelection) selection).iterator();
				while (allMSE && it.hasNext()) {
					Object current = it.next();
					if (current instanceof ModelSpecificEventWrapper) {
						current = ((ModelSpecificEventWrapper) current).getMSE();
					}
					allMSE = current instanceof ModelSpecificEvent && ((ModelSpecificEvent) current).getAction() != null;
				}
				res = allMSE;
			} else {
				res = false;
			}
		} else {
			res = false;
		}
		
		return res;
	}
	
}