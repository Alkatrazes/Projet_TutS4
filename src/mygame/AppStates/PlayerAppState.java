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
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.collision.shapes.CompoundCollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.effect.ParticleEmitter;
import com.jme3.effect.ParticleMesh;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.font.BitmapText;
import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapFont;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.keyBiding;


/**
 *
 * @author Tanguy
 */
public class PlayerAppState extends AbstractAppState implements PhysicsCollisionListener, ScreenController{
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

    
    private int playerStatus;
    private boolean frontCollision, backCollision, rightCollision, leftCollision = false;

    
    
    // 
    private Vector3f walkDirection = new Vector3f(0,0,0);
    private Vector3f viewDirection = new Vector3f(0,1,0);
    private boolean rotateLeft = false, rotateRight = false, forward = false, backward = false;
    private float speed = 8;
    
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
    
    
    // Camera
    private CameraNode camNode;
    
    // Game settings
    private Node playerStart;
    private BitmapFont guiFont;
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
        
        
        //playerStart = gamePlayAppState.getPlayerStart();
        
        initHero();
        initKeys();
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
        rootNode.attachChild(playerAsset);

        
    }
    
    private void initKeys() {
        
        
        // You can map one or several inputs to one named action
        inputManager.addMapping("Left",   new KeyTrigger(keyBindings.HERO_LEFT));
        inputManager.addMapping("Right",  new KeyTrigger(keyBindings.HERO_RIGHT));
        inputManager.addMapping("Forward", new KeyTrigger(keyBindings.HERO_FORWARD));
        inputManager.addMapping("Backward", new KeyTrigger(keyBindings.HERO_BACKWARD));
        
        inputManager.addMapping("Caracteristique", new KeyTrigger(keyBindings.HERO_INVENTAIRE));
        

        //inputManager.addMapping("XRay", new KeyTrigger(KeyInput.KEY_X));

        // Add the names to the action listener.
        inputManager.addListener(actionListner, "Left", "Right", "Forward", "Backward", "Caracteristique");
    }
    
    private ActionListener actionListner = new ActionListener() {
        public void onAction(String name, boolean isPressed, float tpf) {
            if(name.equals("Left")) {
                rotateLeft = isPressed;
            } else if(name.equals("Right")) {
                rotateRight = isPressed;
            } else if(name.equals("Forward")) {
                forward = isPressed;
            } else if(name.equals("Backward")) {
                backward = isPressed;
            }
            
        }
    };

    public void collision(PhysicsCollisionEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(float tpf) {
            // Update recieving sound location
        listener.setLocation(cam.getLocation());
        listener.setRotation(cam.getRotation());
        
        // Move the excavator
        Vector3f modelForwardDir = player.getWorldRotation().mult(Vector3f.UNIT_Z);
        Vector3f modelLeftDir = player.getWorldRotation().mult(Vector3f.UNIT_X);
        
        // Check for change in direction
        walkDirection.set(0,0,0);
        if(!frontCollision && !backCollision) {
            if(forward && frontCollision == false) {
                walkDirection.addLocal(modelForwardDir.mult(speed));
            } else if(backward && backCollision == false) {
                walkDirection.addLocal(modelForwardDir.mult(speed).negate());
            }
        }
        
        if(frontCollision) {
            walkDirection.addLocal(modelForwardDir.mult(speed).negate());
            player.move(tpf*walkDirection.x, tpf*walkDirection.y, tpf*walkDirection.z);
            frontCollision = false;
        }
        
        if(backCollision) {
            walkDirection.addLocal(modelForwardDir.mult(speed));
            player.move(tpf*walkDirection.x, tpf*walkDirection.y, tpf*walkDirection.z);
            backCollision = false;
        }
        
        player.move(tpf*walkDirection.x, tpf*walkDirection.y, tpf*walkDirection.z);
        
        if(rotateLeft && leftCollision == false) {
            Quaternion rotateL = new Quaternion().fromAngleAxis(FastMath.PI * tpf, Vector3f.UNIT_Y);
            rotateL.multLocal(viewDirection);
            player.rotate(0, tpf*viewDirection.y, 0);
        } else if(rotateRight && rightCollision == false) {
            Quaternion rotateR = new Quaternion().fromAngleAxis(-FastMath.PI * tpf, Vector3f.UNIT_Y);
            rotateR.multLocal(viewDirection);
            player.rotate(0, -tpf*viewDirection.y, 0);
        }
        
        if(leftCollision) {
            Quaternion rotateL = new Quaternion().fromAngleAxis(FastMath.PI * tpf, Vector3f.UNIT_Y);
            rotateL.multLocal(viewDirection);
            player.rotate(0, -tpf*viewDirection.y, 0);
            leftCollision = false;
        }
        
        if(rightCollision) {
            Quaternion rotateL = new Quaternion().fromAngleAxis(FastMath.PI * tpf, Vector3f.UNIT_Y);
            rotateL.multLocal(viewDirection);
            player.rotate(0, tpf*viewDirection.y, 0);
            rightCollision = false;
        }
        
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
