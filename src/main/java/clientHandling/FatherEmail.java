package clientHandling;

import data.Email;
import data.Folder;
import jakarta.mail.internet.MimeUtility;
import javafx.collections.ObservableList;
import org.apache.commons.codec.net.QuotedPrintableCodec;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class FatherEmail {
    protected String host;
    protected int port;
    protected SSLSocket sslSocket;
    protected BufferedReader reader;
    protected BufferedWriter writer;

    protected String currentUser;

    protected List<Folder> folders;


    protected static final Pattern PATTERN_WITH_TYPE = Pattern.compile("\"(.*?)\"\\s*<([^+]+)\\+([^>]+)>");
    protected static final Pattern PATTERN_WITHOUT_TYPE = Pattern.compile("(?:\"(.*?)\"|([^<]+))\\s*<([^>]+)>");
    protected static final Pattern BASE64_PATTERN = Pattern.compile(
            "Content-Transfer-Encoding:\\s*base64\\s*\\n([A-Za-z0-9+/=\\s]+)", Pattern.DOTALL);
    protected static final Pattern QUOTED_PRINTABLE_PATTERN = Pattern.compile(
            "Content-Transfer-Encoding:\\s*quoted-printable\\s*\\n(.*?)\\n--", Pattern.DOTALL);
    protected static final Pattern PLAINTEXT_PATTERN = Pattern.compile(
            "Content-Type:\\s*text/plain(?:;.*?)?\\s*(?:.*\\n)*?\\n([\\s\\S]+?)(?=\\n--|$)", Pattern.DOTALL);


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
                if (response.startsWith("* OK") || response.startsWith("+OK") || response.startsWith("-ERR")) {
                    break;
                }
            }
            System.out.println("Server Greeting: " + greeting.toString().trim());
        } catch (IOException e) {
            System.err.println("Error connecting to server: " + e.getMessage());
        }
    }


    public abstract boolean authenticate(String email, String password);

    protected abstract String sendCommand(String command) throws IOException;

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

    public abstract void fetchEmails(Folder folder, ObservableList<Email> emailList);

    public abstract void logOut();

    public abstract void resetSession();

    public abstract void displayFolder(String folderName, ObservableList<Email> emailList);

    public List<Folder> getFolders() {
        return folders;
    };
    protected String extractEmailBody(String rawEmail) {
        String body = "No content";
        try {
            Matcher base64Matcher = BASE64_PATTERN.matcher(rawEmail);
            Matcher quotedPrintableMatcher = QUOTED_PRINTABLE_PATTERN.matcher(rawEmail);
            Matcher plainTextMatcher = PLAINTEXT_PATTERN.matcher(rawEmail);

            if (base64Matcher.find()) {
                body = decodeBase64(base64Matcher.group(1));
            } else if (quotedPrintableMatcher.find()) {
                QuotedPrintableCodec codec = new QuotedPrintableCodec();
                body = codec.decode(quotedPrintableMatcher.group(1));
            } else if (plainTextMatcher.find()) {
                body = MimeUtility.decodeText(plainTextMatcher.group(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body;
    }

    protected String decodeBase64(String encodedText) {
        try {
            encodedText = encodedText.replaceAll("\\s+", "");
            byte[] decodedBytes = Base64.getMimeDecoder().decode(encodedText);
            return new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return "Base64 Decoding Failed";
        }
    }

}
