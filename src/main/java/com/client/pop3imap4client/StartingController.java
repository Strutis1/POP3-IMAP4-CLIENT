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

public class StartingController {

    @FXML
    private Button exitButton;

    @FXML
    private Button imapButton;

    @FXML
    private Button popButton;




    public void initialize(){
        exitButton.setOnAction(event -> System.exit(0));
        imapButton.setOnAction(this::handleProtocol);
        popButton.setOnAction(this::handleProtocol);
    }



    private void handleProtocol(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        if(source == popButton) {
            DataHolder.getInstance().setSelectedProtocol("POP3");
        }
        else if(source == imapButton) {
            DataHolder.getInstance().setSelectedProtocol("IMAP4");
        }
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
