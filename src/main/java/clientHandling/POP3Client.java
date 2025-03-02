package clientHandling;

import java.util.Base64;

public class POP3Client extends EmailClient {

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



}
