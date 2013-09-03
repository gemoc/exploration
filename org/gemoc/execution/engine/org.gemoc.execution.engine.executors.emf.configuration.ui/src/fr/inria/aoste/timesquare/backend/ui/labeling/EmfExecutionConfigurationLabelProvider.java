/*
 * generated by Xtext
 */
package fr.inria.aoste.timesquare.backend.ui.labeling;

import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.xtext.ui.label.DefaultEObjectLabelProvider;

import com.google.inject.Inject;

/**
 * Provides labels for a EObjects.
 * 
 * see
 * http://www.eclipse.org/Xtext/documentation/latest/xtext.html#labelProvider
 */
public class EmfExecutionConfigurationLabelProvider extends DefaultEObjectLabelProvider {

    @Inject
    public EmfExecutionConfigurationLabelProvider(AdapterFactoryLabelProvider delegate) {
        super(delegate);
    }

    /*
     * //Labels and icons can be computed like this:
     * 
     * String text(MyModel ele) { return "my "+ele.getName(); }
     * 
     * String image(MyModel ele) { return "MyModel.gif"; }
     */
}
