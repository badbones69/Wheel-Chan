package me.badbones69.wheelchan.api;

import me.badbones69.wheelchan.api.FileManager.Files;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.Senpai;
import me.badbones69.wheelchan.api.objects.Sensei;
import me.badbones69.wheelchan.listeners.CommandListener;
import me.badbones69.wheelchan.listeners.SpawnPackListener;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.simpleyaml.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WheelChan {
    
    private static WheelChan instance = new WheelChan();
    private JDA jda;
    private List<Sensei> senseis = new ArrayList<>();
    private List<Senpai> senpais = new ArrayList<>();
    private List<String> commandChannels = new ArrayList<>();
    
    public static WheelChan getInstance() {
        return instance;
    }
    
    public void load() {
        loadJDA();
        Messages.loadMessages();
        senseis.clear();
        senpais.clear();
        commandChannels.clear();
        FileConfiguration data = Files.DATA.getFile();
        data.getStringList("Senseis").forEach(id -> senseis.add(new Sensei(id)));
        if (data.contains("Senpais")) {
            if (data.getStringList("Senpais").isEmpty()) {
                for (String senpaiID : data.getConfigurationSection("Senpais").getKeys(false)) {
                    senpais.add(new Senpai(senpaiID, data.getLong("Senpais." + senpaiID + ".Cooldown")));
                }
            }
        }
        commandChannels.addAll(data.getStringList("Command-Channels"));
    }
    
    public JDA getJDA() {
        return jda;
    }
    
    public List<Sensei> getSenseis() {
        return senseis;
    }
    
    public List<String> getSenseiIDs() {
        return senseis.stream().map(Sensei :: getID).collect(Collectors.toList());
    }
    
    public void addSensei(User sensei) {
        addSensei(sensei.getId());
    }
    
    public void addSenseis(List<User> senseiList) {
        boolean newSensei = false;
        for (User sensei : senseiList) {
            if (!isSensei(sensei.getId())) {
                senseis.add(new Sensei(sensei.getId()));
                newSensei = true;
            }
        }
        if (newSensei) {
            saveData();
        }
    }
    
    public void addSensei(String senseiID) {
        senseis.add(new Sensei(senseiID));
        saveData();
    }
    
    public void removeSensei(User sensei) {
        removeSensei(sensei.getId());
    }
    
    public void removeSensei(String senseiID) {
        senseis.remove(getSensei(senseiID));
        saveData();
    }
    
    public boolean isSensei(User sensei, Guild guild) {
        return isSensei(sensei.getId(), guild);
    }
    
    public boolean isSensei(String senseiID, Guild guild) {
        return isSensei(senseiID) || guild.getOwnerId().equals(senseiID);
    }
    
    public boolean isSensei(User sensei) {
        return isSensei(sensei.getId());
    }
    
    public boolean isSensei(String senseiID) {
        return getSensei(senseiID) != null;
    }
    
    public Sensei getSensei(String senseiID) {
        for (Sensei sensei : senseis) {
            if (sensei.getID().equals(senseiID)) {
                return sensei;
            }
        }
        return null;
    }
    
    public List<Senpai> getSenpais() {
        return senpais;
    }
    
    public List<String> getSenpaiNames() {
        return senpais.stream().map(senpai -> senpai.getUser().getName()).collect(Collectors.toList());
    }
    
    public List<String> getSenpaiIDs() {
        return senpais.stream().map(Senpai :: getID).collect(Collectors.toList());
    }
    
    public void clearAllSenpais() {
        senpais.clear();
        saveData();
    }
    
    public void addSenpai(User senpai) {
        addSenpai(senpai.getId());
    }
    
    public void addSenpais(List<User> senpaiList) {
        boolean newSenpai = false;
        for (User senpai : senpaiList) {
            if (!isSenpai(senpai)) {
                senpais.add(new Senpai(senpai.getId()));
                newSenpai = true;
            }
        }
        if (newSenpai) {
            saveData();
        }
    }
    
    public void addSenpai(String senpaiID) {
        senpais.add(new Senpai(senpaiID));
        saveData();
    }
    
    public void removeSenpai(User senpai) {
        removeSenpai(senpai.getId());
    }
    
    public void removeSenpai(String senpaiID) {
        senpais.remove(getSenpai(senpaiID));
        saveData();
    }
    
    public boolean isSenpai(User senpai) {
        return isSenpai(senpai.getId());
    }
    
    public boolean isSenpai(String senpaiID) {
        return getSenpai(senpaiID) != null;
    }
    
    public Senpai getSenpai(User senpai) {
        return getSenpai(senpai.getId());
    }
    
    public Senpai getSenpai(String senpaiID) {
        for (Senpai senpai : senpais) {
            if (senpai.getID().equals(senpaiID)) {
                return senpai;
            }
        }
        return null;
    }
    
    public void removeCommandChannel(MessageChannel channel) {
        removeCommandChannel(channel.getId());
    }
    
    public void removeCommandChannel(String channelID) {
        commandChannels.remove(channelID);
        saveData();
    }
    
    public void addCommandChannel(MessageChannel channel) {
        addCommandChannel(channel.getId());
    }
    
    public void addCommandChannel(String channelID) {
        commandChannels.add(channelID);
        saveData();
    }
    
    public boolean isCommandChannel(MessageChannel channel) {
        return isCommandChannel(channel.getId());
    }
    
    public boolean isCommandChannel(String channel) {
        return commandChannels.contains(channel);
    }
    
    private void loadJDA() {
        if (jda == null) {
            try {
                jda = new JDABuilder(AccountType.BOT)
                .setToken(Files.CONFIG.getFile().getString("Token"))
                .addEventListeners(new CommandListener(), new SpawnPackListener()).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void saveSenpais() {
        FileConfiguration data = Files.DATA.getFile();
        data.set("Senpais", null);
        for (Senpai senpai : senpais) {
            data.set("Senpais." + senpai.getID() + ".Name", senpai.getUser().getName());
            data.set("Senpais." + senpai.getID() + ".Cooldown", senpai.getSpawnPackCooldown());
        }
        Files.DATA.saveFile();
    }
    
    public void saveSenpai(Senpai senpai) {
        FileConfiguration data = Files.DATA.getFile();
        data.set("Senpais." + senpai.getID() + ".Name", senpai.getUser().getName());
        data.set("Senpais." + senpai.getID() + ".Cooldown", senpai.getSpawnPackCooldown());
        Files.DATA.saveFile();
    }
    
    public void saveData() {
        FileConfiguration data = Files.DATA.getFile();
        data.set("Command-Channels", commandChannels);
        data.set("Senpais", null);
        for (Senpai senpai : senpais) {
            data.set("Senpais." + senpai.getID() + ".Name", senpai.getUser().getName());
            data.set("Senpais." + senpai.getID() + ".Cooldown", senpai.getSpawnPackCooldown());
        }
        data.set("Senseis", getSenseiIDs());
        Files.DATA.saveFile();
    }
    
}