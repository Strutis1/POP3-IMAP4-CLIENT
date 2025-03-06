package clientHandling;

import data.DataHolder;
import data.Email;
import data.Folder;
import jakarta.mail.internet.MimeUtility;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class IMAPClient extends FatherEmail {

    private int commandCounter = 1;


    public IMAPClient(String host, int port) {
        super(host, port);
        connect();
        this.folders = new ArrayList<>();
        folders.add(new Folder("Inbox"));
        folders.add(new Folder("Sent"));
        folders.add(new Folder("Drafts"));
        folders.add(new Folder("Trash"));
        folders.add(new Folder("Spam"));
        folders.add(new Folder("Archive"));
    }

    @Override
    public boolean authenticate(String email, String appPassword) {
        try {
            String command = IMAPCommands.LOGIN + " \"" + email + "\" \"" + appPassword + "\"";
            String response = sendCommand(command);

            String[] lines = response.split("\n");
            String taggedResponse = lines[lines.length - 1].trim();

            if (taggedResponse.contains("OK")) {
                DataHolder.getInstance().setCurrentUser(email);
                System.out.println("confirmed");
                return true;
            } else {
                System.err.println("Login failed: " + taggedResponse);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    protected synchronized String sendCommand(String command) throws IOException {
        String tag = "A" + commandCounter++;
        String fullCommand = tag + " " + command;

        writer.write(fullCommand + "\r\n");
        writer.flush();

        String line;
        StringBuilder fullResponse = new StringBuilder();

        while ((line = reader.readLine()) != null) {
            fullResponse.append(line).append("\n");
            if (line.startsWith(tag + " ")) {
                break;
            }
        }
        return fullResponse.toString();
    }


    @Override
    public String retrieveEmail(int id) {
        return "";
    }

    @Override
    public void deleteEmail(int id) {

    }

    @Override
    public void fetchEmails(Folder folder, ObservableList<Email> emailList) {
        for(Folder mailbox : folders){
            try {
                String response = sendCommand(IMAPCommands.SELECT + " " + mailbox.getFolderName().toUpperCase());
            }catch(IOException e){
                e.printStackTrace();
            }


        }

        /*
        a1 SELECT INBOX
    response:
        * 10 EXISTS
        a1 OK [READ-WRITE] SELECT completed

         */
        //use SELECT on all folders
        //use FETCH id (BODY[HEADER]) and (BODY[TEXT])
    }

    @Override
    public void logOut() {
        String response = null;
        try {
            response = sendCommand(IMAPCommands.LOGOUT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (!response.startsWith("+OK")) {
            DataHolder.getInstance().setCurrentUser(null);
        }
    }

    @Override
    public void resetSession() {

    }

    @Override
    public void displayFolder(String folderName, ObservableList<Email> emailList) {
        try {
            sendCommand(IMAPCommands.SELECT + " " + folderName.toUpperCase());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
