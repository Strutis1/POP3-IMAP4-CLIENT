package clientHandling;

import data.Email;

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
            if (sslSocket != null) sslSocket.close();
            if (reader != null) reader.close();
            if (writer != null) writer.close();
        } catch (IOException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }

    public abstract String retrieveEmail(int id);

    public abstract void deleteEmail(int id);

    public abstract List<Email> fetchEmails();
}
