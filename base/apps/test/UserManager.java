package base.apps.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.sql.*;

/*
	A chat room class is implemented as a singleton
*/

public class UserManager
{
	
	private static UserManager userManager;
	private ArrayList<User> users;
	private String db_path;
	private String db_user;
	String db_password;
	String sqlCommand;
	Connection cn = null;
	Statement st = null;
	ResultSet rs = null;
	
	private UserManager()
	{
		users = new ArrayList<User>();
		db_path = "jdbc:h2:~/test";
		db_user = "sa";
		db_password = "";
		sqlCommand = "";
	}
	
	public static synchronized UserManager getInstance()
	{
		if (userManager == null)
		{
			userManager = new UserManager();
			System.out.println("User manager created");
			Runnable r = new Runnable()
			{
				public void run()
				{
					while (true)
					{
						try
						{
							Thread.sleep(1000);
							userManager.removeInactiveUsers();
						}
						catch (InterruptedException e)
						{
							e.printStackTrace(System.out);
						}
					}
				}
			};
			Thread t = new Thread(r, "UserManagerThread");
			t.start();
		}
		return userManager;
	}
	
	// To prevent cloning since it will violate the
	// singleton design pattern!
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	public User getUser(String username)
	{
		Iterator<User> it = users.iterator();
		User user = null;
		while (it.hasNext())
		{
			user = it.next();
			if (username.equals(user.getUsername()))
			{
				break;
			}
			user = null;
		}
		if (user != null)
		{
			if (!user.isActive())
			{
				user = null;
			}
		}
		return user;
	}
	
	public synchronized boolean addUser(User user)
	{
		// Insert user joined message into the database
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
			sqlCommand = "select sq_message_id.nextval from dual";
			rs = st.executeQuery(sqlCommand);
			int messageId = 0;
			if (rs.next())
			{
				messageId = rs.getInt(1);
			}
			sqlCommand = "insert into message values (" + messageId + ", '" + user.getUsername() + "', SYSDATE, '" + user.getUsername() + " has joined')";
			st.executeUpdate(sqlCommand);
			cn.commit();
			user.setLastMessageId(messageId);
		}
		catch (ClassNotFoundException | SQLException ex)
		{
			ex.printStackTrace(System.out);
			return false;
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
				return false;
			}
		}
		users.add(user);
		System.out.println(user.getUsername() + " joined");
		return true;
	}
	
	private synchronized boolean removeUser(User user)
	{
		// Insert user left message into the database
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
			sqlCommand = "select sq_message_id.nextval from dual";
			rs = st.executeQuery(sqlCommand);
			int messageId = 0;
			if (rs.next())
			{
				messageId = rs.getInt(1);
			}
			sqlCommand = "insert into message values (" + messageId + ", '" + user.getUsername() + "', SYSDATE, '" + user.getUsername() + " has left')";
			st.executeUpdate(sqlCommand);
			cn.commit();
		}
		catch (ClassNotFoundException | SQLException ex)
		{
			ex.printStackTrace(System.out);
			return false;
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
				return false;
			}
		}
		users.remove(user);
		System.out.println(user.getUsername() + " left");
		return true;
	}
	
	private void removeInactiveUsers()
	{
		ArrayList<User> inactive = new ArrayList<User>();
		Iterator<User> it = users.iterator();
		while (it.hasNext())
		{
			User user = it.next();
			if (!user.isActive())
			{
				inactive.add(user);
			}
		}
		it = inactive.iterator();
		while (it.hasNext())
		{
			User user = it.next();
			if (!removeUser(user))
			{
				System.out.println("Could not remove " + user.getUsername());
			}
		}
	}
	
}