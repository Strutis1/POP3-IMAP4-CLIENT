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

public class ConnectionController {

    @FXML
    private TextField emailText;

    @FXML
    private Label errorText;

    @FXML
    private Button exitButton;

    @FXML
    private Button backButton;

    @FXML
    private Button connectButton;

    @FXML
    private TextField passwordText;

    @FXML
    private Button seePasswordButton;

    @FXML
    private Label selectedProtocol;

    @FXML
    private Label selectedProvider;




    public void initialize(){
        selectedProtocol.setText(DataHolder.getInstance().getSelectedProtocol());
        selectedProvider.setText(DataHolder.getInstance().getSelectedProvider());
        exitButton.setOnAction(event -> System.exit(0));
        connectButton.setOnAction(this::checkCredentials);
        backButton.setOnAction(this::handleBack);
    }



    private void checkCredentials(ActionEvent actionEvent) {

    }

    private void handleBack(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("serverChoice.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
