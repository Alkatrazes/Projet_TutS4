/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author charly
 */
import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.AnimEventListener;
import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.collision.shapes.CollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.font.BitmapText;
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
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.terrain.geomipmap.TerrainLodControl;
import com.jme3.terrain.geomipmap.TerrainQuad;
import com.jme3.terrain.heightmap.AbstractHeightMap;
import com.jme3.terrain.heightmap.ImageBasedHeightMap;
import com.jme3.texture.Texture;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JME extends SimpleApplication implements ActionListener, InputListener, AnimEventListener {

    private Spatial sceneModel;
    private BulletAppState bulletAppState; //accés à la physique
    private RigidBodyControl map; //rend la carte rigide
    private CharacterControl player;
    private Vector3f walkDirection = new Vector3f();
    private Vector3f viewDirection = new Vector3f();
    private boolean isRunning = true;
    private boolean left = false, right = false, forward = false, back = false, leftCam = false, rightCam = false, up = false, down = false;
    private TerrainQuad terrain;
    private AnimChannel channel;
    private AnimControl control;

    Material mat_terrain;

    private Vector3f camDir = new Vector3f();
    private Vector3f camLeft = new Vector3f();

    public static void main(String[] args) {
        JME app = new JME();
        app.start();
    }

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

        viewPort.setBackgroundColor(new ColorRGBA(0.7f, 0.8f, 1f, 1f));
        setUpKeys();
        initKeys();
        setUpLight();
        initCrossHairs();
        createTerrain();
        Spatial maison = makeMaison(0f, 120f, -498.8811f, "3d_objects/structures/models/cg_house_A/cg_house_A.mesh.xml", "3d_objects/structures/textures/foundation_brick/D.png", 0f, 3.2f, 0f);
        rootNode.attachChild(maison);
        Spatial maison2 = makeMaison(60f, 120f, -498.8811f, "3d_objects/structures/models/cg_house_B/cg_house_B.mesh.xml", "3d_objects/structures/textures/cobblestone_tile_A/D.png", 0f, 3.2f, 0f);
        rootNode.attachChild(maison2);
        Spatial maison3 = makeMaison(120f, 120f, -498.8811f, "3d_objects/structures/models/cg_house_A/cg_house_A.mesh.xml", "3d_objects/structures/textures/foundation_brick/D.png", 0f, 3.2f, 0f);
        rootNode.attachChild(maison3);

        Spatial maison4 = makeMaison(0f, 120f, -418.8811f, "3d_objects/structures/models/cg_house_B/cg_house_B.mesh.xml", "3d_objects/structures/textures/cobblestone_tile_A/D.png", 0f, 0f, 0f);
        rootNode.attachChild(maison4);
        Spatial maison5 = makeMaison(60f, 120f, -418.8811f, "3d_objects/structures/models/cg_house_C/cg_house_C.mesh.xml", "3d_objects/structures/textures/cobblestone_tile_A/D.png", 0f, 0f, 0f);
        rootNode.attachChild(maison5);
        Spatial maison6 = makeMaison(120f, 120f, -418.8811f, "3d_objects/structures/models/cg_house_A/cg_house_A.mesh.xml", "3d_objects/structures/textures/foundation_brick/D.png", 0f, 0f, 0f);
        rootNode.attachChild(maison6);

        Spatial arbre = makeMaison(-40f, 120f, -418.8811f, "3d_objects/plants/trees/models/treeA/treeA.mesh.xml", "Textures/Terrain/splat/grass.jpg", 0f, 0f, 0f);
        rootNode.attachChild(arbre);
        Spatial arbre2 = makeMaison(30f, 120f, -418.8811f, "3d_objects/plants/trees/models/treeA/treeA.mesh.xml", "Textures/Terrain/splat/grass.jpg", 0f, 0f, 0f);
        rootNode.attachChild(arbre2);
        Spatial arbre3 = makeMaison(90f, 120f, -418.8811f, "3d_objects/plants/trees/models/treeA/treeA.mesh.xml", "Textures/Terrain/splat/grass.jpg", 0f, 0f, 0f);
        rootNode.attachChild(arbre3);

        Spatial arbre4 = makeMaison(-40f, 120f, -498.8811f, "3d_objects/plants/trees/models/treeA/treeA.mesh.xml", "Textures/Terrain/splat/grass.jpg", 0f, 0f, 0f);
        rootNode.attachChild(arbre4);
        Spatial arbre5 = makeMaison(30f, 120f, -498.8811f, "3d_objects/plants/trees/models/treeA/treeA.mesh.xml", "Textures/Terrain/splat/grass.jpg", 0f, 0f, 0f);
        rootNode.attachChild(arbre5);
        Spatial arbre6 = makeMaison(90f, 120f, -498.8811f, "3d_objects/plants/trees/models/treeA/treeA.mesh.xml", "Textures/Terrain/splat/grass.jpg", 0f, 0f, 0f);
        rootNode.attachChild(arbre6);
        
        Spatial ninja = assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
        ninja.scale(0.05f, 0.05f, 0.05f);
        ninja.rotate(0.0f, -40.0f, 0.0f);
        ninja.setLocalTranslation(10f, 120f, -485.8811f);
        rootNode.attachChild(ninja);
        
        Spatial orc = assetManager.loadModel("Models/Sinbad/Sinbad.mesh.xml");
        orc.scale(0.8f, 0.8f, 0.8f);
        orc.rotate(0.0f, 30.0f, 0.0f);
        orc.setLocalTranslation(250f, 123.8f, -600.8811f);
        rootNode.attachChild(orc);

        CapsuleCollisionShape capsuleShape = new CapsuleCollisionShape(1.5f, 6f, 1);

        player = new CharacterControl(capsuleShape, 0.05f);
        player.setJumpSpeed(20);
        player.setFallSpeed(speed * 30);
        player.setGravity(30);
        player.setPhysicsLocation(new Vector3f(0f, 185, -498.8811f));
        rootNode.attachChild(sceneModel);
        bulletAppState.getPhysicsSpace().add(map);
        bulletAppState.getPhysicsSpace().add(player);

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

    private void setUpKeys() {
        inputManager.addMapping("leftCam", new KeyTrigger(KeyInput.KEY_LEFT));
        inputManager.addMapping("rightCam", new KeyTrigger(KeyInput.KEY_RIGHT));
        inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_UP));
        inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_DOWN));
        inputManager.addListener(this, "leftCam");
        inputManager.addListener(this, "rightCam");
        inputManager.addListener(this, "Up");
        inputManager.addListener(this, "Down");
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

    private void initKeys() {
        inputManager.addMapping("Pause", new KeyTrigger(KeyInput.KEY_P));
        inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("Forward", new KeyTrigger(KeyInput.KEY_Z));
        inputManager.addMapping("Back", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Jump", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("Click", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(actionListener, "Pause", "Click");
        inputManager.addListener(analogListener, "Forward", "Back", "Left", "Right", "Jump");
    }

    public void onAction(String binding, boolean isPressed, float tpf) {
        if (binding.equals("leftCam")) {
            leftCam = isPressed;
        } else if (binding.equals("rightCam")) {
            rightCam = isPressed;
        } else if (binding.equals("Forward")) {
            up = isPressed;
        } else if (binding.equals("Back")) {
            down = isPressed;
        }
    }

    @Override
    public void onAnimCycleDone(AnimControl control, AnimChannel channel, String animName) {
        if (animName.equals("Walk")) {
            channel.setAnim("stand", 0.50f);
            channel.setLoopMode(LoopMode.DontLoop);
            channel.setSpeed(1f);
        }
    }

    @Override
    public void simpleUpdate(float tpf) {
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
        player.setWalkDirection(walkDirection);
        player.setViewDirection(viewDirection);
        cam.setLocation(player.getPhysicsLocation());
        //inputManager.setCursorVisible(true);
    }

    private com.jme3.input.controls.ActionListener actionListener = new com.jme3.input.controls.ActionListener() {

        public void onAction(String name, boolean keyPressed, float tpf) {
            if (name.equals("Pause") && !keyPressed) {
                isRunning = !isRunning;
            }
        }
    };

    private AnalogListener analogListener = new AnalogListener() {
        public void onAnalog(String name, float value, float tpf) {
            value = 0.4f;
            if (isRunning) {
                if (name.equals("Forward")) {
                    Vector3f v = player.getPhysicsLocation();
                    player.setPhysicsLocation(new Vector3f(v.x, v.y, v.z - value * speed));
                }
                if (name.equals("Back")) {
                    Vector3f v = player.getPhysicsLocation();
                    player.setPhysicsLocation(new Vector3f(v.x, v.y, v.z + value * speed));
                }
                if (name.equals("Right")) {
                    Vector3f v = player.getPhysicsLocation();
                    player.setPhysicsLocation(new Vector3f(v.x + value * speed, v.y, v.z));
                }
                if (name.equals("Left")) {
                    Vector3f v = player.getPhysicsLocation();
                    player.setPhysicsLocation(new Vector3f(v.x - value * speed, v.y, v.z));
                }
                if (name.equals("Up")) {
                    Vector3f v = player.getPhysicsLocation();
                    player.setPhysicsLocation(new Vector3f(v.x, v.y + value * speed, v.z));
                }
                if (name.equals("Down")) {
                    Vector3f v = player.getPhysicsLocation();
                    player.setPhysicsLocation(new Vector3f(v.x, v.y - value * speed, v.z));
                }
                if (name.equals("Jump")) {
                    player.jump();
                }
            } else {
                System.out.println("Press P to unpause.");
            }
        }
    };

    private void createTerrain() {
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

        TerrainLodControl control = new TerrainLodControl(terrain, getCamera());
        terrain.addControl(control);

        sceneModel = terrain;

        CollisionShape sceneShape = CollisionShapeFactory.createMeshShape((Node) sceneModel);
        map = new RigidBodyControl(sceneShape, 0);
        sceneModel.addControl(map);
    }

    protected void initCrossHairs() {
        setDisplayStatView(false);
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
        BitmapText ch = new BitmapText(guiFont, false);
        ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
        ch.setText("+"); // crosshairs
        ch.setLocalTranslation( // center
                settings.getWidth() / 2 - ch.getLineWidth() / 2, settings.getHeight() / 2 + ch.getLineHeight() / 2, 0);
        guiNode.attachChild(ch);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }

    @Override
    public void onAnimChange(AnimControl control, AnimChannel channel, String animName) {

    }

}
