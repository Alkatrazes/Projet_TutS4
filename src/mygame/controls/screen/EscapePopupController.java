/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controls.screen;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.Main;

/**
 *
 * @author root
 */
public class EscapePopupController implements ScreenController {
    
    private Nifty nifty;
    
    public void options() {
        
    }
    
    public void backTitle() {
        nifty.gotoScreen("StartScreen");
    }
    
    public void quit() {
        Main.app.stop();
    }

    public void bind(Nifty nifty, Screen screen) {
        this.nifty = nifty;
    }

    public void onStartScreen() {

    }

    public void onEndScreen() {

    }
    
}
