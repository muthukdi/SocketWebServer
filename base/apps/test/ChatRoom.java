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
				str += "<script>";
				str += "function updateChatWindow() {";
				str += "var xhttp = new XMLHttpRequest();";
			    str += "xhttp.onreadystatechange = function() {";
				str += "if (xhttp.readyState == 4 && xhttp.status == 200) {"; 
				str += "if (xhttp.responseText == 'expired' || xhttp.responseText == 'inactive') { window.location = 'index.html'; }";
				str += "else { var element = document.getElementById('chat_window');";
				str += "element.innerHTML += xhttp.responseText;";
				str += "element.scrollTop = element.scrollHeight; } } };";
			    str += "xhttp.open('GET', '/base/apps/test/GetMessages.class?cache_buster=' + new Date().getTime(), true);";
				str += "xhttp.send(); }";
				str += "function sendMessage() {";
				str += "var message = document.getElementById('message').value.replace(/'/g, \"''\").replace(/\\?/g, '&#63;');";
				str += "document.getElementById('message').value = '';";
				str += "if (!message) return;";
				str += "var xhttp = new XMLHttpRequest();";
			    str += "xhttp.onreadystatechange = function() {";
				str += "if (xhttp.readyState == 4 && xhttp.status == 200) {";
				str += "if (xhttp.responseText == 'expired' || xhttp.responseText == 'inactive') { window.location = 'index.html'; }";
				str += "else { document.getElementById('message_status').innerHTML = xhttp.responseText; } } };";
			    str += "xhttp.open('GET', '/base/apps/test/SendMessage.class?message=' + message + '&cache_buster=' + new Date().getTime(), true);";
				str += "xhttp.send(); }";
				str += "</script></head>";
				str += "<body onload='setInterval(updateChatWindow, 1000)'>";
				str += "<h3>Hello, " + firstname + " " + lastname + ". Enjoy chatting!</h3><br />";
				str += "<div id='chat_window' style='background-color:lightblue;width:640px;height:320px;overflow:scroll;'></div><br />";
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