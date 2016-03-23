package base.apps.test;

import base.ServerProcessImpl;
import base.Response;
import java.util.HashMap;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Photo extends ServerProcessImpl
{	
	public Response execute(String contextPath, HashMap<String, String> queryParams) throws IOException
	{
		Response response = new Response();
		response.setMimeType("image/png");
		BufferedImage image = ImageIO.read(new File(contextPath + "mario.png"));
		String resourcePath = saveImage(contextPath, image, "png");
		response.setPath(resourcePath);
		
		return response;
	}
}