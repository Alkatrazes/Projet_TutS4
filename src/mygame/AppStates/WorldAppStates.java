/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.AppStates;

import com.jme3.app.state.AbstractAppState;
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.AppStateManager;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.post.FilterPostProcessor;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/**
 *
 * @author root
 */



public class WorldAppStates extends AbstractAppState {
    private SimpleApplication app;
    private Camera cam;
    private Node rootNode;
    private AssetManager assetManager;
    private InputManager inputManager;
    private BulletAppState bulletAppState;
    private ViewPort viewPort;
    private GamePlayAppStates gamePlayAppStates;
    private AppStateManager stateManager;
    private FilterPostProcessor filterPostProcessorLight;
    private FilterPostProcessor filterPostProcessorWater;
    private DirectionalLight sun;
    private TerrainQuad terrain;
    Material mat_terrain;
    private Spatial sceneModel;
    private RigidBodyControl map; //rend la carte rigide
    
    private RigidBodyControl scenePhy;
    private Node sceneNode;
    
    @Override
    public void initialize(AppStateManager stateManager, Application app) {
        super.initialize(stateManager, app);
        this.stateManager = stateManager;
        this.app = (SimpleApplication) app;
        this.cam = this.app.getCamera();
        this.rootNode = this.app.getRootNode();
        this.assetManager = this.app.getAssetManager();
        this.inputManager = this.app.getInputManager();
        this.viewPort = this.app.getViewPort();
        
        bulletAppState = stateManager.getState(BulletAppState.class);
        gamePlayAppStates = stateManager.getState(GamePlayAppStates.class);
        //bulletAppState.getPhysicsSpace().enableDebug(assetManager);

        filterPostProcessorLight = new FilterPostProcessor(assetManager);
        filterPostProcessorWater = new FilterPostProcessor(assetManager);
        
        setUpLight();
    }
    
    private void setUpLight() {
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);
    }
    
    private void createTerrain(){
        mat_terrain = new Material(assetManager,
                "Common/MatDefs/Terrain/Terrain.j3md");

        mat_terrain.setTexture("Alpha", assetManager.loadTexture(
                "Textures/Alphamap.png"));

        Texture grass = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
        grass.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex1", grass); 
        mat_terrain.setFloat("Tex1Scale", 128f);

        Texture dirt = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
        dirt.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex2", dirt);
        mat_terrain.setFloat("Tex2Scale", 128f);

        Texture rock = assetManager.loadTexture("Textures/Terrain/splat/grass.jpg");
        rock.setWrap(Texture.WrapMode.Repeat);
        mat_terrain.setTexture("Tex3", rock);
        mat_terrain.setFloat("Tex3Scale", 128f);

        AbstractHeightMap heightmap = null;
        Texture heightMapImage = assetManager.loadTexture("Textures/Heightmap.png");
        heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();

        int patchSize = 65;
        terrain = new TerrainQuad("my terrain", patchSize, 1025, heightmap.getHeightMap());

        terrain.setMaterial(mat_terrain);
        terrain.setLocalTranslation(476.5f, -7.65f, 0);
        terrain.setLocalScale(2f, 1f, 2f);
        rootNode.attachChild(terrain);

        //TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
        //terrain.addControl(control);

        sceneModel = terrain;

        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
        map = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(map);
    }

}
