function updateChatWindow() 
{
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function()
	{
		if (xhttp.readyState == 4 && xhttp.status == 200)
		{
			if (xhttp.responseText == 'expired' || xhttp.responseText == 'inactive')
			{
				window.location = 'index.html';
			}
			else
			{
				var element = document.getElementById('chat_window');
				element.innerHTML += xhttp.responseText;
				if (xhttp.responseText)
				{
					element.scrollTop = element.scrollHeight;
				}
			}
		}
	};
	xhttp.open('GET', '/base/apps/test/GetMessages.class?log=false&cache_buster=' + new Date().getTime(), true);
	xhttp.send();
	}

function updateUsersWindow()
{
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function()
	{
		if (xhttp.readyState == 4 && xhttp.status == 200)
		{
			var element = document.getElementById('users_window');
			element.innerHTML = xhttp.responseText;
			element.scrollTop = element.scrollHeight;
		}
	};
	xhttp.open('GET', '/base/apps/test/GetUsers.class?log=false&cache_buster=' + new Date().getTime(), true);
	xhttp.send();
}

function sendMessage()
{
	var message = document.getElementById('message').value.replace(/'/g, "''").replace(/\?/g, '&#63;');
	document.getElementById('message').value = '';
	if (!message) return;
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function()
	{
		if (xhttp.readyState == 4 && xhttp.status == 200)
		{
			if (xhttp.responseText == 'expired' || xhttp.responseText == 'inactive')
			{
				window.location = 'index.html';
			}
			else
			{
				document.getElementById('message_status').innerHTML = xhttp.responseText;
			}
		}
	};
	xhttp.open('GET', '/base/apps/test/SendMessage.class?log=false&message=' + message + '&cache_buster=' + new Date().getTime(), true);
	xhttp.send();
}