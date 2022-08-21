# MentDB Weak

Hello everyone!  

Please, follow the instructions below to install the server.
Thank you.

 - [https://www.mentdb.org](https://www.mentdb.org)
 - contact@mentdb.org
 - jim@mentdb.org

## INSTALLATION

You can see this video to help you:  
[https://youtu.be/DsYCcszlick?list=PLn4aw_h7C96uxzZVMyBS-_6DeIURc6KtL](https://youtu.be/DsYCcszlick?list=PLn4aw_h7C96uxzZVMyBS-_6DeIURc6KtL)

You must start
 - one serveur
 - and N editor(s) to develop...

## MentDB Weak Server in embedded mode (H2 database)

On OSX/Linux or Windows<br>

 1. BEGIN
 2. Open the 'MentDB_Server_3/bin' directory
 3. Open the directory 'linux' or 'osx' or 'windows'
 4. Double click on 'start.sh' (or .bat on Windows)
 5. END

## MentDB Weak Server with MySQL in remote server

On OSX/Linux or Windows

 1. BEGIN
 2. Install MySQL 8
 3. Create the database 'mentdb' with the 'Latin1' charset
  CREATE DATABASE mentdb CHARACTER SET latin1 COLLATE latin1_swedish_ci;
  CREATE USER 'mentdbuser'@'localhost' IDENTIFIED BY 'pwd';
  GRANT ALL PRIVILEGES ON mentdb.* TO 'mentdbuser'@'localhost';
  FLUSH PRIVILEGES;
 4. Open the directory 'mentdb_weak/conf'
 5. Open the file 'server.conf' and go to the '[MYSQL]' section<
 6. Change in [SQL] section, set light_mode=false
 7. Update the MySQL connection 'host', 'port', 'db', 'user', 'pwd'
 8. Open the 'mentdb_weak/bin' directory
 9. Open the directory 'linux' or 'osx' or 'windows'
10. Double click on 'start.sh' (or .bat on Windows)
11. If MySQL error when create basic tables, update the script in tools/mentdb.sql, delete the 'data' directory, and restart with start.sh (or .bat on Windows)
12. END

## MentDB Weak Editor

On OSX/Linux or Windows<br>

 1. BEGIN
 2. Open the 'mentdb_weak/MentDB_Editor_3/bin' directory
 3. Open the directory 'linux' or 'osx' or 'windows'
 4. Double click on editor.sh (or .bat on Windows)
 5. The trigger window open ...
 6. Double click on the default/admin connection
 7. The editor open ...
 8. END

All videos for the Weak version of MentDB:  
https://www.youtube.com/playlist?list=PLn4aw_h7C96uxzZVMyBS-_6DeIURc6KtL
