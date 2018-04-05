package floatingpointdev.toolkit.midi;


/**
 * interface for any object that recieves Messages from an ITransmitter.
 * @author floatingpointdev
 *
 */
public interface IReceiver {
  public void processMessage(Object transmitter, Object message);
}
