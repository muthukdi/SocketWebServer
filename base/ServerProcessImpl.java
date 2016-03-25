package base;

import base.Response;
import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ServerProcessImpl implements ServerProcess
{
	public Response execute(String contextPath, HashMap<String, String> queryParams, Session session) throws IOException
	{
		return new Response();
	}
	
	public String saveText(String contextPath, String text) throws IOException
	{	
		File file = File.createTempFile("response", null);
		FileOutputStream fos = new FileOutputStream(file);
		byte[] contentInBytes = text.getBytes();
		fos.write(contentInBytes);
		fos.flush();
		fos.close();
		
		return file.getAbsolutePath();
	}
	
	public String saveImage(String contextPath, BufferedImage image, String type) throws IOException
	{
		File file = File.createTempFile("response", null);
		ImageIO.write(image, type, file);
		image.flush();
		
		return file.getAbsolutePath();
	}
}
