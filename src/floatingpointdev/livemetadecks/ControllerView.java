/**
 * 
 */
package floatingpointdev.livemetadecks;

import floatingpointdev.toolkit.UI.IModel;
import floatingpointdev.toolkit.UI.IView;

/**
 * @author floatingpointdev
 *
 */
public class ControllerView implements IView{
  protected IModel controller;
  /** @see floatingpointdev.toolkit.UI.IView#attachToModel(floatingpointdev.toolkit.UI.IModel)*/
  @Override
  public void attachToModel(IModel model) {
    controller = model;
  }

  
  /** 
   * Subclasses of ControllerView should override onModelChange() to
   * respond to model change messages.
   * @see floatingpointdev.toolkit.UI.IView#onModelChanged(floatingpointdev.toolkit.UI.IModel, java.lang.Object)*/
  @Override
  public void onModelChanged(IModel theChanged, Object change) {
  }
  
  public void notifyModel(IView theChanged, Object change) {
    controller.onViewChanged(theChanged, change);
  }
}
