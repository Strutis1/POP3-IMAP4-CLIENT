package com.client.pop3imap4client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

//TODO inform what gmail is disconnecting
//TODO fix disconnecting problem stream closed when exiting
//TODO fix tableview insufficient viewing problem
//TODO when doing imap i need to rewrite the refresh function to work for the current selected folder
//TODO add a search bar for emails
//TODO add email saving on file and be able to open and read emails idek

public class EmailClient extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EmailClient.class.getResource("startScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 630, 437);
        stage.setTitle("Email Client");
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}