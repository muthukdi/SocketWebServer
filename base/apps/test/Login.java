package base.apps.test;

import base.ServerProcessImpl;
import base.Response;
import base.Session;
import java.util.HashMap;
import java.io.IOException;

public class Login extends ServerProcessImpl
{	
	public Response execute(String contextPath, HashMap<String, String> queryParams, Session session) throws IOException
	{
		Response response = new Response();
		response.setMimeType("text/html");
		String username = queryParams.get("username");
		String password = queryParams.get("password");
		session.addDataItem("username", username);
		session.addDataItem("password", password);
		String str = "";
		str += "<html><head><title>Login</title>";
		str += "<script>window.location = 'Hello.class';</script>";
		str += "</head><body></body></html>";
		String resourcePath = saveText(contextPath, str);
		response.setPath(resourcePath);
		
		return response;
	}
}