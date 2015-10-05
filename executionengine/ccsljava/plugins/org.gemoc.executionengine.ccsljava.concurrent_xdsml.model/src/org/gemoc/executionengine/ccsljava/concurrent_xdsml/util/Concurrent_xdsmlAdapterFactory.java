/**
 */
package org.gemoc.executionengine.ccsljava.concurrent_xdsml.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

import org.gemoc.executionengine.ccsljava.concurrent_xdsml.*;

import org.gemoc.executionframework.xdsml_base.LanguageDefinition;
import org.gemoc.executionframework.xdsml_base.ProjectResource;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see org.gemoc.executionengine.ccsljava.concurrent_xdsml.Concurrent_xdsmlPackage
 * @generated
 */
public class Concurrent_xdsmlAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static Concurrent_xdsmlPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Concurrent_xdsmlAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = Concurrent_xdsmlPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Concurrent_xdsmlSwitch<Adapter> modelSwitch =
		new Concurrent_xdsmlSwitch<Adapter>() {
			@Override
			public Adapter caseConcurrentLanguageDefinition(ConcurrentLanguageDefinition object) {
				return createConcurrentLanguageDefinitionAdapter();
			}
			@Override
			public Adapter caseDSAProject(DSAProject object) {
				return createDSAProjectAdapter();
			}
			@Override
			public Adapter caseMoCCProject(MoCCProject object) {
				return createMoCCProjectAdapter();
			}
			@Override
			public Adapter caseDSEProject(DSEProject object) {
				return createDSEProjectAdapter();
			}
			@Override
			public Adapter caseLanguageDefinition(LanguageDefinition object) {
				return createLanguageDefinitionAdapter();
			}
			@Override
			public Adapter caseProjectResource(ProjectResource object) {
				return createProjectResourceAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link org.gemoc.executionengine.ccsljava.concurrent_xdsml.ConcurrentLanguageDefinition <em>Concurrent Language Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gemoc.executionengine.ccsljava.concurrent_xdsml.ConcurrentLanguageDefinition
	 * @generated
	 */
	public Adapter createConcurrentLanguageDefinitionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gemoc.executionengine.ccsljava.concurrent_xdsml.DSAProject <em>DSA Project</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gemoc.executionengine.ccsljava.concurrent_xdsml.DSAProject
	 * @generated
	 */
	public Adapter createDSAProjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gemoc.executionengine.ccsljava.concurrent_xdsml.MoCCProject <em>Mo CC Project</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gemoc.executionengine.ccsljava.concurrent_xdsml.MoCCProject
	 * @generated
	 */
	public Adapter createMoCCProjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gemoc.executionengine.ccsljava.concurrent_xdsml.DSEProject <em>DSE Project</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gemoc.executionengine.ccsljava.concurrent_xdsml.DSEProject
	 * @generated
	 */
	public Adapter createDSEProjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gemoc.executionframework.xdsml_base.LanguageDefinition <em>Language Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gemoc.executionframework.xdsml_base.LanguageDefinition
	 * @generated
	 */
	public Adapter createLanguageDefinitionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link org.gemoc.executionframework.xdsml_base.ProjectResource <em>Project Resource</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see org.gemoc.executionframework.xdsml_base.ProjectResource
	 * @generated
	 */
	public Adapter createProjectResourceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //Concurrent_xdsmlAdapterFactory
