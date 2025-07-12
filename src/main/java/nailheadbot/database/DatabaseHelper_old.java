package nailheadbot.database;

import nailheadbot.EmbedHelper;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DatabaseHelper_old {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseHelper_old.class);
    private static final String url = "jdbc:sqlite:stat_database.db";
    private static final String[] fields = {
            "First Name",
            "Last Name",
            "Nickname",
            "Birthday",
            "Gender",
            "Soul Type",
            "Soul Trait",
            "Home Universe ID",
            "Owner"
    };

    public static String[] lookupCharHandle(String value) {
        String[] output = new String[9];

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement psLookup = connection.prepareStatement("SELECT * FROM Character WHERE Nickname = ?")) {
            psLookup.setString(1, value);
            ResultSet rsLookup = psLookup.executeQuery();

            output[0] = replaceNull(rsLookup.getString("First_Name"));
            output[1] = replaceNull(rsLookup.getString("Last_Name"));
            output[2] = replaceNull(rsLookup.getString("Nickname"));
            output[3] = replaceNull(rsLookup.getString("Birthday"));
            output[4] = replaceNull(rsLookup.getString("Gender"));
            output[5] = replaceNull(rsLookup.getString("Soul_Type"));
            output[6] = replaceNull(rsLookup.getString("Soul_Trait"));
            output[7] = replaceNull(rsLookup.getString("Home_Universe_ID"));
            output[8] = replaceNull(rsLookup.getString("Owner"));

            return output;
        } catch (Exception ex) {
            logger.error("Failed to connect to the database", ex);
            return new String[0];
        }
    }

    public static int lookupCharOwner(String value) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement psLookup = connection.prepareStatement("SELECT Owner FROM Character WHERE Nickname = ?")) {
            psLookup.setString(1, value);
            ResultSet rsLookup = psLookup.executeQuery();

            int output = rsLookup.getInt("Owner");

            return output % 64;
        } catch (Exception ex) {
            logger.error("Failed to connect to the database", ex);
            return -1;
        }
    }

    public static int lookupUserID(String username) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement psLookup = connection.prepareStatement("SELECT * FROM Storyteller WHERE Disc_Username = ?")) {
            psLookup.setString(1, username);
            ResultSet rsLookup = psLookup.executeQuery();

            int output = rsLookup.getInt("Story_IDX");

            return output % 64;
        } catch (Exception ex) {
            logger.error("Failed to connect to the database", ex);
            return -1;
        }
    }

    public static String lookupUser(int id) {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement psLookup = connection.prepareStatement("SELECT * FROM Storyteller WHERE Story_IDX = ?")) {
            psLookup.setInt(1, id);
            ResultSet rsLookup = psLookup.executeQuery();

            return rsLookup.getString("Disc_Username");
        } catch (Exception ex) {
            logger.error("Failed to connect to the database", ex);
            return null;
        }
    }

    public static void editHandle(MessageReceivedEvent event) {
        //get username
        String username = event.getAuthor().getName();
        //get channel
        MessageChannel channel = event.getChannel();
        //get message contents
        String msg = event.getMessage().getContentRaw();
        //split into components
        String[] components = msg.split(" ", 4);
        if (components.length < 4) {
            channel.sendMessage("Invalid syntax!").queue();
        } else {
            //name the components
            String name = components[1];
            String stat = components[2];
            String value = components[3];
            //pass to edit handler
            EditHelper.handle(channel, username, name, stat, value);
        }
    }

    public static void addCharHandle(MessageChannel channel, String msg, String username) {
        String[] components = msg.split(" ");
        String name;
        String lastName = null;

        int uID = lookupUserID(username);

        //based on amount of names added
        switch (components.length) {
            //first and last name
            case 3:
                lastName = components[2];
                //first name only
            case 2:
                name = components[1];

                String[] charFields = lookupCharHandle(name);
                String nick = charFields[2];
                if (nick.equals("Null")) {
                    try {
                        addCharHelp(name, lastName, uID);
                        channel.sendMessage(name + " added to the database!").queue();
                    } catch (SQLException ex) {
                        channel.sendMessage("Something went wrong when connecting to the database. Sorry!").queue();
                        logger.error("Failed to connect to the database", ex);
                    }
                } else {
                    channel.sendMessage("A character named " + nick + " is already in the database!").queue();
                }
                break;
            default:
                channel.sendMessage("Invalid syntax!").queue();
                break;
        }
    }

    public static void addCharHelp(String name, String lastName, int uID) throws SQLException {
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement psIns = connection.prepareStatement("INSERT INTO Character (First_Name, Last_Name, Nickname, Owner) VALUES (?,?,?,?)")) {
            //insert as first name
            psIns.setString(1, name);
            //insert as last name
            psIns.setString(2, lastName);
            //insert as nickname
            psIns.setString(3, name);
            //insert owner id
            psIns.setInt(4, uID);

            psIns.executeUpdate();
        }
    }

    public static void getStatHandle(MessageChannel channel, String msg) {
        String name = msg.split(" ", 2)[1];
        try {

            String[] values = lookupCharHandle(name);
            int ownerID = lookupCharOwner(name);
            String owner = lookupUser(ownerID);

            String firstName = values[0];

            if (firstName.equals("Null")) {
                channel.sendMessage("I can't find a character named " + name + " in the database!").queue();
            } else {
                values[8] = owner;
                EmbedHelper e = new EmbedHelper(channel, "Stat Lookup", fields, values);
                e.handleValues();
            }
        } catch (Exception ex) {
            channel.sendMessage("Something went wrong when connecting to the database. Sorry!").queue();
            logger.error("Failed to connect to the database", ex);
        }
    }

    public static String replaceNull(String str) {
        if (str == null) return "Null";
        return str;
    }
}
