/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.AppStates;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioNode;
import com.jme3.audio.Listener;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.font.BitmapFont;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.keyBiding;

/**
 *
 * @author valentin
 */
public class CombatAppState extends AbstractAppState implements PhysicsCollisionListener, ScreenController{
    /* AppState specific */
    private SimpleApplication app;
    private Camera cam;
    private Node rootNode;
    private AssetManager assetManager;
    private InputManager inputManager;
    private BulletAppState bulletAppState;
    // private GamePlayAppState gamePlayAppState;
    // private StartScreenAppState startScreenAppState;
    private AppStateManager stateManager;
    private FlyByCamera flyCam;
    private Node player;
    // private CustomChaseCamera chaseCam;
    private AudioNode excavatorEngineSoundPassive;
    private AudioNode excavatorEngineSoundRunning;
    private Listener listener;
    private keyBiding keyBindings;
    
    private Nifty nifty;
   
    
    //Player informations
    protected String nom;
    protected String classe;
    protected double vie;
    protected double vieMax;
    protected int niveau;
    protected int force;
    protected int magie;
    protected int attaquePhy;
    protected int attaqueMag;
    protected int defencePhy;
    protected int defenceMag;
    
    
    // Game settings
    private WorldAppState worldAppState;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.app = (SimpleApplication) app;
        this.cam = this.app.getCamera();
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.flyCam = this.app.getFlyByCamera();
        this.stateManager = stateManager;
        this.listener = this.app.getListener();
        keyBindings = new keyBiding();
        
        bulletAppState = stateManager.getState(BulletAppState.class);
        worldAppState = stateManager.getState(WorldAppState.class);
        //startScreenAppState = stateManager.getState(StartScreenAppState.class);
                
        initHero();
        // initFollowCam();
        
        
        
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }
    
    public void initHero(){
        nom="Jean";
        classe="Guerrier";
        vie=500;
        vieMax=500;
        niveau=1;
        force=8;
        magie=4;
        defencePhy=20;
        defenceMag=15;
        Spatial playerAsset = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");       
        playerAsset.setName("player");
        playerAsset.scale(0.05f, 0.05f, 0.05f);
        playerAsset.rotate(0.0f, -3.0f, 0.0f);
        playerAsset.setLocalTranslation(0.0f, -5.0f, -2.0f);
        player.attachChild(playerAsset);
        rootNode.attachChild(player);

        
    }
    
    public void collision(PhysicsCollisionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(float tpf) {
        rootNode.rotate(0, tpf/2, 0); 
        
    } //To change body of generated methods, choose Tools | Templates.

    public void bind(Nifty nifty, Screen screen) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onStartScreen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onEndScreen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void updateLvl(int niveau){
        Screen screen = nifty.getScreen("hud");
        Element txtLvl = screen.findElementByName("txtLvl");
        TextRenderer textRendererLvl = txtLvl.getRenderer(TextRenderer.class);
        textRendererLvl.setText("Niveau : "+ niveau);
    }
    
    public void printName(String nom){
        Screen screen = nifty.getScreen("hud");
        Element txtName = screen.findElementByName("txtName");
        TextRenderer textRendererName = txtName.getRenderer(TextRenderer.class);
        textRendererName.setText("Nom : "+ nom);
    }
    
    public void updateLife(int vie,int vieMax){
        Screen screen = nifty.getScreen("hud");
        Element txtLife = screen.findElementByName("txtLife");
        TextRenderer textRendererLife = txtLife.getRenderer(TextRenderer.class);
        textRendererLife.setText("Vie : "+ vie +"/"+ vieMax);
    }
    
}
