package org.gemoc.execution.engine.commons.dsa.executors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import org.eclipse.emf.ecore.resource.Resource;
import org.gemoc.execution.engine.commons.Activator;
import org.gemoc.execution.engine.commons.dsa.DataConverter;
import org.gemoc.execution.engine.commons.dsa.sentinels.Kermeta3BytecodeSentinel;
import org.gemoc.gemoc_language_workbench.api.dsa.BytecodeSentinel;
import org.gemoc.gemoc_language_workbench.api.dsa.DomainSpecificAction;
import org.gemoc.gemoc_language_workbench.api.dsa.Executor;
import org.gemoc.gemoc_language_workbench.api.dse.DomainSpecificEvent;
import org.gemoc.gemoc_language_workbench.api.feedback.FeedbackData;

public class Kermeta3Executor implements Executor {

	private BytecodeSentinel sentinel = null;

	public Kermeta3Executor(ClassLoader classLoader, String bundleSymbolicName) {
		this.sentinel = new Kermeta3BytecodeSentinel(classLoader,
				bundleSymbolicName);
//		Thread
//		.currentThread().getContextClassLoader(),
//		"org.gemoc.sample.tfsm.k3dsa")
	}

	@Override
	public FeedbackData execute(DomainSpecificAction dsa) {
		Object[] params = dsa.getParameters().toArray();
		Method method = this.sentinel.getMethodFromAction(dsa);
		try {
			return DataConverter.convertToFeedbackData(method.invoke(null, params));
		} catch (IllegalArgumentException e) {
			String errorMessage = e.getClass().getSimpleName() + " when trying to execute a Kermeta3 method";
			Activator.getMessagingSystem().error(errorMessage, Activator.PLUGIN_ID);
			Activator.error(errorMessage, e);
			return null;
		} catch (IllegalAccessException e) {
			String errorMessage = e.getClass().getSimpleName() + " when trying to execute a Kermeta3 method";
			Activator.getMessagingSystem().error(errorMessage, Activator.PLUGIN_ID);
			Activator.error(errorMessage, e);
			return null;
		} catch (InvocationTargetException e) {
			String errorMessage = e.getClass().getSimpleName() + " when trying to execute a Kermeta3 method";
			Activator.getMessagingSystem().error(errorMessage, Activator.PLUGIN_ID);
			Activator.error(errorMessage, e);
			Activator.error("Nested Exception", e.getCause());
			return null;
		}
	}

	@Override
	public FeedbackData execute(DomainSpecificEvent dse) {
		return this.execute(dse.getAction());
	}

	@Override
	public void setModel(Resource modelResource) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<BytecodeSentinel> getSentinels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addSentinel(BytecodeSentinel sentinel) {
		// TODO Auto-generated method stub

	}

}
