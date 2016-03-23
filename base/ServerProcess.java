package base;

import base.Response;
import java.util.HashMap;
import java.io.IOException;
import java.awt.image.BufferedImage;

public interface ServerProcess
{
	public Response execute(String contextPath, HashMap<String, String> queryParams) throws IOException;
	
	public String saveText(String contextPath, String text) throws IOException;
	
	public String saveImage(String contextPath, BufferedImage image, String type) throws IOException;
}