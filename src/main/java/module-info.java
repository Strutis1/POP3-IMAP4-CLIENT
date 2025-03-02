module com.client.pop3imap4client {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.client.pop3imap4client to javafx.fxml;
    exports com.client.pop3imap4client;
    exports data;
    opens data to javafx.fxml;
}