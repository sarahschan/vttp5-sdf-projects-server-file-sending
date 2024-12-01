# SDF Projects - Server File Sending
- Client Server I/O file sending program
- Server sends polar bear file to client once client connects to server
- Program will create a directory "clientRecieved" and the polar bear file will be in it


## Usage
Server: java -cp classes Server 8080 serverFiles/polarbear.jpg
Client: java -cp classes Client localhost:8080
