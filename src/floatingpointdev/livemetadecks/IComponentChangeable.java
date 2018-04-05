/**
 * 
 */
package floatingpointdev.livemetadecks;


/**
 * @author floatingpointdev
 *
 */
public interface IComponentChangeable {
  public void addChangeListener(IComponentChangeListener listener);
  public void removeChangeListener(IComponentChangeListener listener);
  public void removeChangeListeners();
  public void notifyChangeListeners(Object theChanged, Object change);
}

//heres a template for implementing the IComponentChangeable interface.

//IComponentChangeable stuff
/*ChangeableComponent changeableComponent = new ChangeableComponent();
*//** @see floatingpointdev.livemetadecks.IComponentChangeable#addChangeListener(floatingpointdev.livemetadecks.IComponentChangeListener)*//*
@Override
public void addChangeListener(IComponentChangeListener listener) {
changeableComponent.addChangeListener(listener);

}

*//** @see floatingpointdev.livemetadecks.IComponentChangeable#notifyChangeListeners(java.lang.Object, java.lang.Object)*//*
@Override
public void notifyChangeListeners(Object theChanged, Object change) {
changeableComponent.notifyChangeListeners(theChanged, change);
}

*//** @see floatingpointdev.livemetadecks.IComponentChangeable#removeChangeListener(floatingpointdev.livemetadecks.IComponentChangeListener)*//*
@Override
public void removeChangeListener(IComponentChangeListener listener) {
changeableComponent.removeChangeListener(listener);

}

*//** @see floatingpointdev.livemetadecks.IComponentChangeable#removeChangeListeners()*//*
@Override
public void removeChangeListeners() {
changeableComponent.removeChangeListeners();

}*/