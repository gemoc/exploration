package org.gemoc.execution.engine.io.views.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.gemoc.commons.eclipse.ui.ViewHelper;
import org.gemoc.execution.engine.io.views.ExecutionTraceView;


public class ShowClockControllerView extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ViewHelper.retrieveView(ExecutionTraceView.ID);
		return null;
	}


}
