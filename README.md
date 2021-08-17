Hello everyone!<br>
<br>
Please, follow the instructions below to install the server.<br>
Thank you.<br>
<br>
https://www.mentdb.org<br>
contact@mentdb.org<br>
jim@mentdb.org<br>
<br>
INSTALLATION ________________________________________<br>
<br>
You can see this video to help you:<br>
https://youtu.be/DsYCcszlick?list=PLn4aw_h7C96uxzZVMyBS-_6DeIURc6KtL<br>
<br>
You must start <br>
	- one serveur<br>
	- and N editor(s) to develop...<br>
<br>
******* MentDB Weak Server in embedded mode (H2 database) *******<br>
On OSX/Linux or Windows<br>
1 - BEGIN<br>
2 - Open the 'MentDB_Server_3/bin' directory<br>
3 - Open the directory 'linux' or 'osx' or 'windows'<br>
4 - Double click on 'start.sh' (or .bat on Windows)<br>
5 - END<br>
<br>
******* MentDB Weak Server with MySQL in remote server *******<br>
On OSX/Linux or Windows<br>
1 - BEGIN<br>
2 - Install MySQL 8<br>
3 - Create the database 'mentdb' with the 'Latin1' charset<br>
  CREATE DATABASE mentdb CHARACTER SET latin1 COLLATE latin1_swedish_ci;<br>
  CREATE USER 'mentdbuser'@'localhost' IDENTIFIED BY 'pwd';<br>
  GRANT ALL PRIVILEGES ON mentdb.* TO 'mentdbuser'@'localhost';<br>
  FLUSH PRIVILEGES;<br>
4 - Open the directory 'MentDB_Server_3/conf'<br>
5 - Open the file 'server.conf' and go to the '[MYSQL]' section<br>
6 - Change in [SQL] section, set light_mode=false<br>
7 - Update the MySQL connection 'host', 'port', 'db', 'user', 'pwd'<br>
8 - Open the 'MentDB_Server_3/bin' directory<br>
9 - Open the directory 'linux' or 'osx' or 'windows'<br>
10 - Double click on 'start.sh' (or .bat on Windows)<br>
11 - If MySQL error when create basic tables, update the script in tools/mentdb.sql, delete the 'data' directory, and restart with start.sh (or .bat on Windows)<br>
12 - END<br>
<br>
******* MentDB Weak Editor *******<br>
On OSX/Linux or Windows<br>
1 - BEGIN<br>
2 - Open the 'MentDB_Server_3/bin' directory<br>
3 - Open the directory 'linux' or 'osx' or 'windows'<br>
4 - Double click on editor.sh (or .bat on Windows)<br>
5 - The trigger window open ...<br>
6 - Double click on the default/admin connection<br>
7 - The editor open ...<br>
8 - END<br>
<br>
<br>
All videos for the Weak version of MentDB :<br>
https://www.youtube.com/playlist?list=PLn4aw_h7C96uxzZVMyBS-_6DeIURc6KtL<br>
<br>
——————<br>
<br>
<br>