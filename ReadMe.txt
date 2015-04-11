--------------------------------------------------------------
HOW TO USE

	Server Side:
		
		For the server program, it will run automatically. Since it supposes to run forever, push ctrl + c when you want to end the program.

	Client Side:

		This program does not need any argument to run. But after running it, it would prompt for the needed input.

			The program would wait for the very first response from the server in the begin. After the server responses, the client then confirms the connected status and prompts to ask the user to input commands. The program would run forever unless the user enters EXIT command.

			Commands allowed:

				(1) GET FileName

					This command would send a request to the server to retrieve the FileName file. After the server successfully sends the file, the client side would display contents in the file. Otherwise it would prompt error like "ERROR: no such file"

				(2) BOUNCE YourMessage

					By sending BOUNCE YourMessage to server, the server would simply bounce the same message back to the client. The client would then display this message.			

				(3) EXIT or EXIT ExitCode

					This command is used to close the connection. EXIT means exit by default while EXIT ExitCode contains specific code for exit. Both the server and the client would close the connection after the command occurred. Beside the server side would display ExitCode specifically if it is sent, otherwise it would print out normal_exit. The client program would be closed in the end.
				
				PS: For all commands, spaces in the begin and in the end are allowed, the program would use trim() function to trim them. Please notice that the lower cases "get", "bounce", "exit" are not accepted for this program.	

---------------------------------------------------------------
COMPILATION

	cd YourDirectory

	javac *.java

---------------------------------------------------------------
RUN

	PS: The main functions are in the Server.java and Client.java respectively.

	For The Server:

		java Server

	For The Client:

		java Client

---------------------------------------------------------------
FILES FOR TRANSFER

	The files used for transfer from server to client is put in the files folder. Please simply put the folder in the same path as the Server.java file. There are totally three files: <intro.txt>, <gzip.txt>, <gcc.txt>.

---------------------------------------------------------------
END