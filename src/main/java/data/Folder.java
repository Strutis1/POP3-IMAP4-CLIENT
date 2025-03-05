package data;

import java.util.ArrayList;
import java.util.List;

public class Folder {

    String folderName;
    List<Email> folderEmails;


    public Folder(String name){
        this.folderName = name;
        this.folderEmails = new ArrayList<Email>();
    }

    public Folder(String name, List<Email> emails) {
        this.folderName = name;
        this.folderEmails = new ArrayList<>(emails);
    }


    public List<Email> getFolderEmails() {
        return folderEmails;
    }

    public void setFolderEmails(List<Email> folderEmails) {
        this.folderEmails = folderEmails;
    }

    public void addEmail(Email email){
        this.folderEmails.add(email);
    }

    public void updateEmails(List<Email> newEmails) {
        folderEmails.clear();
        folderEmails.addAll(newEmails);
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
