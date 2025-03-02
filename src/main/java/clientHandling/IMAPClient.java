package clientHandling;

public class IMAPClient extends EmailClient {


    public IMAPClient(String host, int port) {
        super(host, port);
        System.out.println("IMAPClient created");
    }

    @Override
    public boolean authenticate(String email, String password) {
        return false;
    }
}
