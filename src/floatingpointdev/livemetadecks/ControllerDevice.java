/**
 * 
 */
package floatingpointdev.livemetadecks;

import floatingpointdev.toolkit.midi.IReceiver;
import floatingpointdev.toolkit.midi.ITransmitter;
import floatingpointdev.toolkit.midi.TransmitterComponent;

/**
 * @author floatingpointdev
 *
 */
public class ControllerDevice implements ITransmitter{
  //tell whoever is listening that something has changed on this device.
  public void transmitToReceivers(Object change){
    //if(change instanceof FpdMidiMessageShort){
      //FpdMidiMessageShort midiMessage = (FpdMidiMessageShort)change;
      //DeviceComponentIDs id = deviceComponents.getDeviceComponentFromMidiMessage(midiMessage);
      //ViewMessage<DeviceComponentIDs> vm = new ViewMessage<DeviceComponentIDs>(id,midiMessage.getCC)
    //}
    transmitToReceivers(this,change);
  }
    
  
  //ITransmitter interface
  private TransmitterComponent transmitterComponent = new TransmitterComponent();
  /** @see floatingpointdev.toolkit.midi.util.ITransmitter#addReceiver(floatingpointdev.toolkit.midi.util.IReceiver)*/
  @Override
  public void addReceiver(IReceiver receiver) {
    transmitterComponent.addReceiver(receiver);  
  }

  /** @see floatingpointdev.toolkit.midi.util.ITransmitter#deleteReceiver(floatingpointdev.toolkit.midi.util.IReceiver)*/
  @Override
  public void deleteReceiver(IReceiver receiver) {
    transmitterComponent.deleteReceiver(receiver);
  }

  /** @see floatingpointdev.toolkit.midi.util.ITransmitter#deleteReceivers()*/
  @Override
  public void deleteReceivers() {
    transmitterComponent.deleteReceivers();
  }

  /** @see floatingpointdev.toolkit.midi.util.ITransmitter#transmitToReceivers(java.lang.Object, java.lang.Object)*/
  @Override
  public void transmitToReceivers(Object transmitter, Object message) {
    transmitterComponent.transmitToReceivers(transmitter, message);
  }
}
