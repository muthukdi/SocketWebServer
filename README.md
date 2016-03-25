# SocketWebServer
Author -- Dilip Muthukrishnan, cheepu82@gmail.com

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