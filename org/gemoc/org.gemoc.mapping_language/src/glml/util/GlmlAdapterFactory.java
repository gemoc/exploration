/**
 */
package glml.util;

import glml.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see glml.GlmlPackage
 * @generated
 */
public class GlmlAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static GlmlPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public GlmlAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = GlmlPackage.eINSTANCE;
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
	protected GlmlSwitch<Adapter> modelSwitch =
		new GlmlSwitch<Adapter>() {
			@Override
			public Adapter caseDomainSpecificEventFile(DomainSpecificEventFile object) {
				return createDomainSpecificEventFileAdapter();
			}
			@Override
			public Adapter caseNamedElement(NamedElement object) {
				return createNamedElementAdapter();
			}
			@Override
			public Adapter caseImportStatement(ImportStatement object) {
				return createImportStatementAdapter();
			}
			@Override
			public Adapter caseDomainSpecificEvent(DomainSpecificEvent object) {
				return createDomainSpecificEventAdapter();
			}
			@Override
			public Adapter caseDomainSpecificAction(DomainSpecificAction object) {
				return createDomainSpecificActionAdapter();
			}
			@Override
			public Adapter caseMocRelation(MocRelation object) {
				return createMocRelationAdapter();
			}
			@Override
			public Adapter caseExtendedCcslRelation(ExtendedCcslRelation object) {
				return createExtendedCcslRelationAdapter();
			}
			@Override
			public Adapter caseJavaSolverRelation(JavaSolverRelation object) {
				return createJavaSolverRelationAdapter();
			}
			@Override
			public Adapter caseModelSpecificEvent(ModelSpecificEvent object) {
				return createModelSpecificEventAdapter();
			}
			@Override
			public Adapter caseModelSpecificAction(ModelSpecificAction object) {
				return createModelSpecificActionAdapter();
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
	 * Creates a new adapter for an object of class '{@link glml.DomainSpecificEventFile <em>Domain Specific Event File</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see glml.DomainSpecificEventFile
	 * @generated
	 */
	public Adapter createDomainSpecificEventFileAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link glml.NamedElement <em>Named Element</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see glml.NamedElement
	 * @generated
	 */
	public Adapter createNamedElementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link glml.DomainSpecificEvent <em>Domain Specific Event</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see glml.DomainSpecificEvent
	 * @generated
	 */
	public Adapter createDomainSpecificEventAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link glml.DomainSpecificAction <em>Domain Specific Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see glml.DomainSpecificAction
	 * @generated
	 */
	public Adapter createDomainSpecificActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link glml.ImportStatement <em>Import Statement</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see glml.ImportStatement
	 * @generated
	 */
	public Adapter createImportStatementAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link glml.MocRelation <em>Moc Relation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see glml.MocRelation
	 * @generated
	 */
	public Adapter createMocRelationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link glml.ExtendedCcslRelation <em>Extended Ccsl Relation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see glml.ExtendedCcslRelation
	 * @generated
	 */
	public Adapter createExtendedCcslRelationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link glml.ModelSpecificEvent <em>Model Specific Event</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see glml.ModelSpecificEvent
	 * @generated
	 */
	public Adapter createModelSpecificEventAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link glml.ModelSpecificAction <em>Model Specific Action</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see glml.ModelSpecificAction
	 * @generated
	 */
	public Adapter createModelSpecificActionAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link glml.JavaSolverRelation <em>Java Solver Relation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see glml.JavaSolverRelation
	 * @generated
	 */
	public Adapter createJavaSolverRelationAdapter() {
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

} //GlmlAdapterFactory