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
import java.util.regex.Pattern;


public class POP3Client extends FatherEmail {


    public POP3Client(String host, int port) {
        super(host, port);
        connect();
        this.folders = new ArrayList<>();
        folders.add(new Folder("Inbox"));
    }

    @Override
    public boolean authenticate(String email, String appPassword) {
        try {
            String responseUser = sendCommand(POPCommands.USER + " " + email);
            if (!responseUser.startsWith("+OK")) {
                return false;
            }
            String responsePass = sendCommand(POPCommands.PASS + " " + appPassword);
            if(responsePass.startsWith("+OK") && responseUser.startsWith("+OK")) {
                DataHolder.getInstance().setCurrentUser(email);
                return true;
            }else{
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected String sendCommand(String command) throws IOException {
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

    @Override
    public String retrieveEmail(int id) {
        return "";
    }

    @Override
    public void deleteEmail(int id) {
        try {
            sendCommand(POPCommands.DELE + " " + id);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void fetchEmails(Folder folder, ObservableList<Email> emailList) {
        try {
            sendCommand(POPCommands.LIST);
            String line;
            List<Integer> messageIds = new ArrayList<>();
            List<Integer> messageSizes = new ArrayList<>();

            while (!(line = reader.readLine()).equals(".")) {
                System.out.println(line);
                String[] parts = line.split(" ");
                if (parts.length > 1) {
                    try {
                        messageIds.add(Integer.parseInt(parts[0])); // Message ID
                        messageSizes.add(Integer.parseInt(parts[1])); // Message size
                    } catch (NumberFormatException ignored) {}
                }
            }

            Folder inbox = getInboxFolder();
            if (inbox == null) return;


            for (int i = 0; i < messageIds.size(); i++) {
                int messageId = messageIds.get(i);
                int messageSize = messageSizes.get(i);
                System.out.println("\nFetching Email #" + messageId);

                sendCommand(POPCommands.RETR + " " + messageId);

                String sender = "Unknown sender";
                String type = "Unknown type";
                String subject = "No Subject";
                String company = "No company";
                String content = "No content";
                String helper;

                boolean readingSubject = false;

                boolean isHeader = true;

                StringBuilder rawEmail = new StringBuilder();

                while ((line = reader.readLine()) != null && !line.equals(".")) {
                    rawEmail.append(line).append("\r\n");
                    if (line.startsWith("From:")) {
                        // gali buti formatas ""Company" <type+sender>" so we get those values
                        helper = line.substring(5).trim();
                        System.out.println("Extracted From line: " + helper);
                        Pattern patternWithType = Pattern.compile("\"(.*?)\"\\s*<([^+]+)\\+([^>]+)>");
                        Matcher matcherWithType = patternWithType.matcher(helper);

                        // gali buti formatas ""Company" <sender>" so we get those values
                        Pattern patternWithoutType = Pattern.compile("(?:\"(.*?)\"|([^<]+))\\s*<([^>]+)>");
                        Matcher matcherWithoutType = patternWithoutType.matcher(helper);

                        if (matcherWithType.find()) {
                            company = matcherWithType.group(1);
                            type = matcherWithType.group(2);
                            sender = matcherWithType.group(3);
                        } else if (matcherWithoutType.find()) {
                            company = matcherWithoutType.group(1) != null ? matcherWithoutType.group(1) : matcherWithoutType.group(2);
                            sender = matcherWithoutType.group(3);
                        }
                    } else if (line.startsWith("Subject:")) {
                        subject = line.substring(8).trim();
                        readingSubject = true;
                    } else if (readingSubject && (line.startsWith(" ") || line.startsWith("\t"))) {
                        subject += " " + line.trim();
                    } else if (!line.isEmpty()) {
                        readingSubject = false;
                    }
                }

                content = extractEmailBody(rawEmail.toString());


                try {
                    if(company.startsWith("=?UTF-8?") || company.startsWith("=?utf-8?"))
                        company = MimeUtility.decodeText(company);
                    if(sender.startsWith("=?UTF-8?") || sender.startsWith("=?utf-8?"))
                        sender = MimeUtility.decodeText(sender);
                    subject = MimeUtility.decodeText(subject);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }



                Email email = new Email(messageId, company, sender, type, subject, messageSize + " bytes", content);

                boolean exists = emailList.stream().anyMatch(e -> e.getId() == email.getId());
                if (!exists) {
                    Platform.runLater(() -> emailList.add(email));
                }


            }
            Platform.runLater(() -> inbox.updateEmails(new ArrayList<>(emailList)));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resetSession() {
        try {
            sendCommand(POPCommands.RSET);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //the only folder managed by pop3 protocol
    public Folder getInboxFolder() {
        return folders.stream()
                .filter(f -> f.getFolderName().equalsIgnoreCase("Inbox"))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void logOut() {
        try {
            sendCommand(POPCommands.QUIT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Logged out of " + DataHolder.getInstance().getCurrentUser());
    }

    @Override
    public void displayFolder(String folderName, ObservableList<Email> emailList) {
        if (!emailList.equals(getInboxFolder().getFolderEmails())) {
            emailList.setAll(getInboxFolder().getFolderEmails());
        }
    }
}
