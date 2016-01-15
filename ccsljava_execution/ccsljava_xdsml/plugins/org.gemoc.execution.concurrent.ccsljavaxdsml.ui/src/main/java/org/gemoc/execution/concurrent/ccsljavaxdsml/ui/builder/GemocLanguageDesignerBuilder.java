package org.gemoc.execution.concurrent.ccsljavaxdsml.ui.builder;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.gemoc.commons.eclipse.core.resources.FileFinderVisitor;
import org.gemoc.commons.eclipse.core.resources.GFile;
import org.gemoc.execution.concurrent.ccsljavaxdsml.api.extensions.languages.ConcurrentLanguageDefinitionExtensionPoint;
import org.gemoc.execution.concurrent.ccsljavaxdsml.concurrent_xdsml.ConcurrentLanguageDefinition;
import org.gemoc.execution.concurrent.ccsljavaxdsml.concurrent_xdsml.DSAProject;
import org.gemoc.execution.concurrent.ccsljavaxdsml.concurrent_xdsml.DSEProject;
import org.gemoc.execution.concurrent.ccsljavaxdsml.concurrent_xdsml.MoCCProject;
import org.gemoc.execution.concurrent.ccsljavaxdsml.ui.Activator;
import org.gemoc.executionframework.xdsml_base.DomainModelProject;
import org.gemoc.executionframework.xdsml_base.SiriusAnimatorProject;
import org.gemoc.executionframework.xdsml_base.SiriusEditorProject;
import org.gemoc.executionframework.xdsml_base.XTextEditorProject;
import org.gemoc.xdsmlframework.api.extensions.languages.LanguageDefinitionExtensionPoint;
import org.gemoc.xdsmlframework.ide.ui.builder.pde.PluginXMLHelper;
import org.jdom2.Element;
import org.osgi.framework.BundleException;

import fr.inria.diverse.commons.eclipse.pde.manifest.ManifestChanger;

public class GemocLanguageDesignerBuilder extends IncrementalProjectBuilder {

	
	public GemocLanguageDesignerBuilder() {
		return;
	}
	
	
	class LanguageProjectDeltaVisitor implements IResourceDeltaVisitor {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse
		 * .core.resources.IResourceDelta)
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			switch (delta.getKind()) {
				case IResourceDelta.ADDED:
					updateProjectPluginConfiguration(resource);
					checkConsistency(resource);
					break;
				case IResourceDelta.REMOVED:
					updateProjectPluginConfiguration(resource);
					checkConsistency(resource);
					break;
				case IResourceDelta.CHANGED:
					updateProjectPluginConfiguration(resource);
					checkConsistency(resource);
					break;
			}
			// return true to continue visiting children.
			return true;
		}

	}
	
	
	class LanguageProjectResourceVisitor implements IResourceVisitor {
		
		public boolean visit(IResource resource) {
			updateProjectPluginConfiguration(resource);
			checkConsistency(resource);
			// return true to continue visiting children.
			return true;
		}

	}

	public static final String BUILDER_ID = Activator.PLUGIN_ID+".gemocLanguageDesignerBuilder";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 * java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	public void checkConsistency(IResource resource){
		// TODO DVK, check consistency of plugin.xml according to existence of projects referenced in the xdsml  
	}
	
	
	/**
	 * Update plugin.xml according to the model
	 * 
	 * @param resource
	 */
	private void updateProjectPluginConfiguration(IResource resource) {
		if (resource instanceof IFile 
			&& resource.getName().equals(Activator.GEMOC_PROJECT_CONFIGURATION_FILE)) {
			IFile file = (IFile) resource;
			IProject project = file.getProject();
			// try {
			if (file.exists()) {
				Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
				Map<String, Object> m = registry.getExtensionToFactoryMap();
				m.put(Activator.GEMOC_PROJECT_CONFIGURATION_FILE_EXTENSION, new XMIResourceFactoryImpl());

				// Obtain a new resource set
				ResourceSet resSet = new ResourceSetImpl();

				// Create the resource
				Resource modelresource = resSet.getResource(URI.createURI(file.getLocationURI().toString()), true);
				ConcurrentLanguageDefinition languageDef =  (ConcurrentLanguageDefinition) modelresource.getContents().get(0);
				// get build option first
//				GemocLanguageWorkbenchConfiguration gemocLanguageWorkbenchConfiguration = (GemocLanguageWorkbenchConfiguration) modelresource.getContents().get(0);
//			    BuildOptions buildOptions = gemocLanguageWorkbenchConfiguration.getBuildOptions();
//			    if(buildOptions == null) buildOptions = confFactoryImpl.eINSTANCE.createBuildOptions();
			    // then look to all the content to do the work
				TreeIterator<EObject> it = modelresource.getAllContents();
				String languageRootElement = "";
				
				ManifestChanger manifestChanger = new ManifestChanger(project);
				try {
					while (it.hasNext()) {
						EObject eObject = (EObject) it.next();
						languageRootElement = updateManifestAndPlugin(project, languageRootElement, manifestChanger, eObject);
					}
					manifestChanger.commit();
					
				} catch (CoreException e) {
					Activator.error(e.getMessage(), e);				
				} catch (IOException e) {
					Activator.error(e.getMessage(), e);				
				} catch (BundleException e) {
					Activator.error(e.getMessage(), e);				
				} 
				
				// update entry in plugin.xdsml
				setPluginLanguageNameAndFilePath(project, languageDef.getName());
			}
		}
	}

	private String updateManifestAndPlugin(IProject project,
			String languageRootElement,
			ManifestChanger manifestChanger, EObject eObject)
			throws BundleException, IOException, CoreException {
		
		if (eObject instanceof ConcurrentLanguageDefinition) {
			ConcurrentLanguageDefinition languageDefinition =  (ConcurrentLanguageDefinition) eObject;
			if(languageDefinition.isNeedMelangeSynchronization()){
				MelangeGenerator melangeGenerator = new MelangeGenerator(project, languageDefinition);
				melangeGenerator.updateGeneratedMelange(manifestChanger);
			}
		}
		
		if (eObject instanceof DomainModelProject) {
			DomainModelProject domainModelProject = (DomainModelProject) eObject;
			updateDependenciesWithProject(manifestChanger, domainModelProject.getProjectName());
			updateModelLoaderClass(project, domainModelProject.getModelLoaderClass());
			if(domainModelProject.getModelLoaderClass() == null){
				manifestChanger.addPluginDependency(org.gemoc.executionframework.extensions.sirius.Activator.PLUGIN_ID);
			}
			languageRootElement = domainModelProject.getDefaultRootEObjectQualifiedName();
		}
		if (eObject instanceof DSAProject) {
			DSAProject dsaProject = (DSAProject) eObject;
			updateDependenciesWithDSAProject(manifestChanger, dsaProject);
			updateCodeExecutorClass(project, (ConcurrentLanguageDefinition) eObject.eContainer(), manifestChanger);
		}

		if (eObject instanceof XTextEditorProject) {
			XTextEditorProject xtextProject = (XTextEditorProject) eObject;
			updateDependenciesWithProject(manifestChanger, xtextProject.getProjectName());
		}
		if (eObject instanceof DSEProject) {
			DSEProject dseProject = (DSEProject) eObject;
			updateDependenciesWithProject(manifestChanger, dseProject.getProjectName());
			updateDependenciesWithDSEProject(manifestChanger, dseProject);
			updateQVTO(project, dseProject.getQvtoURI(), dseProject.getProjectName());
			updateSolverClass(project, dseProject.getSolverClass(), dseProject.getProjectName());
		}
		if( eObject instanceof MoCCProject){
			MoCCProject moccProject = (MoCCProject) eObject;
			updateDependenciesWithProject(manifestChanger, moccProject.getProjectName());
		}

		if( eObject instanceof SiriusEditorProject){
			SiriusEditorProject editorProject = (SiriusEditorProject) eObject;
			updateDependenciesWithProject(manifestChanger, editorProject.getProjectName());
		}
		if( eObject instanceof SiriusAnimatorProject){
			SiriusAnimatorProject animatorProject = (SiriusAnimatorProject) eObject;
			updateDependenciesWithProject(manifestChanger, animatorProject.getProjectName());
		}
		return languageRootElement;
	}
	
	
	/**
	 * create or replace existing CodeExecutorClass by an implementation that is
	 * able to execute method from the concrete DSA
	 * 
	 * @param project
	 * @param ld
	 * @param manifestChanger 
	 * @throws CoreException 
	 * @throws IOException 
	 * @throws BundleException 
	 */
	protected void updateCodeExecutorClass(IProject project, ConcurrentLanguageDefinition ld, ManifestChanger manifestChanger) throws BundleException, IOException, CoreException {
		// if codeExecutor null or empty, generate an executor class and updtae plugin.xml 
		if(ld.getDsaProject() != null ){
			if ( ld.getDsaProject().getCodeExecutorClass() == null || ld.getDsaProject().getCodeExecutorClass().isEmpty()){ 
				// create the java class
				String languageToUpperFirst = getLanguageNameWithFirstUpper(ld.getName());
				String packageName = getPackageName(ld.getName());
				String folderName = getFolderName(ld.getName());
				
				String fileContent = BuilderTemplates.CODEEXECUTOR_CLASS_TEMPLATE;
				fileContent = fileContent.replaceAll(
						Pattern.quote("${package.name}"), packageName);
				fileContent = fileContent.replaceAll(
						Pattern.quote("${language.name.toupperfirst}"),
						languageToUpperFirst);
				
				StringBuilder sbContent = new StringBuilder();
				StringBuilder sbImplementContent = new StringBuilder();
				StringBuilder sbAdditionalOperations = new StringBuilder();
					
					
				sbContent.append("// add K3 DSA specific executor\n");
				sbContent.append("\t\taddExecutor(new org.gemoc.execution.concurrent.ccsljavaengine.extensions.k3.dsa.impl.Kermeta3AspectsCodeExecutor(this,\n");
				sbContent.append("\t\t\t\""+ld.getDsaProject().getProjectName()+"\"));\n");
				
				sbImplementContent.append("\n\t\timplements org.gemoc.execution.concurrent.ccsljavaengine.extensions.k3.dsa.api.IK3DSAExecutorClassLoader ");
				
				sbAdditionalOperations.append("@Override\n"+
"	public Class<?> getClassForName(String className) throws ClassNotFoundException {\n"+
"		return Class.forName(className);\n"+
"	}\n"+
"	@Override\n"+
"	public java.io.InputStream getResourceAsStream(String resourceName) {\n"+
"		//this.getClass().getResourceAsStream(resourceName);\n"+
"		return Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);\n"+	
"	}");
					
				sbContent.append("\t\t// fall back executor : search classic java method\n");
				sbContent.append("\t\taddExecutor(new org.gemoc.execution.concurrent.ccsljavaengine.dsa.executors.JavaCodeExecutor());");
				fileContent = fileContent.replaceAll(
						Pattern.quote("${constructor.content}"), sbContent.toString());
				
				fileContent = fileContent.replaceAll(
						Pattern.quote("${implements.content}"), sbImplementContent.toString());
				
				fileContent = fileContent.replaceAll(
						Pattern.quote("${additional.operations}"), sbAdditionalOperations.toString());
				
				IFile file = project
						.getFile(Activator.EXTENSION_GENERATED_CLASS_FOLDER_NAME
								+ folderName + "/" + languageToUpperFirst
								+ Activator.CODEEXECUTOR_CLASS_NAMEPART + ".java");
				
				writeFile(file, fileContent);
				//ResourceUtil.writeFile(file, fileContent);
				
				// update plugin.xml with the generated codeexecutor
				setPluginCodeExecutorValue(project, packageName + "."	+ languageToUpperFirst + Activator.CODEEXECUTOR_CLASS_NAMEPART);
				
				// add the codeexutor package to exported packages
				manifestChanger.addExportPackage(packageName);
				
			} else {
				// update plugin.xml with the value in the xdsml
				setPluginCodeExecutorValue(project, ld.getDsaProject().getCodeExecutorClass());
			}
		}
	}

	protected void setPluginCodeExecutorValue(IProject project, String value){
		// update plugin.xml
		IFile pluginfile = project.getFile(PluginXMLHelper.PLUGIN_FILENAME);
		PluginXMLHelper.createEmptyTemplateFile(pluginfile, false);
		PluginXMLHelper helper = new PluginXMLHelper();
		helper.loadDocument(pluginfile);
		Element gemocExtensionPoint = helper.getOrCreateExtensionPoint(ConcurrentLanguageDefinitionExtensionPoint.GEMOC_CONCURRENT_LANGUAGE_EXTENSION_POINT);
		helper.updateXDSMLDefinitionAttributeInExtensionPoint(gemocExtensionPoint,
				ConcurrentLanguageDefinitionExtensionPoint.GEMOC_LANGUAGE_EXTENSION_POINT_XDSML_DEF_CODEEXECUTOR_ATT, value);
		helper.saveDocument(pluginfile);
	}
	
	protected void setPluginLanguageNameAndFilePath(IProject project, final String languageName) {
		IFile pluginfile = project.getFile(PluginXMLHelper.PLUGIN_FILENAME);
		PluginXMLHelper.createEmptyTemplateFile(pluginfile, false);
		PluginXMLHelper helper = new PluginXMLHelper();
		helper.loadDocument(pluginfile);
		Element gemocExtensionPoint = helper.getOrCreateExtensionPoint(ConcurrentLanguageDefinitionExtensionPoint.GEMOC_CONCURRENT_LANGUAGE_EXTENSION_POINT);
		helper.updateXDSMLDefinitionInExtensionPoint(gemocExtensionPoint, languageName);
		helper.updateXDSMLDefinitionAttributeInExtensionPoint(gemocExtensionPoint,
				LanguageDefinitionExtensionPoint.GEMOC_LANGUAGE_EXTENSION_POINT_XDSML_DEF_XDSML_FILE_PATH_ATT, project.getFullPath()
						.toString() + "/project.xdsml");
		helper.saveDocument(pluginfile);
	}
	
	protected void updateDependenciesWithProject(ManifestChanger connection, String projectName) throws BundleException, IOException, CoreException {
		connection.addPluginDependency(projectName);
		// TODO find a way to remove possible old domain model dependencies
	}

	protected void updateDependenciesWithDSAProject(ManifestChanger connection, DSAProject dsaPoject) throws BundleException, IOException, CoreException {
		updateDependenciesWithProject(connection, dsaPoject.getProjectName());
		if (dsaPoject.getCodeExecutorClass() == null || dsaPoject.getCodeExecutorClass().isEmpty()) {
			// a k3 code executor has been generated so add the required dependency 
			connection.addPluginDependency(org.gemoc.execution.concurrent.ccsljavaengine.extensions.k3.Activator.PLUGIN_ID);
		}
	}
	protected void updateDependenciesWithDSEProject(ManifestChanger connection, DSEProject dsePoject) throws BundleException, IOException, CoreException {
		String dseProjectName = dsePoject.getProjectName();		
		if(dseProjectName != null && !dseProjectName.isEmpty()){
			updateDependenciesWithProject(connection, dsePoject.getProjectName());
			String solverClassName = dsePoject.getSolverClass();
			if(solverClassName == null || solverClassName.isEmpty()){
				connection.addPluginDependency(org.gemoc.execution.concurrent.ccsljavaengine.extensions.timesquare.Activator.PLUGIN_ID);
			}
		}
	}
	
	/**
	 * create or replace existing ModelLoaderClass by an implementation that is
	 * able to load models of the domain
	 * 
	 * @param project
	 * @param ld
	 */
	protected void updateModelLoaderClass(IProject project, String modelLoaderClass) {
		// update plugin.xml
		IFile pluginfile = project.getFile(PluginXMLHelper.PLUGIN_FILENAME);
		PluginXMLHelper.createEmptyTemplateFile(pluginfile, false);
		PluginXMLHelper helper = new PluginXMLHelper();
		helper.loadDocument(pluginfile);
		Element gemocExtensionPoint = helper.getOrCreateExtensionPoint(ConcurrentLanguageDefinitionExtensionPoint.GEMOC_CONCURRENT_LANGUAGE_EXTENSION_POINT);
		helper.updateXDSMLDefinitionAttributeInExtensionPoint(
				gemocExtensionPoint,
				LanguageDefinitionExtensionPoint.GEMOC_LANGUAGE_EXTENSION_POINT_XDSML_DEF_LOADMODEL_ATT,
				modelLoaderClass != null ? modelLoaderClass : "org.gemoc.executionframework.extensions.sirius.modelloader.DefaultModelLoader");
		helper.saveDocument(pluginfile);


	}

	public static String getLanguageNameWithFirstUpper(String languageDefinitionName) {
		return languageDefinitionName.substring(0, 1).toUpperCase() + languageDefinitionName.substring(1);
	}
	public static String getPackageName(String languageDefinitionName) {
		return languageDefinitionName + ".xdsml.api.impl";
	}
	
	public static String getFolderName(String languageDefinitionName) {
		return getPackageName(languageDefinitionName).replaceAll("\\.", "/");
	}

	public static void writeFile(IFile file, String fileContent) {
		try {
			GFile.writeFile(file, fileContent);
		} catch (CoreException e) {
			Activator.error(e.getMessage(), e);
		}
	}
	
	/**
	 * create or replace existing CodeExecutorClass by an implementation that is
	 * able to execute method from the concrete DSA
	 * 
	 * @param project
	 * @param ld
	 */
	protected void updateSolverClass(IProject project, String solverClassName, String dseProjectName) {
		String computedSolverClassName = "";
		if(dseProjectName!= null && !dseProjectName.isEmpty()){
			if(solverClassName == null || solverClassName.isEmpty()){
				computedSolverClassName = "org.gemoc.execution.concurrent.ccsljavaengine.extensions.timesquare.moc.impl.CcslSolver";
			}
			else{
				computedSolverClassName = solverClassName;
			}
		} else {
			// case of PlainK3
			if(solverClassName == null || solverClassName.isEmpty()){
				computedSolverClassName = "";
			}
			else{
				computedSolverClassName = solverClassName;
			}
		}
		// update plugin.xml
		IFile pluginfile = project.getFile(PluginXMLHelper.PLUGIN_FILENAME);
		PluginXMLHelper.createEmptyTemplateFile(pluginfile, false);
		PluginXMLHelper helper = new PluginXMLHelper();
		helper.loadDocument(pluginfile);
		Element gemocExtensionPoint = helper.getOrCreateExtensionPoint(ConcurrentLanguageDefinitionExtensionPoint.GEMOC_CONCURRENT_LANGUAGE_EXTENSION_POINT);
		helper.updateXDSMLDefinitionAttributeInExtensionPoint(gemocExtensionPoint,
				ConcurrentLanguageDefinitionExtensionPoint.GEMOC_LANGUAGE_EXTENSION_POINT_XDSML_DEF_SOLVER_ATT, 
				computedSolverClassName);
		helper.saveDocument(pluginfile);
	}

	protected void updateQVTO(final IProject project, final String qvtoFileLocationUri, String dseProjectName) {
		String computedQVTOLocationURI = "";
		if(dseProjectName!= null && !dseProjectName.isEmpty()){
			if(qvtoFileLocationUri == null || qvtoFileLocationUri.isEmpty()){
				
				// search the relevant qvto in the dse project
				IProject dseProject = ResourcesPlugin.getWorkspace().getRoot().getProject(dseProjectName);	
				IFolder qvtoFolder = dseProject.getFolder("qvto-gen/modeling");
				if(qvtoFolder != null){			
					FileFinderVisitor fileFinder = new FileFinderVisitor("qvto");
					try {
						qvtoFolder.accept(fileFinder);
						IFile qvtoFile = fileFinder.getFile();
						if(qvtoFile != null){
							computedQVTOLocationURI = qvtoFile.getFullPath().toString();
						}
					} catch (CoreException e) {	}
				}
				//IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(qvtoFilePath.getLocationURI()));
				
			}
			else{
				computedQVTOLocationURI = qvtoFileLocationUri;
			}
			
			// update plugin.xml
			IFile pluginfile = project.getFile(PluginXMLHelper.PLUGIN_FILENAME);
			PluginXMLHelper.createEmptyTemplateFile(pluginfile, false);
			PluginXMLHelper helper = new PluginXMLHelper();
			helper.loadDocument(pluginfile);
			Element gemocExtensionPoint = helper.getOrCreateExtensionPoint(ConcurrentLanguageDefinitionExtensionPoint.GEMOC_CONCURRENT_LANGUAGE_EXTENSION_POINT);
			helper.updateXDSMLDefinitionAttributeInExtensionPoint(
					gemocExtensionPoint,
					ConcurrentLanguageDefinitionExtensionPoint.GEMOC_LANGUAGE_EXTENSION_POINT_XDSML_DEF_TO_CCSL_QVTO_FILE_PATH_ATT,
					computedQVTOLocationURI);
			helper.saveDocument(pluginfile);
		}
		// update plugin.xml
		IFile pluginfile = project.getFile(PluginXMLHelper.PLUGIN_FILENAME);
		PluginXMLHelper.createEmptyTemplateFile(pluginfile, false);
		PluginXMLHelper helper = new PluginXMLHelper();
		helper.loadDocument(pluginfile);
		Element gemocExtensionPoint = helper.getOrCreateExtensionPoint(ConcurrentLanguageDefinitionExtensionPoint.GEMOC_CONCURRENT_LANGUAGE_EXTENSION_POINT);
		helper.updateXDSMLDefinitionAttributeInExtensionPoint(
				gemocExtensionPoint,
				ConcurrentLanguageDefinitionExtensionPoint.GEMOC_LANGUAGE_EXTENSION_POINT_XDSML_DEF_TO_CCSL_QVTO_FILE_PATH_ATT,
				computedQVTOLocationURI);
		helper.saveDocument(pluginfile);

		// TODO check that the qvto file exists
		// The qvto file could be generated here but it exists already a GemocDSEBuilder.
		//IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(qvtoFilePath.getLocationURI()));
		//updateECL_QVTO(file);
	}


	protected void fullBuild(final IProgressMonitor monitor) throws CoreException {
		try {
			// need to check if build is realy required because it seems to be
			// called on each eclipse startup ? more debug analysis required ...
			getProject().accept(new LanguageProjectResourceVisitor());
		} catch (CoreException e) {
		}
	}

	protected void incrementalBuild(IResourceDelta delta, IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		delta.accept(new LanguageProjectDeltaVisitor());
	}

}