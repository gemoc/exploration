/*
 * generated by Xtext
 */
package fr.inria.aoste.timesquare.backend.ui;

import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

import com.google.inject.Injector;

import fr.inria.aoste.timesquare.backend.ui.internal.EmfExecutionConfigurationActivator;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass.
 */
public class EmfExecutionConfigurationExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

    @Override
    protected Bundle getBundle() {
        return EmfExecutionConfigurationActivator.getInstance().getBundle();
    }

    @Override
    protected Injector getInjector() {
        return EmfExecutionConfigurationActivator.getInstance().getInjector(
                EmfExecutionConfigurationActivator.FR_INRIA_AOSTE_TIMESQUARE_BACKEND_EMFEXECUTIONCONFIGURATION);
    }

}
