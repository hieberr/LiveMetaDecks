package floatingpointdev.livemetadecks;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import floatingpointdev.toolkit.util.IObserver;

/**
 * 
 */

/**
 * The user interface. 
 * @author floatingpointdev
 *
 */
public class LiveMetaDecksUI extends JFrame implements IObserver{
  /** */
  private static final long serialVersionUID = 2171091542169431114L;
  LiveMetaDecks liveMetaDecks;
  Settings settings;
  /**
   * @param args
   */
  

  /**
   * @param liveMetaDecks the instance of LiveMetaDecks that this UI observes.
   * @throws HeadlessException
   */
  public LiveMetaDecksUI(LiveMetaDecks liveMetaDecks) throws HeadlessException {
    super();
    this.liveMetaDecks = liveMetaDecks;
    liveMetaDecks.addIObserver(this);
    settings = new Settings();
    setupUI();
  }

  /**
   * @param title
   * @throws HeadlessException
   */
  public LiveMetaDecksUI(String title) throws HeadlessException {
    super(title);
    setupUI();
  }
  
  private void setupUI(){
    JPanel appletFrame = new JPanel();
    JButton btnSettings = new JButton();
    btnSettings.setText("Settings");
    btnSettings.addActionListener(new ActionListener() {   
       public void actionPerformed(ActionEvent e){
         settings.displayUI(true);
       }
     });
    
    JButton btnRunApplicationA = new JButton();
    btnRunApplicationA.setText("dohandshake");
    btnRunApplicationA.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e){
         liveMetaDecks.applicationA.runApplication();
         //ControllerMidiPassThru c = (ControllerMidiPassThru)(liveMetaDecks.controllerA);
         //c.DoHandShake();
       }
     });
    JButton btnRunApplicationB = new JButton();
    btnRunApplicationB.setText("RunAppB");
    btnRunApplicationB.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e){
         liveMetaDecks.applicationB.runApplication();
       }
     });
    
    JPanel content = new JPanel();
    content.add(appletFrame);
    
    content.add(btnSettings);
    content.add(btnRunApplicationA);
    content.add(btnRunApplicationB);
    setContentPane(content);
    pack();
    
    //------------
    // important to call this whenever embedding a PApplet.
    // It ensures that the animation thread is started and
    // that other internal variables are properly set.
    //liveMetaDecksPApplet.init();
    //appletFrame.requestFocusInWindow();
    //liveMetaDecksPApplet.requestFocusInWindow();
    
    
    //Setup the event handler for closing the window.
    addWindowListener(new WindowAdapter() {
        public void windowClosing(WindowEvent e) {
          System.exit(0);
        }
      });

    setTitle ("LiveMetaDecks");
    setSize (300,300);
  }

  //*************** Interface stuff ***************
  /** @see floatingpointdev.toolkit.util.IObserver#update(java.lang.Object, java.lang.Object)*/
  @Override
  public void update(Object theObserved, Object changeCode) {
    //make sure theObserved is an instance of Parameters
    if(!(theObserved instanceof LiveMetaDecks)){
      //error condition, bail out.  
      //DEBUG_ERROR
      return;
    }
    //LiveMetaDecks paramTheObserved = (LiveMetaDecks)theObserved;
    
    //make sure the change code is a string.
    if(changeCode instanceof String){
      //error condition, bail out.  
      //DEBUG_ERROR
      return;
    }
    //String strChangeCode = (String)changeCode;
    
    
    //check the change code to see which element of the parameter has changed.
    //if(strChangeCode.equalsIgnoreCase("userval")){
      //float userVal = paramTheObserved.getUserVal();
      //update the UI
      //sliderUserVal.setValue((int)(userVal*10f));
    //}
    
  }
}
