package base;

import java.io.Serializable;

/*
	The response bean encapsulates the path of the dynamically 
	generated resource as well as its mime type.
*/

public class Response implements Serializable
{
	private String path;
	private String mimeType;
	
	public Response()
	{
		path = "default.txt";
		mimeType = "text/plain";
	}
	
	public String getPath()
	{
		return path;
	}
	
	public void setPath(String path)
	{
		this.path = path;
	}
	
	public String getMimeType()
	{
		return mimeType;
	}
	
	public void setMimeType(String mimeType)
	{
		this.mimeType = mimeType;
	}
}