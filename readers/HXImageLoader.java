package readers;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class HXImageLoader {
	
	public static BufferedImage image_HXTemplate;
	
	public HXImageLoader() {
		try {
			image_HXTemplate = ImageIO.read(HXImageLoader.class.getResourceAsStream("/resources/image.png"));
		} catch (IOException e) {
			// Not handled
		}
	}
}
