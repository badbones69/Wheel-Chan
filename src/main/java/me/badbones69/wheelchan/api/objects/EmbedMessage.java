package me.badbones69.wheelchan.api.objects;

import me.badbones69.wheelchan.api.FileManager.Files;
import me.badbones69.wheelchan.api.enums.Messages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class EmbedMessage {
    
    private String name;
    private String title;
    private String titleURL;
    private String author;
    private String authorURL;
    private String imageURL;
    private String thumbnailURL;
    private Color embedColor;
    private String description;
    private String footer;
    private String footerURL;
    
    public EmbedMessage() {
        this.name = "";
        this.title = "";
        this.titleURL = "";
        this.author = "";
        this.authorURL = "";
        this.imageURL = "";
        this.thumbnailURL = "";
        this.embedColor = Color.YELLOW;
        this.description = "";
        this.footer = "";
        this.footerURL = "";
    }
    
    public EmbedMessage(String name) {
        FileConfiguration config = Files.CONFIG.getFile();
        String path = "Messages." + name + ".";
        this.name = name;
        this.title = config.getString(path + "Title", "");
        this.titleURL = config.getString(path + "Title-URL", "");
        this.author = config.getString(path + "Author", "");
        this.authorURL = config.getString(path + "Author-URL", "");
        this.imageURL = config.getString(path + "Image-URL", "");
        this.thumbnailURL = config.getString(path + "Thumbnail-URL", "");
        this.embedColor = getColor(config.getString(path + "Embed-Color", "yellow"));
        this.description = Messages.convertList(config.getStringList(path + "Description"));
        this.footer = config.getString(path + "Footer", "");
        this.footerURL = config.getString(path + "Footer-URL", "");
    }
    
    public String getName() {
        return name;
    }
    
    public EmbedMessage setName(String name) {
        this.name = name;
        return this;
    }
    
    public String getTitle() {
        return title;
    }
    
    public EmbedMessage setTitle(String title) {
        this.title = title;
        return this;
    }
    
    public String getTitleURL() {
        return titleURL;
    }
    
    public EmbedMessage setTitleURL(String titleURL) {
        this.titleURL = titleURL;
        return this;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public EmbedMessage setAuthor(String author) {
        this.author = author;
        return this;
    }
    
    public String getAuthorURL() {
        return authorURL;
    }
    
    public EmbedMessage setAuthorURL(String authorURL) {
        this.authorURL = authorURL;
        return this;
    }
    
    public String getImageURL() {
        return imageURL;
    }
    
    public EmbedMessage setImageURL(String imageURL) {
        this.imageURL = imageURL;
        return this;
    }
    
    public String getThumbnailURL() {
        return thumbnailURL;
    }
    
    public EmbedMessage setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
        return this;
    }
    
    public Color getEmbedColor() {
        return embedColor;
    }
    
    public EmbedMessage setEmbedColor(Color embedColor) {
        this.embedColor = embedColor;
        return this;
    }
    
    public EmbedMessage setEmbedColor(String embedColor) {
        this.embedColor = getColor(embedColor);
        return this;
    }
    
    public String getDescription() {
        return description;
    }
    
    public EmbedMessage setDescription(String description) {
        this.description = description;
        return this;
    }
    
    public String getFooter() {
        return footer;
    }
    
    public EmbedMessage setFooter(String footer) {
        this.footer = footer;
        return this;
    }
    
    public String getFooterURL() {
        return footerURL;
    }
    
    public EmbedMessage setFooterURL(String footerURL) {
        this.footerURL = footerURL;
        return this;
    }
    
    public EmbedBuilder getEmbedMessage(Guild guild) {
        return getEmbedMessage(guild, new HashMap<>());
    }
    
    public EmbedBuilder getEmbedMessage(Guild guild, Map<String, String> placeholders) {
        Map<String, String> newPlaceholders = new HashMap<>();
        for (String placeholder : placeholders.keySet()) {
            newPlaceholders.put(placeholder, replaceEmotes(placeholders.get(placeholder), guild));
        }
        EmbedBuilder embed = new EmbedBuilder();
        if (embedColor != null) {
            embed.setColor(embedColor);
        }
        if (description != null && !description.isEmpty()) {
            embed.setDescription(Messages.replacePlaceholders(newPlaceholders, replaceEmotes(description, guild)));
        }
        if (footer != null && !footer.isEmpty()) {
            if (footerURL != null && !footerURL.isEmpty()) {
                embed.setFooter(Messages.replacePlaceholders(newPlaceholders, replaceEmotes(footer, guild)), footerURL);
            } else {
                embed.setFooter(Messages.replacePlaceholders(newPlaceholders, replaceEmotes(footer, guild)));
            }
        }
        if (title != null && !title.isEmpty()) {
            if (titleURL != null && !titleURL.isEmpty()) {
                embed.setTitle(Messages.replacePlaceholders(newPlaceholders, replaceEmotes(title, guild)), titleURL);
            } else {
                embed.setTitle(Messages.replacePlaceholders(newPlaceholders, replaceEmotes(title, guild)));
            }
        }
        if (author != null && !author.isEmpty()) {
            if (authorURL != null && !authorURL.isEmpty()) {
                embed.setAuthor(author, authorURL);
            } else {
                embed.setAuthor(author);
            }
        }
        if (imageURL != null && !imageURL.isEmpty()) {
            embed.setImage(imageURL);
        }
        if (thumbnailURL != null && !thumbnailURL.isEmpty()) {
            embed.setThumbnail(thumbnailURL);
        }
        return embed;
    }
    
    private String replaceEmotes(String message, Guild guild) {
        if (message.contains(":")) {
            // - ':emote:'
            // - 'Hello :emote:'
            // - ':emote: Hello'
            // - 'Hello :emote: lol'
            for (Emote emote : guild.getEmotes()) {
                if (message.contains(":" + emote.getName() + ":")) {
                    message = message.replace(":" + emote.getName() + ":", emote.getAsMention());
                }
            }
        }
        return message;
    }
    
    private Color getColor(String color) {
        if (color != null) {
            switch (color.toLowerCase()) {
                case "black":
                    return Color.BLACK;
                case "magenta":
                    return Color.MAGENTA;
                case "cyan":
                    return Color.CYAN;
                case "dark_gray":
                    return Color.DARK_GRAY;
                case "light_gray":
                    return Color.LIGHT_GRAY;
                case "pink":
                    return Color.PINK;
                case "blue":
                    return Color.BLUE;
                case "gray":
                    return Color.GRAY;
                case "green":
                    return Color.GREEN;
                case "orange":
                    return Color.ORANGE;
                case "red":
                    return Color.RED;
                case "white":
                    return Color.WHITE;
                case "yellow":
                    return Color.YELLOW;
            }
        }
        return Color.YELLOW;
    }
    
}