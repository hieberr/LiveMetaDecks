/**
 * 
 */
package floatingpointdev.livemetadecks.hardwareui;

import floatingpointdev.livemetadecks.IComponentChangeListener;

/**
 * Specialized version of HwKnob for the Apc40 Led Knobs.  The knobs on an Apc40
 * are implemented in 2 parts:  The knob and the LED surrounding the knob.  
 * The knob itself sends and receives values between 0 and 127. the Led ring
 * displays that value in one of 4 ways. 
 * 
 *  0 - LedRing off
 *  1 - single led 
 *  2 - bar style
 *  3 - pan style 
 *  
 *  So this version of HwKnob requires 2 slots, one for the knob, and one for the
 *  Led ring.  The style of the Led ring is then set with setLedStyle() function.
 * @author floatingpointdev
 *
 */
public class HwKnobApcLed extends HwKnob {


  public static final int LED_STYLE_OFF = 0;
  public static final int LED_STYLE_SINGLE = 1;
  public static final int LED_STYLE_BAR = 2;
  public static final int LED_STYLE_PAN = 3;

  private int ledStyle = LED_STYLE_SINGLE;
  
  /**
   * @param listener
   * @param name
   */
  public HwKnobApcLed(IComponentChangeListener listener,
      String name) {
    super(2, listener, name);
  }

  
  /**
   * @param listener
   */
  public HwKnobApcLed(IComponentChangeListener listener) {
    super(2, listener);
  }

  
  /**
   * @param name
   */
  public HwKnobApcLed(String name) {
    super(2, name);
  }

  /**
   */
  public HwKnobApcLed() {
    super(2);
  }

  /** 
   * 
 *  0 - LedRing off
 *  1 - single led 
 *  2 - bar style
 *  3 - pan style 
 *  @return the ledStyle */
  public int getLedStyle() {
    return ledStyle;
  }

  /** @param ledStyle the ledStyle to set */
  public void setLedStyle(int ledStyle) {
    this.ledStyle = ledStyle;
  }
  
  @Override 
  public void draw(){
    super.draw();
    updateThisComponent(1,this.ledStyle);  //send the style for the ledRing
  }
}
