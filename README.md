# SocketWebServer
Author -- Dilip Muthukrishnan, cheepu82@gmail.com

Adapted from this version by Matt Mahoney, mmahoney@cs.fit.edu
http://cs.fit.edu/~mmahoney/cse3103/java/Webserver.java

SocketWebServer implements a simple HTTP web server which can be
"extended" by creating custom web applications.

Notes about this web server: 

1. It only accepts HTTP GET requests.
2. It is capable of parsing query parameters.
3. It can only serve files in the current directory and subdirectories.
4. It does not support CGI, cookies, authentication, keepalive, etc.
5. It can generate dynamic responses which consist of text/image content.