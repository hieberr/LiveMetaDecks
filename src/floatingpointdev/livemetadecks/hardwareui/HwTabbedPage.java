/**
 * 
 */
package floatingpointdev.livemetadecks.hardwareui;

import java.util.ArrayList;

import floatingpointdev.livemetadecks.IComponentChangeListener;


/**
 * A Page that holds a list of other pages.  Only one of these tab pages 
 * is active at a time.  A button is automatically supplied for each page
 * that is added.  This button will toggle the corresponding page to be 
 * the active page.  
 * 
 * When creating a HwTabbedPage, the number of slots that 
 * it will occupy must be supplied in the constructor.  Remember
 * to allocate enough slots for the tabButtons that are added
 * for each tab page.  So, for example, if you want a HwTabbedPage
 * that has 10 slots that are shared by the tab pages and 
 * will have 4 pages, then you must allocate 14 slots in the constructor.
 * 
 * @author floatingpointdev
 *
 */
public class HwTabbedPage extends HwPage implements IComponentChangeListener{
  ArrayList<HwPage> subPages;
  ArrayList<HwButton> tabButtons;
  HwPage activePage = null;
  int numTabsAdded = 0;
  int numTabs = 0;
  
  /**
   * Common constructor for all constructors.
   * @param numTabs The number of tabs this page will have.
   */
  private void Constructor(int numTabs){
    this.numTabs = numTabs;
    subPages = new ArrayList<HwPage>(numTabs);
    tabButtons = new ArrayList<HwButton>(numTabs);
    
    HwButton button;
    for(int i = 0; i < numTabs; ++i){
      button = new HwButton(1,this, "tabButton"+i);
      tabButtons.add(button);
      //this.addComponent(button, getNumTabSlots()+i);  
      this.addComponent(button);
      button.setSlot(0, getNumTabSlots()+i);//the tab button follow all other slots.
    }

  }

  /**
   * @param numSlots The total number of slots that the HwTabbedPage will 
   * occupy.  This includes the buttons that switch the tabs.  So for
   * example if you want a HwTabbedPage to hold 4 tabs of 10 components 
   * the total number of slots required is 14.
   * @param name
   */
  public HwTabbedPage(int numSlots, String name, int numTabs) {
    super(numSlots, name);
    Constructor(numTabs);
  }

  /**
   * @param numSlots
   */
  public HwTabbedPage(int numSlots, int numTabs) {
    super(numSlots);
    Constructor(numTabs);
  }
  
  public void addTab(HwPage page){
    //Make sure we are not adding more pages than we were told to expect.
    if(numTabsAdded >= this.numTabs){
      //DEBUG_ERROR adding more tabs than expected.
      return;
    }
    this.numTabsAdded++;
    
    //Check if page already has a parent.
    //if it does then we need to detach it so that this 
    //can become the parent.
    if(page.getParent() != null){
      page.getParent().removeComponent(page);
    }
    page.setParent(this);
    
    //add this page to our list of tabs.
    subPages.add(page);
    //this.childComponents.add(page); //<--maybe this is unnecessary
    
    //if this is the first tab to be added then
    //set it as our active page.
    
    //initially set newly added pages to not visible.
    page.setVisible(false);
    if(activePage == null){
      setActivePage(0);
    }
  }

  public void setActivePage(int pageIndex){
    if(!(pageIndex < subPages.size())){
      //DEBUG ERROR invalid page index.
      return;
    }
    HwPage newPage;
    int oldPageIndex = 0;
       
    newPage = subPages.get(pageIndex);

    //manage the tab buttons for the page switch.
    //turn off the previously selected button.
    if(activePage != null){
      oldPageIndex = subPages.indexOf(activePage);
      tabButtons.get(oldPageIndex).setValue(0);
    }
    //turn on the button that corresponds to the newly selected page.
    tabButtons.get(pageIndex).setValue(1);
    
    //set only the active page to be visible.
    if(activePage != null){ 
      activePage.setVisible(false);
    }
    activePage = newPage;
    activePage.setVisible(true);
    
    redraw();
  }
  
  /**
   * Updates the view of all slots under this Page's control.
   * for a tabbed page this means that the active page is redrawn
   * and the tab switching buttons are redrawn.
   */
  @Override
  public void redraw(){
    super.redraw();
    activePage.redraw();
  }
  
  
  /**Gets the number of slots that are available for the tabs.  So this is 
   * all slots less the number of tab switching buttons.
   * @return Gets the number of slots that are available for tabs.
   */
  private int getNumTabSlots() {
    return this.getNumSlots()-this.getNumTabs();
  }

  /** 
   * @return The number of tabs that this HwTabbedPage has.
   */
  private int getNumTabs() {
    return this.numTabs;
  }

  /** @see floatingpointdev.livemetadecks.hardwareui.HwComponent#processViewMessage(int, java.lang.Object)
   */
  @Override
  public void processViewMessage(int slot, Object newValue) {
    super.processViewMessage(slot, newValue); //takes care of the tab selection buttons.
    
    //A change has occurred in one of this tabbedPage's slots.
    
    //if this corresponds to an element on one of the tab pages
    //then we need to pass the message along to the active tab.
    if(slot < getNumTabSlots()){
      activePage.processViewMessage(getChildSlotFromParentSlot(slot), newValue);
    }
  }


  /** Process change messages from the components on this Page.
   *  @see floatingpointdev.livemetadecks.IComponentChangeListener#processComponentChangeMessage(java.lang.Object, java.lang.Object)*/
  @Override
  public void processComponentChangeMessage(Object transmitter, Object change) {
    HwButton button;

    if((Integer)change == 1){ // only respond to the button pressed event.
      for(int i = 0; i < this.numTabs; ++i){
        button = tabButtons.get(i);
        if(transmitter == tabButtons.get(i)){
          setActivePage(i);
          break;
        }//if
      }//for
    }//if
  }

}
