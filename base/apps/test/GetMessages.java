package base.apps.test;

import base.ServerProcessImpl;
import base.Response;
import base.Session;
import java.util.HashMap;
import java.io.IOException;
import java.sql.*;

public class GetMessages extends ServerProcessImpl
{	
	public Response execute(String contextPath, HashMap<String, String> queryParams, Session session) throws IOException
	{
		Response response = new Response();
		response.setMimeType("text/html");
		String str = "";
		String message = queryParams.get("message");
		String db_path = "jdbc:h2:~/test";
		String db_user = "sa";
		String db_password = "";
		String sqlCommand = "";
		Connection cn = null;
		Statement st = null;
		ResultSet rs = null;
		User user = (User)session.getDataItem("user");
		if (user == null)
		{
			str = "expired";
			System.out.println("A user session has expired");
		}
		else
		{
			UserManager um = UserManager.getInstance();
			if (um.getUser(user.getUsername()) == null)
			{
				str = "inactive";
				System.out.println(user.getUsername() + " is inactive");
			}
			else
			{
				try
				{
					Class.forName("org.h2.Driver");
					cn = DriverManager.getConnection(db_path, db_user, db_password);
					st = cn.createStatement();
					sqlCommand = "create table if not exists message(" + 
								 "message_id int NOT NULL," +
								 "username varchar(40) NOT NULL," +
								 "timestamp datetime NOT NULL," +
								 "message varchar(400) DEFAULT NULL," +
								 "PRIMARY KEY (message_id))";
					st.executeUpdate(sqlCommand);
					sqlCommand = "create sequence if not exists sq_message_id";
					st.executeUpdate(sqlCommand);
					cn.commit();
					int messageId = user.getLastMessageId();
					sqlCommand = "select message_id, username, timestamp, message from message where message_id > " + messageId;
					rs = st.executeQuery(sqlCommand);
					while (rs.next())
					{
						messageId = rs.getInt(1);
						str += "<b>" + rs.getTimestamp(3) + " ";
						str += rs.getString(2) + ":</b> ";
						str += rs.getString(4) + "<br />";
						
					}
					user.setLastMessageId(messageId);
				}
				catch (ClassNotFoundException | SQLException ex)
				{
					ex.printStackTrace(System.out);
					str = ex.getMessage() + "";
				}
				finally
				{
					try
					{
						if (cn != null) cn.close();
						if (st != null) st.close();
						if (rs != null) rs.close();
					}
					catch (SQLException e)
					{
						e.printStackTrace(System.out);
						str = e.getMessage() + "";
					}
				}
			}
		}
		
		String resourcePath = saveText(contextPath, str);
		response.setPath(resourcePath);
		
		return response;
	}
}