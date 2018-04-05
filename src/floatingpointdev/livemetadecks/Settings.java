/**
 * 
 */
package floatingpointdev.livemetadecks;

/**
 * @author floatingpointdev
 *
 */
public class Settings {
  private SettingsUI settingsUI;
  
  /**
   * 
   */
  public Settings() {
    super();
    settingsUI = new SettingsUI();
    //initialize all the settings
    //read settings from file.
  }


  public void displayUI(boolean visible){
     settingsUI.setVisible(visible);
  }
}
