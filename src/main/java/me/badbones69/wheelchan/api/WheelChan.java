package me.badbones69.wheelchan.api;

import me.badbones69.wheelchan.api.FileManager.Files;
import me.badbones69.wheelchan.api.enums.Messages;
import me.badbones69.wheelchan.api.objects.Senpie;
import me.badbones69.wheelchan.api.objects.Sensei;
import me.badbones69.wheelchan.listeners.CommandListener;
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
    private List<Senpie> senpais = new ArrayList<>();
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
        data.getStringList("Senpais").forEach(id -> senpais.add(new Senpie(id)));
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
    
    public List<Senpie> getSenpais() {
        return senpais;
    }
    
    public List<String> getSenpieNames() {
        return senpais.stream().map(senpie -> senpie.getUser().getName()).collect(Collectors.toList());
    }
    
    public List<String> getSenpieIDs() {
        return senpais.stream().map(Senpie :: getID).collect(Collectors.toList());
    }
    
    public void clearAllSenpais() {
        senpais.clear();
        saveData();
    }
    
    public void addSenpie(User senpie) {
        addSenpie(senpie.getId());
    }
    
    public void addSenpais(List<User> senpieList) {
        boolean newSenpie = false;
        for (User senpie : senpieList) {
            if (!isSenpie(senpie)) {
                senpais.add(new Senpie(senpie.getId()));
                newSenpie = true;
            }
        }
        if (newSenpie) {
            saveData();
        }
    }
    
    public void addSenpie(String senpieID) {
        senpais.add(new Senpie(senpieID));
        saveData();
    }
    
    public void removeSenpie(User senpie) {
        removeSenpie(senpie.getId());
    }
    
    public void removeSenpie(String senpieID) {
        senpais.remove(getSenpie(senpieID));
        saveData();
    }
    
    public boolean isSenpie(User senpie) {
        return isSenpie(senpie.getId());
    }
    
    public boolean isSenpie(String senpieID) {
        return getSenpie(senpieID) != null;
    }
    
    public Senpie getSenpie(String senpieID) {
        for (Senpie senpie : senpais) {
            if (senpie.getID().equals(senpieID)) {
                return senpie;
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
                .addEventListeners(new CommandListener()).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void saveData() {
        FileConfiguration data = Files.DATA.getFile();
        data.set("Command-Channels", commandChannels);
        data.set("Senpais", getSenpieIDs());
        data.set("Senseis", getSenseiIDs());
        Files.DATA.saveFile();
    }
    
}