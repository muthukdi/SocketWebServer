package base.apps.test;

import base.ServerProcessImpl;
import base.Response;
import base.Session;
import java.util.HashMap;
import java.io.IOException;

public class Logout extends ServerProcessImpl
{	
	public Response execute(String contextPath, HashMap<String, String> queryParams, Session session) throws IOException
	{
		Response response = new Response();
		response.setMimeType("text/html");
		String str = "";
		User user = (User)session.getDataItem("user");
		if (user == null)
		{
			str += "<html><head><title>Logout</title>";
			str += "<script>window.location = 'index.html';</script>";
			str += "</head><body></body></html>";
		}
		else
		{
			UserManager um = UserManager.getInstance();
			if (um.getUser(user.getUsername()) == null)
			{
				session.invalidate();
				str += "<html><head><title>Logout</title>";
				str += "<script>window.location = 'index.html';</script>";
				str += "</head><body></body></html>";
			}
			else
			{
				session.invalidate();
				user.deactivate();
				str += "<html><head><title>Logout</title>";
				str += "<script>window.location = 'index.html';</script>";
				str += "</head><body></body></html>";
			}
		}
		String resourcePath = saveText(contextPath, str);
		response.setPath(resourcePath);
		
		return response;
	}
}