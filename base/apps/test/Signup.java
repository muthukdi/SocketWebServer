package base.apps.test;

import base.ServerProcessImpl;
import base.Response;
import base.Session;
import java.util.HashMap;
import java.io.IOException;
import java.sql.*;

public class Signup extends ServerProcessImpl
{	
	public Response execute(String contextPath, HashMap<String, String> queryParams, Session session) throws IOException
	{
		Response response = new Response();
		response.setMimeType("text/html");
		String response_str = "";
		String response_msg = "";
		String username = queryParams.get("username");
		String password = queryParams.get("password");
		String firstname = queryParams.get("firstname");
		String lastname = queryParams.get("lastname");
		String db_path = "jdbc:h2:~/test";
		String db_user = "sa";
		String db_password = "";
		String sqlCommand = "";
		Connection cn = null;
		Statement st = null;
		ResultSet rs = null;
		
		if (username == null || username.trim().equals("") || password == null || password.trim().equals("") || 
			firstname == null || firstname.trim().equals("") || lastname == null || lastname.trim().equals(""))
		{
			response_msg = "Invalid input detected!  ";
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
				sqlCommand = "insert into user values ('" + username + "','" + password + "','" + firstname + "','" + lastname + "')";
				st.executeUpdate(sqlCommand);
				response_msg = "New user created successfully.  ";
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
		
		response_str += "<html><head><title>Signup</title></head><body>";
		response_str += response_msg;
		response_str += "Click <a href='index.html'>here</a> to go back.";
		response_str += "</body></html>";
		
		String resourcePath = saveText(contextPath, response_str);
		response.setPath(resourcePath);
		
		return response;
	}
}