package me.badbones69.wheelchan.api.enums;

import me.badbones69.wheelchan.api.FileManager.Files;
import me.badbones69.wheelchan.api.objects.EmbedMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Messages {
    
    RELOAD("Reload"),
    ADD_SENPIE("Add-Senpai"),
    ADD_SENPIE_DESCRIPTION("Add-Senpai-Description"),
    REMOVE_SENPIE("Remove-Senpai"),
    REMOVE_SENPIE_DESCRIPTION("Remove-Senpai-Description"),
    ALREADY_MY_SENPIE("Already-My-Senpai"),
    NOT_MY_SENPIE("Not-My-Senpai"),
    ADD_SENSEI("Add-Sensei"),
    ADD_SENSEI_DESCRIPTION("Add-Sensei-Description"),
    REMOVE_SENSEI("Remove-Sensei"),
    REMOVE_SENSEI_DESCRIPTION("Remove-Sensei-Description"),
    ALREADY_MY_SENSEI("Already-My-Sensei"),
    NOT_MY_SENSEI("Not-My-Sensei"),
    NO_PERMISSION("No-Permission"),
    CLEAR_SENPAIS("Clear-Senpais"),
    ADD_COMMAND_CHANNEL("Add-Command-Channel"),
    REMOVE_COMMAND_CHANNEL("Remove-Command-Channel"),
    ALREADY_COMMAND_CHANNEL("Already-Command-Channel"),
    NOT_COMMAND_CHANNEL("Not-Command-Channel"),
    LINKED_COOLDOWN("Linked-Cooldown"),
    FAILED_LINKED_COOLDOWN("Failed-Linked-Cooldown"),
    LIST_SENPAIS("List-Senpais"),
    LIST_SENSEIS("List-Senseis"),
    SPIN_WHEEL("Spin-Wheel"),
    TRACKER_STATS("Tracker-Stats"),
    TRACKER_MISSED("Tracker-Missed"),
    HELP("Help");
    
    private String path;
    private static Map<Messages, EmbedMessage> messageCache = new HashMap<>();
    
    public static void loadMessages() {
        for (Messages message : values()) {
            messageCache.put(message, new EmbedMessage(message.getPath()));
        }
    }
    
    private Messages(String path) {
        this.path = path;
    }
    
    public static String convertList(List<String> list) {
        String message = "";
        for (String line : list) {
            message += line + "\n";
        }
        return message;
    }
    
    public static String replacePlaceholders(String placeholder, String replacement, String message) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(placeholder, replacement);
        return replacePlaceholders(placeholders, message);
    }
    
    public static String replacePlaceholders(Map<String, String> placeholders, String message) {
        for (String placeholder : placeholders.keySet()) {
            message = message.replaceAll(placeholder, placeholders.get(placeholder))
            .replaceAll(placeholder.toLowerCase(), placeholders.get(placeholder));
        }
        return message;
    }
    
    public static List<String> replacePlaceholders(String placeholder, String replacement, List<String> messageList) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put(placeholder, replacement);
        return replacePlaceholders(placeholders, messageList);
    }
    
    public static List<String> replacePlaceholders(Map<String, String> placeholders, List<String> messageList) {
        List<String> newMessageList = new ArrayList<>();
        for (String message : messageList) {
            for (String placeholder : placeholders.keySet()) {
                newMessageList.add(message.replaceAll(placeholder, placeholders.get(placeholder))
                .replaceAll(placeholder.toLowerCase(), placeholders.get(placeholder)));
            }
        }
        return newMessageList;
    }
    
    public EmbedBuilder getMessage(Guild guild) {
        return getMessage(guild, new HashMap<>());
    }
    
    public EmbedBuilder getMessage(Guild guild, String placeholder, List<String> replacement) {
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put(placeholder, convertList(replacement));
        return getMessage(guild, placeholders);
    }
    
    public EmbedBuilder getMessage(Guild guild, String placeholder, String replacement) {
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put(placeholder, replacement);
        return getMessage(guild, placeholders);
    }
    
    public EmbedBuilder getMessage(Guild guild, Map<String, String> placeholders) {
        return messageCache.get(this).getEmbedMessage(guild, placeholders);
    }
    
    private boolean exists() {
        return Files.CONFIG.getFile().contains("Messages." + path);
    }
    
    private String getPath() {
        return path;
    }
    
}