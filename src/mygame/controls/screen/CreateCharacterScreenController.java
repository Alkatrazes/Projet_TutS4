/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controls.screen;

import com.jme3.app.state.AppStateManager;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.AppStates.GamePlayAppStates;

/**
 *
 * @author root
 */
public class CreateCharacterScreenController implements ScreenController {

    private Nifty nifty;
    private AppStateManager stateManager;
    
    public void valid() {
        GamePlayAppStates gamePlayAppStates = new GamePlayAppStates();
        stateManager.attach(gamePlayAppStates);
    }
    
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
