package clientHandling;

public class POPCommands {
    // Authentication commands
    public static final String USER = "USER"; // Send the username (email address)
    public static final String PASS = "PASS"; // Send the password for authentication

    // Mailbox statistics
    public static final String STAT = "STAT"; // Get number of emails and mailbox size

    // Retrieving emails
    public static final String LIST = "LIST"; // List available emails
    public static final String RETR = "RETR"; // Retrieve an email by its index

    // Deleting emails
    public static final String DELE = "DELE"; // Delete an email

    // Ending session
    public static final String QUIT = "QUIT"; // Close the POP3 session

    // Optional commands
    public static final String NOOP = "NOOP"; // No operation, keeps connection alive
    public static final String RSET = "RSET"; // Resets the session, undeletes messages

    // Secure authentication (OAuth2, if needed later)
    public static final String AUTH = "AUTH"; // Used for OAuth2 authentication (e.g., AUTH XOAUTH2)
}