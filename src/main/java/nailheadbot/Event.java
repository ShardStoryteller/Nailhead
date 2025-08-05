package nailheadbot;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.requests.RestAction;

public class Event {
    public User creator;
    public Guild guild;
    public MessageChannel channel;
    public String eventName;
    public Role notifyRole;

    public Event (User creator, Guild guild, MessageChannel channel, String eventName, String roleName) {
        this.creator = creator;
        this.guild = guild;
        this.channel = channel;
        this.eventName = eventName;

        Role targetRole = guild.getRolesByName(roleName, true).stream().findFirst().orElse(null);
        if (targetRole == null) {
            RestAction<Role> roleAction = guild.createRole()
                    .setName(roleName)
                    .setHoisted(false)
                    .setMentionable(true);
            roleAction.queue();
        }
        this.notifyRole = guild.getRolesByName(roleName, true).stream().findFirst().orElse(null);
    }
}