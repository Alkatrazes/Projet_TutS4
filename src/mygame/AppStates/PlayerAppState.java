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
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.font.BitmapFont;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import mygame.keyBiding;
import mygame.controls.screen.HudScreenController;


/**
 *
 * @author Tanguy
 */
public class PlayerAppState extends AbstractAppState implements PhysicsCollisionListener{
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
    
    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();
    
    private boolean rotateLeft = false, rotateRight = false, forward = false, back = false, left = false, right = false;
    private float speed = 1f;
    
    //Player informations
    protected String nom;
    protected String classe;
    protected int vie;
    protected int vieMax;
    protected int niveau;
    protected int force;
    protected int magie;
    protected int attaquePhy;
    protected int attaqueMag;
    protected int defencePhy;
    protected int defenceMag;
    
    
    // Camera
    private CameraNode camNode;
    private CharacterControl joueur;

    // Game settings
    private Node playerStart;
    private BitmapFont guiFont;
    private WorldAppState worldAppState;
    private HudScreenController hudScreenController;
    private CombatAppState combatAppState;
    
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
        hudScreenController = new HudScreenController();
        //startScreenAppState = stateManager.getState(StartScreenAppState.class);
        
        
        //playerStart = gamePlayAppState.getPlayerStart();
        
        initHero();
        initKeys();
        // initFollowCam();
        
        
        
        //bulletAppState.getPhysicsSpace().addCollisionListener(this);
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
        //Spatial playerAsset = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");       
        //playerAsset.setName("player");
        //playerAsset.scale(0.05f, 0.05f, 0.05f);
        //playerAsset.rotate(0.0f, -3.0f, 0.0f);
        //playerAsset.setLocalTranslation(0.0f, -5.0f, -2.0f);
        //rootNode.attachChild(playerAsset);
        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);
        joueur = new CharacterControl(capsuleShape, 0.05f);
        joueur.setJumpSpeed(20);
        joueur.setFallSpeed(speed * 30);
        joueur.setGravity(30);
        joueur.setPhysicsLocation(new Vector3f(25f, 185, -450f));
       // bulletAppState.getPhysicsSpace().add(player);
        
    }
    
    private void initKeys() {
        
        
        // You can map one or several inputs to one named action
        inputManager.addMapping("Left",   new KeyTrigger(keyBindings.HERO_LEFT));
        inputManager.addMapping("Right",  new KeyTrigger(keyBindings.HERO_RIGHT));
        inputManager.addMapping("Forward", new KeyTrigger(keyBindings.HERO_FORWARD));
        inputManager.addMapping("Backward", new KeyTrigger(keyBindings.HERO_BACKWARD));
        
        //inputManager.addMapping("Caracteristique", new KeyTrigger(keyBindings.HERO_INVENTAIRE));
        

        //inputManager.addMapping("XRay", new KeyTrigger(KeyInput.KEY_X));

        // Add the names to the action listener.
        //inputManager.addListener(actionListner, "Left", "Right", "Forward", "Backward", "Caracteristique");
        inputManager.addListener(analogListener, "Forward", "Back", "Left", "Right");

    }
    /*
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
    };*/
    
        private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            value = 0.4f;
                if (name.equals("Forward")) {
                    Vector3f v = joueur.getPhysicsLocation();
                    joueur.setPhysicsLocation(new Vector3f(v.x, v.y, v.z - value * speed));
                }
                if (name.equals("Back")) {
                    Vector3f v = joueur.getPhysicsLocation();
                    joueur.setPhysicsLocation(new Vector3f(v.x, v.y, v.z + value * speed));
                }
                if (name.equals("Right")) {
                    Vector3f v = joueur.getPhysicsLocation();
                    joueur.setPhysicsLocation(new Vector3f(v.x + value * speed, v.y, v.z));
                }
                if (name.equals("Left")) {
                    Vector3f v = joueur.getPhysicsLocation();
                    joueur.setPhysicsLocation(new Vector3f(v.x - value * speed, v.y, v.z));
                }
        }
    };

    public void collision(PhysicsCollisionEvent event) {
        String nameA = event.getNodeA().getName();
        String nameB = event.getNodeB().getName();
        if(nameA.equals("player") && nameB.equals("ninjaNode")) {
            // Belt hits World
            System.out.println("Veuillez tuer ce monstre");
        }
        
        if(nameA.equals("player") && nameB.equals("orcNode")) {
            // Belt hits World
            combatAppState = new CombatAppState();
            stateManager.attach(combatAppState);
            
        }
    }

    @Override
    public void update(float tpf) {
        /*
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
        }*/
       camDir.set(cam.getDirection()).multLocal(0.6f);
        camLeft.set(cam.getLeft()).multLocal(0.4f);
        viewDirection.set(camDir);
        walkDirection.set(0, 0, 0);

        if (left) {
            walkDirection.addLocal(camLeft);
        }
        if (right) {
            walkDirection.addLocal(camLeft.negate());
        }
        if (forward) {
            walkDirection.addLocal(camDir);
        }
        if (back) {
            walkDirection.addLocal(camDir.negate());
        }
        joueur.setWalkDirection(walkDirection);
        joueur.setViewDirection(viewDirection);
        cam.setLocation(joueur.getPhysicsLocation());
        //inputManager.setCursorVisible(true);
        hudScreenController.updateLvl(niveau);
        hudScreenController.printName(nom);
        hudScreenController.updateLife(vie,vieMax);
        
        
        
        
    } //To change body of generated methods, choose Tools | Templates.

    
    
   
    
}
