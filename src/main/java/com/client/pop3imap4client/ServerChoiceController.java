package com.client.pop3imap4client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerChoiceController {

    @FXML
    private Button gmailButton;

    @FXML
    private Button otherButton;

    @FXML
    private TextField otherProviderText;

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
        gmailButton.setOnAction(this::handleServerChoice);
        outlookButton.setOnAction(this::handleServerChoice);
        yahooButton.setOnAction(this::handleServerChoice);
        otherButton.setOnAction(this::handleServerChoice);
        otherProviderText.setOnAction(this::handleOtherProvider);
    }



    private void handleOtherProvider(ActionEvent actionEvent) {

    }

    private void handleServerChoice(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (source == gmailButton) {
            DataHolder.getInstance().setSelectedProvider("Gmail");
            openConnection(actionEvent);
        } else if (source == outlookButton) {
            DataHolder.getInstance().setSelectedProvider("Outlook");
            openConnection(actionEvent);
        } else if (source == yahooButton) {
            DataHolder.getInstance().setSelectedProvider("yahoo");
            openConnection(actionEvent);
        } else if (source == otherButton) {
            otherProviderText.setVisible(true);
//            openConnection();
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
