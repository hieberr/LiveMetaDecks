/**
 * 
 */
package floatingpointdev.livemetadecks;

import floatingpointdev.toolkit.UI.IModel;
import floatingpointdev.toolkit.UI.IView;
import floatingpointdev.toolkit.UI.ModelComponent;
import floatingpointdev.toolkit.midi.IReceiver;
import floatingpointdev.toolkit.midi.ITransmitter;
import floatingpointdev.toolkit.midi.TransmitterComponent;

/**
 * A midi controller.  It sends midi output to all of its registered receivers
 * and receives midi input through the IReceiver interface.
 * @author floatingpointdev
 *
 */
public class Controller implements IReceiver, ITransmitter, IModel {

  private String name;  //Optional name of the controller.
  private String description; //optional description of what this controller does.
  
  public Controller(){
    setName("");
    setDescription("");
  }
  
  
  /**
   * @param name Optional name of the controller.
   * @param description Optional description of what this controller does.
   */
  public Controller(String name, String description) {
    setName(name);
    setDescription(description);
  }

  /** @return the name */
  public String getName() {
    return name;
  }

  /** @param name the name to set */
  public void setName(String name) {
    this.name = name;
    modelComponent.notifyIViews(this, "name");
  }

  /** @return the description */
  public String getDescription() {
    return description;
  }

  /** @param description the description to set */
  public void setDescription(String description) {
    this.description = description;
    modelComponent.notifyIViews(this, "description");
  }

  /**
   * Implementers should override this method and have it
   * notify its observers that everything has changed.  Then 
   * the observers will get a complete update. 
   * Implementers should override this method.
   */
  public void syncViews(){
    //set every observed value as changed so that all observers get a
    //complete dump of the controller's state.
  }

  // ------- Interface Stuff ----------
  
  //IReceiver interface.
  /** 
   * Implementers should override this method to handle 
   * incoming messages through the IReceiver interface.
   *@see floatingpointdev.toolkit.midi.util.IReceiver#processMessage(java.lang.Object, java.lang.Object)*/
  @Override
  public void processMessage(Object transmitter, Object message) {
  }
  
  //ITransmitter interface.
  private TransmitterComponent transmitterComponent = new TransmitterComponent();

  /** @see
   * floatingpointdev.toolkit.midi.util.ITransmitter#addReceiver(floatingpointdev.toolkit.midi.util.IReceiver)*/
  @Override
  public void addReceiver(IReceiver receiver) {
    transmitterComponent.addReceiver(receiver);
  }

  /** @see floatingpointdev.toolkit.midi.util.ITransmitter#deleteMidiReceiver(floatingpointdev.toolkit.midi.util.IReceiver)*/
  @Override
  public void deleteReceiver(IReceiver receiver) {
    transmitterComponent.deleteReceiver(receiver);
  }

  /** @see floatingpointdev.toolkit.midi.util.ITransmitter#deleteMidiReceivers()*/
  @Override
  public void deleteReceivers() {
    transmitterComponent.deleteReceivers();
  }

  /** @see floatingpointdev.toolkit.midi.util.ITransmitter#transmitToReceivers(java.lang.Object, java.lang.Object)*/
  @Override
  public void transmitToReceivers(Object transmitter, Object message) {
    transmitterComponent.transmitToReceivers(transmitter, message);
  }

  
  
  // ---------- IModel methods -----------
  protected ModelComponent modelComponent = new ModelComponent();

  /** Implementors should override this method to receive change notification 
   * from any view attached to this controller.
   *  @see floatingpointdev.toolkit.UI.IModel#onViewChanged(floatingpointdev.toolkit.UI.IView, java.lang.Object)*/
  @Override
  public void onViewChanged(IView theChanged, Object change) {
  }
  
  /** @see floatingpointdev.toolkit.UI.IModel#addIView(floatingpointdev.toolkit.UI.IView)*/
  @Override
  public void addIView(IView view) {
    modelComponent.addIView(view);
  }

  /** @see floatingpointdev.toolkit.UI.IModel#removeIView(floatingpointdev.toolkit.UI.IView)*/
  @Override
  public void removeIView(IView view) {
    modelComponent.removeIView(view);
  }
  
  /** @see floatingpointdev.toolkit.UI.IModel#removeAllIViews()*/
  @Override
  public void removeAllIViews() {
    modelComponent.removeAllIViews();
    
  }
}
