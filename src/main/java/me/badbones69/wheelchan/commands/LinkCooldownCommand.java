package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.Senpai;
import me.badbones69.wheelchan.api.objects.Server;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class LinkCooldownCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e, Server server) {
        User user = e.getAuthor();
        Guild guild = e.getGuild();
        EmbedBuilder embed;
        if (server.isSensei(user, guild)) {
            Message message = grabMessage(e.getMessage().getContentDisplay(), e.getChannel());
            User mentioned = message.getMentionedUsers().isEmpty() ? null : message.getMentionedUsers().get(0);
            if (message != null && message.getAuthor().isBot() && message.getAuthor().getId().equals("673362753489993749") && isSpawnPackMessage(message.getContentDisplay()) && wheelChan.isSenpai(mentioned)) {
                Senpai senpai = wheelChan.getSenpai(mentioned);
                senpai.newCooldown(message.getTimeCreated().toInstant().toEpochMilli());
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("%senpai%", "*" + mentioned.getName() + "*");
                placeholders.put("%nextredeem%", senpai.getCooldownString());
                embed = Messages.LINKED_COOLDOWN.getMessage(guild, placeholders);
            } else {
                embed = Messages.FAILED_LINKED_COOLDOWN.getMessage(guild);
            }
        } else {
            embed = Messages.NO_PERMISSION.getMessage(guild);
        }
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
    private static Message grabMessage(String command, MessageChannel channel) {
        String[] split = command.split(" ");
        if (split.length > 2) {
            try {
                return channel.retrieveMessageById(split[2]).complete();
            } catch (Exception ignore) {
            }
        }
        return null;
    }
    
    private static boolean isSpawnPackMessage(String message) {
        return message.contains("be the one to collect it!");
    }
    
}