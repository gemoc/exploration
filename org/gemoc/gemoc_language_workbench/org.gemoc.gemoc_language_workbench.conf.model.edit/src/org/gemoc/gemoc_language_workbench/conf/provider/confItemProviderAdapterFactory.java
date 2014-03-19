/**
 */
package org.gemoc.gemoc_language_workbench.conf.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

import org.gemoc.gemoc_language_workbench.conf.util.confAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class confItemProviderAdapterFactory extends confAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection<Object> supportedTypes = new ArrayList<Object>();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public confItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.gemoc.gemoc_language_workbench.conf.GemocLanguageWorkbenchConfiguration} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected GemocLanguageWorkbenchConfigurationItemProvider gemocLanguageWorkbenchConfigurationItemProvider;

	/**
	 * This creates an adapter for a {@link org.gemoc.gemoc_language_workbench.conf.GemocLanguageWorkbenchConfiguration}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createGemocLanguageWorkbenchConfigurationAdapter() {
		if (gemocLanguageWorkbenchConfigurationItemProvider == null) {
			gemocLanguageWorkbenchConfigurationItemProvider = new GemocLanguageWorkbenchConfigurationItemProvider(this);
		}

		return gemocLanguageWorkbenchConfigurationItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.gemoc.gemoc_language_workbench.conf.LanguageDefinition} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LanguageDefinitionItemProvider languageDefinitionItemProvider;

	/**
	 * This creates an adapter for a {@link org.gemoc.gemoc_language_workbench.conf.LanguageDefinition}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createLanguageDefinitionAdapter() {
		if (languageDefinitionItemProvider == null) {
			languageDefinitionItemProvider = new LanguageDefinitionItemProvider(this);
		}

		return languageDefinitionItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.gemoc.gemoc_language_workbench.conf.CCSLMoCProject} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CCSLMoCProjectItemProvider ccslMoCProjectItemProvider;

	/**
	 * This creates an adapter for a {@link org.gemoc.gemoc_language_workbench.conf.CCSLMoCProject}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createCCSLMoCProjectAdapter() {
		if (ccslMoCProjectItemProvider == null) {
			ccslMoCProjectItemProvider = new CCSLMoCProjectItemProvider(this);
		}

		return ccslMoCProjectItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.gemoc.gemoc_language_workbench.conf.EMFEcoreProject} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EMFEcoreProjectItemProvider emfEcoreProjectItemProvider;

	/**
	 * This creates an adapter for a {@link org.gemoc.gemoc_language_workbench.conf.EMFEcoreProject}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createEMFEcoreProjectAdapter() {
		if (emfEcoreProjectItemProvider == null) {
			emfEcoreProjectItemProvider = new EMFEcoreProjectItemProvider(this);
		}

		return emfEcoreProjectItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.gemoc.gemoc_language_workbench.conf.ODProject} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ODProjectItemProvider odProjectItemProvider;

	/**
	 * This creates an adapter for a {@link org.gemoc.gemoc_language_workbench.conf.ODProject}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createODProjectAdapter() {
		if (odProjectItemProvider == null) {
			odProjectItemProvider = new ODProjectItemProvider(this);
		}

		return odProjectItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.gemoc.gemoc_language_workbench.conf.SiriusAnimatorProject} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SiriusAnimatorProjectItemProvider siriusAnimatorProjectItemProvider;

	/**
	 * This creates an adapter for a {@link org.gemoc.gemoc_language_workbench.conf.SiriusAnimatorProject}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createSiriusAnimatorProjectAdapter() {
		if (siriusAnimatorProjectItemProvider == null) {
			siriusAnimatorProjectItemProvider = new SiriusAnimatorProjectItemProvider(this);
		}

		return siriusAnimatorProjectItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.gemoc.gemoc_language_workbench.conf.ModHelXMoCProject} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModHelXMoCProjectItemProvider modHelXMoCProjectItemProvider;

	/**
	 * This creates an adapter for a {@link org.gemoc.gemoc_language_workbench.conf.ModHelXMoCProject}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createModHelXMoCProjectAdapter() {
		if (modHelXMoCProjectItemProvider == null) {
			modHelXMoCProjectItemProvider = new ModHelXMoCProjectItemProvider(this);
		}

		return modHelXMoCProjectItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.gemoc.gemoc_language_workbench.conf.ECLProject} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ECLProjectItemProvider eclProjectItemProvider;

	/**
	 * This creates an adapter for a {@link org.gemoc.gemoc_language_workbench.conf.ECLProject}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createECLProjectAdapter() {
		if (eclProjectItemProvider == null) {
			eclProjectItemProvider = new ECLProjectItemProvider(this);
		}

		return eclProjectItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.gemoc.gemoc_language_workbench.conf.TreeEditorProject} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TreeEditorProjectItemProvider treeEditorProjectItemProvider;

	/**
	 * This creates an adapter for a {@link org.gemoc.gemoc_language_workbench.conf.TreeEditorProject}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createTreeEditorProjectAdapter() {
		if (treeEditorProjectItemProvider == null) {
			treeEditorProjectItemProvider = new TreeEditorProjectItemProvider(this);
		}

		return treeEditorProjectItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.gemoc.gemoc_language_workbench.conf.EMFGenmodel} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EMFGenmodelItemProvider emfGenmodelItemProvider;

	/**
	 * This creates an adapter for a {@link org.gemoc.gemoc_language_workbench.conf.EMFGenmodel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createEMFGenmodelAdapter() {
		if (emfGenmodelItemProvider == null) {
			emfGenmodelItemProvider = new EMFGenmodelItemProvider(this);
		}

		return emfGenmodelItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.gemoc.gemoc_language_workbench.conf.XTextEditorProject} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected XTextEditorProjectItemProvider xTextEditorProjectItemProvider;

	/**
	 * This creates an adapter for a {@link org.gemoc.gemoc_language_workbench.conf.XTextEditorProject}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createXTextEditorProjectAdapter() {
		if (xTextEditorProjectItemProvider == null) {
			xTextEditorProjectItemProvider = new XTextEditorProjectItemProvider(this);
		}

		return xTextEditorProjectItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.gemoc.gemoc_language_workbench.conf.K3DSAProject} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected K3DSAProjectItemProvider k3DSAProjectItemProvider;

	/**
	 * This creates an adapter for a {@link org.gemoc.gemoc_language_workbench.conf.K3DSAProject}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createK3DSAProjectAdapter() {
		if (k3DSAProjectItemProvider == null) {
			k3DSAProjectItemProvider = new K3DSAProjectItemProvider(this);
		}

		return k3DSAProjectItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.gemoc.gemoc_language_workbench.conf.ECLFile} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ECLFileItemProvider eclFileItemProvider;

	/**
	 * This creates an adapter for a {@link org.gemoc.gemoc_language_workbench.conf.ECLFile}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createECLFileAdapter() {
		if (eclFileItemProvider == null) {
			eclFileItemProvider = new ECLFileItemProvider(this);
		}

		return eclFileItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.gemoc.gemoc_language_workbench.conf.QVToFile} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected QVToFileItemProvider qvToFileItemProvider;

	/**
	 * This creates an adapter for a {@link org.gemoc.gemoc_language_workbench.conf.QVToFile}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createQVToFileAdapter() {
		if (qvToFileItemProvider == null) {
			qvToFileItemProvider = new QVToFileItemProvider(this);
		}

		return qvToFileItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.gemoc.gemoc_language_workbench.conf.BuildOptions} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BuildOptionsItemProvider buildOptionsItemProvider;

	/**
	 * This creates an adapter for a {@link org.gemoc.gemoc_language_workbench.conf.BuildOptions}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter createBuildOptionsAdapter() {
		if (buildOptionsItemProvider == null) {
			buildOptionsItemProvider = new BuildOptionsItemProvider(this);
		}

		return buildOptionsItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class<?>) || (((Class<?>)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void dispose() {
		if (gemocLanguageWorkbenchConfigurationItemProvider != null) gemocLanguageWorkbenchConfigurationItemProvider.dispose();
		if (languageDefinitionItemProvider != null) languageDefinitionItemProvider.dispose();
		if (ccslMoCProjectItemProvider != null) ccslMoCProjectItemProvider.dispose();
		if (emfEcoreProjectItemProvider != null) emfEcoreProjectItemProvider.dispose();
		if (odProjectItemProvider != null) odProjectItemProvider.dispose();
		if (siriusAnimatorProjectItemProvider != null) siriusAnimatorProjectItemProvider.dispose();
		if (modHelXMoCProjectItemProvider != null) modHelXMoCProjectItemProvider.dispose();
		if (eclProjectItemProvider != null) eclProjectItemProvider.dispose();
		if (treeEditorProjectItemProvider != null) treeEditorProjectItemProvider.dispose();
		if (k3DSAProjectItemProvider != null) k3DSAProjectItemProvider.dispose();
		if (emfGenmodelItemProvider != null) emfGenmodelItemProvider.dispose();
		if (eclFileItemProvider != null) eclFileItemProvider.dispose();
		if (qvToFileItemProvider != null) qvToFileItemProvider.dispose();
		if (xTextEditorProjectItemProvider != null) xTextEditorProjectItemProvider.dispose();
		if (buildOptionsItemProvider != null) buildOptionsItemProvider.dispose();
	}

}
