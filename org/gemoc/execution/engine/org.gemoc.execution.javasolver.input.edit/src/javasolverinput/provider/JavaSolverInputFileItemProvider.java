/**
 */
package javasolverinput.provider;


import java.util.Collection;
import java.util.List;

import javasolverinput.JavaSolverInputFile;
import javasolverinput.JavasolverinputPackage;

import javasolverinput.creation.CreationFactory;

import javasolverinput.usage.UsageFactory;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link javasolverinput.JavaSolverInputFile} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class JavaSolverInputFileItemProvider
	extends NamedElementItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public JavaSolverInputFileItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

		}
		return itemPropertyDescriptors;
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(JavasolverinputPackage.Literals.JAVA_SOLVER_INPUT_FILE__CLOCK_DECLARATIONS);
			childrenFeatures.add(JavasolverinputPackage.Literals.JAVA_SOLVER_INPUT_FILE__CONSTRAINTS);
			childrenFeatures.add(JavasolverinputPackage.Literals.JAVA_SOLVER_INPUT_FILE__RELATION_DECLARATIONS);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns JavaSolverInputFile.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/JavaSolverInputFile"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((JavaSolverInputFile)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_JavaSolverInputFile_type") :
			getString("_UI_JavaSolverInputFile_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(JavaSolverInputFile.class)) {
			case JavasolverinputPackage.JAVA_SOLVER_INPUT_FILE__CLOCK_DECLARATIONS:
			case JavasolverinputPackage.JAVA_SOLVER_INPUT_FILE__CONSTRAINTS:
			case JavasolverinputPackage.JAVA_SOLVER_INPUT_FILE__RELATION_DECLARATIONS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(JavasolverinputPackage.Literals.JAVA_SOLVER_INPUT_FILE__CLOCK_DECLARATIONS,
				 UsageFactory.eINSTANCE.createClock()));

		newChildDescriptors.add
			(createChildParameter
				(JavasolverinputPackage.Literals.JAVA_SOLVER_INPUT_FILE__CONSTRAINTS,
				 UsageFactory.eINSTANCE.createCustomConstraint()));

		newChildDescriptors.add
			(createChildParameter
				(JavasolverinputPackage.Literals.JAVA_SOLVER_INPUT_FILE__CONSTRAINTS,
				 UsageFactory.eINSTANCE.createOnce()));

		newChildDescriptors.add
			(createChildParameter
				(JavasolverinputPackage.Literals.JAVA_SOLVER_INPUT_FILE__CONSTRAINTS,
				 UsageFactory.eINSTANCE.createPrecedes()));

		newChildDescriptors.add
			(createChildParameter
				(JavasolverinputPackage.Literals.JAVA_SOLVER_INPUT_FILE__CONSTRAINTS,
				 UsageFactory.eINSTANCE.createWaitUntil()));

		newChildDescriptors.add
			(createChildParameter
				(JavasolverinputPackage.Literals.JAVA_SOLVER_INPUT_FILE__RELATION_DECLARATIONS,
				 CreationFactory.eINSTANCE.createRelationDeclaration()));
	}

}
