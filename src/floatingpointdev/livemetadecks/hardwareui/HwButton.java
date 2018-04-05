/**
 * 
 */
package floatingpointdev.livemetadecks.hardwareui;

import floatingpointdev.livemetadecks.IComponentChangeListener;

/**
 * @author floatingpointdev
 *
 */
public class HwButton extends HwSimpleElement {
  /**
   */
  public HwButton(int numSlots) {
    super(numSlots);
  }
  
  /**
   * Handy constructor for creating a HwSimpleElement and attaching it
   * to a IComponentChangeListener.
   * 
   * @param Name the name for the component
   * @param Listener the instance of IComponentChangeListener that will receive 
   * change messages from this view component.
   */
  public HwButton(int numSlots, IComponentChangeListener listener){
    super(numSlots, listener);

  }

  /**
   * @param listener Listener the instance of IComponentChangeListener that will receive 
   * @param name Name the name for the component
   */
  public HwButton(int numSlots, IComponentChangeListener listener, String name) {
    super(numSlots, listener, name);
  }

  /**
   * @param name Name the name for the component
   */
  public HwButton(int numSlots, String name) {
    super(numSlots, name);
  }


  
}
