package floatingpointdev.livemetadecks;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import floatingpointdev.toolkit.util.IObserver;

/**
 * @author floatingpointdev
 *
 */
public class SettingsUI extends JFrame implements ActionListener, IObserver{
  /** */
  private static final long serialVersionUID = -6680262041127137580L;

  /**
   * @throws HeadlessException
   */
  public SettingsUI() throws HeadlessException {
    super();
    setupUI();
  }


  /**
   * Build the user interface for this swing component.
   */
  public void setupUI(){
    this.setTitle("Settings");
    
  }
  
  
  /** @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)*/
  @Override
  public void actionPerformed(ActionEvent e) {
  }

  /** @see floatingpointdev.toolkit.util.IObserver#update(java.lang.Object, java.lang.Object)*/
  @Override
  public void update(Object theObserved, Object changeCode) {
  }
}
