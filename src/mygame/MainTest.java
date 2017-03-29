/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.font.BitmapText;
import mygame.AppStates.WorldAppState;

    
/**
 *
 * @author Tanguy
 */
public class MainTest extends SimpleApplication{
    private static MainTest app;
    public static void main(String[] args) {
        app = new MainTest();
        app.start(); 
    }
    
    @Override
    public void simpleInitApp() {
        setDisplayFps(false);
        setDisplayStatView(false);
        initCrossHairs();
        
        WorldAppState gameplayAppState = new WorldAppState();
        stateManager.attach(gameplayAppState);
    }
      /** A centred plus sign to help the player aim. */
  protected void initCrossHairs() {
    setDisplayStatView(false);
    guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
    BitmapText ch = new BitmapText(guiFont, false);
    ch.setSize(guiFont.getCharSet().getRenderedSize() * 2);
    ch.setText("+"); // crosshairs
    ch.setLocalTranslation( // center
    settings.getWidth() / 2 - ch.getLineWidth()/2, settings.getHeight() / 2 + ch.getLineHeight()/2, 0);
    guiNode.attachChild(ch);
  }
}
