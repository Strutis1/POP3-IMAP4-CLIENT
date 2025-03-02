package data;

public class DataHolder {

    private String selectedProvider;
    private String selectedProtocol;
    private String hostName;
    private int securePort;
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

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getSecurePort() {
        return securePort;
    }

    public void setSecurePort(int securePort) {
        this.securePort = securePort;
    }

}
