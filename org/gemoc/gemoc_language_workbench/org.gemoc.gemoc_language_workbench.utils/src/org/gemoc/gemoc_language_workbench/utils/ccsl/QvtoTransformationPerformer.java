package org.gemoc.gemoc_language_workbench.utils.ccsl;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.m2m.qvt.oml.BasicModelExtent;
import org.eclipse.m2m.qvt.oml.ExecutionContextImpl;
import org.eclipse.m2m.qvt.oml.ExecutionDiagnostic;
import org.eclipse.m2m.qvt.oml.ModelExtent;
import org.eclipse.m2m.qvt.oml.TransformationExecutor;
import org.eclipse.xtext.resource.IResourceServiceProvider;
import org.eclipse.xtext.resource.SaveOptions;
import org.eclipse.xtext.resource.SaveOptions.Builder;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;

import com.google.inject.Injector;

import fr.inria.aoste.timesquare.ccslkernel.model.TimeModel.ImportStatement;
import fr.inria.aoste.timesquare.ccslkernel.model.TimeModel.CCSLModel.ClockConstraintSystem;
import fr.inria.aoste.timesquare.ccslkernel.model.TimeModel.CCSLModel.impl.ClockConstraintSystemImpl;
import fr.inria.aoste.timesquare.ccslkernel.parser.xtext.ExtendedCCSLStandaloneSetup;

public class QvtoTransformationPerformer {

	private XtextResourceSet aModelResourceSet=null;
	private XtextResourceSet outputResourceSet=null;

	/**
	 * just initialization stuff from xtext for an ecl resource
	 */
	private void initializeXtext(){
		ExtendedCCSLStandaloneSetup ess= new ExtendedCCSLStandaloneSetup();
		Injector injector = ess.createInjector();
		// instanciate a resource set
		aModelResourceSet = injector.getInstance(XtextResourceSet.class);
		outputResourceSet = injector.getInstance(XtextResourceSet.class);
		//set.setClasspathURIContext(getClasspathURIContext());
		aModelResourceSet.addLoadOption(XtextResource.OPTION_RESOLVE_ALL, Boolean.TRUE);
		ExtendedCCSLStandaloneSetup.doSetup();
	}
	
	public String run(String transformationPath, String modelPath, String outputPath) {		
	    URI transformationURI = URI.createPlatformPluginURI(transformationPath, false);
	    URI modelURI = URI.createPlatformResourceURI(modelPath, false);
		IResourceServiceProvider.Registry.INSTANCE.getResourceServiceProvider(modelURI);
		initializeXtext();
	    //model resource
	    Resource modelResource = aModelResourceSet.getResource(modelURI, true);
			   
	    HashMap<Object, Object> saveOptions = new HashMap<Object, Object>();
	    Builder aBuilder = SaveOptions.newBuilder();
	    SaveOptions anOption = aBuilder.getOptions();
	    anOption.addTo(saveOptions);
	    try {
	    	modelResource.load(saveOptions);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	    //transformationURI is the URI of qvto file
		TransformationExecutor executor = new TransformationExecutor(transformationURI);
		//inResource have the vaule of inModel
		ModelExtent input = new BasicModelExtent(modelResource.getContents());
		ModelExtent output = new BasicModelExtent();

		ExecutionContextImpl context = new ExecutionContextImpl();

		ExecutionDiagnostic diagnostic = executor.execute(context, input, output);
		System.out.println(diagnostic);
		//output resource saving
	    
	    URI outputUri = URI.createPlatformResourceURI(outputPath, false);
	    Resource outputResource = null;
	    try{
	    	outputResource = outputResourceSet.createResource(outputUri);
	    }catch( Exception e){
	    	System.out.println(e);
	    	outputResource = outputResourceSet.createResource(outputUri);

	    //	outputResource = outputResourceSet.getResource(outputUri,true);
	    };
	    outputResource.getContents().addAll(output.getContents());
	    hackImportStatements(modelURI, output);
	    
	    try {
			outputResource.save(null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return outputPath;
	}

	private void hackImportStatements(URI modelURI, ModelExtent output) {
		if (output.getContents().size() > 0
	    	&& output.getContents().get(0) instanceof ClockConstraintSystem) {
	    	ClockConstraintSystem system = (ClockConstraintSystem)output.getContents().get(0);
	    	for(ImportStatement i : system.getImports()) {
	    		if (!i.getImportURI().equals(modelURI.toString())) {
	    			String pluginBasedURI = i.getImportURI().replace("platform:/resource", "platform:/plugin");
	    			i.setImportURI(pluginBasedURI);
	    		}
	    	}
	    }
	}
	
}