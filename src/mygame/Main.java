package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;

import mygame.controls.screen.*;

public class Main extends SimpleApplication {
    
    public static Main app;
    
    public static void main(String[] args) {
        app = new Main(); 
 
        AppSettings setting = new AppSettings(true); 
        setting.setTitle("ProjectS4"); 
        setting.setHeight(600); 
        setting.setWidth(800); 

        app.start(); 
    }
    
    public Main() {
        super(new StatsAppState());
    }
    
    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        
        this.stateManager.attach(new GUI(this));
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
