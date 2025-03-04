package clientHandling;

import data.Email;

import jakarta.mail.internet.MimeUtility;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

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
    }

    @Override
    public boolean authenticate(String email, String appPassword) {
        try {
            String responseUser = sendCommand(POPCommands.USER + " " + email);
            if (!responseUser.startsWith("+OK")) {
                return false;
            }
            String responsePass = sendCommand(POPCommands.PASS + " " + appPassword);
            return responsePass.startsWith("+OK");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String retrieveEmail(int id) {
        return "";
    }

    @Override
    public void deleteEmail(int id) {

    }

    @Override
    public void fetchEmails(ObservableList<Email> emailList) {
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

            Platform.runLater(emailList::clear);

            for (int i = 0; i < messageIds.size(); i++) {
                int messageId = messageIds.get(i);
                int messageSize = messageSizes.get(i);
                System.out.println("\nFetching Email #" + messageId);

                sendCommand(POPCommands.RETR + " " + messageId);

                String sender = "Unknown sender";
                String type = "Unknown type";
                String subject = "No Subject";
                String company = "No company";
                String helper;

                boolean readingSubject = false;

                boolean isHeader = true;
                while (!(line = reader.readLine()).equals(".")) {
                    if (isHeader) {
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
                        }
                        else if (readingSubject && (line.startsWith(" ") || line.startsWith("\t"))) {
                            subject += " " + line.trim();
                        }
                        else if (!line.isEmpty()) {
                            readingSubject = false;
                        }
                    } else if (line.isEmpty()) {
                            isHeader = false;
                    }
                }

                try {
                    if(company.startsWith("=?UTF-8?"))
                        company = MimeUtility.decodeText(company);
                    if(sender.startsWith("=?UTF-8?"))
                        sender = MimeUtility.decodeText(sender);

                    if(subject.startsWith("=?UTF-8?"))
                        subject = MimeUtility.decodeText(subject);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                Email email = new Email(messageId, company, sender, type, subject, messageSize + " bytes");

                Platform.runLater(() -> {
                    emailList.add(email);
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void logOut() {
        sendCommand(POPCommands.QUIT);
    }

}
