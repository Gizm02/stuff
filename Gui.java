import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.Desktop;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JTabbedPane;
import java.io.*;
import java.net.URI; //Use Uniform Resource Identifier-class for browsing-option in context "help"
import javax.media.jai.*;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 * 
 * 
 * @author Ben
 * @version 18.09.2012
 */
public class Gui extends Frame
{
	private JFileChooser jfc;
    JPanel j;
    ImgPanel p;
    public Gui() {
        super("ImageViewer V. 1.0");
        setBackground(Color.BLUE); /* TODO Affects everything and the image is not loaded correctly. */
        this.create();
        addWindowListener( new WindowAdapter() {
        	@Override
        	public void windowClosing(WindowEvent e) {
        		System.exit(0);
        	}
        });
        pack();
        setSize(300,300);
        setVisible(true);
    }
    
    public static void main(String[] args) {
    	Gui g = new Gui();
    }
    
    public void create() {
        // Create general content, the border
        MenuBar m = new MenuBar();
        Menu sub = new Menu("Program");
        MenuItem quit = new MenuItem("Quit");
        // Klasse System, void exit(int status), Wert groesser 0 bedeutet Abbruch durch Fehler,
        // = 0 sauberes Beenden
        quit.addActionListener(new ActionListener() {
                                                    public void actionPerformed(ActionEvent e) {
                                                        System.exit(0);
                                                    }});
        // To open several images simultaneously tabs are used
        final JTabbedPane tp = new JTabbedPane();
        // Add a possibility to open files via the Gui, this is realized through
        // the class java.awt.FileDialog: 
        // public FileDialog(Component comp, String dialogtitle)
        // void setFile(String s)     sets default text in the file name box
        // String getDirectory()      returns the name of the directory the chosen file is in
        // String getFile()           returns the name of the chosen file, not an absolute path
        // void show()                starts the dialog
        MenuItem open = new MenuItem("Open");
        open.addActionListener(new ActionListener() {
                                                    @SuppressWarnings("deprecation")
													public void actionPerformed(ActionEvent e) {
                                                    	File f = new File("");
                                                    	 // Open a dialog which provides the files
                                                        jfc = new JFileChooser();
                                                        FileNameExtensionFilter filter = new FileNameExtensionFilter("Choose a *.jpg file", "JPG");
                                                        jfc.setFileFilter(filter);
                                                        jfc.setVisible(true);
                                                        // If a file is chosen:
                                                        if(jfc.showOpenDialog(null) == jfc.APPROVE_OPTION)
                                                            f = jfc.getSelectedFile();
                                                        p = new ImgPanel(f); /* Initialisiere ImgPanel p */
                                                        tp.addTab(f.toString(), p);
                                                        resize(tp.getWidth(),tp.getHeight());
                                                    }});
        MenuItem store = new MenuItem("Store");
        store.addActionListener(new ActionListener() {
        											public void actionPerformed(ActionEvent e) {
        												File f = new File("");
        												 // Open a dialog which provides the files
        										        jfc = new JFileChooser();
        										        FileNameExtensionFilter filter = new FileNameExtensionFilter("Choose a *.txt file", "jpg");
        										        jfc.setFileFilter(filter);
        										        jfc.setVisible(true);
        										        // If a file is chosen:
        										        if(jfc.showOpenDialog(null) == jfc.APPROVE_OPTION)
        										            f = jfc.getSelectedFile();
        												p.store(f.toString());
        											} } );
        add(tp);
        // Add the menu items to the menu bar
        sub.add(store);
        sub.add(open);
        sub.add(quit);
        m.add(sub);
        Menu edit = new Menu("Edit");
        MenuItem e = new MenuItem("Edit color");
        e.addActionListener( new ActionListener() { //TODO
        	//The rendered image must be added to jtabbedpane!!!
                                                public void actionPerformed(ActionEvent e) {
                                                        p.addConst2();
                                                }
                                                });
       MenuItem i = new MenuItem("Invert color");
       i.addActionListener( new ActionListener() {
                                                public void actionPerformed(ActionEvent e) {
                                                    p.invert2();
                                                }});
       MenuItem rotate= new MenuItem("Rotate Image");
       rotate.addActionListener(new ActionListener() {
    	   public void actionPerformed(ActionEvent e) {
    		   p.rotate();
    	   }
       });
       MenuItem brows = new MenuItem("Help");
       brows.addActionListener(new ActionListener() { //Open Webbrowser:
    	   										public void actionPerformed(ActionEvent e) {
    	   											try {
    	   												Desktop.getDesktop().browse(new URI("http://www.oracle.com/"));
    	   											}
    	   											catch(Exception e2) {
    	   											};
    	   										}
       });
       edit.add(brows);
       edit.add(i);
       edit.add(e);
       edit.add(rotate);
       m.add(edit);
       // Add the Menubar
       setMenuBar(m);
       //Choose an image for an icon to be displayed in the upper left corner of the JFrame:
       /*try {
       setIconImage(ImageIO.read(new File("biohazard004.jpg")));
       }
       catch(IOException ef) {
           ef.printStackTrace();
       }*/
       pack();
       setSize(300,300);
    }   
}
