package base;

/* SocketWebServer.java -- Dilip Muthukrishnan, cheepu82@gmail.com

Adapted from this version by Matt Mahoney, mmahoney@cs.fit.edu
http://cs.fit.edu/~mmahoney/cse3103/java/Webserver.java

SocketWebServer.java implements a simple HTTP web server which can be
"extended" by creating custom web applications.

Web server's capabilities: 

1. It only accepts HTTP GET requests.
2. It is capable of parsing query parameters.
3. It can serve static content from the current directory and subdirectories.
4. It can serve dynamic text/image content via user-defined server processes.
5. It supports a basic HTTP session via cookies.
6. It does not support CGI, authentication, keepalive, etc.


*/

import java.util.*;
import java.io.*;
import java.net.*;

// A SocketWebServer waits for clients to connect, then starts a separate
// thread to handle the request.
public class SocketWebServer 
{
	private static ServerSocket serverSocket;

	public static void main(String[] args) throws IOException
	{
		serverSocket = new ServerSocket(9090);  // Start, listen on port 9090
		while (true)
		{
			try
			{
				Socket s = serverSocket.accept();  // Wait for a client to connect
				SessionManager.getInstance().removeExpiredSessions();
				new ClientHandler(s);  // Handle the client in a separate thread
			}
			catch (Exception x)
			{
				System.out.println(new Date() + ": " + x.getMessage());
			}
		}
	}
}

// A ClientHandler reads an HTTP request and responds
class ClientHandler extends Thread
{
	private Socket socket;  // The accepted socket from the SocketWebServer

	// Start the thread in the constructor
	public ClientHandler(Socket s)
	{
		socket = s;
		start();
	}

	// Read the HTTP request, respond, and close the connection
	public void run()
	{
		BufferedReader in = null;
		PrintStream out = null;
		InputStream fis = null;
		String filename = "";
		boolean newSession = false;
		try 
		{
			// Open connections to the socket
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintStream(new BufferedOutputStream(socket.getOutputStream()));

			// Read filename from first input line "GET /filename.html ..."
			String firstLine = in.readLine();
			
			// If the filename is null or favicon.ico, then reject this request.
			// This will mitigate against bogus requests.
			if (firstLine == null || firstLine.contains("favicon.ico"))
			{
				System.out.println(new Date() + ": " + socket.getRemoteSocketAddress() + " " + "Rejecting an invalid request.");  // Bad request
				return;
			}
			
			// Keep reading to find the session cookie header
			String header = ".";
			String sessionCookieHeader = "";
			while (!header.equals(""))
			{
				header = in.readLine();
				if (header.contains("sessionId"))
				{
					sessionCookieHeader = header;
					break;
				}
			}
			// Get/create a session associated with this request
			Session session = null;
			synchronized (SessionManager.getInstance())
			{
				if (sessionCookieHeader.equals(""))
				{
					newSession = true;
					session = SessionManager.getInstance().createSession();
				}
				else
				{
					// Parse session Id from cookie
					String sessionId = sessionCookieHeader.split("=")[1];
					// Get the session associated with this session Id
					session = SessionManager.getInstance().getSessionWithId(sessionId);
					// If the session has expired, create a new one
					if (session == null)
					{
						newSession = true;
						session = SessionManager.getInstance().createSession();
					}
				}
			}

			// Attempt to serve the file.  Catch FileNotFoundException and
			// return an HTTP error "404 Not Found".  Treat invalid requests
			// the same way.
			StringTokenizer st = new StringTokenizer(firstLine);
			HashMap<String, String> queryParams = new HashMap<String, String>();
			try
			{
				// Only handle the GET method
				if (st.hasMoreElements() && st.nextToken().equalsIgnoreCase("GET") && st.hasMoreElements())
				{
					filename = st.nextToken();
				}
				else
				{
					throw new FileNotFoundException("Unable to parse the resource from the header!");  // Bad request
				}
				// Extract query parameters
				if (filename.length() - filename.replaceAll("\\?", "").length() > 1)
				{
					throw new FileNotFoundException("More than one \"?\" mark detected!");  // Bad request
				}
				String[] urlComponents = filename.split("\\?");
				filename = urlComponents[0];
				if (urlComponents.length == 2)
				{
					String params = urlComponents[1];
					String[] paramComponents = params.split("&");
					int amp_count = params.length() - params.replaceAll("&", "").length();
					if (paramComponents.length != amp_count + 1)
					{
						throw new FileNotFoundException("Too many \"&\" signs detected!");  // Bad request
					}
					int eq_count = params.length() - params.replaceAll("=", "").length();
					if (paramComponents.length != eq_count)
					{
						throw new FileNotFoundException("Incorrect number of \"=\" detected!");  // Bad request
					}
					for (int i = 0; i < paramComponents.length; i++)
					{
						String[] pair = paramComponents[i].split("=");
						if (pair.length == 2 && (pair[0] == null || pair[0].equals("")))
						{
							throw new FileNotFoundException("A key is missing from the query parameters!");  // Bad request
						}
						String key = null;
						String value = null;
						if (pair.length == 2)
						{
							key = URLDecoder.decode(pair[0], "UTF-8");
							value = URLDecoder.decode(pair[1], "UTF-8");
						}
						else
						{
							key = URLDecoder.decode(pair[0], "UTF-8");
						}
						queryParams.put(key, value);
					}
				}
				
				// Log this request if log parameter is not set
				String log = queryParams.get("log");
				if (log == null)
				{
					System.out.println(new Date() + ": " + socket.getRemoteSocketAddress() + " " + firstLine);
				}
				
				// Append trailing "/" with "index.html"
				if (filename.endsWith("/"))
				{
					filename += "index.html";
				}
				// Remove leading / from filename
				while (filename.indexOf("/") == 0)
				{
					filename = filename.substring(1);
				}
				// Replace "/" with "\" in path for PC-based servers
				filename = filename.replace('/', File.separator.charAt(0));
				// Check for illegal characters to prevent access to superdirectories
				if (filename.indexOf("..") >= 0 || filename.indexOf(':') >= 0 || filename.indexOf('|') >= 0)
				{
					throw new FileNotFoundException("Do not attempt to access super directories!");
				}
				// If a directory is requested and the trailing / is missing,
				// send the client an HTTP request to append it.  (This is
				// necessary for relative links to work correctly in the client).
				if (new File(filename).isDirectory())
				{
					filename = filename.replace('\\', '/');
					out.print("HTTP/1.0 301 Moved Permanently\r\n" + "Location: /" + filename + "/\r\n\r\n");
					return;
				}

				// Determine the MIME type
				String mimeType = "text/plain";
				if (filename.endsWith(".html") || filename.endsWith(".htm"))
				{
					mimeType = "text/html";
				}
				else if (filename.endsWith(".jpg") || filename.endsWith(".jpeg"))
				{
					mimeType = "image/jpeg";
				}
				else if (filename.endsWith(".gif"))
				{
					mimeType = "image/gif";
				}
				else if (filename.endsWith(".png"))
				{
					mimeType = "image/png";
				}

				// Generate resource dynamically if requested by the client. This could be 
				// an entry point into a custom web application (i.e. dynamic functionality
				// existing within /apps/<application-name>)
				if (filename.endsWith(".class"))
				{
					// Parse the class name
					String[] pieces = filename.split("\\.");
					String classname = pieces[0];
					classname = classname.replace(File.separator.charAt(0), '.');
					// Instantiate class via reflection
					Class c = Class.forName(classname);
					ServerProcess sp = (ServerProcess)c.newInstance();
					// Path to directory which contains this class file
					String contextPath = filename.substring(0, filename.lastIndexOf(File.separator.charAt(0))+1);
					// Delegate to custom ServerProcess
					Response response = sp.execute(contextPath, queryParams, session);
					// New file path and mime type for dynamic resource
					filename = response.getPath(); 
					mimeType = response.getMimeType();
					// Open the file (may throw FileNotFoundException)
					try
					{
						fis = new FileInputStream(filename);
						// Output the header
						if (newSession)
						{
							out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + mimeType + "\r\nSet-Cookie: sessionId=" + session.getSessionId() + "; Path=/;" + "\r\n\r\n");
						}
						else
						{
							out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + mimeType + "\r\n\r\n");
						}
					}
					catch (FileNotFoundException x)
					{
						filename = "default.txt";
						mimeType = "text/plain";
						fis = new FileInputStream(filename);
						// Output the header
						if (newSession)
						{
							out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + mimeType + "\r\nSet-Cookie: sessionId=" + session.getSessionId() + "; Path=/;" + "\r\n\r\n");
						}
						else
						{
							out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + mimeType + "\r\n\r\n");
						}
					}
					// Send file contents to client
					// Could be text, html, images, etc.
					byte[] buffer = new byte[4096];
					int n;
					while ((n = fis.read(buffer)) > 0)
					{
						out.write(buffer, 0, n);
					}
				}
				else
				{
					// Open the file (may throw FileNotFoundException)
					fis = new FileInputStream(filename);
					// Output the header
					if (newSession)
					{
						out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + mimeType + "\r\nSet-Cookie: sessionId=" + session.getSessionId() + "; Path=/;" + "\r\n\r\n");
					}
					else
					{
						out.print("HTTP/1.0 200 OK\r\n" + "Content-type: " + mimeType + "\r\n\r\n");
					}
					// Send file contents to client
					// Could be text, html, images, etc.
					byte[] buffer = new byte[4096];
					int n;
					while ((n = fis.read(buffer)) > 0)
					{
						out.write(buffer, 0, n);
					}
				}
			}
			catch (FileNotFoundException | 
				   ClassNotFoundException | 
				   InstantiationException | 
				   IllegalAccessException | 
				   UnsupportedEncodingException |
				   IllegalArgumentException |
				   NoClassDefFoundError x)
			{
				String reason = x.getMessage();
				StringWriter sw = new StringWriter();
				x.printStackTrace(new PrintWriter(sw));
				String stackTrace = sw.toString();
				out.println("HTTP/1.0 404 Not Found\r\n" + "Content-type: text/html\r\n\r\n" + "<html><head></head><body style='color:red;'><h3>" +
							filename + " not found<br /><br />Reason:<br />" + reason + "<br /><br />Exception Stack Trace:<br />" + stackTrace + "</h3></body></html>\n");
			}
		}
		catch (IOException x)
		{
			x.printStackTrace(System.out);
		}
		finally
		{
			try
			{
				if (out != null) out.close();
				if (in != null) in.close();
				if (fis != null) fis.close();
				// Delete the temp file
				if (filename.contains("response") && filename.endsWith(".tmp"))
				{
					File file = new File(filename);
					if (!file.delete())
					{
						throw new IOException("Unable to delete temporary response file: " + filename);
					}
					//Files.delete(FileSystems.getDefault().getPath(filename));
				}
			}
			catch (IOException | SecurityException ex)
			{
				ex.printStackTrace(System.out);
			}
		}
	}
}