/**
 * 
 */
package floatingpointdev.livemetadecks;

import java.util.ArrayList;
import java.util.Iterator;

import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

//import floatingpointdev.livemetadecks.ControllerDeviceApc40.DeviceComponentIDs;
import floatingpointdev.toolkit.UI.IView;
import floatingpointdev.toolkit.UI.ViewMessage;
import floatingpointdev.toolkit.midi.FpdMidiMessageShort;
import floatingpointdev.toolkit.midi.MidiDebugWindow;

/**
 * 
 * @author floatingpointdev
 *
 */
public class ControllerLiveMetaDecks extends Controller {

  /**
   * Holds all of the components that make up this view.  This is all of the knobs, buttons sliders, etc.
   */
  ControllerComponents controllerComponents;
  
  public enum ControllerComponentIDs 
  {
    //Controls for the default apc40 device knob functionality.
    DEVICECONTROL01, DEVICECONTROL02, DEVICECONTROL03, DEVICECONTROL04, 
    DEVICECONTROL05, DEVICECONTROL06, DEVICECONTROL07, DEVICECONTROL08,
    
    DEVICECONTROL01_LEDTYPE, DEVICECONTROL02_LEDTYPE, DEVICECONTROL03_LEDTYPE, DEVICECONTROL04_LEDTYPE, 
    DEVICECONTROL05_LEDTYPE, DEVICECONTROL06_LEDTYPE, DEVICECONTROL07_LEDTYPE, DEVICECONTROL08_LEDTYPE,
    
    BTNCLIPTRACK, BTNDEVICEONOFF, BTNDEVICELEFT, BTNDEVICERIGHT,
    BTNPAN, BTNSENDA, BTNSENDB, BTNSENDC,
    
    
    TOPKNOB1_LEDTYPE, TOPKNOB2_LEDTYPE, TOPKNOB3_LEDTYPE, TOPKNOB4_LEDTYPE,
    TOPKNOB5_LEDTYPE, TOPKNOB6_LEDTYPE, TOPKNOB7_LEDTYPE, TOPKNOB8_LEDTYPE,
    
    //Controls for the Parameters
    PARAMETER01, PARAMETER02, PARAMETER03, PARAMETER04,
    PARAMETER05, PARAMETER06, PARAMETER07, PARAMETER08, 
    PARAMETER09, PARAMETER10, PARAMETER11, PARAMETER12,
    PARAMETER13, PARAMETER14, PARAMETER15, PARAMETER16, 

    PARAMETER01_LEDTYPE, PARAMETER02_LEDTYPE, PARAMETER03_LEDTYPE, PARAMETER04_LEDTYPE,
    PARAMETER05_LEDTYPE, PARAMETER06_LEDTYPE, PARAMETER07_LEDTYPE, PARAMETER08_LEDTYPE, 
    PARAMETER09_LEDTYPE, PARAMETER10_LEDTYPE, PARAMETER11_LEDTYPE, PARAMETER12_LEDTYPE,
    PARAMETER13_LEDTYPE, PARAMETER14_LEDTYPE, PARAMETER15_LEDTYPE, PARAMETER16_LEDTYPE, 
  }
  
  
  ControllerLiveMetaDecks(){
    super();
    controllerComponents = new ControllerComponents();
  }
  /**
   * Implementers should override this method and have it
   * notify its observers that everything has changed.  Then 
   * the observers will get a complete update. 
   * Implementers should override this method.
   */
  @Override
  public void syncViews(){
    controllerComponents.syncView();
  }
  
  MidiDebugWindow dbgWindowFromLive = new MidiDebugWindow("(cApc)From Live");
  //IReceiver interface.
  /** 
   * processes a message from the application.
   *@see floatingpointdev.toolkit.midi.util.IReceiver#processMessage(java.lang.Object, java.lang.Object)*/
  @Override
  public void processMessage(Object transmitter, Object message) {
    if(message instanceof FpdMidiMessageShort){
      dbgWindowFromLive.processMessage(this, message);
      FpdMidiMessageShort m = (FpdMidiMessageShort)message;

      //ComponentIDs id;
      int value = 0;
      //figure out which component this message is addressed to.
      ControllerComponentMidi cComponent = controllerComponents.getControllerComponentFromMidiMessage((FpdMidiMessageShort)message);
      
      if(cComponent != null){
        if((cComponent instanceof ControllerComponentMultiStateNoteBtn)){
          if(m.getCommand() == ShortMessage.NOTE_ON){
            //value = 1; // 1 means the button has the state determined by the velocity of the note.
            cComponent.setValue(m.getVelocity());
            return;
          } else if(m.getCommand() == ShortMessage.NOTE_OFF){
            value = 0; // 0 means the button is in the off state.
            cComponent.setValue(value);
            return;
          } else {
            //DEBUG_ERROR   multi state buttons should only be responding too note on and note off messages.
          }
        } else if(cComponent instanceof ParameterControl){
          if(m.getCommand() == ShortMessage.CONTROL_CHANGE){
            cComponent.setValue(m.getCCValue());
            return;
          }     
        } else if(cComponent instanceof ParameterControlLedType){
          if(m.getCommand() == ShortMessage.CONTROL_CHANGE){
            cComponent.setValue(m.getCCValue());
            return;
          }
        } else if((cComponent instanceof ButtonNoteToggle)){
          if(m.getCommand() == ShortMessage.NOTE_ON){
            if(m.getVelocity() == 0){ 
              ///catch the case where a note on with 0 velocity is used instead of a proper note off message.
              cComponent.setValue(0);
            } else {
              cComponent.setValue(1);// 1 means the button is pressed    
            }

            return;
          } else if(m.getCommand() == ShortMessage.NOTE_OFF){
            cComponent.setValue(0);// 0 means the button is released
            return;
          } else {
            //DEBUG_ERROR  note buttons should only be responding too note on and note off messages.
          }
        }
      }
    } else if(message instanceof SysexMessage){
      //SysexMessage sysexMessage = (SysexMessage)message;
      modelComponent.notifyIViews(this, message);
    } else {
      modelComponent.notifyIViews(this, message);
    }
  }
  
  /** Implementors should override this method to receive change notification 
   * from any view attached to this controller.
   *  @see floatingpointdev.toolkit.UI.IModel#onViewChanged(floatingpointdev.toolkit.UI.IView, java.lang.Object)*/
  @Override
  public void onViewChanged(IView theChanged, Object change) {
    if(change instanceof ViewMessage<?>){
      ViewMessage<ControllerComponentIDs> vm = (ViewMessage<ControllerComponentIDs>)change;
      ControllerComponentIDs id = vm.getComponentID();
      Object newValue = vm.getNewValue();
       
      ControllerComponentMidi controllerComponent = controllerComponents.getControllerComponentFromControllerId(id);
      controllerComponent.onViewChanged(newValue);

//      //Also transmit the midi message to receivers
//      ControllerComponentMultiStateNoteBtn controllerComponentBtn;
//      if(controllerComponent instanceof ControllerComponentMultiStateNoteBtn){
//        controllerComponentBtn = (ControllerComponentMultiStateNoteBtn)controllerComponent;
//            
//        FpdMidiMessageShort m;
//        int iVal = (Integer)vm.getNewValue();
//        if(iVal == 0){
//          m = FpdMidiMessageShort.createFpdMidiMessageNoteOff(controllerComponentBtn.getChannel(), controllerComponentBtn.getNote(), iVal);
//        } else {
//          //m = FpdMidiMessageShort.createFpdMidiMessageNoteOn(controllerComponentBtn.getChannel(), controllerComponentBtn.getNote(), iVal);
//          m = FpdMidiMessageShort.createFpdMidiMessageNoteOn(controllerComponentBtn.getChannel(), controllerComponentBtn.getNote(), 127);
//          
//  
//        }
//        transmitToReceivers(m);
//      }
    }
    
    if(change instanceof FpdMidiMessageShort){
      //transmitToReceivers(this, change);
    } else if(change instanceof SysexMessage){
      //SysexMessage sysexMessage = (SysexMessage)change;
      transmitToReceivers(this, change);
    } else {
      //transmitToReceivers(this, change);
    }
  }
  
  /**
   * @param change the change to send to all views.
   */
  public void notifyViews(Object change){
    modelComponent.notifyIViews(this, change);
  }
  
  /**
   * @param message the message to send to all receivers.
   */
  public void transmitToReceivers(Object message) {
    this.transmitToReceivers(this,message);
  }
  
  
  
  private class ControllerComponentMidi{
    ControllerComponentIDs id;
    Object value;
    int channel;

    ControllerComponentMidi(ControllerComponentIDs id, int channel){  
      this.id = id;
      this.channel = channel;
      setValue(0);
    }
    
    /**
     * @param value
     */
    public void onViewChanged(Object value) {
    }

    public void setValue(Object value){
      this.value = value;
    };
    
    /**Implementers should override this function to properly determine
     * if they are controlled by message.
     * @param message
     * @return true if this component is controlled by the midi message.
     */
    public boolean isControlledByMidiMessage(FpdMidiMessageShort message){
      return true;
    }

    /** @return the id */
    public ControllerComponentIDs getId() {
      return id;
    }

    /** @param id the id to set */
    public void setId(ControllerComponentIDs id) {
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

  }
  
  private class ButtonNoteMomentary extends ControllerComponentMidi{
    private int note = 0;
    private int myValue = 0;
    /**
     * @param id
     * @param channel
     */
    ButtonNoteMomentary(ControllerComponentIDs id,
      int channel, 
      int note) {
      
      super(id, channel);
      this.id = id;
      this.note = note;
    }
    
    public void onViewChanged(Object value){
      int newVal = (Integer)(value);
      if( myValue != newVal){
        //super.setValue(value);
        myValue = newVal;
        ViewMessage<ControllerComponentIDs> change = new ViewMessage<ControllerComponentIDs>(id, value);
        
        //Notify the views of the new value.
        notifyViews(change);
        
        //send the message along to the application.
        
        if(newVal == 0){
          FpdMidiMessageShort message = FpdMidiMessageShort.createFpdMidiMessageNoteOff(channel, note, 0);
          transmitToReceivers(message);
        } else if(newVal == 1){
          FpdMidiMessageShort message = FpdMidiMessageShort.createFpdMidiMessageNoteOn(channel, note, 127);
          transmitToReceivers(message);
        }
      }
    }
    
    @Override
    public void setValue(Object value){
      //super.setValue(value);
      ViewMessage change = new ViewMessage(id, value);
      //Notify the view of the new value.
      notifyViews(change);
     
      int newVal = (Integer)value;
      myValue = newVal;
//      if(newVal == 0){
//        //value of 0 means button off.
//        FpdMidiMessageShort message = FpdMidiMessageShort.createFpdMidiMessageNoteOff(channel, note, 0); 
//      }
//      /* if value != 0 then the button is not pressed.
//       *  1 - button is pressed.
//       *  0 - button not pressed.
//       */
//      FpdMidiMessageShort message = FpdMidiMessageShort.createFpdMidiMessageNoteOn(channel, note, 127);
//      transmitToReceivers(message);
    }
    
    @Override
    public boolean isControlledByMidiMessage(FpdMidiMessageShort message){
      if((message.getCommand() == ShortMessage.NOTE_ON) || (message.getCommand() == ShortMessage.NOTE_OFF)){
        if(this.channel == message.getChannel() && (this.note == message.getNote())){
          return true;
        }
      }
      return false;
    }

    /** @return the note */
    public int getNote() {
      return note;
    }

    /** @param note the note to set */
    public void setNote(int note) {
      this.note = note;
    }
  }
  
  private class ButtonNoteToggle extends ControllerComponentMidi{
    private int note = 0;
    private int myValue = 0;
    /**
     * @param id
     * @param channel
     */
    ButtonNoteToggle(ControllerComponentIDs id,
      int channel, 
      int note) {
      
      super(id, channel);
      this.id = id;
      this.note = note;
    }
    
    public void onViewChanged(Object value){
      int newVal = (Integer)(value);
      if(newVal == 1){ //catch the button pressed event.
        myValue = newVal;
        
        FpdMidiMessageShort message = FpdMidiMessageShort.createFpdMidiMessageNoteOn(channel, note, 127);
        transmitToReceivers(message);
      }
    }
    
    /*
     *  if value != 0 then the button is not pressed.
     *  1 - button is pressed.
     *  0 - button not pressed.
     */
    @Override
    public void setValue(Object value){
      //super.setValue(value);
      ViewMessage change = new ViewMessage(id, value);
      //Notify the view of the new value.
      notifyViews(change);
     
      int newVal = (Integer)value;
      myValue = newVal;
    }
    
    @Override
    public boolean isControlledByMidiMessage(FpdMidiMessageShort message){
      if((message.getCommand() == ShortMessage.NOTE_ON) || (message.getCommand() == ShortMessage.NOTE_OFF)){
        if(this.channel == message.getChannel() && (this.note == message.getNote())){
          return true;
        }
      }
      return false;
    }

    /** @return the note */
    public int getNote() {
      return note;
    }

    /** @param note the note to set */
    public void setNote(int note) {
      this.note = note;
    }
    
  }
  
  private class ParameterControl extends ControllerComponentMidi{
    private int controlChangeNum;
    private int myValue = 0;
    public ParameterControl(ControllerComponentIDs id,
                            int channel, 
                            int controlChangeNum) {
      super(id, channel);
      this.controlChangeNum = controlChangeNum;
    }
    
    
    @Override
    public void setValue(Object value){
      
      int newVal = (Integer)(value);
      if( myValue != newVal){ 
        //super.setValue(value);
        myValue = newVal;
        ViewMessage<ControllerComponentIDs> change = new ViewMessage<ControllerComponentIDs>(id, value);
        
        //Notify the views of the new value.
        notifyViews(change);
      }
    }
 
    @Override
    public void onViewChanged(Object value){
      int newVal = (Integer)(value);
      if( myValue != newVal){
        myValue = newVal;
        ViewMessage<ControllerComponentIDs> change = new ViewMessage<ControllerComponentIDs>(id, value);
        
        //Notify the views of the new value.
        notifyViews(change);
        
        //send the message along to the application.
        FpdMidiMessageShort message = FpdMidiMessageShort.createFpdMidiMessageControlChange(channel, this.controlChangeNum, (Integer)value);
        transmitToReceivers(message);
      }
    }
    
    @Override
    public boolean isControlledByMidiMessage(FpdMidiMessageShort message){
      if((message.getCommand() == ShortMessage.CONTROL_CHANGE)){
        if(this.channel == message.getChannel() && (this.controlChangeNum == message.getCCNumber())){
          return true;
        }
      }
      return false;
    }

    /** @return the note */
    public int getControlChangeNum() {
      return controlChangeNum;
    }

    /** @param note the note to set */
    public void setControlChangeNum(int controlChangeNum) {
      this.controlChangeNum = controlChangeNum;
    }

  }

  
  private class ParameterControlLedType extends ControllerComponentMidi{
    private int controlChangeNum;
    private int myValue = 16;
    public ParameterControlLedType(ControllerComponentIDs id,
                            int channel, 
                            int controlChangeNum) {
      super(id, channel);
      this.controlChangeNum = controlChangeNum;
    }
    
    
    @Override
    public void setValue(Object value){
      int b = (Integer)(value);
      if( myValue != b){
        //super.setValue(value);
        myValue = b;
        ViewMessage<ControllerComponentIDs> change = new ViewMessage<ControllerComponentIDs>(id, value);
        notifyViews(change);        
      }
    }
    

    @Override
    public boolean isControlledByMidiMessage(FpdMidiMessageShort message){
      if((message.getCommand() == ShortMessage.CONTROL_CHANGE)){
        if(this.channel == message.getChannel() && (this.controlChangeNum == message.getCCNumber())){
          return true;
        }
      }
      return false;
    }

    /** @return the note */
    public int getControlChangeNum() {
      return controlChangeNum;
    }

    /** @param note the note to set */
    public void setControlChangeNum(int controlChangeNum) {
      this.controlChangeNum = controlChangeNum;
    }
  }


  private class ControllerComponentMultiStateNoteBtn extends ControllerComponentMidi{
    int note;
    /**
     * @param channel
     * @param note
     */
    public ControllerComponentMultiStateNoteBtn(ControllerComponentIDs id,
                                                int channel, 
                                                int note) {
      super(id, channel);
      this.id = id;
      this.note = note;
    }
    
    
    @Override
    public void setValue(Object value){
      super.setValue(value);
      
      ViewMessage change = new ViewMessage(id, value);
      //Notify the view of the new value.
      notifyViews(change);

      int iVal = (Integer)value;

      if(iVal == 0){
        //value of 0 means button off.
        //FpdMidiMessageShort message = FpdMidiMessageShort.createFpdMidiMessageNoteOff(channel, note, 0); 
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
      //externalDeviceDestination.processMessage(this, message);
      transmitToReceivers(message);
    }
    
    @Override
    public boolean isControlledByMidiMessage(FpdMidiMessageShort message){
      if((message.getCommand() == ShortMessage.NOTE_ON) || (message.getCommand() == ShortMessage.NOTE_OFF)){
        if(this.channel == message.getChannel() && (this.note == message.getNote())){
          return true;
        }
      }
      return false;
    }

    /** @return the note */
    public int getNote() {
      return note;
    }

    /** @param note the note to set */
    public void setNote(int note) {
      this.note = note;
    }
  }

  
  private class ControllerComponents{
    ArrayList<ControllerComponentMidi> allComponents = new ArrayList<ControllerComponentMidi>();
    
    public ControllerComponents(){
      //setup the grid
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID00, ViewComponentIDs.BTNGRID00, 0, 53));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID01, ViewComponentIDs.BTNGRID01, 0, 54));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID02, ViewComponentIDs.BTNGRID02, 0, 55));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID03, ViewComponentIDs.BTNGRID03, 0, 56)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID04, ViewComponentIDs.BTNGRID04, 0, 57)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID05, ViewComponentIDs.BTNGRID05, 0, 52)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID06, ViewComponentIDs.BTNGRID06, 0, 51)); 
//
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID10, ViewComponentIDs.BTNGRID10, 1, 53));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID11, ViewComponentIDs.BTNGRID11, 1, 54));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID12, ViewComponentIDs.BTNGRID12, 1, 55));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID13, ViewComponentIDs.BTNGRID13, 1, 56)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID14, ViewComponentIDs.BTNGRID14, 1, 57)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID15, ViewComponentIDs.BTNGRID15, 1, 52)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID16, ViewComponentIDs.BTNGRID16, 1, 51)); 
//      
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID20, ViewComponentIDs.BTNGRID20, 2, 53));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID21, ViewComponentIDs.BTNGRID21, 2, 54));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID22, ViewComponentIDs.BTNGRID22, 2, 55));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID23, ViewComponentIDs.BTNGRID23, 2, 56)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID24, ViewComponentIDs.BTNGRID24, 2, 57)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID25, ViewComponentIDs.BTNGRID25, 2, 52)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID26, ViewComponentIDs.BTNGRID26, 2, 51)); 
//      
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID30, ViewComponentIDs.BTNGRID30, 3, 53));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID31, ViewComponentIDs.BTNGRID31, 3, 54));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID32, ViewComponentIDs.BTNGRID32, 3, 55));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID33, ViewComponentIDs.BTNGRID33, 3, 56)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID34, ViewComponentIDs.BTNGRID34, 3, 57)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID35, ViewComponentIDs.BTNGRID35, 3, 52)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID36, ViewComponentIDs.BTNGRID36, 3, 51));   
//      
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID40, ViewComponentIDs.BTNGRID40, 4, 53));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID41, ViewComponentIDs.BTNGRID41, 4, 54));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID42, ViewComponentIDs.BTNGRID42, 4, 55));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID43, ViewComponentIDs.BTNGRID43, 4, 56)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID44, ViewComponentIDs.BTNGRID44, 4, 57)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID45, ViewComponentIDs.BTNGRID45, 4, 52)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID46, ViewComponentIDs.BTNGRID46, 4, 51));   
//      
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID50, ViewComponentIDs.BTNGRID50, 5, 53));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID51, ViewComponentIDs.BTNGRID51, 5, 54));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID52, ViewComponentIDs.BTNGRID52, 5, 55));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID53, ViewComponentIDs.BTNGRID53, 5, 56)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID54, ViewComponentIDs.BTNGRID54, 5, 57)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID55, ViewComponentIDs.BTNGRID55, 5, 52)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID56, ViewComponentIDs.BTNGRID56, 5, 51));   
//      
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID60, ViewComponentIDs.BTNGRID60, 6, 53));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID61, ViewComponentIDs.BTNGRID61, 6, 54));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID62, ViewComponentIDs.BTNGRID62, 6, 55));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID63, ViewComponentIDs.BTNGRID63, 6, 56)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID64, ViewComponentIDs.BTNGRID64, 6, 57)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID65, ViewComponentIDs.BTNGRID65, 6, 52)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID66, ViewComponentIDs.BTNGRID66, 6, 51));   
//      
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID70, ViewComponentIDs.BTNGRID70, 7, 53));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID71, ViewComponentIDs.BTNGRID71, 7, 54));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID72, ViewComponentIDs.BTNGRID72, 7, 55));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID73, ViewComponentIDs.BTNGRID73, 7, 56)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID74, ViewComponentIDs.BTNGRID74, 7, 57)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID75, ViewComponentIDs.BTNGRID75, 7, 52)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID76, ViewComponentIDs.BTNGRID76, 7, 51));   
//      
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID80, ViewComponentIDs.BTNGRID80, 8, 53));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID81, ViewComponentIDs.BTNGRID81, 8, 54));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID82, ViewComponentIDs.BTNGRID82, 8, 55));
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID83, ViewComponentIDs.BTNGRID83, 8, 56)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID84, ViewComponentIDs.BTNGRID84, 8, 57)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID85, ViewComponentIDs.BTNGRID85, 8, 52)); 
//      allComponents.add(new ControllerComponentMultiStateNoteBtn(ControllerComponentIDs.BTNGRID86, ViewComponentIDs.BTNGRID86, 8, 51));   

        //add the components for the device controls
        allComponents.add(new ParameterControl(ControllerComponentIDs.DEVICECONTROL01, 0, 16));
        allComponents.add(new ParameterControl(ControllerComponentIDs.DEVICECONTROL02, 0, 17));
        allComponents.add(new ParameterControl(ControllerComponentIDs.DEVICECONTROL03, 0, 18));
        allComponents.add(new ParameterControl(ControllerComponentIDs.DEVICECONTROL04, 0, 19));
        allComponents.add(new ParameterControl(ControllerComponentIDs.DEVICECONTROL05, 0, 20));
        allComponents.add(new ParameterControl(ControllerComponentIDs.DEVICECONTROL06, 0, 21)); 
        allComponents.add(new ParameterControl(ControllerComponentIDs.DEVICECONTROL07, 0, 22));       
        allComponents.add(new ParameterControl(ControllerComponentIDs.DEVICECONTROL08, 0, 23));   

        allComponents.add(new ParameterControlLedType(ControllerComponentIDs.DEVICECONTROL01_LEDTYPE, 0, 0x18));
        allComponents.add(new ParameterControlLedType(ControllerComponentIDs.DEVICECONTROL02_LEDTYPE, 0, 0x19));
        allComponents.add(new ParameterControlLedType(ControllerComponentIDs.DEVICECONTROL03_LEDTYPE, 0, 0x1a));
        allComponents.add(new ParameterControlLedType(ControllerComponentIDs.DEVICECONTROL04_LEDTYPE, 0, 0x1b));
        allComponents.add(new ParameterControlLedType(ControllerComponentIDs.DEVICECONTROL05_LEDTYPE, 0, 0x1c));
        allComponents.add(new ParameterControlLedType(ControllerComponentIDs.DEVICECONTROL06_LEDTYPE, 0, 0x1d));
        allComponents.add(new ParameterControlLedType(ControllerComponentIDs.DEVICECONTROL07_LEDTYPE, 0, 0x1e));
        allComponents.add(new ParameterControlLedType(ControllerComponentIDs.DEVICECONTROL08_LEDTYPE, 0, 0x1f));
        
        allComponents.add(new ButtonNoteToggle(ControllerComponentIDs.BTNCLIPTRACK, 0, 58));
        allComponents.add(new ButtonNoteToggle(ControllerComponentIDs.BTNDEVICEONOFF, 0, 59));
        allComponents.add(new ButtonNoteMomentary(ControllerComponentIDs.BTNDEVICELEFT, 0, 60));
        allComponents.add(new ButtonNoteMomentary(ControllerComponentIDs.BTNDEVICERIGHT, 0, 61));
        
        
        
        
        allComponents.add(new ParameterControlLedType(ControllerComponentIDs.TOPKNOB1_LEDTYPE, 0, 0x38));
        allComponents.add(new ParameterControlLedType(ControllerComponentIDs.TOPKNOB2_LEDTYPE, 0, 0x39));
        allComponents.add(new ParameterControlLedType(ControllerComponentIDs.TOPKNOB3_LEDTYPE, 0, 0x3a));
        allComponents.add(new ParameterControlLedType(ControllerComponentIDs.TOPKNOB4_LEDTYPE, 0, 0x3b));
        allComponents.add(new ParameterControlLedType(ControllerComponentIDs.TOPKNOB5_LEDTYPE, 0, 0x3c));
        allComponents.add(new ParameterControlLedType(ControllerComponentIDs.TOPKNOB6_LEDTYPE, 0, 0x3d));
        allComponents.add(new ParameterControlLedType(ControllerComponentIDs.TOPKNOB7_LEDTYPE, 0, 0x3e));
        allComponents.add(new ParameterControlLedType(ControllerComponentIDs.TOPKNOB8_LEDTYPE, 0, 0x3f));
        
        //add the components for the parameters
        allComponents.add(new ParameterControl(ControllerComponentIDs.PARAMETER01, 10, 0));
        allComponents.add(new ParameterControl(ControllerComponentIDs.PARAMETER02, 10, 1));
        allComponents.add(new ParameterControl(ControllerComponentIDs.PARAMETER03, 10, 2));
        allComponents.add(new ParameterControl(ControllerComponentIDs.PARAMETER04, 10, 3));
        allComponents.add(new ParameterControl(ControllerComponentIDs.PARAMETER05, 10, 4));
        allComponents.add(new ParameterControl(ControllerComponentIDs.PARAMETER06, 10, 5));
        allComponents.add(new ParameterControl(ControllerComponentIDs.PARAMETER07, 10, 6));
        allComponents.add(new ParameterControl(ControllerComponentIDs.PARAMETER08, 10, 7));
        allComponents.add(new ParameterControl(ControllerComponentIDs.PARAMETER09, 10, 8));
        allComponents.add(new ParameterControl(ControllerComponentIDs.PARAMETER10, 10, 9));
        allComponents.add(new ParameterControl(ControllerComponentIDs.PARAMETER11, 10, 10));
        allComponents.add(new ParameterControl(ControllerComponentIDs.PARAMETER12, 10, 11));
        allComponents.add(new ParameterControl(ControllerComponentIDs.PARAMETER13, 10, 12));
        allComponents.add(new ParameterControl(ControllerComponentIDs.PARAMETER14, 10, 13));
        allComponents.add(new ParameterControl(ControllerComponentIDs.PARAMETER15, 10, 14));
        allComponents.add(new ParameterControl(ControllerComponentIDs.PARAMETER16, 10, 15));
        
    }
    
    
    
    /**
     * 
     */
    public void syncView() {
      //tell all of the components to resend their values to the view.

      for(Iterator<ControllerComponentMidi> i = allComponents.iterator(); i.hasNext();){
        ControllerComponentMidi component = i.next();
        component.setValue(component.value);
      }
    }


    /** 
     * @param id the id to compare to.
     * @return the ViewComponentMidi that maps to the ControllerComponentIds specified.  
     * returns null if a component isn't found.
     */
    public ControllerComponentMidi getControllerComponentFromControllerId(ControllerComponentIDs id){
      //figure out which component this message maps to.
      for(Iterator<ControllerComponentMidi> i = allComponents.iterator(); i.hasNext();){
        ControllerComponentMidi controllerComponent = i.next();
        if(controllerComponent.getId() == id){
          return controllerComponent;
        }
      }
      return null;
    }
    
    
    /** 
     * @param midiMessage The message to compare to.
     * @return the ViewComponentMidi that maps to the midiMessage specified.  
     * returns null if a component isn't found.
     */
    public ControllerComponentMidi getControllerComponentFromMidiMessage(FpdMidiMessageShort midiMessage){
      //figure out which component this message maps to.
      for(Iterator<ControllerComponentMidi> i = allComponents.iterator(); i.hasNext();){
        ControllerComponentMidi controllerComponent = i.next();
        if(controllerComponent.isControlledByMidiMessage(midiMessage)){
          return controllerComponent;
        }
      }
      return null;
    }
  }
}
