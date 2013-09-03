/*
 * generated by Xtext
 */
package fr.inria.aoste.timesquare.backend.ui.internal;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.xtext.ui.shared.SharedStateModule;
import org.eclipse.xtext.util.Modules2;
import org.osgi.framework.BundleContext;

import com.google.common.collect.Maps;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass.
 */
public class EmfExecutionConfigurationActivator extends AbstractUIPlugin {

    public static final String FR_INRIA_AOSTE_TIMESQUARE_BACKEND_EMFEXECUTIONCONFIGURATION = "fr.inria.aoste.timesquare.backend.EmfExecutionConfiguration";

    private static final Logger logger = Logger.getLogger(EmfExecutionConfigurationActivator.class);

    private static EmfExecutionConfigurationActivator INSTANCE;

    public static EmfExecutionConfigurationActivator getInstance() {
        return EmfExecutionConfigurationActivator.INSTANCE;
    }

    private Map<String, Injector> injectors = Collections.synchronizedMap(Maps
            .<String, Injector> newHashMapWithExpectedSize(1));

    protected Injector createInjector(String language) {
        try {
            Module runtimeModule = this.getRuntimeModule(language);
            Module sharedStateModule = this.getSharedStateModule();
            Module uiModule = this.getUiModule(language);
            Module mergedModule = Modules2.mixin(runtimeModule, sharedStateModule, uiModule);
            return Guice.createInjector(mergedModule);
        } catch (Exception e) {
            EmfExecutionConfigurationActivator.logger.error("Failed to create injector for " + language);
            EmfExecutionConfigurationActivator.logger.error(e.getMessage(), e);
            throw new RuntimeException("Failed to create injector for " + language, e);
        }
    }

    public Injector getInjector(String language) {
        synchronized (this.injectors) {
            Injector injector = this.injectors.get(language);
            if (injector == null) {
                this.injectors.put(language, injector = this.createInjector(language));
            }
            return injector;
        }
    }

    protected Module getRuntimeModule(String grammar) {
        if (EmfExecutionConfigurationActivator.FR_INRIA_AOSTE_TIMESQUARE_BACKEND_EMFEXECUTIONCONFIGURATION
                .equals(grammar)) {
            return new fr.inria.aoste.timesquare.backend.EmfExecutionConfigurationRuntimeModule();
        }

        throw new IllegalArgumentException(grammar);
    }

    protected Module getSharedStateModule() {
        return new SharedStateModule();
    }

    protected Module getUiModule(String grammar) {
        if (EmfExecutionConfigurationActivator.FR_INRIA_AOSTE_TIMESQUARE_BACKEND_EMFEXECUTIONCONFIGURATION
                .equals(grammar)) {
            return new fr.inria.aoste.timesquare.backend.ui.EmfExecutionConfigurationUiModule(this);
        }

        throw new IllegalArgumentException(grammar);
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        EmfExecutionConfigurationActivator.INSTANCE = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        this.injectors.clear();
        EmfExecutionConfigurationActivator.INSTANCE = null;
        super.stop(context);
    }

}
