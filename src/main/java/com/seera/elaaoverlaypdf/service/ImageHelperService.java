package com.seera.elaaoverlaypdf.service;

import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;

/**
 * 
 * @author ricardo.paulo
 *
 */
@Service
public class ImageHelperService {
	
	
	/**
	 * 
	 * @param originalImage
	 * @return
	 * @throws IOException
	 */
	public byte[] bufferedImgToByte(BufferedImage originalImage) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(originalImage, "jpg", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		return imageInByte;
	}
	
	
	/**
	 * 
	 * @param bimg
	 * @return
	 */
	public BufferedImage grayScale(BufferedImage bimg) {
	    ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);  
	    ColorConvertOp op = new ColorConvertOp(cs, null);  
	    BufferedImage image = op.filter(bimg, null);

	    
	    return image;
	}
}
