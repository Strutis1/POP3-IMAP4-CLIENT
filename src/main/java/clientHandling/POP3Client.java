package clientHandling;

import data.Email;

import java.util.List;

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
    public List<Email> fetchEmails() {
        return List.of();
    }


}
