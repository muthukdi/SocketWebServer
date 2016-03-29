package base;

import java.util.ArrayList;
import java.util.Iterator;

/*
	A session manager class is implemented as a singleton
*/

public class SessionManager
{
	
	private static SessionManager sessionManager;
	private ArrayList<Session> sessions;
	
	private SessionManager()
	{
		sessions = new ArrayList<Session>();
	}
	
	public static synchronized SessionManager getInstance()
	{
		if (sessionManager == null)
		{
			sessionManager = new SessionManager();
		}
		return sessionManager;
	}
	
	// To prevent cloning since it will violate the
	// singleton design pattern!
	public Object clone() throws CloneNotSupportedException
	{
		throw new CloneNotSupportedException();
	}
	
	public Session getSessionWithId(String id)
	{
		Iterator<Session> it = sessions.iterator();
		Session session = null;
		while (it.hasNext())
		{
			session = it.next();
			if (id.equals(session.getSessionId()))
			{
				break;
			}
			session = null;
		}
		if (session != null)
		{
			if (session.isExpired())
			{
				session = null;
			}
			else
			{
				session.resetExpiryTime();
			}
		}
		return session;
	}
	
	public synchronized Session createSession()
	{
		Session session = new Session();
		sessions.add(session);
		return session;
	}
	
	public synchronized void removeExpiredSessions()
	{
		ArrayList<Session> expired = new ArrayList<Session>();
		Iterator<Session> it = sessions.iterator();
		while (it.hasNext())
		{
			Session session = it.next();
			if (session.isExpired())
			{
				expired.add(session);
			}
		}
		it = expired.iterator();
		while (it.hasNext())
		{
			sessions.remove(it.next());
		}
	}
	
}