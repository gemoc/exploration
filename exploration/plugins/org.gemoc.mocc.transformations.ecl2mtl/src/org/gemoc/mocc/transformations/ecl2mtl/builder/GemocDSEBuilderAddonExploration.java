package org.gemoc.mocc.transformations.ecl2mtl.builder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.gemoc.commons.eclipse.core.resources.Project;
import org.gemoc.execution.concurrent.ccsljavaxdsml.ui.Activator;
import org.gemoc.execution.concurrent.ccsljavaxdsml.ui.dse.IGemocDSEBuilderAddon;
import org.gemoc.mocc.transformations.ecl2mtl.main.AcceleoTemplateFromEclToMTL;

import toools.io.file.RegularFile;

public class GemocDSEBuilderAddonExploration implements IGemocDSEBuilderAddon {

	public static String MTL_GEN_FOLDER = "mtl-gen";
	
	public GemocDSEBuilderAddonExploration() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void processResourceAddon(IResource resource) {
		if(resource instanceof IFile && resource.getName().endsWith(".ecl")){
			updateMTLFromECL(resource);
		}
	}

	void updateMTLFromECL(IResource resource) {		
		if (resource instanceof IFile && resource.getName().endsWith(".ecl")) {
			IProject project = resource.getProject();
			final IFile eclFile = (IFile) resource;
			IFile propertyFile = (IFile) resource.getProject().getFile("moc2as.properties");
			if(!propertyFile.exists()){
				return;
			}
			try {
			
				String uristring = eclFile.getLocation().toOSString();
			    final URI uri = URI.createFileURI(uristring);
			    
			    String genFolder = MTL_GEN_FOLDER;
                final IFolder modelingFolder = Project.createFolder(project, genFolder + "/modeling");                   
                final IFolder languageFolder = Project.createFolder(project, genFolder + "/language");                   

                final String mtlFileName = eclFile.getFullPath()
                                    .removeFileExtension()
                                    .addFileExtension("mtl")
                                    .lastSegment();

                Properties properties = new Properties();
				properties.load(propertyFile.getContents());
				String rootElement = properties.getProperty("rootElement");
				if(rootElement==null || rootElement.isEmpty()){
					return;
				}

                final List<String> arguments = new ArrayList<String>();

                arguments.add(mtlFileName);
                
                String fixedRootElement = rootElement;
				if(rootElement.contains("::")){
					fixedRootElement = rootElement.substring(rootElement.indexOf("::")+2);
				}
				arguments.add(fixedRootElement); 
		    
				// create MTL file
				ISafeRunnable runnable = new ISafeRunnable() {
					@Override
					public void handleException(Throwable e) {
						Activator.error(e.getMessage(), e);
					}
	
					@Override
					public void run() throws Exception {
	
						try {

                            System.out.println("launching ecl to mtl:\n\turi=" + uri + "\n\tfolder=" + languageFolder + "\n\targs="
                                    + arguments);
                            AcceleoTemplateFromEclToMTL generator = new AcceleoTemplateFromEclToMTL(uri, new File(languageFolder.getLocation().toOSString()), arguments);
                            generator.doGenerate(new BasicMonitor());
                            IFile mtlFileForLanguage = languageFolder.getFile(mtlFileName);
                            mtlFileForLanguage.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
                        
                            IFile mtlFileForModeling = modelingFolder.getFile(mtlFileName);

                            RegularFile reg_mtlFileForLanguage = new RegularFile(mtlFileForLanguage.getLocation().toOSString());
                            RegularFile reg_mtlFileForModeling = new RegularFile(mtlFileForModeling.getLocation().toOSString());

                            String mtlLanguageContent = new String(reg_mtlFileForLanguage.getContent());
                            String mtlModelingContent = mtlLanguageContent.replaceAll("platform:/resource", "platform:/plugin");
                            reg_mtlFileForModeling.setContent(mtlModelingContent.getBytes());

                            mtlFileForModeling.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
   

						} catch (IOException e) {
							Activator.error(e.getMessage(), e);
						}
					}
				};
				SafeRunner.run(runnable);
			} catch (CoreException e1) {

				Activator.error(e1.getMessage(), e1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
