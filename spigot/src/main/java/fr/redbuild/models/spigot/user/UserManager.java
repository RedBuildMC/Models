package fr.redbuild.models.spigot.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.redbuild.models.spigot.Autowired.Autowired;
import fr.redbuild.models.spigot.event.PlayerFirstJoinEvent;
import fr.redbuild.models.spigot.event.UserInitEvent;
import fr.redbuild.models.spigot.grade.GradeManager;

public class UserManager {
    private List<User> registeredUser = new ArrayList<>();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GradeManager gradeManager;

    public void init(){
        registeredUser.addAll(userRepository.findAll());
        gradeManager.init();
    }

    public void deInit(){
        registeredUser.forEach(user -> userRepository.save(user));
        registeredUser.clear();
    }

    public void playerJoin(Player Player){
        User user = userRepository.findById(Player.getUniqueId()).orElse(null);
        if(user == null){
            user = createUser(Player);
            Bukkit.getServer().getPluginManager().callEvent(new PlayerFirstJoinEvent(Player,user));
        }
        registeredUser.add(user);
    }

    public void playerQuit(Player Player){
        User user = getUser(Player);
        userRepository.save(user);
        registeredUser.remove(user);
    }

    public User getUser(Player player){
        return getUser(player.getUniqueId());
    }

    public User getUser(String name){
        return registeredUser.stream().filter(u -> u.getDisplayName().equals(name)).findFirst().orElse(null);
    }

    public User getUser(UUID uuid){
        if(registeredUser.stream().noneMatch(u -> u.getUuid().equals(uuid)))
            return userRepository.findById(uuid).orElse(null);
        else
            return registeredUser.stream().filter(u -> u.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public void addUser(User user){
        registeredUser.add(user);
        userRepository.save(user);
    }

    public void removeUser(User user){
        registeredUser.remove(user);
        userRepository.delete(user);
    }

    public void updateUser(User user){
        userRepository.save(user);
    }

    public boolean hasUser(User user){
        return registeredUser.contains(user);
    }

    public boolean hasUser(UUID uuid){
        return registeredUser.stream().anyMatch(u -> u.getUuid().equals(uuid));
    }

    public boolean hasUser(String name){
        return registeredUser.stream().anyMatch(u -> u.getDisplayName().equals(name));
    }

    public List<User> getRegisteredUser() {
        return registeredUser;
    }

    public User createUser(Player player) {
        User user = new User(player.getUniqueId(),player.getName(),gradeManager.getDEFAULT().getUuid(), new Date());
        userRepository.save(user);
        addUser(user);
        return user;
    }

    public void initUser(User user){
        gradeManager.initUser(user);
        Bukkit.getServer().getPluginManager().callEvent(new UserInitEvent(user));
    }


    
}
