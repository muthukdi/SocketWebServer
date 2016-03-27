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
		String str = "";
		String username = queryParams.get("username");
		String password = queryParams.get("password");
		if (username == null || username.trim().equals("") || password == null || password.trim().equals(""))
		{
			str += "<html><head><title>Login</title>";
			str += "<script>window.location = 'index.html';</script>";
			str += "</head><body></body></html>";
		}
		else
		{
			User user = new User();
			user.setUsername(username.trim());
			user.setPassword(password.trim());
			session.addDataItem("user", user);
			str += "<html><head><title>Login</title>";
			str += "<script>window.location = 'Hello.class';</script>";
			str += "</head><body></body></html>";
		}
		String resourcePath = saveText(contextPath, str);
		response.setPath(resourcePath);
		
		return response;
	}
}