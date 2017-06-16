package org.eclipse.gemoc.execution.concurrent.ccsljavaxdsml.ui.dialogs;

import java.util.Arrays;

import org.eclipse.swt.widgets.Shell;
import org.eclipse.gemoc.commons.eclipse.ui.dialogs.SelectPluginIProjectWithFileExtensionDialog;

public class SelectDSEIProjectDialog extends SelectPluginIProjectWithFileExtensionDialog {

	public SelectDSEIProjectDialog(Shell parentShell) {
		super(parentShell, Arrays.asList("ecl"));
	}

}