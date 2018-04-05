/**
 * 
 */
package floatingpointdev.livemetadecks.hardwareui;

import java.util.ArrayList;

import floatingpointdev.livemetadecks.Apc40;
import floatingpointdev.livemetadecks.ControllerDevice;
import floatingpointdev.livemetadecks.DeviceComponentId;
import floatingpointdev.livemetadecks.Apc40.DeviceComponentIDsApc40;
import floatingpointdev.toolkit.UI.ViewMessage;

/**
 * @author floatingpointdev
 *
 */
public class RootPage extends HwPage{

  ArrayList<DeviceComponentId> deviceIds;
  /**
   * @param numSlots
   */
  public RootPage(int numSlots) {
    super(numSlots);
    deviceIds = new ArrayList<DeviceComponentId>(numSlots);
    for(int i = 0; i < numSlots; ++i){
      deviceIds.add(null);
    }
  }
  
  public void setSlot(int slotIndex, DeviceComponentId deviceId){
    if(!this.isSlotIndexValid(slotIndex)){
      //DEBUG_ERROR invalid slot index.
      return;
    }
    deviceIds.set(slotIndex, deviceId);
  }
  
  public void setSlot(int[] slotIndices, DeviceComponentId[] deviceIds){
    if(slotIndices.length != deviceIds.length){
      //DEBUG_ERROR the arrays slotIndices and deviceIds must be the same length.
      return;
    }
    
    for(int i = 0; i < slotIndices.length; ++i){
      int index = slotIndices[i];
      if(this.isSlotIndexValid(index)){
        this.setSlot(index, deviceIds[index]);
      }
    }
    
  }
  
  
  /** @see floatingpointdev.livemetadecks.hardwareui.HwComponent#isVisibleAbsolute()*/
  @Override
  public boolean isVisibleAbsolute() {
    
    return true;
  }

  
  /** @see floatingpointdev.livemetadecks.hardwareui.HwComponent#updateThisComponent(int, java.lang.Object)*/
  @Override
  public void updateThisComponent(int slotToUpdate, Object newValue) {
    DeviceComponentId deviceId = deviceIds.get(slotToUpdate);
    
    //send the message along to the hardware to draw the update.
    if(deviceId != null){
      ControllerDevice device = deviceId.getDevice();

      if(device instanceof Apc40){
        ViewMessage<DeviceComponentIDsApc40> vm = new ViewMessage<DeviceComponentIDsApc40>((DeviceComponentIDsApc40) deviceId.getDeviceId(), newValue);
        ((Apc40)device).sendToDevice(vm);
      }
    }
  }

  
  public void processViewMessage(ViewMessage<DeviceComponentId<?>> message){
    //determine the slot that this message is targeting.
    int slot = deviceIds.indexOf(message.getComponentID());
    //super.processViewMessage(slot, message.getNewValue());   
    
    //pass the message on to all children so that the one that manages
    // the given slot can do its thing.
    for(int i = 0; i< childComponents.size(); ++i){
      childComponents.get(i).processViewMessage(slot, message.getNewValue());
    }
  }

}
