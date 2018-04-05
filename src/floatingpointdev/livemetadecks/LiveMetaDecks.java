/**
 * 
 */
package floatingpointdev.livemetadecks;
import floatingpointdev.toolkit.midi.IoMidi;
import floatingpointdev.toolkit.util.IObservable;
import floatingpointdev.toolkit.util.IObserver;
import floatingpointdev.toolkit.util.ObservableComponent;
/**
 * The Live MetaDecks Application.
 * @author floatingpointdev
 *
 */
public class LiveMetaDecks implements IObservable{
  private Globals globals;
  private LiveMetaDecksUI liveMetaDecksUI;
  public ApplicationMidi applicationA;
  public ApplicationMidi applicationB;
  public ControllerLiveMetaDecks controllerA;
  private ControllerLiveMetaDecks controllerB;
  
  private Deck deckA;
  private Deck deckB;
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    @SuppressWarnings("unused")
    LiveMetaDecks liveMetaDecks = new LiveMetaDecks();
  }
  /**
   * 
   */
  public LiveMetaDecks() {
    globals = Globals.getInstance();

    //setup the two controllers.
    controllerA = new ControllerLiveMetaDecks();
    controllerB = new ControllerLiveMetaDecks();
    
    //setup the views for the two hardware controllers.
    //ControllerLiveMetaDecksUIOLD hardwareUI = new ControllerLiveMetaDecksUIOLD(controllerA, controllerB);
    ControllerLiveMetaDecksUi hardwareUI = new ControllerLiveMetaDecksUi(controllerA, controllerB);

    //setup the two Live applications.
    applicationA = new ApplicationMidi(globals.ioMidi, "AppA", "IAC Driver - IAC Bus 2", "IAC Driver - IAC Bus 1", "/scripts/LiveA");
    applicationB = new ApplicationMidi(globals.ioMidi, "AppB", "IAC Driver - IAC Bus 4", "IAC Driver - IAC Bus 3", "/scripts/LiveB");
    
    //create the two decks.
    deckA = new Deck(applicationA, controllerA);
    deckB = new Deck(applicationB, controllerB);

    //create and display the on screen  UI.
    liveMetaDecksUI = new LiveMetaDecksUI(this);
    liveMetaDecksUI.setVisible(true);    
  }
  

  // ---------- IOBservable methods -----------
  private ObservableComponent observableComponent = new ObservableComponent();
  /** @see floatingpointdev.toolkit.util.IObservable#addIObserver(floatingpointdev.toolkit.util.IObserver)*/
  @Override
  public void addIObserver(IObserver anIObserver) {
    observableComponent.addIObserver(anIObserver);
  }

  /** @see floatingpointdev.toolkit.util.IObservable#deleteIObserver(floatingpointdev.toolkit.util.IObserver)*/
  @Override
  public void deleteIObserver(IObserver anIObserver) {
    observableComponent.deleteIObserver(anIObserver);
  }

  /** @see floatingpointdev.toolkit.util.IObservable#deleteIObservers()*/
  @Override
  public void deleteIObservers() {
    observableComponent.deleteIObservers();
  }
}
