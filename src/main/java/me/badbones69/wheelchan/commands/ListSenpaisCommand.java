package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.objects.Senpai;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.Instant;

public class ListSenpaisCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        Guild guild = e.getGuild();
        EmbedBuilder embed = new EmbedBuilder()
        .setTitle(wheelChan.replaceEmotes("Here is a list of my current Senpais :teriwoke:", guild))
        .setColor(Color.GREEN)
        .setDescription(wheelChan.replaceEmotes(
        "**__Senpais and SP Cooldowns:__**\n" +
        getSenpaiField() +
        "\n" +
        "**__Emote Legend__**:\n" +
        ":agree: SP is available.\n" +
        ":disagree: SP still in cooldown.\n" +
        ":Bully: SP cooldown unknown.", guild))
        .setTimestamp(Instant.now());
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
    public static String getSenpaiField() {
        String senpaiList = "";
        for (Senpai senpai : wheelChan.getSenpais()) {
            senpaiList += "- " + getName(senpai) + getCooldown(senpai) + "\n";
        }
        return senpaiList;
    }
    
    private static String getName(Senpai senpai) {
        //Was getting weird NPE from this line so needed to add some checks.
        return getEmote(senpai) + " **" + (senpai != null && senpai.getUser() != null ? senpai.getUser().getName() : senpai.getCacheName()) + "**";
    }
    
    private static String getEmote(Senpai senpai) {
        return senpai.hasCooldown() ? (senpai.isCooldownOver() ? ":agree:" : ":disagree:") : ":Bully:";
    }
    
    private static String getCooldown(Senpai senpai) {
        return senpai.hasCooldown() && !senpai.isCooldownOver() ? ": `" + senpai.getCooldownString() + "`" : "";
    }
    
}