/**
 * 
 */
package floatingpointdev.livemetadecks;

import javax.sound.midi.SysexMessage;

import floatingpointdev.livemetadecks.Apc40.DeviceComponentIDsApc40;
import floatingpointdev.livemetadecks.ControllerLiveMetaDecks.ControllerComponentIDs;
import floatingpointdev.livemetadecks.hardwareui.HwButton;
import floatingpointdev.livemetadecks.hardwareui.HwKnobApcLed;
import floatingpointdev.livemetadecks.hardwareui.HwPage;
import floatingpointdev.livemetadecks.hardwareui.HwTabbedPage;
import floatingpointdev.livemetadecks.hardwareui.RootPage;
import floatingpointdev.toolkit.UI.IModel;
import floatingpointdev.toolkit.UI.IView;
import floatingpointdev.toolkit.UI.ViewMessage;
import floatingpointdev.toolkit.midi.FpdMidiMessageShort;
import floatingpointdev.toolkit.midi.IReceiver;

/**
 * @author floatingpointdev
 *
 */
public class ControllerLiveMetaDecksUi implements IView, IReceiver{
  Controller controllerA;
  Controller controllerB;
  Controller selectedController;
  ControllerPage controllerAPage;
  Apc40 apc40Device;

  ControllerLiveMetaDecksUi(Controller controllerA, Controller controllerB){
      this.controllerA = controllerA;
      this.controllerB = controllerB;
      
      //setup the model view relationship between this IView and the IModel controllers.
      controllerA.addIView(this);
      controllerB.addIView(this);
      
      selectedController = controllerA;
      apc40Device = new Apc40();
      apc40Device.addReceiver(this);
      
      controllerAPage = new ControllerPage(apc40Device);
    }

    public void setSelectedController(Controller controller){
      selectedController = controller;
    }


    //IView Interface stuff.
    /**
     *  This method shouldnt  be used. set the two controllers that this UI Views
     *  through the constructor.
     *  @see floatingpointdev.toolkit.UI.IView#attachToModel(floatingpointdev.toolkit.UI.IModel)
     */
    @Override
    public void attachToModel(IModel model) {
    }

    /** @see floatingpointdev.toolkit.UI.IView#onModelChanged(floatingpointdev.toolkit.UI.IModel, java.lang.Object)*/
    @Override
    public void onModelChanged(IModel theChanged, Object change) {
      if(theChanged == controllerA){
        //process a change message from controllerA
        processMessageFromControllerA(change);
      } else if(theChanged == controllerB){
        //process a change message from controllerB
      }
    }

    
    private void processMessageFromControllerA(Object message){
      if(message instanceof ViewMessage){
        ViewMessage<ControllerComponentIDs> vm = (ViewMessage<ControllerComponentIDs>)message;
        controllerAPage.processModelMessage(vm);
      }else if(message instanceof SysexMessage){
        if(selectedController == controllerA){
          apc40Device.sendToDevice(message);
        }
      } else if(message instanceof FpdMidiMessageShort){
        if(selectedController == controllerA){
          apc40Device.sendToDevice(message);
        }
      }
    }
    
    
    //IReceiver Interface stuff.
    /** process a message from the midi device. 
     * @see floatingpointdev.toolkit.midi.util.IReceiver#processMessage(java.lang.Object, java.lang.Object)*/
    @Override
    public void processMessage(Object transmitter, Object message) {
      if(transmitter == apc40Device){
        //process a message from the apc40.
        if(message instanceof ViewMessage){
          controllerAPage.processViewMessage((ViewMessage<DeviceComponentId<?>>)message);
        }else if(message instanceof SysexMessage){
          //pass the sysex on to abletonLive
          selectedController.onViewChanged(null, message);
        } else if(message instanceof FpdMidiMessageShort){
          selectedController.onViewChanged(null, message);  
        }
      }
    }
    
      
    public class ControllerPage extends RootPage implements IComponentChangeListener{
      ControllerDevice controllerDeviceApc40;
      HwKnobApcLed deviceKnob1 = new HwKnobApcLed(this, "deviceKnob1");
      HwKnobApcLed deviceKnob2 = new HwKnobApcLed(this, "deviceKnob2");
      HwKnobApcLed deviceKnob3 = new HwKnobApcLed(this, "deviceKnob3");
      HwButton btnClipTrack = new HwButton(1, this, "btn Clip/Track" );
      HwButton btnDeviceOnOff = new HwButton(1, this, "btn Device On Off");
      HwButton btnLeft = new HwButton(1, this, "btn Left");
      HwButton btnRight = new HwButton(1, this, "btn Right");
//      TabbedPage    topBankTab;
//      TabbedPage    bottomBankTab;
//      PageDeviceKnobs pageDeviceKnobsBottom = new PageDeviceKnobs();
//      PageDeviceKnobs pageDeviceKnobsTop = new PageDeviceKnobs();
//      PageParameterKnobs pageParameterKnobs = new PageParameterKnobs();
//        


      ControllerPage(ControllerDevice controllerDeviceApc40){
        super(128);
        this.controllerDeviceApc40 = controllerDeviceApc40;
        
        this.setSlot(0,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB1));
        this.setSlot(1,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB2));
        this.setSlot(4,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB3));
        this.setSlot(6,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB4));
        this.setSlot(8,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB5));
        this.setSlot(10,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB6));
        this.setSlot(12,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB7));
        this.setSlot(14,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB8));
        
        this.setSlot(2,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB1_LEDTYPE));
        this.setSlot(3,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB2_LEDTYPE));
        this.setSlot(5,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB3_LEDTYPE));
        this.setSlot(7,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB4_LEDTYPE));
        this.setSlot(9,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB5_LEDTYPE));
        this.setSlot(11,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB6_LEDTYPE));
        this.setSlot(13,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB7_LEDTYPE));
        this.setSlot(15,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB8_LEDTYPE));
        
        this.setSlot(16,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.BTNCLIPTRACK));
        this.setSlot(17,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.BTNDEVICEONOFF));
        this.setSlot(18,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.BTNDEVICELEFT));
        this.setSlot(19,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.BTNDEVICERIGHT));
        
        HwPage a = new HwPage(4, "a");
        HwPage b = new HwPage(4, "b");
        HwTabbedPage tabAb = new HwTabbedPage(6,"tabAb",2);
        
        int[] tabAbIds = {0,2,1,3,16,17};
        int[] aIds = {0,1,2,3};
        int[] bIds = {0,1,2,3};     
        int[] deviceKnob1Ids = {0,1};
        int[] deviceKnob2Ids = {0,1};
        int[] deviceKnob3Ids = {2,3};
        
        tabAb.addTab(a);
        a.setSlots(aIds);
        tabAb.addTab(b);
        b.setSlots(bIds);
        tabAb.setActivePage(1);
        
        a.addComponent(deviceKnob1);
        deviceKnob1.setSlots(deviceKnob1Ids);
        deviceKnob1.setLedStyle(3);
        b.addComponent(deviceKnob2);
        deviceKnob2.setSlots(deviceKnob2Ids);
        b.addComponent(deviceKnob3);
        deviceKnob3.setSlots(deviceKnob3Ids);
        
        //this.addComponent(btnClipTrack, 8);
        //this.addComponent(btnDeviceOnOff, 9);
        this.addComponent(btnLeft);
        btnLeft.setSlot(0, 18);
        this.addComponent(btnRight);
        btnRight.setSlot(0, 19);
        
        this.addComponent(tabAb);
        tabAb.setSlots(tabAbIds);

        this.redraw();
        //this.removeComponent(b);
//        //initialize the slots with their device ids.
//        topBankTab = new TabbedPage(20);
//        topBankTab.setSlot(0,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB1));
//        topBankTab.setSlot(1,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB2));
//        topBankTab.setSlot(2,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB3));
//        topBankTab.setSlot(3,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB4));
//        topBankTab.setSlot(4,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB5));
//        topBankTab.setSlot(5,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB6));
//        topBankTab.setSlot(6,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB7));
//        topBankTab.setSlot(7,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB8));
//
//        topBankTab.setSlot(8,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB1_LEDTYPE));
//        topBankTab.setSlot(9,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB2_LEDTYPE));
//        topBankTab.setSlot(10,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB3_LEDTYPE));
//        topBankTab.setSlot(11,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB4_LEDTYPE));
//        topBankTab.setSlot(12,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB5_LEDTYPE));
//        topBankTab.setSlot(13,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB6_LEDTYPE));
//        topBankTab.setSlot(14,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB7_LEDTYPE));
//        topBankTab.setSlot(15,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB8_LEDTYPE));
//        
//        
//        topBankTab.setSlot(16,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.BTNPAN));
//        topBankTab.setSlot(17,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.BTNSENDA));
//        topBankTab.setSlot(18,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.BTNSENDB));
//        topBankTab.setSlot(19,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.BTNSENDC));
//        
//        //setup the bottom knob bank tab
//        bottomBankTab = new TabbedPage(20); //8 knobs, 8 knob leds, 4 buttons.  no tab buttons.
//        bottomBankTab.setSlot(0,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB9));
//        bottomBankTab.setSlot(1,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB10));
//        bottomBankTab.setSlot(2,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB11));
//        bottomBankTab.setSlot(3,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB12));
//        bottomBankTab.setSlot(4,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB13));
//        bottomBankTab.setSlot(5,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB14));
//        bottomBankTab.setSlot(6,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB15));
//        bottomBankTab.setSlot(7,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB16));
//        
//        bottomBankTab.setSlot(8,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB9_LEDTYPE));
//        bottomBankTab.setSlot(9,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB10_LEDTYPE));
//        bottomBankTab.setSlot(10,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB11_LEDTYPE));
//        bottomBankTab.setSlot(11,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB12_LEDTYPE));
//        bottomBankTab.setSlot(12,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB13_LEDTYPE));
//        bottomBankTab.setSlot(13,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB14_LEDTYPE));
//        bottomBankTab.setSlot(14,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB15_LEDTYPE));
//        bottomBankTab.setSlot(15,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.KNOB16_LEDTYPE));
//        
//        bottomBankTab.setSlot(16,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.BTNCLIPTRACK));
//        bottomBankTab.setSlot(17,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.BTNDEVICEONOFF));
//        bottomBankTab.setSlot(18,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.BTNDEVICELEFT));
//        bottomBankTab.setSlot(19,new DeviceComponentId<DeviceComponentIDsApc40>(controllerDeviceApc40,DeviceComponentIDsApc40.BTNDEVICERIGHT));
//        
//        pageParameterKnobs.attachToModel(controllerA);
//        topBankTab.addTab(pageParameterKnobs);
//        topBankTab.addTab(pageDeviceKnobsTop);
//        topBankTab.setSelectedPage(0);
//        pageDeviceKnobsTop.attachToModel(controllerA);
//        pageDeviceKnobsBottom.attachToModel(controllerA);
//        bottomBankTab.addTab(pageDeviceKnobsBottom);
//        
//        this.addPage(topBankTab);
//        this.addPage(bottomBankTab);
        //this.setVisible(true);
      }

      /**
       * @param message
       */
      public void processModelMessage(ViewMessage<ControllerComponentIDs> message) {
        ControllerComponentIDs changedId = message.getComponentID();
        Object newValue = message.getNewValue();
        
        switch (changedId) {
          case DEVICECONTROL01 :
            this.deviceKnob1.setValue(newValue);
            break;
          case DEVICECONTROL02 :
            this.deviceKnob2.setValue(newValue);
            break;
          case DEVICECONTROL03 :
            this.deviceKnob3.setValue(newValue);
            break;
          case BTNCLIPTRACK :
            this.btnClipTrack.setValue(newValue);
            break;
          case BTNDEVICEONOFF :
            this.btnDeviceOnOff.setValue(newValue);
            break;
          case BTNDEVICELEFT :
            this.btnLeft.setValue(newValue);
            break;
          case BTNDEVICERIGHT :
            this.btnRight.setValue(newValue);
            break;
        }
      }

      /** Process change messages from all of the components.
       *  @see floatingpointdev.livemetadecks.IComponentChangeListener#processComponentChangeMessage(java.lang.Object, java.lang.Object)*/
      @Override
      public void processComponentChangeMessage(Object transmitter, Object change) {
        ViewMessage<ControllerComponentIDs> vm = null;
        if(transmitter == deviceKnob1){
            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.DEVICECONTROL01,change);
        } else if(transmitter == deviceKnob2){
            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.DEVICECONTROL02,change);
        } else if(transmitter == deviceKnob3){
            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.DEVICECONTROL03,change);
        }// else if(transmitter == DeviceKnob4){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.DEVICECONTROL04,change);
//        } else if(transmitter == DeviceKnob5){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.DEVICECONTROL05,change);
//        } else if(transmitter == DeviceKnob6){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.DEVICECONTROL06,change);
//        } else if(transmitter == DeviceKnob7){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.DEVICECONTROL07,change);
//        } else if(transmitter == DeviceKnob8){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.DEVICECONTROL08,change);
//        }
//            
        else if(transmitter == btnClipTrack){
            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.BTNCLIPTRACK,change);
        } else if(transmitter == btnDeviceOnOff){
            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.BTNDEVICEONOFF,change);
        } else if(transmitter == btnLeft){
            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.BTNDEVICELEFT,change);
        } else if(transmitter == btnRight){
            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.BTNDEVICERIGHT,change);
        }
//            
//         if(transmitter == parameter01){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.PARAMETER01,change);
//        }else if(transmitter == parameter02){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.PARAMETER02,change);
//        }else if(transmitter == parameter03){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.PARAMETER03,change);
//        }else if(transmitter == parameter04){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.PARAMETER04,change);
//        }else if(transmitter == parameter05){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.PARAMETER05,change);
//        }else if(transmitter == parameter06){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.PARAMETER06,change);
//        }else if(transmitter == parameter07){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.PARAMETER07,change);
//        }else if(transmitter == parameter08){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.PARAMETER08,change);
//        }else if(transmitter == parameter09){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.PARAMETER09,change);
//        }else if(transmitter == parameter10){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.PARAMETER10,change);
//        }else if(transmitter == parameter11){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.PARAMETER11,change);
//        }else if(transmitter == parameter12){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.PARAMETER12,change);
//        }else if(transmitter == parameter13){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.PARAMETER13,change);
//        }else if(transmitter == parameter14){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.PARAMETER14,change);
//        }else if(transmitter == parameter15){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.PARAMETER15,change);
//        }else if(transmitter == parameter16){
//            vm = new ViewMessage<ControllerComponentIDs>(ControllerComponentIDs.PARAMETER16,change);
//        }

        if(vm != null){
          controllerA.onViewChanged(null, vm);
        }
      }
    }
}