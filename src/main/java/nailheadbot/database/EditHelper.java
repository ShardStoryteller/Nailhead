package nailheadbot.database;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditHelper {

    public static String[] editableStats = {"Firstname", "Lastname", "Gender", "SoulTrait"};

    public static void handle(MessageChannel channel, String username, String name, String stat, String value) {
        //make the connection
        try {
            //get the user's ID
            int uID = DatabaseHelper_old.lookupUserID(username);
            //get the character's owner ID
            int ownerID = DatabaseHelper_old.lookupCharOwner(name);
            //if the values match
            if (uID == ownerID) {
                //select proper stat
                switch (stat.toLowerCase()) {
                    case "firstname":
                    case "first":
                    case "name":
                    case "first_name":
                        //edit first name
                        editFirstName(channel, name, value);
                        break;
                    case "lastname":
                    case "last":
                    case "last_name":
                        //edit last name
                        editLastName(channel, name, value);
                        break;
                    case "gender":
                        //edit gender
                        editGender(channel, name, value);
                        break;
                    case "birthday":
                        //to be added later
                        channel.sendMessage("The Birthday stat is not editable at this time. Check back again later!").queue();
                        break;
                    case "soul":
                    case "soultype":
                    case "soul_type":
                        //edit soul type
                        channel.sendMessage("The Soul stat is not editable at this time. Check back again later!").queue();
                        break;
                    case "soultrait":
                    case "trait":
                    case "soulcolor":
                    case "soul_trait":
                    case "soul_color":
                        //edit soul trait
                        editTrait(channel, name, value);
                        break;
                    case "universe":
                    case "homeuniverse":
                    case "home_universe":
                        //edit home universe id
                        channel.sendMessage("The Home Universe stat is not editable at this time. Check back again later!").queue();
                        break;
                    default:
                        channel.sendMessage("Not a valid stat to edit!").queue();
                }
            } else {
                channel.sendMessage(name + " is not your character!").queue();
            }
        } catch (Exception ex) {
            channel.sendMessage("Something went wrong when connecting to the database. Sorry!").queue();
        }
    }

    public static void editFirstName(MessageChannel channel, String name, String value) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:stat_database.db");
             PreparedStatement psUpd = connection.prepareStatement("UPDATE Character SET First_Name = ? WHERE Nickname = ?")) {
            psUpd.setString(1, value);
            psUpd.setString(2, name);
            psUpd.executeUpdate();
            channel.sendMessage(name + "'s first name has been updated!").queue();
        }
    }

    public static void editLastName(MessageChannel channel, String name, String value) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:stat_database.db");
             PreparedStatement psUpd = connection.prepareStatement("UPDATE Character SET Last_Name = ? WHERE Nickname = ?")) {
            psUpd.setString(1, value);
            psUpd.setString(2, name);
            psUpd.executeUpdate();
            channel.sendMessage(name + "'s last name has been updated!").queue();
        }
    }

    public static void editGender(MessageChannel channel, String name, String value) throws SQLException {
        //send string to lowercase
        String sex = value.toLowerCase();

        //alternate spellings
        sex = switch (sex) {
            case "non-binary", "non binary" -> "nonbinary";
            case "genderfluid", "gender fluid", "gender-fluid" -> "fluid";
            default -> sex;
        };

        if (sex.equals("male") || sex.equals("female") || sex.equals("nonbinary") || sex.equals("fluid")) {
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:stat_database.db");
                 PreparedStatement psUpd = connection.prepareStatement("UPDATE Character SET Gender = ? WHERE Nickname = ?")) {
                psUpd.setString(1, sex);
                psUpd.setString(2, name);
                psUpd.executeUpdate();
                channel.sendMessage(name + "'s gender has been updated!").queue();
            }
        } else {
            channel.sendMessage("Not a valid gender option!").queue();
        }
    }

    public static void editTrait(MessageChannel channel, String name, String value) throws SQLException {
        String trait = value.toLowerCase();
        boolean valid = false;

        //make sure trait is valid
        switch (trait) {
            case "red":
                trait = "determination";
            case "determination":
                valid = true;
                break;
            case "orange":
                trait = "bravery";
            case "bravery":
                valid = true;
                break;
            case "yellow":
                trait = "justice";
            case "justice":
                valid = true;
                break;
            case "lime":
                trait = "fairness";
            case "fairness":
                valid = true;
                break;
            case "green":
                trait = "kindness";
            case "kindness":
                valid = true;
                break;
            case "aqua":
            case "turquoise":
            case "teal":
                trait = "clemency";
            case "clemency":
                valid = true;
                break;
            case "cyan":
            case "light_blue":
            case "light blue":
            case "lightblue":
            case "light-blue":
                trait = "patience";
            case "patience":
                valid = true;
                break;
            case "blue":
                trait = "humility";
            case "humility":
                valid = true;
                break;
            case "indigo":
            case "dark_blue":
            case "dark blue":
            case "darkblue":
            case "dark-blue":
                trait = "integrity";
            case "integrity":
                valid = true;
                break;
            case "purple":
            case "darkpurple":
            case "dark purple":
            case "dark-purple":
            case "dark_purple":
                trait = "trust";
            case "trust":
                valid = true;
                break;
            case "magenta":
            case "light_purple":
            case "light purple":
            case "light-purple":
            case "lightpurple":
                trait = "perseverance";
            case "perseverance":
                valid = true;
                break;
            case "pink":
            case "hotpink":
            case "hot pink":
            case "hot-pink":
            case "hot_pink":
                trait = "dedication";
            case "dedication":
                valid = true;
                break;
            case "white":
                trait = "purity";
            case "purity":
                valid = true;
                break;
            case "black":
            case "dark":
                trait = "hatred";
            case "hatred":
                valid = true;
                break;
            case "gray":
            case "blank":
                trait = "neutrality";
            case "neutrality":
                valid = true;
                break;
        }

        //only if a valid trait
        if (valid) {
            try (Connection connection = DriverManager.getConnection("jdbc:sqlite:stat_database.db");
                 PreparedStatement psUpd = connection.prepareStatement("UPDATE Character SET Soul_Trait = ? WHERE Nickname = ?")) {
                psUpd.setString(1, trait);
                psUpd.setString(2, name);

                psUpd.executeUpdate();
                channel.sendMessage(name + "'s trait has been updated!").queue();
            }
        } else {
            channel.sendMessage("Not a valid trait!").queue();
        }
    }
}
