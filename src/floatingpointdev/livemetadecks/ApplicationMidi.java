package floatingpointdev.livemetadecks;
import javax.sound.midi.SysexMessage;

import floatingpointdev.toolkit.midi.*;
import floatingpointdev.toolkit.util.ShellUtility;

/**
 * @author floatingpointdev
 *
 */
public class ApplicationMidi implements IReceiver, ITransmitter {
  private IoMidi ioMidi;
  private String name;
  private String midiInputPortName;
  private String midiOutputPortName;
  private String execPath;

  
  /**
   * The Handler for midi messages coming from the external application.
   */
  private MidiReceiver externalApplicationReceiver = new MidiReceiver();
  
  /**
   * The receiver in ioMidi that receives midi messages destined for the
   * external application.
   */
  IReceiver externalApplicationDestination;
  
  
  /**
   * 
   */
  public ApplicationMidi(IoMidi ioMidi, String Name) {
    this.ioMidi = ioMidi;
    setName(name);
    setMidiInputPortName(IoMidi.BUSS_SELECTION_NONE); //default "none"
    setMidiOutputPortName(IoMidi.BUSS_SELECTION_NONE); //default "none"
  }

  /**
   * 
   * @param ioMidi The midiSystem that this application sends through.
   * @param name The name of this application.
   * @param midiInputPortName The name of the midiInputPort in ioMidi 
   * that this application should receive midi messages from.
   * 
   * @param midiOutputPortName The name of the midiOutputPort in ioMidi 
   * that this application should send midi messages to.
   * @param execPath
   */
  public ApplicationMidi(IoMidi ioMidi, String name,
                                        String midiInputPortName, 
                                        String midiOutputPortName, 
                                        String execPath){
    this.ioMidi = ioMidi;
    setMidiInputPortName(midiInputPortName);
    setMidiOutputPortName(midiOutputPortName);
    setExecPath(execPath);
    setName(name);
  }
  

  /** @return the midiInputPortName */
  public String getMidiInputPortName() {
    return midiInputPortName;
  }
  

  /** @param midiInputPortName the midiInputPortName to set */
  public void setMidiInputPortName(String midiInputPortName) {
    
    ITransmitter inputPortTransmitter = ioMidi.addMidiReceiver(externalApplicationReceiver, midiInputPortName);
    if(inputPortTransmitter != null){
      this.midiInputPortName = midiInputPortName;
    } else{
      //DEBUG ERROR, invalid midi port.
    }
  }

  /** @return the midiOutputPortName */
  public String getMidiOutputPortName() {
    return midiOutputPortName;
  }

  /** @param midiOutputPortName the midiOutputPortName to set */
  public void setMidiOutputPortName(String midiOutputPortName) {
    externalApplicationDestination = ioMidi.getOutputPort(midiOutputPortName);
    if(externalApplicationDestination != null){
      this.midiOutputPortName = midiOutputPortName;
    } else{
      //DEBUG ERROR, invalid midi port.
    }
  }

  /** @return the execPath */
  public String getExecPath() {
    return execPath;
  }

  /** @param execPath the execPath to set */
  public void setExecPath(String execPath) {
    this.execPath = execPath;
  }
  
  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
  }

  public void runApplication(){
    ShellUtility.execShellCommand(execPath);
  }
  
  public void closeApplication(){
    //execShellCommand("/scripts/LiveA");
  }

  // ------- Interface Stuff ----------
  
  //IReceiver interface.
  /** @see floatingpointdev.toolkit.midi.util.IReceiver#processMessage(java.lang.Object, java.lang.Object)*/
  public void processMessage(Object transmitter, Object message) {
      
    //receive a midi message.
    //send it out to the external application.
    externalApplicationDestination.processMessage(this, message);
  }
  
  //ITransmitter interface.
  private TransmitterComponent transmitterComponent = new TransmitterComponent();

  /** @see
   * floatingpointdev.toolkit.midi.util.ITransmitter#addReceiver(floatingpointdev.toolkit.midi.util.IReceiver)*/
  public void addReceiver(IReceiver receiver) {
    transmitterComponent.addReceiver(receiver);
  }

  /** @see floatingpointdev.toolkit.midi.util.ITransmitter#deleteMidiReceiver(floatingpointdev.toolkit.midi.util.IReceiver)*/
  public void deleteReceiver(IReceiver receiver) {
    transmitterComponent.deleteReceiver(receiver);
  }

  /** @see floatingpointdev.toolkit.midi.util.ITransmitter#deleteMidiReceivers()*/
  public void deleteReceivers() {
    transmitterComponent.deleteReceivers();
  }

  /** @see floatingpointdev.toolkit.midi.util.ITransmitter#transmitToReceivers(java.lang.Object, java.lang.Object)*/
  public void transmitToReceivers(Object transmitter, Object message) {
    transmitterComponent.transmitToReceivers(transmitter, message);
  }

 
  /** This class receives midi messages from from the external midi application.*/
  public class MidiReceiver implements IReceiver{
    //IReceiver interface.
    /** @see floatingpointdev.toolkit.midi.util.IReceiver#processMessage(java.lang.Object, java.lang.Object)*/
    public void processMessage(Object transmitter, Object message) {  
      if(message instanceof FpdMidiMessageShort){
        //FpdMidiMessageShort m = (FpdMidiMessageShort)message;
        //int channel = m.getChannel();
        //int command = m.getCommand();
        //int note = m.getNote();
        //int status = m.getStatus();
        //int foo = m.getVelocity();
      } else if(message instanceof SysexMessage){
        //SysexMessage sysexMessage = (SysexMessage)message;
      } else {
      }
      //if this message is coming in from the external application.
      //send it along to the controller.
      transmitToReceivers(this, message);
    }
  }
}
