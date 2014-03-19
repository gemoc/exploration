/**
 */
package glml.impl;

import glml.DomainSpecificAction;
import glml.GlmlPackage;
import glml.ModelSpecificAction;
import glml.ModelSpecificEvent;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model Specific Action</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link glml.impl.ModelSpecificActionImpl#getReification <em>Reification</em>}</li>
 *   <li>{@link glml.impl.ModelSpecificActionImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link glml.impl.ModelSpecificActionImpl#getOperation <em>Operation</em>}</li>
 *   <li>{@link glml.impl.ModelSpecificActionImpl#getParameters <em>Parameters</em>}</li>
 *   <li>{@link glml.impl.ModelSpecificActionImpl#getOwningModelSpecificEvent <em>Owning Model Specific Event</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModelSpecificActionImpl extends MinimalEObjectImpl.Container implements ModelSpecificAction {
	/**
	 * The cached value of the '{@link #getReification() <em>Reification</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReification()
	 * @generated
	 * @ordered
	 */
	protected DomainSpecificAction reification;

	/**
	 * The cached value of the '{@link #getTarget() <em>Target</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTarget()
	 * @generated
	 * @ordered
	 */
	protected EObject target;

	/**
	 * The cached value of the '{@link #getOperation() <em>Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperation()
	 * @generated
	 * @ordered
	 */
	protected EOperation operation;

	/**
	 * The cached value of the '{@link #getParameters() <em>Parameters</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> parameters;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelSpecificActionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return GlmlPackage.Literals.MODEL_SPECIFIC_ACTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DomainSpecificAction getReification() {
		if (reification != null && reification.eIsProxy()) {
			InternalEObject oldReification = (InternalEObject)reification;
			reification = (DomainSpecificAction)eResolveProxy(oldReification);
			if (reification != oldReification) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, GlmlPackage.MODEL_SPECIFIC_ACTION__REIFICATION, oldReification, reification));
			}
		}
		return reification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DomainSpecificAction basicGetReification() {
		return reification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setReification(DomainSpecificAction newReification) {
		DomainSpecificAction oldReification = reification;
		reification = newReification;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GlmlPackage.MODEL_SPECIFIC_ACTION__REIFICATION, oldReification, reification));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject getTarget() {
		if (target != null && target.eIsProxy()) {
			InternalEObject oldTarget = (InternalEObject)target;
			target = eResolveProxy(oldTarget);
			if (target != oldTarget) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, GlmlPackage.MODEL_SPECIFIC_ACTION__TARGET, oldTarget, target));
			}
		}
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetTarget() {
		return target;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTarget(EObject newTarget) {
		EObject oldTarget = target;
		target = newTarget;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GlmlPackage.MODEL_SPECIFIC_ACTION__TARGET, oldTarget, target));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation getOperation() {
		if (operation != null && operation.eIsProxy()) {
			InternalEObject oldOperation = (InternalEObject)operation;
			operation = (EOperation)eResolveProxy(oldOperation);
			if (operation != oldOperation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, GlmlPackage.MODEL_SPECIFIC_ACTION__OPERATION, oldOperation, operation));
			}
		}
		return operation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation basicGetOperation() {
		return operation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOperation(EOperation newOperation) {
		EOperation oldOperation = operation;
		operation = newOperation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GlmlPackage.MODEL_SPECIFIC_ACTION__OPERATION, oldOperation, operation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<EObject> getParameters() {
		if (parameters == null) {
			parameters = new EObjectResolvingEList<EObject>(EObject.class, this, GlmlPackage.MODEL_SPECIFIC_ACTION__PARAMETERS);
		}
		return parameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelSpecificEvent getOwningModelSpecificEvent() {
		if (eContainerFeatureID() != GlmlPackage.MODEL_SPECIFIC_ACTION__OWNING_MODEL_SPECIFIC_EVENT) return null;
		return (ModelSpecificEvent)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOwningModelSpecificEvent(ModelSpecificEvent newOwningModelSpecificEvent, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newOwningModelSpecificEvent, GlmlPackage.MODEL_SPECIFIC_ACTION__OWNING_MODEL_SPECIFIC_EVENT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOwningModelSpecificEvent(ModelSpecificEvent newOwningModelSpecificEvent) {
		if (newOwningModelSpecificEvent != eInternalContainer() || (eContainerFeatureID() != GlmlPackage.MODEL_SPECIFIC_ACTION__OWNING_MODEL_SPECIFIC_EVENT && newOwningModelSpecificEvent != null)) {
			if (EcoreUtil.isAncestor(this, newOwningModelSpecificEvent))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newOwningModelSpecificEvent != null)
				msgs = ((InternalEObject)newOwningModelSpecificEvent).eInverseAdd(this, GlmlPackage.MODEL_SPECIFIC_EVENT__MODEL_SPECIFIC_ACTIONS, ModelSpecificEvent.class, msgs);
			msgs = basicSetOwningModelSpecificEvent(newOwningModelSpecificEvent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, GlmlPackage.MODEL_SPECIFIC_ACTION__OWNING_MODEL_SPECIFIC_EVENT, newOwningModelSpecificEvent, newOwningModelSpecificEvent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GlmlPackage.MODEL_SPECIFIC_ACTION__OWNING_MODEL_SPECIFIC_EVENT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetOwningModelSpecificEvent((ModelSpecificEvent)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case GlmlPackage.MODEL_SPECIFIC_ACTION__OWNING_MODEL_SPECIFIC_EVENT:
				return basicSetOwningModelSpecificEvent(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case GlmlPackage.MODEL_SPECIFIC_ACTION__OWNING_MODEL_SPECIFIC_EVENT:
				return eInternalContainer().eInverseRemove(this, GlmlPackage.MODEL_SPECIFIC_EVENT__MODEL_SPECIFIC_ACTIONS, ModelSpecificEvent.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case GlmlPackage.MODEL_SPECIFIC_ACTION__REIFICATION:
				if (resolve) return getReification();
				return basicGetReification();
			case GlmlPackage.MODEL_SPECIFIC_ACTION__TARGET:
				if (resolve) return getTarget();
				return basicGetTarget();
			case GlmlPackage.MODEL_SPECIFIC_ACTION__OPERATION:
				if (resolve) return getOperation();
				return basicGetOperation();
			case GlmlPackage.MODEL_SPECIFIC_ACTION__PARAMETERS:
				return getParameters();
			case GlmlPackage.MODEL_SPECIFIC_ACTION__OWNING_MODEL_SPECIFIC_EVENT:
				return getOwningModelSpecificEvent();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case GlmlPackage.MODEL_SPECIFIC_ACTION__REIFICATION:
				setReification((DomainSpecificAction)newValue);
				return;
			case GlmlPackage.MODEL_SPECIFIC_ACTION__TARGET:
				setTarget((EObject)newValue);
				return;
			case GlmlPackage.MODEL_SPECIFIC_ACTION__OPERATION:
				setOperation((EOperation)newValue);
				return;
			case GlmlPackage.MODEL_SPECIFIC_ACTION__PARAMETERS:
				getParameters().clear();
				getParameters().addAll((Collection<? extends EObject>)newValue);
				return;
			case GlmlPackage.MODEL_SPECIFIC_ACTION__OWNING_MODEL_SPECIFIC_EVENT:
				setOwningModelSpecificEvent((ModelSpecificEvent)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case GlmlPackage.MODEL_SPECIFIC_ACTION__REIFICATION:
				setReification((DomainSpecificAction)null);
				return;
			case GlmlPackage.MODEL_SPECIFIC_ACTION__TARGET:
				setTarget((EObject)null);
				return;
			case GlmlPackage.MODEL_SPECIFIC_ACTION__OPERATION:
				setOperation((EOperation)null);
				return;
			case GlmlPackage.MODEL_SPECIFIC_ACTION__PARAMETERS:
				getParameters().clear();
				return;
			case GlmlPackage.MODEL_SPECIFIC_ACTION__OWNING_MODEL_SPECIFIC_EVENT:
				setOwningModelSpecificEvent((ModelSpecificEvent)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case GlmlPackage.MODEL_SPECIFIC_ACTION__REIFICATION:
				return reification != null;
			case GlmlPackage.MODEL_SPECIFIC_ACTION__TARGET:
				return target != null;
			case GlmlPackage.MODEL_SPECIFIC_ACTION__OPERATION:
				return operation != null;
			case GlmlPackage.MODEL_SPECIFIC_ACTION__PARAMETERS:
				return parameters != null && !parameters.isEmpty();
			case GlmlPackage.MODEL_SPECIFIC_ACTION__OWNING_MODEL_SPECIFIC_EVENT:
				return getOwningModelSpecificEvent() != null;
		}
		return super.eIsSet(featureID);
	}

} //ModelSpecificActionImpl