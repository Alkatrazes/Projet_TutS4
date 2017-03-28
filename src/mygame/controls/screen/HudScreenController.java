/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame.controls.screen;

import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;

/**
 *
 * @author root
 */
public class HudScreenController implements ScreenController {
    
    private Nifty nifty;
    private Screen screenBis;

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
    
    public void bind(Nifty nifty, Screen screen) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onStartScreen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void onEndScreen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
