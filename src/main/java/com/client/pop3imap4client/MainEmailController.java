package com.client.pop3imap4client;

import clientHandling.FatherEmail;
import clientHandling.POPCommands;
import data.DataHolder;
import data.Email;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class MainEmailController {

    @FXML
    private Button backButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Email> emailTable;

    @FXML
    private Button exitButton;

    @FXML
    private ListView<String> folderList;

    @FXML
    private TableColumn<Email, Integer> idColumn;

    @FXML
    private Label previewEmailSender;

    @FXML
    private Button refreshButton;

    @FXML
    private TextArea selectedEmailPreview;

    @FXML
    private Label selectedProtocol;

    @FXML
    private Label selectedProvider;

    @FXML
    private TableColumn<Email, String> senderColumn;

    @FXML
    private TableColumn<Email, String> sizeColumn;

    @FXML
    private TableColumn<Email, String> subjectColumn;

    @FXML
    private TableColumn<Email, String> typeColumn;

    @FXML
    private TableColumn<Email, String> companyColumn;

    FatherEmail chosenClient;

    private ObservableList<Email> emailList = FXCollections.observableArrayList();

//TODO need to handle folder list

    public void initialize(FatherEmail client) {
        this.chosenClient = client;

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        companyColumn.setCellValueFactory(cellDate -> cellDate.getValue().companyProperty());
        typeColumn.setCellValueFactory(cellDate -> cellDate.getValue().typeProperty());
        senderColumn.setCellValueFactory(cellData -> cellData.getValue().senderProperty());
        subjectColumn.setCellValueFactory(cellData -> cellData.getValue().subjectProperty());
        sizeColumn.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());

        selectedProvider.setText(DataHolder.getInstance().getSelectedProvider());
        selectedProtocol.setText(DataHolder.getInstance().getSelectedProtocol());

        refreshButton.setOnAction(this::handleRefresh);
        backButton.setOnAction(this::handleBack);
        exitButton.setOnAction(this::handleExit);
        deleteButton.setOnAction(this::handleDelete);

        folderList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null){
                showFolderEmails(newValue);
            }
        });


        //bind
        emailTable.setItems(emailList);

        emailTable.getSelectionModel().selectedItemProperty().addListener((obs, oldEmail, newEmail) -> {
            if (newEmail != null) {
                showEmailDetails(newEmail);
                deleteButton.setDisable(true);
            }else{
                deleteButton.setDisable(false);
            }
        });
        loadInbox();
    }

    private void showFolderEmails(String newValue) {

    }


    private void loadInbox() {
        new Thread(() -> {
            chosenClient.fetchEmails(emailList);

        }).start();
    }

    private void showEmailDetails(Email email) {
        String fullMessage = chosenClient.retrieveEmail(email.getId());
        selectedEmailPreview.setText(fullMessage);
        previewEmailSender.setText(email.getSender());
    }


    private void handleDelete(ActionEvent actionEvent) {
        Email selectedEmail = emailTable.getSelectionModel().getSelectedItem();
        if (selectedEmail != null) {
            chosenClient.deleteEmail(selectedEmail.getId());
            loadInbox();
        }
    }


    private void handleRefresh(ActionEvent actionEvent) {
        loadInbox();
    }

    private void handleExit(ActionEvent actionEvent) {
        chosenClient.close();
        System.exit(0);
    }

    private void handleBack(ActionEvent actionEvent){
        //log out client
        try {
            chosenClient.logOut();
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
