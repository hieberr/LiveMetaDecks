/**
 * 
 */
package floatingpointdev.livemetadecks.hardwareui;

/**
 * A HwComponent that holds other components.
 * @author floatingpointdev
 *
 */
public class HwPage extends HwComponent {

  /**
   * @param numSlots
   */
  public HwPage(int numSlots) {
    super(numSlots);

  }

  /**
   * @param numSlots
   * @param name
   */
  public HwPage(int numSlots, String name) {
    super(numSlots, name);
  }

  
//  /**
//   * This function gets called by a child of this HwComponent that wishes to have itself 
//   * redrawn.  childSlotToUpdate is the slot *in the child* that needs to be updated. 
//   * This is not to be confused with the slot on this component that the child slot occupies.
//   * 
//   * @param childComponent The child HwComponent of this HwComponent that needs to be redrawn.
//   * @param childSlotToUpdate The slot in the child that needs to be updated.
//   * @param newValue The value that should be drawn.
//   */
//  protected void updateChildComponent(HwComponent childComponent, int childSlotToUpdate, Object newValue){
//    if(!childComponents.contains(childComponent)) {
//      //DEBUG_ERROR attempting to update an invalid childComponent.
//      return;
//    }
//    
//    int thisSlotToUpdate;
//    thisSlotToUpdate = slots.getSlot(childComponent, childSlotToUpdate).getParentSlot();
//    getParent().updateChildComponent(this, thisSlotToUpdate, newValue);
//    
//  }

//  /**
//   * This function gets called by a child of this HwComponent that wishes to have itself 
//   * redrawn.  slotToUpdate is the slot in the parent that needs to be updated. 
//   * This is not to be confused with the slot on this component that the child slot occupies.
//   * 
//   * @param slotToUpdate The slot in the child that needs to be updated.
//   * @param newValue The value that should be drawn.
//   */
//  protected void updateChildComponent(int slotToUpdate, Object newValue){
//
//    
//    int thisSlotToUpdate;
//    thisSlotToUpdate = slots.getSlot(childComponent, slotToUpdate).getParentSlot();
//    getParent().updateChildComponent(this, thisSlotToUpdate, newValue);
//    
//  }


}
