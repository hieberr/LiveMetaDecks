/**
 * 
 */
package floatingpointdev.livemetadecks;

import java.util.ArrayList;
import java.util.ListIterator;

/**
 * @author floatingpointdev
 *
 */
public class ChangeableComponent implements IComponentChangeable {
  private ArrayList<IComponentChangeListener> listeners = new ArrayList<IComponentChangeListener>();
  /** @see floatingpointdev.livemetadecks.IComponentChangeable#addChangeListener(floatingpointdev.toolkit.util.IReceiver)*/
  @Override
  public void addChangeListener(IComponentChangeListener listener) {
    listeners.add(listener);
  }

  /** @see floatingpointdev.livemetadecks.IComponentChangeable#removeChangeListener(floatingpointdev.toolkit.util.IReceiver)*/
  @Override
  public void removeChangeListener(IComponentChangeListener listener) {
    listeners.remove(listener);
  }

  /** @see floatingpointdev.livemetadecks.IComponentChangeable#removeChangeListeners()*/
  @Override
  public void removeChangeListeners() {
    listeners.removeAll(listeners);
  }
  
  /** @see floatingpointdev.livemetadecks.IComponentChangeable#notifyChangeListeners(java.lang.Object, java.lang.Object)*/
  @Override
  public void notifyChangeListeners(Object theChanged, Object change) {
    //send the message to each of the listeners.
    ListIterator<IComponentChangeListener> iterator = listeners.listIterator();
    while(iterator.hasNext()){
      IComponentChangeListener aListener = iterator.next();
      aListener.processComponentChangeMessage(theChanged, change);
    }
  }
}


