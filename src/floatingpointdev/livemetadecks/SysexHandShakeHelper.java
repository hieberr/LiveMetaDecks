package floatingpointdev.livemetadecks;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.SysexMessage;

/**
 * 
 */

/**
 * @author floatingpointdev
 *
 */
public class SysexHandShakeHelper {
  
  public SysexMessage ConstructSysexChallenge(byte[] data1, byte[] data2){
    //construct the sysex challenge message
    SysexMessage message;
    message = new SysexMessage();
    byte[] data = new byte[24];
    byte status = (byte) 240;//(0xF7 & 0xFF);
    byte dataBytesLength = (byte) 16;
    byte sysexEnd = (byte)247;
    
    data[0] = status;
    data[1] = (byte)71;
    data[2] = (byte)0;
    data[3] = (byte)115;
    data[4] = (byte)81;
    data[5] = (byte)0;
    data[6] = dataBytesLength;
    data[23] = sysexEnd;
    
    for(int i = 0; i < 8; ++i){
      data[i+7] = data1[i];
      data[i+15] = data2[i];
    }
    
    
    
    try {
      message.setMessage(data, data.length);
      return message;
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
    return null;
    
  }
  
  public SysexMessage ConstructDeviceInquiryMessage(){
    //240 126 0 6 1 247 240
    byte[] data = {(byte)240,
                  (byte)126,
                  (byte)0,
                  (byte)6,
                  (byte)1,
                  (byte)247,
                  (byte)240};
              

    SysexMessage message = new SysexMessage();
    try {
      message.setMessage(data, data.length);
      return message;
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public SysexMessage ConstructDeviceAcknowledgeMessage(){
    //240 71 0 115 96 0 4 65 xx xx xx 247
    byte[] data = {(byte)240,
                  (byte)71,
                  (byte)0,
                  (byte)115,
                  (byte)96,
                  (byte)0,
                  (byte)4,
                  (byte)65,
                  (byte)7,
                  (byte)0,   
                  (byte)16,
                  (byte)247
    };
              

    SysexMessage message = new SysexMessage();
    try {
      message.setMessage(data, data.length);
      return message;
    } catch (InvalidMidiDataException e) {
      e.printStackTrace();
    }
    return null;
  }
}
