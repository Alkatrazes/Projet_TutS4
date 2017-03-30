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
import com.jme3.font.BitmapText;
import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.awt.Font;
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
    
    private TerrainQuad terrain;
    private Material mat_terrain;

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
   
    
    //Monstre informations
    protected String nom;
    protected String classe;
    protected double vie;
    protected double vieMax;
    protected int niveau;
    protected int attaque;
    protected String elementType;
    
    //Joueur informations
    protected String nomJ;
    protected String classeJ;
    protected double vieJ;
    protected double vieMaxJ;
    protected int niveauJ;
    protected int attaqueEauJ;
    protected int attaqueAirJ;
    protected int attaqueFeuJ;
    protected int attaqueTerreJ;
    
    
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
        this.app.setDisplayFps(false);
        this.app.setDisplayStatView(false);
        flyCam.setMoveSpeed(100);
        
        nomJ="Wolf";
        classeJ="Wolf";
        vieJ=100;
        vieMaxJ=100;
        niveauJ=1;
        attaqueFeuJ=8;
        attaqueEauJ=4;
        attaqueTerreJ=20;
        attaqueAirJ=15;
        
        Box monstre = new Box(10,10,10); // a cuboid default mesh
        Geometry thing2 = new Geometry("thing2", monstre); 
        Material mat = new Material(assetManager,"Common/MatDefs/Misc/ShowNormals.j3md");
        thing2.setMaterial(mat);
        thing2.setLocalTranslation(-100, -90, 0);
        rootNode.attachChild(thing2);

        /** 1. Create terrain material and load four textures into it. */
        mat_terrain = new Material(assetManager,"Common/MatDefs/Terrain/Terrain.j3md");

        /** 1.1) Add ALPHA map (for red-blue-green coded splat textures) */
        mat_terrain.setTexture("Alpha", assetManager.loadTexture("Textures/Terrain/splat/alphamap.png"));

        /** 1.2) Add GRASS texture into the red layer (Tex1). */
        Texture grass = assetManager.loadTexture("Textures/Terrain/Rocky/RockyTexture.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex1", grass);
        mat_terrain.setFloat("Tex1Scale", 64f);


        /** 1.4) Add ROAD texture into the blue layer (Tex3) */
        Texture rock = assetManager.loadTexture(
                "Textures/Terrain/splat/road.jpg");
        rock.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex3", rock);
        mat_terrain.setFloat("Tex3Scale", 128f);

        /** 2. Create the height map */
        AbstractHeightMap heightmap = null;
        Texture heightMapImage = assetManager.loadTexture("Textures/Terrain/splat/alpha1.png");
        heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();

        this.app.getGuiNode().detachAllChildren();
        BitmapFont guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText helloText = new BitmapText(guiFont, false);
        helloText.setSize(guiFont.getCharSet().getRenderedSize());
        helloText.setText("100/100");
        helloText.setLocalTranslation(0, 1000, 0);
        this.app.getGuiNode().attachChild(helloText);

        /** 3. We have prepared material and heightmap. 
         * Now we create the actual terrain:
         * 3.1) Create a TerrainQuad and name it "my terrain".
         * 3.2) A good value for terrain tiles is 64x64 -- so we supply 64+1=65.
         * 3.3) We prepared a heightmap of size 512x512 -- so we supply 512+1=513.
         * 3.4) As LOD step scale we supply Vector3f(1,1,1).
         * 3.5) We supply the prepared heightmap itself.
         */
        int patchSize = 65;
        terrain = new TerrainQuad("my terrain", patchSize, 513, heightmap.getHeightMap());

        /** 4. We give the terrain its material, position & scale it, and attach it. */
        terrain.setMaterial(mat_terrain);
        terrain.setLocalTranslation(0, -100, 0);
        terrain.setLocalScale(2f, 1f, 2f);
        // Charge un modèle de test_data (ogrexml + matériel + texture)

        rootNode.attachChild(terrain);


        /** 5. The LOD (level of detail) depends on were the camera is: */
        TerrainLodControl control = new TerrainLodControl(terrain, this.app.getCamera());
        terrain.addControl(control);
        cam.setLocation(new Vector3f(1,0,500));
        flyCam.setEnabled(false);
        bulletAppState = stateManager.getState(BulletAppState.class);
        worldAppState = stateManager.getState(WorldAppState.class);                
        initMonster();       
    }
    
    public void initMonster(){
        nom="Wolf";
        classe="Wolf";
        vie=100;
        vieMax=100;
        niveau=1;
        attaque=8;
        elementType = "Feu";
        
        Box mesh = new Box(10,10,10); // a cuboid default mesh
        Geometry thing = new Geometry("thing", mesh); 
        Material mat = new Material(assetManager,"Common/MatDefs/Misc/ShowNormals.j3md");
        thing.setMaterial(mat);
        thing.setLocalTranslation(100, -90, 0);
        rootNode.attachChild(thing);
        

        
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
    
    public int getAttqueFeuJoueur(){
        return attaqueFeuJ;
    }
    
    public double getVieMob(){
        return vie;
    }
    
    public String getEleMob(){
        return elementType;
    }

    public void setVie(double vie, int i){
        if (i == 0){
            this.vie = vie;
        }
        else {
            this.vieJ = vie;
        }
    }
    
}
