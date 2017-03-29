package mygame;

import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import com.jme3.texture.Image;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import javax.swing.*;

public class Test7JME3 {
    
	
	// TODO : Add here a static variable which will make possible 
	// to store a link to your JMonkeyEngine application 
	private static MainTest canvasApplication;
	
	private static Canvas canvas; // JAVA Swing Canvas
	
	private static JFrame frame;
	private static JPanel panel;
	
	private static void createNewJFrame() throws IOException {

		frame = new JFrame("Java - Graphique - IHM");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO : Uncomment this in order to stop the application
				// when the windows will be closed.
				canvasApplication.stop();
			}
		});
		
		panel = new JPanel(new BorderLayout());

		// Create the menus
		final JMenuBar menubar = new JMenuBar();
		final JMenu objectsMenu = new JMenu("File");
		final JMenu helpMenu = new JMenu("Help");

		final JMenuItem createObjectItem = new JMenuItem("Create an object");
		final JMenuItem deleteObjectItem = new JMenuItem("Delete an object");
		final JMenuItem getControlsItem = new JMenuItem("Get controls");

		objectsMenu.add(createObjectItem);
		objectsMenu.add(deleteObjectItem);
		helpMenu.add(getControlsItem);
		menubar.add(objectsMenu);
		menubar.add(helpMenu);
		frame.setJMenuBar(menubar);

		getControlsItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				final JFrame dial = new JFrame("Controls");
				final JPanel pane = new JPanel();
				pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

				JTextArea cautionText = new JTextArea(
						"Add some text here to describe the controls \n" + '\n');
				cautionText.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
				cautionText.setEditable(false);
				pane.add(cautionText);

				JButton okButton = new JButton("Ok");
				okButton.setSize(50, okButton.getHeight());
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dial.dispose();
					}
				});

				JPanel buttonPane = new JPanel();
				buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
				buttonPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
				buttonPane.add(Box.createHorizontalGlue());
				buttonPane.add(okButton);

				pane.add(buttonPane);
				pane.add(Box.createRigidArea(new Dimension(0, 5)));
				dial.add(pane);
				dial.pack();
				dial.setLocationRelativeTo(frame);
				dial.setVisible(true);
			}
		});
                JButton feuBut = new JButton();
                feuBut.setContentAreaFilled(false);
                feuBut.setFocusPainted(false);
                feuBut.setPreferredSize(new Dimension(200,200));
                
		JButton eauBut = new JButton();
                eauBut.setContentAreaFilled(false);
                eauBut.setFocusPainted(false);
                eauBut.setPreferredSize(new Dimension(200,200));
                
                JButton airBut = new JButton();
                airBut.setContentAreaFilled(false);
                airBut.setFocusPainted(false);
                airBut.setPreferredSize(new Dimension(200,200));
                
                JButton terreBut = new JButton();
                terreBut.setContentAreaFilled(false);
                terreBut.setFocusPainted(false);
                terreBut.setPreferredSize(new Dimension(200,200));
                
                GridLayout gl = new GridLayout(1, 4);
                gl.setHgap(20);

                PanelSpe attaquePanel = new PanelSpe(gl);
                attaquePanel.setImage("eleBut.jpg");
                attaquePanel.add(feuBut, BorderLayout.SOUTH);
                attaquePanel.add(eauBut, BorderLayout.SOUTH);
                attaquePanel.add(airBut, BorderLayout.SOUTH);
                attaquePanel.add(terreBut, BorderLayout.SOUTH);
                attaquePanel.setPreferredSize(new Dimension(500,225));
                
                panel.add(attaquePanel, BorderLayout.SOUTH);

		
		// Add the canvas to the panel
		panel.add(canvas, BorderLayout.CENTER);
		
		frame.add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		// Fix an alignment bug on Mac OS X:
		// re-add the canvas and resize the JFrame
		/*try { Thread.sleep(1000);} catch (InterruptedException ex) {}
		frame.setSize(frame.getWidth()+1, frame.getHeight());
		frame.setSize(frame.getWidth()-1, frame.getHeight());*/
	}
	

	public static void main(String[] args) throws IOException{
                boolean resultat = false;
		// create new JME appsettings
		AppSettings settings = new AppSettings(true);
		settings.setResolution(1280, 800);
		settings.setSamples(8);

		// TODO : create here a new JMonkeyEngine application
		canvasApplication = new MainTest();
		
		// TODO : apply the settings and configure our application
		// in the same way than in the "public static void main()" method from SimpleApplication
		canvasApplication.setPauseOnLostFocus(false);
                
		// TODO : Uncomment this line to start the application
		// NB : this line is used instead of the app.start();
		canvasApplication.createCanvas(); // create canvas!
		
		// TODO : Uncomment the following lines to get the canvas from our application
		JmeCanvasContext ctx = (JmeCanvasContext) canvasApplication.getContext();
		canvas = ctx.getCanvas();
		Dimension dim = new Dimension(settings.getWidth(), settings.getHeight());
		canvas.setPreferredSize(dim);

		// Create the JFrame with the Canvas on the middle
		createNewJFrame();
	}
}