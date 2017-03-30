/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import mygame.AppStates.CombatAppState;
import mygame.AppStates.WorldAppState;

    
/**
 *
 * @author Tanguy
 */
public class MainTest extends SimpleApplication{
    private static MainTest app;
    public CombatAppState worldAppState;
    
    public static void main(String[] args) {
        app = new MainTest();
        app.start(); 
    }
    
    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        
        worldAppState = new CombatAppState();
        stateManager.attach(worldAppState);
    }
      /** A centred plus sign to help the player aim. */
}
