/**
 * 
 */
package floatingpointdev.livemetadecks;
import java.util.ArrayList;
import java.util.Iterator;

import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import floatingpointdev.toolkit.UI.ViewMessage;
import floatingpointdev.toolkit.midi.*;

/** A controller device for an external akai apc40 midi controller.
 * @author floatingpointdev
 *
 */
public class Apc40 extends ControllerDevice{
  private String midiInputPortName = "Akai APC40 - Akai APC40";
  private String midiOutputPortName = "Akai APC40 - Akai APC40";

  /**
   * The Handler for midi messages coming from the external device.
   */
  private MidiReceiver externalDeviceReceiver = new MidiReceiver(this);
  
  /**
   * The receiver in ioMidi that receives midi messages destined for the
   * external device.
   */
  private IReceiver externalDeviceDestination;
  
  /** defines all of the components that this view has*/
  public enum DeviceComponentIDsApc40 {
    SLIDER1, SLIDER2, SLIDER3, SLIDER4, SLIDER5, SLIDER6, SLIDER7, SLIDER8, SLIDER9,
    CROSSFADER,
    
    //The top bank of knobs.
    KNOB1,KNOB2,KNOB3,KNOB4,KNOB5,KNOB6,KNOB7,KNOB8,
    KNOB1_LEDTYPE,KNOB2_LEDTYPE,KNOB3_LEDTYPE,KNOB4_LEDTYPE,KNOB5_LEDTYPE,KNOB6_LEDTYPE,KNOB7_LEDTYPE,KNOB8_LEDTYPE,
    //The bottom bank of knobs.
    KNOB9,KNOB10,KNOB11,KNOB12,KNOB13,KNOB14,KNOB15,KNOB16,
    KNOB9_LEDTYPE,KNOB10_LEDTYPE,KNOB11_LEDTYPE,KNOB12_LEDTYPE,KNOB13_LEDTYPE,KNOB14_LEDTYPE,KNOB15_LEDTYPE,KNOB16_LEDTYPE,
    
    KNOBCUE,
    
    //The grid of cliplaunch buttons including the scene launch column, 
    //the stop clip row, and the track selection row.
    BTNGRID00, BTNGRID10, BTNGRID20, BTNGRID30, BTNGRID40, BTNGRID50, BTNGRID60, BTNGRID70, BTNGRID80,
    BTNGRID01, BTNGRID11, BTNGRID21, BTNGRID31, BTNGRID41, BTNGRID51, BTNGRID61, BTNGRID71, BTNGRID81,
    BTNGRID02, BTNGRID12, BTNGRID22, BTNGRID32, BTNGRID42, BTNGRID52, BTNGRID62, BTNGRID72, BTNGRID82, 
    BTNGRID03, BTNGRID13, BTNGRID23, BTNGRID33, BTNGRID43, BTNGRID53, BTNGRID63, BTNGRID73, BTNGRID83,
    BTNGRID04, BTNGRID14, BTNGRID24, BTNGRID34, BTNGRID44, BTNGRID54, BTNGRID64, BTNGRID74, BTNGRID84,
    BTNGRID05, BTNGRID15, BTNGRID25, BTNGRID35, BTNGRID45, BTNGRID55, BTNGRID65, BTNGRID75, BTNGRID85, 
    BTNGRID06, BTNGRID16, BTNGRID26, BTNGRID36, BTNGRID46, BTNGRID56, BTNGRID66, BTNGRID76, BTNGRID86,
    
    //The three rows of buttons, activator, solo/cue, record/arm.
    BTNGRIDSMALL00, BTNGRIDSMALL10, BTNGRIDSMALL20, BTNGRIDSMALL30, BTNGRIDSMALL40, BTNGRIDSMALL50, BTNGRIDSMALL60, BTNGRIDSMALL70, 
    BTNGRIDSMALL01, BTNGRIDSMALL11, BTNGRIDSMALL21, BTNGRIDSMALL31, BTNGRIDSMALL41, BTNGRIDSMALL51, BTNGRIDSMALL61, BTNGRIDSMALL71, 
    BTNGRIDSMALL02, BTNGRIDSMALL12, BTNGRIDSMALL22, BTNGRIDSMALL32, BTNGRIDSMALL42, BTNGRIDSMALL52, BTNGRIDSMALL62, BTNGRIDSMALL72, 
        
    BTNPAN, BTNSENDA, BTNSENDB, BTNSENDC,
    BTNSHIFT, BTNUP, BTNDOWN, BTNLEFT, BTNRIGHT,
    BTNNUDGEMINUS, BTNNUDGEPLUS, BTNTAPTEMPO,
    
    BTNCLIPTRACK, BTNDEVICEONOFF, BTNDEVICELEFT, BTNDEVICERIGHT,
    BTNDETAILVIEW, BTNRECQUANTIZATION, BTNMIDIOBERDUB, BTNMETRONOME,
    BTNPLAY, BTNSTOP, BTNREC
  };
  
  
  /**
   * Holds all of the components that make up this view.  This is all of the knobs, buttons sliders, etc.
   */
  private DeviceComponents deviceComponents;
  /**
   * 
   */
  public Apc40() {
    //setup the midi system.
    IoMidi ioMidi = Globals.getInstance().ioMidi;
    
    //attach this as the receiver of midi from the midiinput port in ioMIdi
    if(ioMidi.addMidiReceiver(externalDeviceReceiver, midiInputPortName) == null){
      //invalid midi port;      
    }
    
    //get the port from ioMidi that we will be sending midi messages to.
    externalDeviceDestination = ioMidi.getOutputPort(midiOutputPortName);
    if(externalDeviceDestination == null){
      //invalid midi port;
    }
        
    //setup the ViewComponents
    deviceComponents = new DeviceComponents();
  }


  /**
   * Send out a message to the device.
   * @param change
   */
  public void sendToDevice(Object change) {
     if(change instanceof ViewMessage<?>){
      //find out which DeviceComponent we are dealing with.
      ViewMessage<DeviceComponentIDsApc40> vm = (ViewMessage<DeviceComponentIDsApc40>)change;
      DeviceComponentMidi vc = deviceComponents.getDeviceComponentFromId(vm.getComponentID());
      vc.setValue(vm.getNewValue());
    } else if(change instanceof FpdMidiMessageShort){
      //pass everything else through.
      sendToExternalDevice(change);
      
    } else if(change instanceof SysexMessage){
      sendToExternalDevice(change);
    } 
  }
  
  private MidiDebugWindow debugWindowMidiToApc= new MidiDebugWindow("(cApc)midiToApc");
  protected void sendToExternalDevice(Object message){
    debugWindowMidiToApc.processMessage(this, message);
    this.externalDeviceDestination.processMessage(this, message);
  }


  /** This class receives midi messages from from the external midi device.*/
  private class MidiReceiver implements IReceiver{
    MidiDebugWindow debugWindowMidiFromApc = new MidiDebugWindow("(cApc)from APC40");
    private Apc40 apc40;
    
    /**
     * 
     */
    public MidiReceiver(Apc40 apc40) {
      super();
      this.apc40 = apc40;
    }

    //IReceiver interface.
    /** @see floatingpointdev.toolkit.midi.util.IReceiver#processMessage(java.lang.Object, java.lang.Object)*/
    public void processMessage(Object transmitter, Object message) {
      debugWindowMidiFromApc.processMessage(this, message);

      if(message instanceof FpdMidiMessageShort){
        FpdMidiMessageShort m = (FpdMidiMessageShort)message;
        
        //ComponentIDs id;
        int value = 0;
        
        //figure out which view component this message originated from.
        DeviceComponentMidi deviceComponent = deviceComponents.getDeviceComponentFromMidiMessage(m);
        DeviceComponentId<DeviceComponentIDsApc40> deviceComponentId = new DeviceComponentId<DeviceComponentIDsApc40>(apc40, deviceComponent.getId());
        
        if(deviceComponent != null){
          if((deviceComponent instanceof DeviceComponentMultiStateNoteBtn) || (deviceComponent instanceof DeviceComponentNoteBtn)){
            if(m.getCommand() == ShortMessage.NOTE_ON){
              value = 1; // 1 means the button pressed event.
              
              //construct a ViewChangeMessage and pass it to the model.
              ViewMessage<DeviceComponentId<DeviceComponentIDsApc40>> viewMessage = new ViewMessage<DeviceComponentId<DeviceComponentIDsApc40>>(deviceComponentId,value); 
              transmitToReceivers(viewMessage);
              return;
            } else if(m.getCommand() == ShortMessage.NOTE_OFF){
              value = 0; // 0 means the button released event.
              //construct a ViewChangeMessage and pass it to the model.
              ViewMessage<DeviceComponentId<DeviceComponentIDsApc40>> viewMessage = new ViewMessage<DeviceComponentId<DeviceComponentIDsApc40>>(deviceComponentId,value); 
              transmitToReceivers(viewMessage);
              return;
            } else {
              //DEBUG_ERROR 
            }
          } else if(deviceComponent instanceof DeviceComponentKnob){
            //construct a ViewChangeMessage and pass it to the model.
            ViewMessage<DeviceComponentId<DeviceComponentIDsApc40>> viewMessage = new ViewMessage<DeviceComponentId<DeviceComponentIDsApc40>>(deviceComponentId,m.getCCValue()); 
            transmitToReceivers(viewMessage);
            return;
          }
        }     
      } else if(message instanceof SysexMessage){
        //SysexMessage sysexMessage = (SysexMessage)message;
        transmitToReceivers(message);
      } else {
        transmitToReceivers(message);
      }  
    }
  }
  
  private class DeviceComponentMidi{
    DeviceComponentIDsApc40 id;
    int channel;
    
    DeviceComponentMidi(DeviceComponentIDsApc40 id, int channel){
      this.id = id;
      this.channel = channel;
    }
    
    public void setValue(Object value){};
    
    /**Implementers should override this function to properly determine
     * if they are controlled by message.
     * @param message
     * @return true if this component is controlled by the midi message.
     */
    public boolean isControlledByMidiMessage(FpdMidiMessageShort message){
      return true;
    }

    /** @return the id */
    public DeviceComponentIDsApc40 getId() {
      return id;
    }

    /** @param id the id to set */
    public void setId(DeviceComponentIDsApc40 id) {
      this.id = id;
    }

    /** @return the channel */
    public int getChannel() {
      return channel;
    }

    /** @param channel the channel to set */
    public void setChannel(int channel) {
      this.channel = channel;
    }
    //public void addViewComponentListener();
  }
  
  private class DeviceComponentKnob extends DeviceComponentMidi{
    private int channel;
    private int controlChangeNum;
    
    /**
     * @param id
     * @param channel
     */
    DeviceComponentKnob(DeviceComponentIDsApc40 id, int channel, int controlChangeNum) {
      super(id, channel);
      this.channel = channel;
      this.controlChangeNum = controlChangeNum;
    }

    @Override
    public void setValue(Object value){
      //create a midi message and send it to the hardware device.
      //(Integer) cast may or may not work?
      
      int iVal = (Integer)value;
      FpdMidiMessageShort message = FpdMidiMessageShort.createFpdMidiMessageControlChange(channel, controlChangeNum, iVal);
      sendToExternalDevice(message);
    }
    
    @Override
    public boolean isControlledByMidiMessage(FpdMidiMessageShort message){
      //int status = message.getStatus(); 
      if(message.getCommand() == ShortMessage.CONTROL_CHANGE){
        if(this.channel == message.getChannel() && (this.controlChangeNum == message.getCCNumber())){
          return true;
        }
      }
      return false;
    }
  }
  
  private class DeviceComponentKnobLedType extends DeviceComponentMidi{
    private int channel;
    private int controlChangeNum;
    
    /**
     * @param id
     * @param channel
     */
    DeviceComponentKnobLedType(DeviceComponentIDsApc40 id, int channel, int controlChangeNum) {
      super(id, channel);
      this.channel = channel;
      this.controlChangeNum = controlChangeNum;
    }
  
    @Override
    public void setValue(Object value){
      //create a midi message and send it to the hardware device.
      //(Integer) cast may or may not work?
      
      int iVal = (Integer)value;
      // value of the integer sets the LED ring style
      //0=off, 1=Single, 2=Volume Style, 3=Pan Style, 4- 127=Single
      
      FpdMidiMessageShort message = FpdMidiMessageShort.createFpdMidiMessageControlChange(channel, controlChangeNum, iVal);
      sendToExternalDevice(message);
    }
    
    @Override
    public boolean isControlledByMidiMessage(FpdMidiMessageShort message){
      //int status = message.getStatus(); 
      if(message.getCommand() == ShortMessage.CONTROL_CHANGE){
        if(this.channel == message.getChannel() && (this.controlChangeNum == message.getCCNumber())){
          return true;
        }
      }
      return false;
    }
  }
  
  private class DeviceComponentNoteBtn extends DeviceComponentMidi{
    private int channel;
    private int note;
    /**
     * @param id
     * @param channel
     * @param note
     */
    public DeviceComponentNoteBtn(DeviceComponentIDsApc40 id, int channel, int note) {
      super(id, channel);
      this.id = id;
      this.channel = channel;
      this.note = note;
    }
    
    @Override
    public void setValue(Object value){
      //create a midi message and send it to the hardware device.
      int iVal = (Integer)value;
      /* if value != 0 then it is one of the following states:
       *  0 - Off
       *  1 - On
       */
      if(iVal == 0){
        //value of 0 means set the button to the off state.
        FpdMidiMessageShort message = FpdMidiMessageShort.createFpdMidiMessageNoteOff(channel, note, 0); 
        sendToExternalDevice(message);
      } else if(iVal == 1){
        //value of 1 means set the button to the on state.
        FpdMidiMessageShort message = FpdMidiMessageShort.createFpdMidiMessageNoteOn(channel, note, 127);
        sendToExternalDevice(message);
      }
    }
    
    @Override
    public boolean isControlledByMidiMessage(FpdMidiMessageShort message){
      //int status = message.getStatus(); 
      if((message.getCommand() == ShortMessage.NOTE_ON) || (message.getCommand() == ShortMessage.NOTE_OFF)){
        if(this.channel == message.getChannel() && (this.note == message.getNote())){
          return true;
        }
      }
      return false;
    }
  }
  
  private class DeviceComponentMultiStateNoteBtn extends DeviceComponentMidi{
    private int channel;
    private int note;
    /**
     * @param id
     * @param channel
     * @param note
     */
    public DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40 id, int channel, int note) {
      super(id, channel);
      this.id = id;
      this.channel = channel;
      this.note = note;
    }
    
    @Override
    public void setValue(Object value){
      //create a midi message and send it to the hardware device.
      //(Integer) cast may or may not work?
      
      int iVal = (Integer)value;
      
      if(iVal == 0){
        //value of 0 means button off.
        FpdMidiMessageShort message = FpdMidiMessageShort.createFpdMidiMessageNoteOff(channel, note, 0); 
      }
      /* if value != 0 then it is one of the following states:
       *  1 - Green
          2 - Green Flashing
          3 - Red
          4 - Red Flashing
          5 - Orange
          6 - Orange Flashing
          7-127 - Green
          
          These states correspond to the Velocity that should be sent to the apc40.
       */
      FpdMidiMessageShort message = FpdMidiMessageShort.createFpdMidiMessageNoteOn(channel, note, iVal);
      sendToExternalDevice(message);
    }
    
    @Override
    public boolean isControlledByMidiMessage(FpdMidiMessageShort message){
      int status = message.getStatus(); 
      if((message.getCommand() == ShortMessage.NOTE_ON) || (message.getCommand() == ShortMessage.NOTE_OFF)){
        if(this.channel == message.getChannel() && (this.note == message.getNote())){
          return true;
        }
      }
      return false;
    }
  }

  private class DeviceComponents{
    ArrayList<DeviceComponentMidi> allComponents = new ArrayList<DeviceComponentMidi>();
    
    public DeviceComponents(){
      //setup the top knob bank.
      allComponents.add(new DeviceComponentKnob(DeviceComponentIDsApc40.KNOB1, 0, 48));
      allComponents.add(new DeviceComponentKnob(DeviceComponentIDsApc40.KNOB2, 0, 49));
      allComponents.add(new DeviceComponentKnob(DeviceComponentIDsApc40.KNOB3, 0, 50));
      allComponents.add(new DeviceComponentKnob(DeviceComponentIDsApc40.KNOB4, 0, 51));
      allComponents.add(new DeviceComponentKnob(DeviceComponentIDsApc40.KNOB5, 0, 52));
      allComponents.add(new DeviceComponentKnob(DeviceComponentIDsApc40.KNOB6, 0, 53));
      allComponents.add(new DeviceComponentKnob(DeviceComponentIDsApc40.KNOB7, 0, 54));
      allComponents.add(new DeviceComponentKnob(DeviceComponentIDsApc40.KNOB8, 0, 55));
      //the top bank led types
      allComponents.add(new DeviceComponentKnobLedType(DeviceComponentIDsApc40.KNOB1_LEDTYPE, 0, 0x38));
      allComponents.add(new DeviceComponentKnobLedType(DeviceComponentIDsApc40.KNOB2_LEDTYPE, 0, 0x39));
      allComponents.add(new DeviceComponentKnobLedType(DeviceComponentIDsApc40.KNOB3_LEDTYPE, 0, 0x3a));
      allComponents.add(new DeviceComponentKnobLedType(DeviceComponentIDsApc40.KNOB4_LEDTYPE, 0, 0x3b));
      allComponents.add(new DeviceComponentKnobLedType(DeviceComponentIDsApc40.KNOB5_LEDTYPE, 0, 0x3c));
      allComponents.add(new DeviceComponentKnobLedType(DeviceComponentIDsApc40.KNOB6_LEDTYPE, 0, 0x3d));
      allComponents.add(new DeviceComponentKnobLedType(DeviceComponentIDsApc40.KNOB7_LEDTYPE, 0, 0x3e));
      allComponents.add(new DeviceComponentKnobLedType(DeviceComponentIDsApc40.KNOB8_LEDTYPE, 0, 0x3f));

      //setup the bottom knob bank.
      allComponents.add(new DeviceComponentKnob(DeviceComponentIDsApc40.KNOB9, 0, 16));
      allComponents.add(new DeviceComponentKnob(DeviceComponentIDsApc40.KNOB10, 0, 17));
      allComponents.add(new DeviceComponentKnob(DeviceComponentIDsApc40.KNOB11, 0, 18));
      allComponents.add(new DeviceComponentKnob(DeviceComponentIDsApc40.KNOB12, 0, 19));
      allComponents.add(new DeviceComponentKnob(DeviceComponentIDsApc40.KNOB13, 0, 20));
      allComponents.add(new DeviceComponentKnob(DeviceComponentIDsApc40.KNOB14, 0, 21));
      allComponents.add(new DeviceComponentKnob(DeviceComponentIDsApc40.KNOB15, 0, 22));
      allComponents.add(new DeviceComponentKnob(DeviceComponentIDsApc40.KNOB16, 0, 23));
      //the bottom bank led types
      allComponents.add(new DeviceComponentKnobLedType(DeviceComponentIDsApc40.KNOB9_LEDTYPE, 0, 0x18));
      allComponents.add(new DeviceComponentKnobLedType(DeviceComponentIDsApc40.KNOB10_LEDTYPE, 0, 0x19));
      allComponents.add(new DeviceComponentKnobLedType(DeviceComponentIDsApc40.KNOB11_LEDTYPE, 0, 0x1a));
      allComponents.add(new DeviceComponentKnobLedType(DeviceComponentIDsApc40.KNOB12_LEDTYPE, 0, 0x1b));
      allComponents.add(new DeviceComponentKnobLedType(DeviceComponentIDsApc40.KNOB13_LEDTYPE, 0, 0x1c));
      allComponents.add(new DeviceComponentKnobLedType(DeviceComponentIDsApc40.KNOB14_LEDTYPE, 0, 0x1d));
      allComponents.add(new DeviceComponentKnobLedType(DeviceComponentIDsApc40.KNOB15_LEDTYPE, 0, 0x1e));
      allComponents.add(new DeviceComponentKnobLedType(DeviceComponentIDsApc40.KNOB16_LEDTYPE, 0, 0x1f));

      //buttons
      allComponents.add(new DeviceComponentNoteBtn(DeviceComponentIDsApc40.BTNPAN, 0, 87));
      allComponents.add(new DeviceComponentNoteBtn(DeviceComponentIDsApc40.BTNSENDA, 0, 88));
      allComponents.add(new DeviceComponentNoteBtn(DeviceComponentIDsApc40.BTNSENDB, 0, 89));
      allComponents.add(new DeviceComponentNoteBtn(DeviceComponentIDsApc40.BTNSENDC, 0, 90));
      
      allComponents.add(new DeviceComponentNoteBtn(DeviceComponentIDsApc40.BTNCLIPTRACK, 0, 58));
      allComponents.add(new DeviceComponentNoteBtn(DeviceComponentIDsApc40.BTNDEVICEONOFF, 0, 59));
      allComponents.add(new DeviceComponentNoteBtn(DeviceComponentIDsApc40.BTNDEVICELEFT, 0, 60));
      allComponents.add(new DeviceComponentNoteBtn(DeviceComponentIDsApc40.BTNDEVICERIGHT, 0, 61));
      
      //setup the grid
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID00, 0, 53));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID01, 0, 54));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID02, 0, 55));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID03, 0, 56)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID04, 0, 57)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID05, 0, 52)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID06, 0, 51)); 

      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID10, 1, 53));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID11, 1, 54));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID12, 1, 55));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID13, 1, 56)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID14, 1, 57)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID15, 1, 52)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID16, 1, 51)); 
      
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID20, 2, 53));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID21, 2, 54));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID22, 2, 55));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID23, 2, 56)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID24, 2, 57)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID25, 2, 52)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID26, 2, 51)); 
      
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID30, 3, 53));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID31, 3, 54));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID32, 3, 55));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID33, 3, 56)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID34, 3, 57)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID35, 3, 52)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID36, 3, 51));   

      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID40, 4, 53));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID41, 4, 54));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID42, 4, 55));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID43, 4, 56)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID44, 4, 57)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID45, 4, 52)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID46, 4, 51));   
      
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID50, 5, 53));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID51, 5, 54));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID52, 5, 55));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID53, 5, 56)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID54, 5, 57)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID55, 5, 52)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID56, 5, 51));   
      
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID60, 6, 53));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID61, 6, 54));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID62, 6, 55));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID63, 6, 56)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID64, 6, 57)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID65, 6, 52)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID66, 6, 51));   
      
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID70, 7, 53));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID71, 7, 54));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID72, 7, 55));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID73, 7, 56)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID74, 7, 57)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID75, 7, 52)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID76, 7, 51));   
      
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID80, 8, 53));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID81, 8, 54));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID82, 8, 55));
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID83, 8, 56)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID84, 8, 57)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID85, 8, 52)); 
      allComponents.add(new DeviceComponentMultiStateNoteBtn(DeviceComponentIDsApc40.BTNGRID86, 8, 51));   
    }
    
    /** 
     * @param midiMessage The message to compare to.
     * @return the ViewComponentMidi that maps to the midiMessage specified.  
     * returns null if a component isn't found.
     */
    public DeviceComponentMidi getDeviceComponentFromMidiMessage(FpdMidiMessageShort midiMessage){
      //figure out which device component this message maps to.
      for(Iterator<DeviceComponentMidi> i = allComponents.iterator(); i.hasNext();){
        DeviceComponentMidi deviceComponent = i.next();
        if(deviceComponent.isControlledByMidiMessage(midiMessage)){
          return deviceComponent;
        }
      }
      return null;
    }
    
    /**
     * 
     * @param id the id to search for
     * @return the component with the specified id
     */
    public DeviceComponentMidi getDeviceComponentFromId(DeviceComponentIDsApc40 id){
      //figure out which view component this message maps to.
      for(Iterator<DeviceComponentMidi> i = allComponents.iterator(); i.hasNext();){
        DeviceComponentMidi deviceComponent = i.next();
        if(deviceComponent.getId() == id){
          return deviceComponent;
        }
      }
      return null;
    }
  }
}
