package com.client.pop3imap4client;

import clientHandling.EmailClient;
import clientHandling.IMAPClient;
import data.DataHolder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import clientHandling.POP3Client;

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
    private PasswordField passwordText;

    @FXML
    private TextField visiblePasswordText;

    @FXML
    private Button seePasswordButton;

    @FXML
    private Label selectedProtocol;

    @FXML
    private Label selectedProvider;


    private boolean isPasswordVisible = false;

    private EmailClient chosenClient;



    public void initialize(){
        selectedProtocol.setText(DataHolder.getInstance().getSelectedProtocol());
        selectedProvider.setText(DataHolder.getInstance().getSelectedProvider());
        exitButton.setOnAction(event -> System.exit(0));
        connectButton.setOnAction(this::checkCredentials);
        backButton.setOnAction(this::handleBack);

        visiblePasswordText.setManaged(false);
        visiblePasswordText.setVisible(false);

        visiblePasswordText.textProperty().bindBidirectional(passwordText.textProperty());

        seePasswordButton.setOnAction(this::togglePasswordVisibility);

        chosenClient = (selectedProtocol.getText().equals("POP3"))
                ? new POP3Client(DataHolder.getInstance().getHostName(), DataHolder.getInstance().getSecurePort())
                : new IMAPClient(DataHolder.getInstance().getHostName(), DataHolder.getInstance().getSecurePort());

    }

    private void togglePasswordVisibility(ActionEvent actionEvent) {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            visiblePasswordText.setText(passwordText.getText());
            visiblePasswordText.setVisible(true);
            visiblePasswordText.setManaged(true);
            passwordText.setVisible(false);
            passwordText.setManaged(false);
        } else {
            passwordText.setText(visiblePasswordText.getText());
            passwordText.setVisible(true);
            passwordText.setManaged(true);
            visiblePasswordText.setVisible(false);
            visiblePasswordText.setManaged(false);
        }
    }


    private void checkCredentials(ActionEvent actionEvent) {
        if(emailText.getText().contains("@") && emailText.getText().split("@")[1].contains(".")){
            errorText.setVisible(false);
            if(chosenClient.authenticate(emailText.getText(), passwordText.getText()))
                System.out.println("Credentials accepted");
            else{
                errorText.setText("Incorrect email or password");
                errorText.setVisible(true);
            }
        }
        else{
            errorText.setText("Invalid email address or password");
            errorText.setVisible(true);
        }
    }

    private void handleBack(ActionEvent actionEvent) {
        try {
            chosenClient.close();
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
