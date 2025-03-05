package clientHandling;

import data.Email;
import data.Folder;
import javafx.collections.ObservableList;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.util.List;

public abstract class FatherEmail {
    protected String host;
    protected int port;
    protected SSLSocket sslSocket;
    protected BufferedReader reader;
    protected BufferedWriter writer;

    protected String currentUser;

    protected List<Folder> folders;

    public FatherEmail(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void connect() {
        try {
            System.out.println("Connecting to " + host + ":" + port);
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            sslSocket = (SSLSocket) factory.createSocket(host, port);
            reader = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(sslSocket.getOutputStream()));

            String response;
            StringBuilder greeting = new StringBuilder();
            while ((response = reader.readLine()) != null) {
                greeting.append(response).append("\n");
                if (response.startsWith("+OK") || response.startsWith("-ERR")) break;
            }
            System.out.println("Server Greeting: " + greeting.toString().trim());
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }


    public abstract boolean authenticate(String email, String password);

    protected String sendCommand(String command) {
        try {
            writer.write(command + "\r\n");
            writer.flush();

            String response;
            StringBuilder fullResponse = new StringBuilder();
            while ((response = reader.readLine()) != null) {
                fullResponse.append(response).append("\n");
                if (response.startsWith("+OK") || response.startsWith("-ERR") || response.startsWith("+ ")) break; // Stop reading for known responses
            }
            return fullResponse.toString();
        } catch (IOException e) {
            return "Error sending command: " + e.getMessage();
        }
    }

    public void close() {
        try {
            System.out.println("Closing connection to " + host + ":" + port);

            if (writer != null) {
                writer.write("RSET\r\n");
                writer.flush();
            }

            if (reader != null) {
                reader.close();
                reader = null;
            }

            if (writer != null) {
                writer.close();
                writer = null;
            }

            if (sslSocket != null && !sslSocket.isClosed()) {
                sslSocket.close();
                sslSocket = null;
            }

            System.out.println("Connection closed successfully.");
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }


    public abstract String retrieveEmail(int id);

    public abstract void deleteEmail(int id);

    public abstract void fetchEmails(ObservableList<Email> emailList);

    public abstract void logOut();

    public void resetSession() {
        sendCommand(POPCommands.RSET);
    }

    public abstract void displayFolder(String folderName, ObservableList<Email> emailList);

    public abstract List<Folder> getFolders();
}
