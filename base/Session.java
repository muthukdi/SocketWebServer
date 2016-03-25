package base;

import java.util.HashMap;
import java.security.SecureRandom;
import java.math.BigInteger;

public class Session
{
	
	private String sessionId;
	private HashMap<String, Object> data;
	private long expiryTime;
	private static final long EXPIRE_INTERVAL = 10000L;
	
	public Session()
	{
		SecureRandom random = new SecureRandom();
		sessionId = new BigInteger(130, random).toString(32);
		data = new HashMap<String, Object>();
		expiryTime = System.currentTimeMillis() + EXPIRE_INTERVAL;
	}
	
	public String getSessionId()
	{
		return sessionId;
	}
	
	public Object getDataItem(String key)
	{
		return data.get(key);
	}
	
	public synchronized Object addDataItem(String key, Object value)
	{
		return data.put(key, value);
	}
	
	public synchronized Object removeDataItem(String key)
	{
		return data.remove(key);
	}
	
	public boolean isExpired()
	{
		return System.currentTimeMillis() > expiryTime;
	}
	
	public void resetExpiryTime()
	{
		expiryTime = System.currentTimeMillis() + EXPIRE_INTERVAL;
	}
	
}