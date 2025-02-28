package com.client.pop3imap4client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class ConnectionController {

    @FXML
    private TextField emailText;

    @FXML
    private Label errorText;

    @FXML
    private Button exitButton;


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
    }

}
