package fr.inria.aoste.timesquare.backend;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.ISetup;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Generated from StandaloneSetup.xpt!
 */
@SuppressWarnings("all")
public class EmfExecutionConfigurationStandaloneSetupGenerated implements ISetup {

    public Injector createInjector() {
        return Guice.createInjector(new fr.inria.aoste.timesquare.backend.EmfExecutionConfigurationRuntimeModule());
    }

    public Injector createInjectorAndDoEMFRegistration() {
        org.eclipse.xtext.common.TerminalsStandaloneSetup.doSetup();

        Injector injector = this.createInjector();
        this.register(injector);
        return injector;
    }

    public void register(Injector injector) {
        if (!EPackage.Registry.INSTANCE
                .containsKey("http://www.inria.fr/aoste/timesquare/backend/EmfExecutionConfiguration")) {
            EPackage.Registry.INSTANCE
                    .put("http://www.inria.fr/aoste/timesquare/backend/EmfExecutionConfiguration",
                            fr.inria.aoste.timesquare.backend.emfExecutionConfiguration.EmfExecutionConfigurationPackage.eINSTANCE);
        }

        org.eclipse.xtext.resource.IResourceFactory resourceFactory = injector
                .getInstance(org.eclipse.xtext.resource.IResourceFactory.class);
        org.eclipse.xtext.resource.IResourceServiceProvider serviceProvider = injector
                .getInstance(org.eclipse.xtext.resource.IResourceServiceProvider.class);
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("emfExecConf", resourceFactory);
        org.eclipse.xtext.resource.IResourceServiceProvider.Registry.INSTANCE.getExtensionToFactoryMap().put(
                "emfExecConf", serviceProvider);

    }
}
