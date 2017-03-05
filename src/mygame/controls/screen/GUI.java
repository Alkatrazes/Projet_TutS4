/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controls.screen;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.niftygui.NiftyJmeDisplay;

import de.lessvoid.nifty.Nifty;

/**
 *
 * @author root
 */
public class GUI extends AbstractAppState {
    
    private Nifty nifty;
    private Application app;
    
    public GUI(Application app) {
        
        this.app = app;
        
        NiftyJmeDisplay niftyDisplay = new NiftyJmeDisplay(app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
        nifty = niftyDisplay.getNifty();
        
        nifty.addXml("Interface/Nifty/startScreen.xml");
        nifty.addXml("Interface/Nifty/createCharacterScreen.xml");
        nifty.addXml("Interface/Nifty/loadScreen.xml");
        
        nifty.gotoScreen("StartScreen");
        
        app.getGuiViewPort().addProcessor(niftyDisplay); 
    }
}
