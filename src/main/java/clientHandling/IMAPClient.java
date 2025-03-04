package clientHandling;

import data.Email;
import javafx.collections.ObservableList;

import java.util.List;

public class IMAPClient extends FatherEmail {


    public IMAPClient(String host, int port) {
        super(host, port);
        System.out.println("IMAPClient created");
    }

    @Override
    public boolean authenticate(String email, String password) {
        return false;
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
    }

    @Override
    public void logOut() {

    }
}
