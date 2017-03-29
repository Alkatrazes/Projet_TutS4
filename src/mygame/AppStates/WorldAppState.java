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
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.input.InputManager;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
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

/**
 *
 * @author Tanguy
 */
public class WorldAppState extends AbstractAppState{
    /* AppState specific */
    private SimpleApplication app;
    private Camera cam;
    private Node rootNode;
    private AssetManager assetManager;
    private InputManager inputManager;
    private BulletAppState bulletAppState;
    private ViewPort viewPort;
    private PlayerAppState playerAppState;
    private AppStateManager stateManager;
    //private StartScreenAppState startScreenAppState;
    private FilterPostProcessor filterPostProcessorLight;
    private FilterPostProcessor filterPostProcessorWater;
    private DirectionalLight sun;
    private Spatial sceneModel;
    private RigidBodyControl map; //rend la carte rigide

    

    
    private RigidBodyControl scenePhy;
    
    Material mat_terrain;
    private TerrainQuad terrain;

    
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
        //gamePlayAppState = stateManager.getState(GamePlayAppState.class);
        //+startScreenAppState = stateManager.getState(StartScreenAppState.class);
        //bulletAppState.getPhysicsSpace().enableD ebug(assetManager);

        filterPostProcessorLight = new FilterPostProcessor(assetManager);
        filterPostProcessorWater = new FilterPostProcessor(assetManager);
        
        initLight();
        initMap();
        
        playerAppState = new PlayerAppState();
        stateManager.attach(playerAppState);
    }
     
     public void initMap(){
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
        Texture heightMapImage = assetManager.loadTexture("Textures/HeightmapArene.png");
        heightmap = new ImageBasedHeightMap(heightMapImage.getImage());
        heightmap.load();

        int patchSize = 65;
        terrain = new TerrainQuad("my terrain", patchSize, 1025, heightmap.getHeightMap());

        terrain.setMaterial(mat_terrain);
        terrain.setLocalTranslation(476.5f, -7.65f, 0);
        terrain.setLocalScale(2f, 1f, 2f);
        rootNode.attachChild(terrain);

        TerrainLodControl control = new TerrainLodControl(terrain, app.getCamera());
        terrain.addControl(control);
        
        Spatial maison = makeMaison(0f, 180.5f, -498.8811f, "3d_objects/structures/models/cg_house_A/cg_house_A.mesh.xml", "3d_objects/structures/textures/foundation_brick/D.png", 0f, 3.2f, 0f);
        rootNode.attachChild(maison);
        Spatial maison2 = makeMaison(60f, 180.5f, -498.8811f, "3d_objects/structures/models/cg_house_B/cg_house_B.mesh.xml", "3d_objects/structures/textures/cobblestone_tile_A/D.png", 0f, 3.2f, 0f);
        rootNode.attachChild(maison2);
        Spatial maison3 = makeMaison(120f, 180.5f, -498.8811f, "3d_objects/structures/models/cg_house_A/cg_house_A.mesh.xml", "3d_objects/structures/textures/foundation_brick/D.png", 0f, 3.2f, 0f);
        rootNode.attachChild(maison3);

        Spatial maison4 = makeMaison(0f, 180.5f, -418.8811f, "3d_objects/structures/models/cg_house_B/cg_house_B.mesh.xml", "3d_objects/structures/textures/cobblestone_tile_A/D.png", 0f, 0f, 0f);
        rootNode.attachChild(maison4);
        Spatial maison5 = makeMaison(60f, 180.5f, -418.8811f, "3d_objects/structures/models/cg_house_C/cg_house_C.mesh.xml", "3d_objects/structures/textures/cobblestone_tile_A/D.png", 0f, 0f, 0f);
        rootNode.attachChild(maison5);
        Spatial maison6 = makeMaison(120f, 180.5f, -418.8811f, "3d_objects/structures/models/cg_house_A/cg_house_A.mesh.xml", "3d_objects/structures/textures/foundation_brick/D.png", 0f, 0f, 0f);
        rootNode.attachChild(maison6);

        Spatial arbre = makeTree(-40f, 180.5f, -418.8811f, "3d_objects/plants/trees/models/treeA/treeA.mesh.xml", "Textures/Terrain/splat/grass.jpg", 0f, 0f, 0f);
        rootNode.attachChild(arbre);
        Spatial arbre2 = makeTree(30f, 180.5f, -418.8811f, "3d_objects/plants/trees/models/treeA/treeA.mesh.xml", "Textures/Terrain/splat/grass.jpg", 0f, 0f, 0f);
        rootNode.attachChild(arbre2);
        Spatial arbre3 = makeTree(90f, 180.5f, -418.8811f, "3d_objects/plants/trees/models/treeA/treeA.mesh.xml", "Textures/Terrain/splat/grass.jpg", 0f, 0f, 0f);
        rootNode.attachChild(arbre3);

        Spatial arbre4 = makeTree(-40f, 180.5f, -498.8811f, "3d_objects/plants/trees/models/treeA/treeA.mesh.xml", "Textures/Terrain/splat/grass.jpg", 0f, 0f, 0f);
        rootNode.attachChild(arbre4);
        Spatial arbre5 = makeTree(30f, 180.5f, -498.8811f, "3d_objects/plants/trees/models/treeA/treeA.mesh.xml", "Textures/Terrain/splat/grass.jpg", 0f, 0f, 0f);
        rootNode.attachChild(arbre5);
        Spatial arbre6 = makeTree(90f, 180.5f, -498.8811f, "3d_objects/plants/trees/models/treeA/treeA.mesh.xml", "Textures/Terrain/splat/grass.jpg", 0f, 0f, 0f);
        rootNode.attachChild(arbre6);

        sceneModel = terrain;

        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
        map = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(map);
     }
     protected Spatial makeMaison(float a, float b, float c, String model, String texture, float ra, float rb, float rc) {
        Spatial home = assetManager.loadModel(model);
        home.setName("home");
        home.scale(2.5f);
        home.setLocalTranslation(a, b, c);
        home.rotate(ra, rb, rc);
        Material mat_brick = new Material(
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_brick.setTexture("ColorMap",
                assetManager.loadTexture(texture));
        home.setMaterial(mat_brick);
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        home.addLight(sun);
        return home;
    }
     
     protected Spatial makeTree(float a, float b, float c, String model, String texture, float ra, float rb, float rc) {
        Spatial home = assetManager.loadModel(model);
        home.setName("home");
        home.scale(2.5f);
        home.setLocalTranslation(a, b, c);
        home.rotate(ra, rb, rc);
        Material mat_brick = new Material(
                assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat_brick.setTexture("ColorMap",
                assetManager.loadTexture(texture));
        home.setMaterial(mat_brick);
        DirectionalLight sun = new DirectionalLight();
        sun.setDirection(new Vector3f(-0.1f, -0.7f, -1.0f));
        home.addLight(sun);
        return home;
    }
     
     private void initLight() {
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);

        DirectionalLight dl = new DirectionalLight();
        dl.setColor(ColorRGBA.White);
        dl.setDirection(new Vector3f(2.8f, -2.8f, -2.8f).normalizeLocal());
        rootNode.addLight(dl);
    }

}
