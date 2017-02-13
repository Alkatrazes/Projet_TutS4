package mygame.controls.screen;


import com.jme3.app.Application;
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
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.controls.Controller;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import de.lessvoid.xml.xpp3.Attributes;
import java.util.Properties;

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
    ViewPort guiViewPort;
    NiftyJmeDisplay niftyDisplay;
    
    public void initialize(AppStateManager stateManager, Application app){
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.stateManager = stateManager;
        initPointers();
        
        niftyDisplay = new NiftyJmeDisplay(assetManager, inputManager, audioRenderer, guiViewPort);
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/Nifty/startScreen.xml", "start", this);
        guiViewPort.addProcessor(niftyDisplay);
    }
    
    public void initPointers(){
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.cam = this.app.getCamera();

        this.guiNode = this.app.getGuiNode();
        this.rootNode = this.app.getRootNode();
        this.audioRenderer = this.app.getAudioRenderer();
        this.viewPort = this.app.getViewPort();
        this.guiViewPort = this.app.getGuiViewPort();
        rootNode.attachChild(localRootNode);

    }
    
    public void newGame(){
        
    }
    
    public void quit(){
        System.exit(1);
    }
    
    public void bind(Nifty nifty, Screen screen) {

    }

    public void onStartScreen() {

    }

    public void onEndScreen() {

    }

    public void bind(Nifty nifty, Screen screen, Element element, Properties parameter, Attributes controlDefinitionAttributes) {

    }

    public void init(Properties parameter, Attributes controlDefinitionAttributes) {

    }

    public void onFocus(boolean getFocus) {

    }

    public boolean inputEvent(NiftyInputEvent inputEvent) {
          boolean oblige = false;
          return oblige;
    }
    
}
    