package com.client.pop3imap4client;

import data.DataHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerChoiceController {

    @FXML
    private Button gmailButton;

    @FXML
    private Button backButton;

    @FXML
    private Button outlookButton;

    @FXML
    private Label selectedProtocol;

    @FXML
    private Button yahooButton;

    @FXML
    private Button exitButton;


    public void initialize(){
        selectedProtocol.setText(DataHolder.getInstance().getSelectedProtocol());
        exitButton.setOnAction(event -> System.exit(0));
        backButton.setOnAction(this::handleBack);
        gmailButton.setOnAction(this::handleServerChoice);
        outlookButton.setOnAction(this::handleServerChoice);
        yahooButton.setOnAction(this::handleServerChoice);
    }

    private void handleBack(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("startScreen.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void handleOtherProvider(ActionEvent actionEvent) {

    }

    private void handleServerChoice(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source == gmailButton) {
            DataHolder.getInstance().setSelectedProvider("Gmail");
            if(DataHolder.getInstance().getSelectedProtocol().equals("IMAP4")) {
                DataHolder.getInstance().setHostName("imap.gmail.com");
            }
            else if(DataHolder.getInstance().getSelectedProtocol().equals("POP3")) {
                DataHolder.getInstance().setHostName("pop.gmail.com");
            }
            openConnection(actionEvent);
        } else if (source == outlookButton) {
            DataHolder.getInstance().setSelectedProvider("Outlook");
            if(DataHolder.getInstance().getSelectedProtocol().equals("IMAP4")){
                DataHolder.getInstance().setHostName("imap-mail.outlook.com");
            }
            else if(DataHolder.getInstance().getSelectedProtocol().equals("POP3")) {
                DataHolder.getInstance().setHostName("outlook.office365.com");
            }
            openConnection(actionEvent);
        } else if (source == yahooButton) {
            DataHolder.getInstance().setSelectedProvider("yahoo");
            if(DataHolder.getInstance().getSelectedProtocol().equals("IMAP4")){
                DataHolder.getInstance().setHostName("imap.mail.yahoo.com");
            }
            else if(DataHolder.getInstance().getSelectedProtocol().equals("POP3")) {
                DataHolder.getInstance().setHostName("pop.mail.yahoo.com");
            }
            openConnection(actionEvent);
        }
    }

    private void openConnection(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("connectionScreen.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
