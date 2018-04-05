/**
 * 
 */
package floatingpointdev.livemetadecks;

/**
 * Class used to identify a specific component on an external device.  it holds a reference
 * to the instance of the device and the enum id of a component on that device.  The two together
 * should form a unique id for each component on each external device.
 * @author floatingpointdev
 *
 */
public class DeviceComponentId<ENUM_TYPE> {
  ControllerDevice device;
  ENUM_TYPE deviceId;
  
  /**
   * @param device
   * @param deviceId
   */
  public DeviceComponentId(ControllerDevice device, ENUM_TYPE deviceId) {
    super();
    this.device = device;
    this.deviceId = deviceId;
  }

  /** @return the device */
  public ControllerDevice getDevice() {
    return device;
  }
//
//  /** @param device the device to set */
//  public void setDevice(ControllerDevice device) {
//    this.device = device;
//  }

  /** @return the deviceId */
  public ENUM_TYPE getDeviceId() {
    return deviceId;
  }
//
//  /** @param deviceId the deviceId to set */
//  public void setDeviceId(ENUM_TYPE deviceId) {
//    this.deviceId = deviceId;
//  }
  /**
   * returns true if this DeviceComponentId references the same device component as 
   * the input DeviceComponentId.
   */
  public boolean equals(DeviceComponentId deviceComponentId){
    if((this.device == deviceComponentId.device) && (this.deviceId == deviceComponentId.deviceId)){
      return true;
    }
    return false;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    if(obj instanceof DeviceComponentId){
      return equals((DeviceComponentId)obj);
    } else {
      return super.equals(obj);
    }
  }
}
