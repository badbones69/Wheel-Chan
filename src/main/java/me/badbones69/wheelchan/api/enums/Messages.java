package me.badbones69.wheelchan.api.enums;

import me.badbones69.wheelchan.api.FileManager.Files;
import me.badbones69.wheelchan.api.objects.EmbedMessage;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Messages {
    
    RELOAD("Reload"),
    ADD_SENPIE("Add-Senpie"),
    ADD_SENPIE_DESCRIPTION("Add-Senpie-Description"),
    REMOVE_SENPIE("Remove-Senpie"),
    REMOVE_SENPIE_DESCRIPTION("Remove-Senpie-Description"),
    ALREADY_MY_SENPIE("Already-My-Senpie"),
    NOT_MY_SENPIE("Not-My-Senpie"),
    ADD_COMMAND_CHANNEL("Add-Command-Channel"),
    REMOVE_COMMAND_CHANNEL("Remove-Command-Channel"),
    ALREADY_COMMAND_CHANNEL("Already-Command-Channel"),
    NOT_COMMAND_CHANNEL("Not-Command-Channel"),
    LIST_SENPAIS("List-Senpais"),
    SPIN_WHEEL("Spin-Wheel");
    
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
    
    public EmbedBuilder getMessage() {
        return getMessage(true);
    }
    
    public EmbedBuilder getMessage(String placeholder, List<String> replacement) {
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put(placeholder, convertList(replacement));
        return getMessage(placeholders, true);
    }
    
    public EmbedBuilder getMessage(String placeholder, String replacement) {
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put(placeholder, replacement);
        return getMessage(placeholders, true);
    }
    
    public EmbedBuilder getMessage(Map<String, String> placeholders) {
        return getMessage(placeholders, true);
    }
    
    private EmbedBuilder getMessage(boolean prefix) {
        return getMessage(new HashMap<>(), prefix);
    }
    
    private EmbedBuilder getMessage(Map<String, String> placeholders, boolean prefix) {
        return messageCache.get(this).getEmbedMessage(placeholders);
    }
    
    private boolean exists() {
        return Files.CONFIG.getFile().contains("Messages." + path);
    }
    
    private String getPath() {
        return path;
    }
    
}