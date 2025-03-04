package com.client.pop3imap4client;

import clientHandling.FatherEmail;
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
    private PasswordField appPassword;

    @FXML
    private TextField visiblePasswordText;

    @FXML
    private Button seePasswordButton;

    @FXML
    private Label selectedProtocol;

    @FXML
    private Label selectedProvider;


    private boolean isPasswordVisible = false;

    private FatherEmail chosenClient;

    private String currentHost = null;



    public void initialize(){
        selectedProtocol.setText(DataHolder.getInstance().getSelectedProtocol());
        selectedProvider.setText(DataHolder.getInstance().getSelectedProvider());
        exitButton.setOnAction(event -> System.exit(0));
        connectButton.setOnAction(this::checkCredentials);
        backButton.setOnAction(this::handleBack);

        visiblePasswordText.setManaged(false);
        visiblePasswordText.setVisible(false);

        visiblePasswordText.textProperty().bindBidirectional(appPassword.textProperty());

        seePasswordButton.setOnAction(this::togglePasswordVisibility);

        String newHost = DataHolder.getInstance().getHostName();

        if (chosenClient != null && currentHost != null && currentHost.equals(newHost)) {
            System.out.println("Reusing existing connection to " + currentHost);
        } else {
            if (chosenClient != null) {
                chosenClient.close();  // Close the previous connection if switching provider
            }
            chosenClient = (selectedProtocol.getText().equals("POP3"))
                    ? new POP3Client(newHost, DataHolder.getInstance().getSecurePort())
                    : new IMAPClient(newHost, DataHolder.getInstance().getSecurePort());

            currentHost = newHost;  // Update stored host
        }

    }

    private void togglePasswordVisibility(ActionEvent actionEvent) {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            visiblePasswordText.setText(appPassword.getText());
            visiblePasswordText.setVisible(true);
            visiblePasswordText.setManaged(true);
            appPassword.setVisible(false);
            appPassword.setManaged(false);
        } else {
            appPassword.setText(visiblePasswordText.getText());
            appPassword.setVisible(true);
            appPassword.setManaged(true);
            visiblePasswordText.setVisible(false);
            visiblePasswordText.setManaged(false);
        }
    }


    private void checkCredentials(ActionEvent actionEvent) {
        if(emailText.getText().contains("@") && emailText.getText().split("@")[1].contains(".")){
            errorText.setVisible(false);
            if(chosenClient.authenticate(emailText.getText(), appPassword.getText())) {
                System.out.println("Credentials accepted");
                connectClient(actionEvent);
            }

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

    private void connectClient(ActionEvent actionEvent) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainEmailScreen.fxml"));
            Parent root = loader.load();

            MainEmailController controller = loader.getController();
            controller.initialize(chosenClient);

            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(new Scene(root));

            stage.centerOnScreen();

            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleBack(ActionEvent actionEvent) {
        try {
            chosenClient.logOut();
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
