package base.apps.test;

import base.ServerProcessImpl;
import base.Response;
import base.Session;
import java.util.HashMap;
import java.io.IOException;
import java.sql.*;

public class Login extends ServerProcessImpl
{	
	public Response execute(String contextPath, HashMap<String, String> queryParams, Session session) throws IOException
	{
		Response response = new Response();
		response.setMimeType("text/html");
		String response_str = "";
		String response_msg = "";
		String username = queryParams.get("username");
		String password = queryParams.get("password");
		String db_path = "jdbc:h2:~/test";
		String db_user = "sa";
		String db_password = "";
		String sqlCommand = "";
		Connection cn = null;
		Statement st = null;
		ResultSet rs = null;
		String firstname = null;
		String lastname = null;
		
		if (username == null || username.trim().equals("") || password == null || password.trim().equals(""))
		{
			response_msg = "Invalid input detected!  ";
		}
		else if (UserManager.getInstance().getUser(username) != null)
		{
			response_msg = "This user is already in the chat room!  ";
		}
		else
		{
			try
			{
				Class.forName("org.h2.Driver");
				cn = DriverManager.getConnection(db_path, db_user, db_password);
				st = cn.createStatement();
				sqlCommand = "create table if not exists user(" + 
							 "username varchar(40) NOT NULL," +
							 "password varchar(20) DEFAULT NULL," +
							 "firstname varchar(20) DEFAULT NULL," +
							 "lastname varchar(20) DEFAULT NULL," +
							 "PRIMARY KEY (username))";
				st.executeUpdate(sqlCommand);
				cn.commit();
				sqlCommand = "select * from user where username = '" + username.trim() + "' and password = '" + password.trim() + "'";
				rs = st.executeQuery(sqlCommand);
				if (rs.next())
				{
					firstname = rs.getString("firstname");
					lastname = rs.getString("lastname");
					User user = new User();
					user.setUsername(username.trim());
					user.setPassword(password.trim());
					user.setFirstname(firstname);
					user.setLastname(lastname);
					session.addDataItem("user", user);
					if (UserManager.getInstance().addUser(user))
					{
						response_str += "<html><head><title>Login</title>";
						response_str += "<script>window.location = 'ChatRoom.class';</script>";
						response_str += "</head><body></body></html>";
						String resourcePath = saveText(contextPath, response_str);
						response.setPath(resourcePath);
						return response;
					}
					else
					{
						response_msg = "Unable to join chat room!  ";
					}
				}
				else
				{
					response_msg = "Invalid login credentials!  ";
				}
			}
			catch (ClassNotFoundException | SQLException ex)
			{
				ex.printStackTrace(System.out);
				response_msg = ex.getMessage() + "  ";
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
					response_msg = e.getMessage() + "  ";
				}
			}
		}
		
		response_str += "<html><head><title>Login</title></head><body>";
		response_str += response_msg;
		response_str += "Click <a href='index.html'>here</a> to go back.";
		response_str += "</body></html>";
		
		String resourcePath = saveText(contextPath, response_str);
		response.setPath(resourcePath);
		
		return response;
	}
}