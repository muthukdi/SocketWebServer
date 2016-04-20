package base.apps.test;

import base.ServerProcessImpl;
import base.Response;
import base.Session;
import java.util.HashMap;
import java.io.IOException;

public class ChatRoom extends ServerProcessImpl
{	
	public Response execute(String contextPath, HashMap<String, String> queryParams, Session session) throws IOException
	{
		Response response = new Response();
		response.setMimeType("text/html");
		String str = "";
		User user = (User)session.getDataItem("user");
		if (user == null)
		{
			str += "<html><head><title>Chat Room</title>";
			str += "<script>window.location = 'index.html';</script>";
			str += "</head><body></body></html>";
		}
		else
		{
			UserManager um = UserManager.getInstance();
			if (um.getUser(user.getUsername()) == null)
			{
				str += "<html><head><title>Chat Room</title>";
				str += "<script>window.location = 'index.html';</script>";
				str += "</head><body></body></html>";
			}
			else
			{
				String firstname = (String)user.getFirstname();
				String lastname = (String)user.getLastname();
				str += "<html><head><title>Chat Room</title>";
				str += "<link rel='stylesheet' href='ChatRoom.css'>";
				str += "<script type='text/javascript' src='ChatRoom.js'></script>";
				str += "</head>";
				str += "<body onload='setInterval(updateChatWindow, 1000);setInterval(updateUsersWindow, 1000);'>";
				str += "<a href='/base/apps/test/Logout.class' class='button'><b>Logout</b></a>";
				str += "<h3>Hello, " + firstname + " " + lastname + ". Enjoy chatting!</h3><br />";
				str += "<div id='chat_window'></div>";
				str += "<div id='users_window'></div><br />";
				str += "<input id='message' type='text' onkeyup='if (event.keyCode == 13) sendMessage();'><button type='button' onclick='sendMessage()'>Send</button><br />";
				str += "<div id='message_status'>Message status</div>";
				str += "</body></html>";
			}
		}
		String resourcePath = saveText(contextPath, str);
		response.setPath(resourcePath);
		
		return response;
	}
}