package clientHandling;

import java.util.Base64;

public class POP3Client extends EmailClient {

    public POP3Client(String host, int port) {
        super(host, port);
        connect();
    }

    @Override
    public boolean authenticate(String email, String accessCode) {
        try {
            String authString = "user=" + email + "\u0001auth=Bearer " + accessCode + "\u0001\u0001";
            String base64Auth = Base64.getEncoder().encodeToString(authString.getBytes());

            String response = sendCommand(POPCommands.AUTH + " XOAUTH2 " + base64Auth);

            return response.startsWith("+OK");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
