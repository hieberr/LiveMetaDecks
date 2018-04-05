/**
 * 
 */
package floatingpointdev.livemetadecks.hardwareui;

import floatingpointdev.livemetadecks.ChangeableComponent;
import floatingpointdev.livemetadecks.IComponentChangeListener;
import floatingpointdev.livemetadecks.IComponentChangeable;

/**
 * A HwSimpleElement is a component that has a single value and can notify
 * a registered IComponentChangeListener when this value changes.
 * This class is meant to be a base class for things like knobs and buttons.  It uses
 * the first slot (slot 0) to display its value.  A HwSimpleElement is also assumed to 
 * not have any children, but this design decision may change in the future.
 * 
 * @author floatingpointdev
 *
 */
public class HwSimpleElement extends HwComponent implements IComponentChangeable{
  Object value = 0;
  /**
   */
  public HwSimpleElement(int numSlots) {
    super(numSlots); 
  }
  
  public HwSimpleElement(int numSlots, String name) {
    super(1, name);
  }
  
  /**
   * Handy constructor for creating a HwSimpleElement and attaching it
   * to a IComponentChangeListener.
   * 
   * @param Name the name for the component
   * @param Listener the instance of IComponentChangeListener that will receive 
   * change messages from this view component.
   */
  public HwSimpleElement(int numSlots, IComponentChangeListener listener, String name){
    super(numSlots, name);
    this.addChangeListener(listener);
  }

  /**
   * Handy constructor for creating a HwSimpleElement and attaching it
   * to a IComponentChangeListener.
   * 
   * @param Listener the instance of IComponentChangeListener that will receive 
   * change messages from this view component.
   */
  public HwSimpleElement(int numSlots, IComponentChangeListener listener){
    super(numSlots);
    this.addChangeListener(listener);
  }
  
  public void setValue(Object value){
    this.value = value;
    redraw();
  }
  
  public Object getValue(){
    return value;
  }
  
  @Override
  public void draw(){
    updateThisComponent(0,this.value); 
  }
  
  /**
   * @see floatingpointdev.livemetadecks.hardwareui.HwComponent#processViewMessage(int, java.lang.Object)
   */
  @Override
  public void processViewMessage(int parentSlot, Object newValue) {
    int mySlot = slots.getChildSlotFromParentSlot(parentSlot);
    //verify that the slot this references is valid.
    if(mySlot < 0){
      //not a slot that this HwComponent manages. do nothing.
      return;
    }
    this.notifyChangeListeners(this, newValue);
  }
  
  //------------------IComponentChangeable stuff------------------
  ChangeableComponent changeableComponent = new ChangeableComponent();
  /** @see floatingpointdev.livemetadecks.IComponentChangeable#addChangeListener(floatingpointdev.livemetadecks.IComponentChangeListener)*/
  @Override
  public void addChangeListener(IComponentChangeListener listener) {
    changeableComponent.addChangeListener(listener);
  }

  /** @see floatingpointdev.livemetadecks.IComponentChangeable#notifyChangeListeners(java.lang.Object, java.lang.Object)*/
  @Override
  public void notifyChangeListeners(Object theChanged, Object change) {
    changeableComponent.notifyChangeListeners(theChanged, change);
  }

  /** @see floatingpointdev.livemetadecks.IComponentChangeable#removeChangeListener(floatingpointdev.livemetadecks.IComponentChangeListener)*/
  @Override
  public void removeChangeListener(IComponentChangeListener listener) {
    changeableComponent.removeChangeListener(listener);
  }

  /** @see floatingpointdev.livemetadecks.IComponentChangeable#removeChangeListeners()*/
  @Override
  public void removeChangeListeners() {
    changeableComponent.removeChangeListeners();
  }

}
