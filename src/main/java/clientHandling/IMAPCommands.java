package clientHandling;

public class IMAPCommands {
    // Authentication
    public static final String LOGIN = "LOGIN"; // LOGIN user@example.com password
    public static final String AUTHENTICATE = "AUTHENTICATE"; // AUTHENTICATE PLAIN or XOAUTH2
    public static final String LOGOUT = "LOGOUT"; // Ends session

    // Mailbox Selection
    public static final String SELECT = "SELECT"; // SELECT INBOX (switch to a mailbox)
    public static final String EXAMINE = "EXAMINE"; // Similar to SELECT but read-only

    // Fetching Messages
    public static final String FETCH = "FETCH"; // FETCH <msg_id> BODY[] (Retrieve full email)
    public static final String SEARCH = "SEARCH"; // SEARCH ALL (Find messages by criteria)
    public static final String LIST = "LIST"; // LIST "" "*" (List available mailboxes)
    public static final String STATUS = "STATUS"; // STATUS INBOX (Mailbox status)

    // Message Flags & Marking
    public static final String STORE = "STORE"; // STORE <msg_id> +FLAGS (\Seen) (Mark as read)
    public static final String EXPUNGE = "EXPUNGE"; // Removes deleted emails permanently

    // Deleting & Moving Emails
    public static final String COPY = "COPY"; // COPY <msg_id> <folder> (Copy email to another folder)
    public static final String DELETE = "STORE"; // STORE <msg_id> +FLAGS (\Deleted), then EXPUNGE

    // Optional Commands
    public static final String CAPABILITY = "CAPABILITY"; // CAPABILITY (Lists server capabilities)
    public static final String NOOP = "NOOP"; // Keeps the connection alive
}
