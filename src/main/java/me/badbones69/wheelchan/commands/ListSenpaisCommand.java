package me.badbones69.wheelchan.commands;

import me.badbones69.wheelchan.api.WheelChan;
import me.badbones69.wheelchan.api.objects.Senpai;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.time.Instant;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class ListSenpaisCommand {
    
    private static WheelChan wheelChan = WheelChan.getInstance();
    
    public static void runCommand(MessageReceivedEvent e) {
        Guild guild = e.getGuild();
        Field[] senpaiFields = getSenpaiField(guild);
        EmbedBuilder embed = new EmbedBuilder()
        .setTitle(wheelChan.replaceEmotes("Here is a list of my current Senpais :teriwoke:", guild))
        .setColor(Color.GREEN)
        .addField(senpaiFields[0])
        .addField(senpaiFields[1])
        .addField("",
        wheelChan.replaceEmotes(
        "Legend:\n" +
        ":agree: Spawn Pack is available.\n" +
        ":disagree: Spawn Pack still in cooldown.\n" +
        ":Bully: Spawn Pack cooldown unknown.", guild), false)
        .setTimestamp(Instant.now());
        e.getChannel().sendMessage(embed.build()).complete();
    }
    
    public static Field[] getSenpaiField(Guild guild) {
        Map<String, String> senpais = wheelChan.getSenpais().stream().collect(Collectors.toMap(ListSenpaisCommand :: getCooldown, ListSenpaisCommand :: getName, (a, b) -> b));
        TestCommand.sortByValue(senpais, false);
        String senpaiNames = "";
        String senpaiCooldowns = "";
        for (Entry<String, String> entry : senpais.entrySet()) {
            senpaiNames += entry.getValue() + "\n";
            senpaiCooldowns += entry.getKey() + "\n";
        }
        return new Field[] {new Field("**__Senpais:__**", wheelChan.replaceEmotes(senpaiNames, guild), true), new Field("**__Spawn Pack Cooldowns:__**", wheelChan.replaceEmotes(senpaiCooldowns, guild), true)};
    }
    
    private static String getName(Senpai senpai) {
        //Was getting weird NPE from this line so needed to add some checks.
        return "`" + (senpai != null && senpai.getUser() != null ? senpai.getUser().getName() : senpai.getCacheName()) + "`";
    }
    
    private static String getCooldown(Senpai senpai) {
        String cooldownString;
        if (senpai.hasCooldown()) {
            if (senpai.isCooldownOver()) {
                cooldownString = ":agree:";
            } else {
                cooldownString = ":disagree: `" + senpai.getCooldownString() + "`";
            }
        } else {
            cooldownString = ":Bully:";
        }
        return cooldownString;
    }
    
}