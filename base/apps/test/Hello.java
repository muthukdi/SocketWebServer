package base.apps.test;

import base.ServerProcessImpl;
import base.Response;
import base.Session;
import java.util.HashMap;
import java.io.IOException;

public class Hello extends ServerProcessImpl
{	
	public Response execute(String contextPath, HashMap<String, String> queryParams, Session session) throws IOException
	{
		Response response = new Response();
		response.setMimeType("text/html");
		String str = "";
		User user = (User)session.getDataItem("user");
		if (user == null)
		{
			str += "<html><head><title>Hello</title>";
			str += "<script>window.location = 'index.html';</script>";
			str += "</head><body></body></html>";
		}
		else
		{
			String username = (String)user.getUsername();
			String password = (String)user.getPassword();
			str += "<html><head><title>Hello</title></head><body>";
			str += "<h1>Hello, " + username + " " + password + "</h1>";
			str += "</body></html>";
		}
		String resourcePath = saveText(contextPath, str);
		response.setPath(resourcePath);
		
		return response;
	}
}