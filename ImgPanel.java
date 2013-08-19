import java.awt.*;
import javax.swing.JPanel;

import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.*;
import javax.media.jai.Interpolation;
import javax.media.jai.*;
import javax.imageio.ImageIO;
/**
 * Provides methods for image manipulation based on BufferedImages and JAI.
 * 
 * @author Gizm02
 * @version Latest ;-)
 */
public class ImgPanel extends JPanel
{
    public BufferedImage img;
    private File f;
    //private TiledImage t;
    /**Load a chosen image.*/
    public ImgPanel(File f) {
        //Identify the image by its name.
    	this.f = f;
        try {
            this.loadImg(f);
            setBackground(Color.BLUE);
        }
        catch(IOException e) {
			e.printStackTrace();
        }
    }
    /* paintComponent wird bei Aufruf von paint() automatisch aufgerufen. */
    /**{@inheritDoc} */
    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	if(this.img!=null) {
    		g.drawImage(this.img,0,0,this.getWidth(),this.getHeight(),this);
    	}
    }
    /**Load image.
     * @throws IOException*/
    public void loadImg(File file) throws IOException  {
        if(file==null)
            throw new IOException("No suitable source found!");
        // Only supports .gif and .jpeg
        // Class Component.Toolkit, Toolkit getToolkit
        // Class Toolkit, Image getToolkit.getImage
        img=ImageIO.read(file);
        // Now the image will be painted
        repaint();
        // Now resize the window referring to the image size
    }
    
    /**Set the RGB values of all pixel to a common one*/
    public void addConst(int a, int r, int g, int b) {
        for(int x = 0; x < img.getWidth(); x++) {
            for(int y = 0; y < img.getHeight(); y++) {
                int rgb = img.getRGB(x, y);
                rgb += (a%256) << 24;
                rgb += (r%256) << 16;
                rgb += (g%256) << 8;
                rgb += b%256;
                img.setRGB(x, y, rgb);
            }
        }
        repaint();
    }
    /**Rotate image. */
    public void rotate() {
    	int angle = 45;
    	float sigma=(float)(Math.PI/(float)180)*angle;
    	Interpolation interpol = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
    	ParameterBlock pb = new ParameterBlock();
    	pb.addSource(this.img); //source image
    	pb.add(0.0F);
    	pb.add(0.0F);
    	pb.add(sigma);
    	pb.add(interpol);
    	RenderedOp op = JAI.create("rotate",pb,null);
    	this.img=op.getAsBufferedImage();
    	repaint();
    }
    /**Add constant values to every sample values.*/
    public void addConst2() {
    	//Addconst in JAI-style
    	//TODO Image must be reloaded, unnecessary loss of performance
    	//TODO pixel transformation is applicable for one single time and then there is just
    	//no effect.
    	RenderedOp p = JAI.create("fileload", this.f.getPath());
    	ParameterBlock z = new ParameterBlock();	//Setup a parameterblock
    	z.addSource(p);
    	//Use an double[]-array for the constants
    	double[] d = new double[3];
    	d[0] = 100.3d;
    	d[1] = 200.0d;
    	d[2] = 300.0d;
    	z.add(d);
    	RenderedOp p1 = JAI.create("addconst", z);	//Manipulate pixel values
    	//Create a BufferedImage-instance of the manipulated image p1
    	img = p1.getAsBufferedImage();
    	repaint();
    }
    /**Invert the color of the image.*/
    public void invert() {
        //For every pixel in the image invert its value
        for(int x = 0; x < img.getWidth(); x++) {
            for(int y = 0; y < img.getHeight(); y++) {
                int rgb = img.getRGB(x, y);
                //For the XOR-operation use 2^32 in binary as a key for the transformation.
                //32 ones are necessary as the rgb value consists of 32 bits.
                int _value = 0xFFFFF;
                //You don't want to change the alpha-value so only 24 ones are neccessary
                //but there must be 32 bits. The 8 bits less are filled with 0.
                //_value = (int)Math.pow(2, 32);
                //_value = _value >> 8;
                rgb = rgb^_value;
                img.setRGB(x, y, rgb);
            }
        }
        repaint();
    }
    /**Invert operation referring to JAI*/
    public void invert2() {
    	RenderedOp a = JAI.create("fileload", this.f.getPath()); //load file from path
        //Now manipulate the image using the invert operation:
        RenderedOp b = JAI.create("invert", a);
        //Display the image:
        this.img = b.getAsBufferedImage();
        repaint();
    }
     /**Store manipulated images.*/
    public void store(String filename) {
    	JAI.create("filestore",this.img, filename, "JPEG");
    }
}
