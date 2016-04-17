package base.apps.test;

public class User
{
	
	private String username;
	private String password;
	private String firstname;
	private String lastname;
	private long inactiveTime;
	private static final long INACTIVE_INTERVAL = 240000L;
	private int lastMessageId;
	
	public User()
	{
		inactiveTime = System.currentTimeMillis() + INACTIVE_INTERVAL;
	}
	
	public String getUsername() 
	{
		return username;
	}
	
	public void setUsername(String username) 
	{
		this.username = username;
	}
	
	public String getPassword() 
	{
		return password;
	}
	
	public void setPassword(String password) 
	{
		this.password = password;
	}
	
	public String getFirstname() 
	{
		return firstname;
	}
	
	public void setFirstname(String firstname) 
	{
		this.firstname = firstname;
	}
	
	public String getLastname() 
	{
		return lastname;
	}
	
	public void setLastname(String lastname)
	{
		this.lastname = lastname;
	}
	
	public int getLastMessageId() 
	{
		return lastMessageId;
	}
	
	public void setLastMessageId(int lastMessageId)
	{
		this.lastMessageId = lastMessageId;
	}
	
	public boolean isActive() 
	{
		return System.currentTimeMillis() < inactiveTime;
	}
	
	public void resetInactiveTime()
	{
		inactiveTime = System.currentTimeMillis() + INACTIVE_INTERVAL;
	}

}