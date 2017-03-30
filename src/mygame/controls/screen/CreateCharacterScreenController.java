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
import mygame.AppStates.WorldAppState;
import mygame.AppStates.WorldAppStates;

/**
 *
 * @author root
 */
public class CreateCharacterScreenController implements ScreenController {

    private Nifty nifty;
    private AppStateManager stateManager;
    
    
    public void valid() {
        WorldAppState worldAppState = new WorldAppState();
        stateManager.attach(worldAppState);
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
