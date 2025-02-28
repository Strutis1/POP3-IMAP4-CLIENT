package com.client.pop3imap4client;

public class DataHolder {

    private String selectedProvider;
    private String selectedProtocol;
    private static final DataHolder INSTANCE = new DataHolder();

    private DataHolder() {}

    public static DataHolder getInstance() {
        return INSTANCE;
    }

    public String getSelectedProtocol() {
        return selectedProtocol;
    }

    public void setSelectedProtocol(String selectedProtocol) {
        this.selectedProtocol = selectedProtocol;
    }

    public String getSelectedProvider() {
        return selectedProvider;
    }

    public void setSelectedProvider(String selectedProvider) {
        this.selectedProvider = selectedProvider;
    }

}
