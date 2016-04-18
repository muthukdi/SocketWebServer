package base.apps.test;

import base.ServerProcessImpl;
import base.Response;
import base.Session;
import java.util.HashMap;
import java.io.IOException;

public class GetUsers extends ServerProcessImpl
{	
	public Response execute(String contextPath, HashMap<String, String> queryParams, Session session) throws IOException
	{
		Response response = new Response();
		response.setMimeType("text/html");
		String str = "";
		String color = "";
		User user = (User)session.getDataItem("user");
		UserManager um = UserManager.getInstance();
		Object[] objArray = um.getUsers();
		for (int i = 0; i < objArray.length; i++)
		{
			String username = ((User)objArray[i]).getUsername();
			color = username.equals(user.getUsername()) ? "blue" : "black";
			str += "<b style='color:" + color + "';>" + ((User)objArray[i]).getUsername() + "</b><br />";
		}
		
		String resourcePath = saveText(contextPath, str);
		response.setPath(resourcePath);
		
		return response;
	}
}