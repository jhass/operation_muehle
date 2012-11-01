package de.hshannover.operation_muehle.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/** Some image manipulation utilities, mainly stolen from StackOverFlow.
 *  JMagick would likely a lot faster but also less portable (?).
 * 
 * @author Jonne Haß
 * @author SOF
 *
 */
public class TextureUtils {
	/** Return the given texture as BufferedImage or null on failure,
	 *  so we get a nice debug backtrace but don't have to add all that
	 *  throws or try-catch mess at the caller level.
	 * 
	 * @param filename
	 * @return
	 */
	public static BufferedImage load(String filename) {
		try {
			return ImageIO.read(new File("res/textures/"+filename));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** Do an efficient and smooth (down) scaling
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 * @throws IOException
	 */
	// http://stackoverflow.com/questions/3967731/java-scale-image-best-practice
	public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
		int imageWidth  = image.getWidth();
		int imageHeight = image.getHeight();

		double scaleX = (double)width/imageWidth;
		double scaleY = (double)height/imageHeight;
		AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
		AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

		return bilinearScaleOp.filter(
			image,
			new BufferedImage(width, height, image.getType()));
	}
	
	/** Round the corners
	 * 
	 * @param image
	 * @param cornerRadius
	 * @return
	 */
	// http://stackoverflow.com/questions/7603400/how-to-make-a-rounded-corner-image-in-java
	public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
		int w = image.getWidth();
		int h = image.getHeight();
		BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = output.createGraphics();

		// This is what we want, but it only does hard-clipping, i.e. aliasing
		// g2.setClip(new RoundRectangle2D ...)

		// so instead fake soft-clipping by first drawing the desired clip shape
		// in fully opaque white with antialiasing enabled...
		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

		// ... then compositing the image on top,
		// using the white shape from above as alpha source
		g2.setComposite(AlphaComposite.SrcAtop);
		g2.drawImage(image, 0, 0, null);

		g2.dispose();

		return output;
	}
	
	
	/** Crop the image to a smooth oval
	 * 
	 * @param input
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage makeOval(BufferedImage input, int width, int height) {
		BufferedImage output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = output.createGraphics();
		
		g.setComposite(AlphaComposite.Src);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.WHITE);
		g.fill(new Ellipse2D.Float(0, 0, width, height));
		
		g.setComposite(AlphaComposite.SrcAtop);
		g.drawImage(input, 0, 0, null);
		
		g.dispose();
		
		return output;
	}
}
