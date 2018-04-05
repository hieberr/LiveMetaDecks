/**
 * 
 */
package floatingpointdev.livemetadecks.hardwareui;

import java.util.ArrayList;

/**
 * @author floatingpointdev
 *
 */
public class HwComponent {
  boolean isVisible = true;
  private HwComponent parent = null;
  protected ArrayList<HwComponent> childComponents = null;
  
  /**maintains the mapping of the slots in an HwComponent to the slots in its parent.*/
  protected Slots slots = null;
  private String name = new String();
  
  /**
   * Initialize the component with numSlots number of slots.
   * @param numSlots The number of slots that this component manages.
   */
  public HwComponent(int numSlots){
    childComponents = new ArrayList<HwComponent>();
    slots = new Slots(numSlots);
    name = "";
  }
  
  /**
   * Initialize the component with numSlots number of slots.
   * @param numSlots The number of slots that this component manages.
   * @param name
   */
  public HwComponent(int numSlots, String name){
    childComponents = new ArrayList<HwComponent>();
    slots = new Slots(numSlots);
    this.name = name;
  }  
  
  public void addComponent(HwComponent hwComponent){
    
    if(hwComponent == null){
      //bail out
      //DEBUG_ERROR attempting to add a null component.
      return;
    }
    
    //check if this component already has a parent.  If it does then
    // we need to first remove it from its parent.
    if(hwComponent.getParent() != null){
      hwComponent.getParent().removeComponent(hwComponent);
    }
    
    //the component should reference this as its parent.
    hwComponent.setParent(this);
    
    //add the component to the list of children.
    childComponents.add(hwComponent);
  }
    
  
  /**
   * Removes a child component from this component if it exists.
   * @param hwComponent the component to remove.
   */
  public void removeComponent(HwComponent hwComponent){
    
    childComponents.remove(hwComponent);
    hwComponent.setParent(null);
  }
  
  
  /**
   * Tells this HwComponent which parent slot corresponds to one of its
   * slots.
   * @param parentSlot A slot in this HwComponent's parent.
   * @param childSlot A slot in this HwComponent.
   */
  public void setSlot(int childSlot, int parentSlot){
    if(slots.isSlotIndexValid(childSlot) != true){
      //DEBUG_ERROR invalid childSlot
      return;
    }
    
    slots.setSlot(childSlot, parentSlot);
    
  }
  
  /**
   * Convenience version of <Code>setSlot(int childSlot, int parentSlot)</Code>
   * for setting multiple slots at once. each element in childSlots corresponds
   * to an element in parentSlots.  childSlots and parentSlots must be the same 
   * length for this function to work.
   * @param childSlots
   * @param parentSlots
   */
  public void setSlots(int[] childSlots, int[] parentSlots){
    if(childSlots.length != parentSlots.length){
      //DEBUG_ERROR the array of child slots is not the same size as the
      //array of parent slots.
      return;
    }
    
    for(int i = 0; i < childSlots.length; ++i){
      setSlot(childSlots[i], parentSlots[i]);
    }
  }
  
  /**
   * Convenience version of <Code>setSlot(int childSlot, int parentSlot)</Code>
   * for setting multiple slots at once. This version of setSlots sets the slots
   * in this HwComponent to the elements in parentSlots.  This is the same as
   * setSlots(int[] childSlots, int[] parentSlots) except that childSlots are
   * assumed are implied in the order of parentSlots.
   * @param childSlots
   * @param parentSlots
   */
  public void setSlots(int[] parentSlots){
    if(parentSlots.length > this.getNumSlots()){
      //DEBUG_ERROR the array of parentSlots must not be
      //larger than the slots in this HwComponent
      return;
    }
    
    for(int i = 0; i < parentSlots.length; ++i){
      setSlot(i, parentSlots[i]);
    }
  }
  
  /**
   * Returns the number of slots that this component has.
   * @return the number of slots that this component has.
   */
  public int getNumSlots(){
    return slots.getNumSlots();
  }
  
  
  /**
   * Returns the component that currently resides in the
   * specified slot.
   * @param slot the slot to be queried.
   * @return
   */
//  public HwComponent getSlotComponent(int slot){
//    if((slot > this.getNumSlots()) || (slot < 0)){
//      //DEBUG_ERROR attempting to get a slot that doesn't exist..
//      return null;
//    }
//    return slots.getSlot(slot).getChild();
//  }

  public boolean isVisibleAbsolute(){
    if(isVisible){
      if(parent != null){
        return parent.isVisibleAbsolute();
      }
    }
    return false;
  }
  
  /** @return the isVisible */
  public boolean isVisible() {
    return isVisible;
  }

  /** @param isVisible the isVisible to set */
  public void setVisible(boolean isVisible) {
    this.isVisible = isVisible;
  }

  /**
   * Tests whether or not a slotIndex is valid for this component.  Basically
   * does a range check.  the value should be between 0 and the number of slots -1.
   * @param slotIndex
   * @return true if the slot index is valid for this component.
   */
  public boolean isSlotIndexValid(int slotIndex){
    return slots.isSlotIndexValid(slotIndex);
  }
  
  
  /** @param parent the parent to set for this HwComponent*/
  public void setParent(HwComponent parent) {
    this.parent = parent;
  }

  
  /** @return the parent of this HwComponent*/
  public HwComponent getParent() {
    return parent;
  }
  
    
  /** @return the name of this HwComponent.*/
  public String getName() {
    return name;
  }

  /** @param name the name for this HwComponent. */
  public void setName(String name) {
    this.name = name;
  }

  public void updateThisComponent(int slotToUpdate, Object newValue){
    if(isVisibleAbsolute()){
      HwComponent parent = getParent();
      if(parent != null){
        //translate the slot index in this <Code>Component</Code> to its parent slot.
        
        int parentSlotToUpdate = slots.getSlot(slotToUpdate).getParentSlot();
        parent.updateThisComponent(parentSlotToUpdate, newValue);
      }
    }
  }
  
  public int getChildSlotFromParentSlot(int parentSlot){
    return slots.getChildSlotFromParentSlot(parentSlot);
  }
  
  /**
   * process a message from the external device
   * @param parentSlot the slot index of this HwComponent's parent that has changed.
   * @param newValue the new value of the HwComponent that changed.
   */
  public void processViewMessage(int parentSlot, Object newValue){
    //ViewComponentView componentView = getViewFromDeviceComponentId(message.getComponentID());
    
    int mySlot = getChildSlotFromParentSlot(parentSlot);
    //verify that the slot this references is valid.
    if(mySlot < 0){
      //not a slot that this HwComponent manages. do nothing.
      return;
    }
    
    //pass the message on to all children so that the one that manages
    // the given slot can do its thing.
    for(int i = 0; i< childComponents.size(); ++i){
      childComponents.get(i).processViewMessage(mySlot, newValue);
    }
  }
  
  
  /**
   * Updates the view of all slots under this HwComponent's control.
   */
  public void redraw(){
    //draw child components.
    for(int i = 0; i < childComponents.size(); ++i){
      childComponents.get(i).redraw();
    }
    this.draw();
  }

  public void draw(){
    //draw should be overridden.
  }
  
  protected class Slots{
    private ArrayList<Slot> slots;
    
    public Slots(int numSlots){
      slots = new ArrayList<Slot>(numSlots);
      for(int i = 0; i < numSlots; ++i){
        //slots.add(new Slot(i, null, 0));
        slots.add(new Slot(i, -1));
      }
    }
    
    /**
     * @param parentSlot the parent slot to find a corresponding childSlot for
     * @return the index of the child slot that corresponds to parentSlot. or
     * -1 if parentSlot is not found. 
     */
    public int getChildSlotFromParentSlot(int parentSlot) {
      for(int i = 0; i < slots.size(); ++i){
        
        Slot slot = slots.get(i);
        if(slot != null){
          if(slot.getParentSlot() == parentSlot){
            return slot.getChildSlot();
          }
        }
      }
      return -1;
    }

    public void setSlot(int childSlot, int parentSlot){
      if(this.isSlotIndexValid(childSlot)){
        slots.get(childSlot).setSlot(childSlot, parentSlot);
      }
    }
    
    
    /**
     * Returns the slot specified by slotIndex.
     * @param slotIndex the slot index to get
     * @return the slot specified by slotIndex.
     */
    public Slot getSlot(int slotIndex){
      if(!this.isSlotIndexValid(slotIndex)){
        //DEBUG_ERROR attempting to get an invalid slot.
        return null;
      }
      
      return slots.get(slotIndex);
    }
    
   
    
    
    /**
     * 
     * @return The number of slots this HwComponent contains.
     */
    public int getNumSlots(){
      return slots.size();
    }
    
    
    /**
     * Tests whether or not a slotIndex is valid for this component.  Basically
     * does a range check.
     * @param slotIndex
     * @return true if the slot index is valid for this component.
     */
    public boolean isSlotIndexValid(int slotIndex){
      if(slotIndex < 0){
        return false;
      }
      if(slotIndex >= slots.size()){
        return false;
      }
      return true;
    }
    
//    
//    /**
//     * 
//     * @param hwComponent the child to add.
//     * @param componentSlots the slots that this child will occupy.
//     */
//    public void addChild(HwComponent hwComponent, int[] componentSlots){
//      //set each slot in the set componentSlots to point to the child component.
//      for(int i = 0; i < componentSlots.length; ++i){
//        if(!this.isSlotIndexValid(componentSlots[i])){
//          //DEBUG_ERROR attempting to add a component to an invalid slot.
//          return;
//        }
//        this.setSlot(componentSlots[i], i);
//      }
//    }
    
    
//    /**
//     * Removes all references to child from the list of slots.
//     * @param childToRemove
//     */
//    public void removeChild(HwComponent childToRemove){
//      //set all references to component in the slots to null.
//      for(int i = 0; i < slots.size(); ++i){
//        Slot slot = slots.get(i);
//        if(slot.getChild() == childToRemove){
//          slot.setSlot(i, 0);
//        }   
//      }
//    }
//  }
  
  
  
  
  
  
  }
  
  
  protected class Slot{
    private int childSlot;
    private int parentSlot;
    
//    public Slot(int parentSlot, HwComponent child, int childSlot){
//      setSlot(parentSlot, child, childSlot);
//    }
    public Slot(int childSlot, int parentSlot){
      setSlot(childSlot, parentSlot);
    }
    
    public void setSlot(int childSlot, int parentSlot){
      this.parentSlot = parentSlot;
      this.childSlot = childSlot;
    }

    /** @return the child */
//    public HwComponent getChild() {
//      return child;
//    }

//    /** @param child the child to set */
//    public void setChild(HwComponent child) {
//      this.child = child;
//    }

    /** @return the childSlot */
    public int getChildSlot() {
      return childSlot;
    }

    /** @param childSlot the childSlot to set */
    public void setChildSlot(int childSlot) {
      this.childSlot = childSlot;
    }

    /** @return the parentSlot */
    public int getParentSlot() {
      return parentSlot;
    }

    /** @param parentSlot the parentSlot to set */
    public void setParentSlot(int parentSlot) {
      this.parentSlot = parentSlot;
    }
    
  }
}


