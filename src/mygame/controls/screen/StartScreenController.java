package mygame.controls.screen;


import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioRenderer;
import com.jme3.input.InputManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;

import mygame.Main;



public class StartScreenController extends AbstractAppState
        implements ScreenController, Controller{

    private SimpleApplication app;
    private AppStateManager stateManager;
    private AssetManager assetManager;
    private InputManager inputManager;
    private AudioRenderer audioRenderer;
    private Camera cam;
    private ViewPort viewPort;
    
    Node guiNode, rootNode;
    Node localRootNode = new Node("startRoot");
    
    Nifty nifty;
    Screen screen;
    Element element;
    ViewPort guiViewPort;
    NiftyJmeDisplay niftyDisplay;
    
    public void newGame() {
        nifty.gotoScreen("CreateCharacterScreen");
    }
    
    public void loadGame() {
        nifty.gotoScreen("LoadScreen");
    }
    
    public void quit() {
        Main.app.stop();
    }

    public void onStartScreen() {

    }

    public void onEndScreen() {

    }

    public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {
           this.nifty = nifty;
           this.screen = screen;
           this.element = element;
    }

    public void init(Properties parameter, Attributes controlDefinitionAttributes) {

    }

    public void onFocus(boolean getFocus) {

    }

    public boolean inputEvent(NiftyInputEvent inputEvent) {
          boolean oblige = false;
          return oblige;
    }

    public void bind(Nifty nifty, Screen screen) {
        
    }
    
}
    