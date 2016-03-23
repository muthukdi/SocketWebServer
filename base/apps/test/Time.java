package base.apps.test;

import base.ServerProcessImpl;
import base.Response;
import java.util.Date;
import java.util.HashMap;
import java.io.IOException;

public class Time extends ServerProcessImpl
{	
	public Response execute(String contextPath, HashMap<String, String> queryParams) throws IOException
	{
		Response response = new Response();
		response.setMimeType("text/html");
		String str = "";
		str += "<html><head><title>Current Server Time</title></head><body>";
		str += "<div style='color:blue;background-color:yellow;'>";
		str += new Date().toString() + "";
		str += "</div></body></html>";
		String resourcePath = saveText(contextPath, str);
		response.setPath(resourcePath);
		
		return response;
	}
}