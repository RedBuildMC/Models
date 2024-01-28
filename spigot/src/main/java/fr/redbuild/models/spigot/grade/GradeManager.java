package fr.redbuild.models.spigot.grade;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.redbuild.models.spigot.Autowired.Autowired;
import fr.redbuild.models.spigot.plugin.PluginController;
import fr.redbuild.models.spigot.user.User;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class GradeManager {
    @Getter
    @Setter
    private Grade DEFAULT = new Grade(UUID.randomUUID(), "default", "Default", "");

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private MiniMessage mm;

    private List<Grade> registeredGrade = new ArrayList<>();

    public void init() {
        registeredGrade.addAll(gradeRepository.findAll());
    }

    public Grade getGrade(UUID uuid) {
        return registeredGrade.stream().filter(g -> g.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    public Grade getGrade(String name) {
        return registeredGrade.stream().filter(g -> g.getName().equals(name)).findFirst().orElse(null);
    }

    public void addGrade(Grade grade) {
        registeredGrade.add(grade);
        gradeRepository.save(grade);
    }

    public void removeGrade(Grade grade) {
        registeredGrade.remove(grade);
        gradeRepository.delete(grade);
    }

    public void updateGrade(Grade grade) {
        gradeRepository.save(grade);
    }

    public boolean hasGrade(Grade grade) {
        return registeredGrade.contains(grade);
    }

    public boolean hasGrade(UUID uuid) {
        return registeredGrade.stream().anyMatch(g -> g.getUuid().equals(uuid));
    }

    public boolean hasGrade(String name) {
        return registeredGrade.stream().anyMatch(g -> g.getName().equals(name));
    }

    public List<Grade> getRegisteredGrade() {
        return registeredGrade;
    }

    public Grade createGrade(String name, String displayName, String prefix) {
        Grade grade = new Grade(UUID.randomUUID(), name, displayName, prefix);
        addGrade(grade);
        return grade;
    }

    public void initUser(User user){
        if(user.getGrade() == null){
            user.addGrade(DEFAULT);
        }
        Player player = user.getPlayer();
        Grade grade = user.getGrade();
        player.displayName(mm.deserialize(grade.getPrefix() + user.getDisplayName()));
        player.playerListName(mm.deserialize(grade.getPrefix() + user.getDisplayName()));
        for(String permission : grade.getPermissions().keySet()){
            player.addAttachment(PluginController.INSTANCE, permission, grade.getPermissions().get(permission));
        }
    }
}
