/**
 * 
 */
package floatingpointdev.livemetadecks;

/**
 * Represents an instance of Ableton live.  Essentially a virtual
 * DJ "Deck".  This class connects a Controller to the Application.
 * @author floatingpointdev
 *
 */
public class Deck {
  ApplicationMidi applicationMidi;
  Controller controller;
  
  /**
   * @param applicationMidi
   * @param controller
   */
  public Deck(ApplicationMidi applicationMidi, Controller controller) {
    super();
    this.applicationMidi = applicationMidi;
    this.controller = controller;
    
    linkApplicationAndController();
  }
  
  private void linkApplicationAndController(){
    applicationMidi.addReceiver(controller);
    controller.addReceiver(applicationMidi);
  }
  
  /**
   * If the application and the controller are linked up 
   * then they are unlinked. 
   */
  private void unlinkApplicationAndController(){
    if(this.controller != null){
      //tear down old connections
      this.controller.deleteReceiver(this.applicationMidi);
      this.controller = null;
    }
    if(this.applicationMidi != null){
      this.applicationMidi.deleteReceiver(this.controller);
    }
  }
  
  /** @return the applicationMidi */
  public ApplicationMidi getApplicationMidi() {
    return applicationMidi;
  }
  
  /** @param applicationMidi the applicationMidi to set */
  public void setApplicationMidi(ApplicationMidi applicationMidi) {
    unlinkApplicationAndController();
    
    this.applicationMidi = applicationMidi;
    linkApplicationAndController();
  }
  
  /** @return the controller */
  public Controller getController() {
    return controller;
  }
  
  /** @param controller the controller to set */
  public void setController(Controller controller) {
    unlinkApplicationAndController();
    this.controller = controller;
    linkApplicationAndController();
  }
}
