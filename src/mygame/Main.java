package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.cursors.plugins.JmeCursor;
import com.jme3.renderer.RenderManager;
import com.jme3.system.AppSettings;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.util.BufferUtils;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

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
        
        UpdateCursor();
        
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
    
    private void UpdateCursor() {
        Texture tex = assetManager.loadTexture("Interface/Nifty/images/curseur.png");
        inputManager.setMouseCursor(textureToCursor(tex));
    }

    private JmeCursor textureToCursor(Texture tex) {
        Image image = tex.getImage();
        ByteBuffer imgByteBuff = (ByteBuffer) image.getData(0).rewind();
        IntBuffer curIntBuff = BufferUtils.createIntBuffer(40 * 40);

        for (int y = 0; y < 32; y++) //image.getHeight()
        {
            if (y >= image.getHeight())
            {
                for (int i = 0; i < image.getWidth(); i++)
                {
                    curIntBuff.put(0xff000000);
                }
                continue;
            }

            for (int x = 0; x < image.getWidth(); x++)
            {
                int rgba = imgByteBuff.getInt();
                if (x >= 48) continue;

                 int argb = ((rgba & 255) << 24) | (rgba >> 8);
                 curIntBuff.put(argb);
            }
        }

        JmeCursor c = new JmeCursor();
        c.setHeight(image.getHeight()); //image.getHeight()-2);
        c.setWidth(32); //image.getWidth());
        c.setNumImages(1);
        c.setyHotSpot(27 - 3); //image.getHeight()-3);
        c.setxHotSpot(3);
        c.setImagesData((IntBuffer) curIntBuff.rewind());

        return c;
    }
}
