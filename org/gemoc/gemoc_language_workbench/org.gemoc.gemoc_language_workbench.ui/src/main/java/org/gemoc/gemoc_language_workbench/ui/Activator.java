package org.gemoc.gemoc_language_workbench.ui;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.gemoc.gemoc_language_workbench.ui"; //$NON-NLS-1$

	public static final String GEMOC_PROJECT_CONFIGURATION_FILE_EXTENSION = "gemoc_language_conf";
	public static final String GEMOC_PROJECT_CONFIGURATION_FILE = "project."+GEMOC_PROJECT_CONFIGURATION_FILE_EXTENSION;

	
	// be careful do not use "." in the property name !!! because it is used to rebuild the qualified name from a string by ResourcePropertyTester
	public static final String GEMOC_PROJECT_PROPERTY_HAS_DOMAINMODEL = "gemoc_project_has_DomainModel";
	public static final String GEMOC_PROJECT_PROPERTY_HAS_DSA = "gemoc_project_has_DSA";
	public static final String GEMOC_PROJECT_PROPERTY_HAS_DSE = "gemoc_project_has_DSE";
	public static final String GEMOC_PROJECT_PROPERTY_HAS_MOC = "gemoc_project_has_MoC";
	public static final String GEMOC_PROJECT_PROPERTY_HAS_EDITOR = "gemoc_project_has_editor";
	public static final String GEMOC_PROJECT_PROPERTY_HAS_ANIMATOR = "gemoc_project_has_animator";
	
	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	public static void warn(String msg, Throwable e){
		Activator.getDefault().getLog().log(new Status(Status.WARNING, PLUGIN_ID,
                Status.OK, 
                msg, 
                e));
	}
	public static void error(String msg, Throwable e){
		Activator.getDefault().getLog().log(new Status(Status.ERROR, PLUGIN_ID,
                Status.OK, 
                msg, 
                e));
	}
	
}