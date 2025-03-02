package clientHandling;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class POP3Client extends EmailClient {

    public POP3Client(String host, int port) {
        super(host, port);
        connect();
    }

    @Override
    public boolean authenticate(String email, String password) {
        String userResponse = sendCommand(POPCommands.USER + " " + email);
        String passResponse = sendCommand(POPCommands.PASS + " " + password);

        return userResponse.startsWith("+OK") && passResponse.startsWith("+OK");
    }

}
