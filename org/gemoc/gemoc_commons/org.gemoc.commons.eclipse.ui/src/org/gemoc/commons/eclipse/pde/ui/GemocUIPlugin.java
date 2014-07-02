package org.gemoc.commons.eclipse.pde.ui;

import java.util.ArrayList;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.gemoc.commons.eclipse.logging.ILoggingBackend;
import org.gemoc.commons.eclipse.pde.IPlugin;

public abstract class GemocUIPlugin extends AbstractUIPlugin implements IPlugin {

	/**
	 * Get the plugin id as specified in the manifest.
	 * @return
	 */
	public abstract String getId();
	
	/**
	 * Get the plugin name as specified in the manifest.
	 * @return
	 */
	public String getName()
	{
		return this.getBundle().getHeaders().get("Bundle-Name");
	}
	
	private ArrayList<ILoggingBackend> _loggingBackends = new ArrayList<ILoggingBackend>();
	public void addLoggingBackend(ILoggingBackend backend) 
	{
		if (backend == null)
			throw new IllegalArgumentException("backend");
		_loggingBackends.add(backend);
	}
	
	public void debug(String message) {
		
		for(ILoggingBackend b : _loggingBackends)
			b.debug(message);
	}

	public void error(String message) {
		for(ILoggingBackend b : _loggingBackends)
			b.error(message);
	}
	
	public void info(String message) {
		for(ILoggingBackend b : _loggingBackends)
			b.info(message);
	}
	
	public void warn(String message) {
		for(ILoggingBackend b : _loggingBackends)
			b.warn(message);
	}
	
	public void error(String message, Throwable e) {
		for(ILoggingBackend b : _loggingBackends)
			b.error(message, e);
	}
	
	public void warn(String message, Throwable e) {
		for(ILoggingBackend b : _loggingBackends)
			b.warn(message, e);
	}
	
	public void info(String message, Throwable e) {
		for(ILoggingBackend b : _loggingBackends)
			b.info(message, e);
	}

}