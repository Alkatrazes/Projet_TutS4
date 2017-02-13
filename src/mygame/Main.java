package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.renderer.RenderManager;
import mygame.controls.screen.StartScreenController;

public class Main extends SimpleApplication {

    StartScreenController startScreen;
    
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        
        startScreen = new StartScreenController();
        stateManager.attach(startScreen);
        flyCam.setEnabled(false);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
