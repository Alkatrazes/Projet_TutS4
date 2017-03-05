/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controls.screen;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;

/**
 *
 * @author root
 */
public class CreateCharacterScreenController implements ScreenController {

    private Nifty nifty;
    
    public void back() {
        nifty.gotoScreen("StartScreen");
    }
    
    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
        
    }

    public void onStartScreen() {

    }

    public void onEndScreen() {

    }
}
