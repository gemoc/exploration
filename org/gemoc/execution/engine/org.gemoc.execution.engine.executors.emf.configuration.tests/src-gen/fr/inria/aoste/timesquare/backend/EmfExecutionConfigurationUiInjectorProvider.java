/*
 * generated by Xtext
 */
package fr.inria.aoste.timesquare.backend;

import org.eclipse.xtext.junit4.IInjectorProvider;

import com.google.inject.Injector;

public class EmfExecutionConfigurationUiInjectorProvider implements IInjectorProvider {

    @Override
    public Injector getInjector() {
        return fr.inria.aoste.timesquare.backend.ui.internal.EmfExecutionConfigurationActivator.getInstance()
                .getInjector("fr.inria.aoste.timesquare.backend.EmfExecutionConfiguration");
    }

}
