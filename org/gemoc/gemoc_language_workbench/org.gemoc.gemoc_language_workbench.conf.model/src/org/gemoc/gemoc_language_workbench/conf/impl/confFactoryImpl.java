/**
 */
package org.gemoc.gemoc_language_workbench.conf.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.gemoc.gemoc_language_workbench.conf.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class confFactoryImpl extends EFactoryImpl implements confFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static confFactory init() {
		try {
			confFactory theconfFactory = (confFactory)EPackage.Registry.INSTANCE.getEFactory(confPackage.eNS_URI);
			if (theconfFactory != null) {
				return theconfFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new confFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public confFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case confPackage.GEMOC_LANGUAGE_WORKBENCH_CONFIGURATION: return createGemocLanguageWorkbenchConfiguration();
			case confPackage.LANGUAGE_DEFINITION: return createLanguageDefinition();
			case confPackage.CCSL_MO_CPROJECT: return createCCSLMoCProject();
			case confPackage.EMF_ECORE_PROJECT: return createEMFEcoreProject();
			case confPackage.OD_PROJECT: return createODProject();
			case confPackage.SIRIUS_ANIMATOR_PROJECT: return createSiriusAnimatorProject();
			case confPackage.MOD_HEL_XMO_CPROJECT: return createModHelXMoCProject();
			case confPackage.ECL_PROJECT: return createECLProject();
			case confPackage.TREE_EDITOR_PROJECT: return createTreeEditorProject();
			case confPackage.K3DSA_PROJECT: return createK3DSAProject();
			case confPackage.EMF_GENMODEL: return createEMFGenmodel();
			case confPackage.ECL_FILE: return createECLFile();
			case confPackage.QV_TO_FILE: return createQVToFile();
			case confPackage.XTEXT_EDITOR_PROJECT: return createXTextEditorProject();
			case confPackage.BUILD_OPTIONS: return createBuildOptions();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case confPackage.PROJECT_KIND:
				return createProjectKindFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case confPackage.PROJECT_KIND:
				return convertProjectKindToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GemocLanguageWorkbenchConfiguration createGemocLanguageWorkbenchConfiguration() {
		GemocLanguageWorkbenchConfigurationImpl gemocLanguageWorkbenchConfiguration = new GemocLanguageWorkbenchConfigurationImpl();
		return gemocLanguageWorkbenchConfiguration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public LanguageDefinition createLanguageDefinition() {
		LanguageDefinitionImpl languageDefinition = new LanguageDefinitionImpl();
		return languageDefinition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CCSLMoCProject createCCSLMoCProject() {
		CCSLMoCProjectImpl ccslMoCProject = new CCSLMoCProjectImpl();
		return ccslMoCProject;
	}


	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMFEcoreProject createEMFEcoreProject() {
		EMFEcoreProjectImpl emfEcoreProject = new EMFEcoreProjectImpl();
		return emfEcoreProject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ODProject createODProject() {
		ODProjectImpl odProject = new ODProjectImpl();
		return odProject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SiriusAnimatorProject createSiriusAnimatorProject() {
		SiriusAnimatorProjectImpl siriusAnimatorProject = new SiriusAnimatorProjectImpl();
		return siriusAnimatorProject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModHelXMoCProject createModHelXMoCProject() {
		ModHelXMoCProjectImpl modHelXMoCProject = new ModHelXMoCProjectImpl();
		return modHelXMoCProject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ECLProject createECLProject() {
		ECLProjectImpl eclProject = new ECLProjectImpl();
		return eclProject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TreeEditorProject createTreeEditorProject() {
		TreeEditorProjectImpl treeEditorProject = new TreeEditorProjectImpl();
		return treeEditorProject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMFGenmodel createEMFGenmodel() {
		EMFGenmodelImpl emfGenmodel = new EMFGenmodelImpl();
		return emfGenmodel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public XTextEditorProject createXTextEditorProject() {
		XTextEditorProjectImpl xTextEditorProject = new XTextEditorProjectImpl();
		return xTextEditorProject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public K3DSAProject createK3DSAProject() {
		K3DSAProjectImpl k3DSAProject = new K3DSAProjectImpl();
		return k3DSAProject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ECLFile createECLFile() {
		ECLFileImpl eclFile = new ECLFileImpl();
		return eclFile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QVToFile createQVToFile() {
		QVToFileImpl qvToFile = new QVToFileImpl();
		return qvToFile;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BuildOptions createBuildOptions() {
		BuildOptionsImpl buildOptions = new BuildOptionsImpl();
		return buildOptions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProjectKind createProjectKindFromString(EDataType eDataType, String initialValue) {
		ProjectKind result = ProjectKind.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertProjectKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public confPackage getconfPackage() {
		return (confPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static confPackage getPackage() {
		return confPackage.eINSTANCE;
	}

} //confFactoryImpl
