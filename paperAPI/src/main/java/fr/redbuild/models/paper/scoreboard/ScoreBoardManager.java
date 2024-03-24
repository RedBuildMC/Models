package fr.redbuild.models.paper.scoreboard;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

public class ScoreBoardManager {
    private Map<UUID,ScoreBoard> players = new HashMap<>();

    public void registerPlayers(Player player,ScoreBoard scoreBoard){
        if(players.containsKey(player.getUniqueId()))
            players.remove(player.getUniqueId());
        players.put(player.getUniqueId(), scoreBoard);
    }

    public void deRegisterPlayers(Player player,ScoreBoard scoreBoard){
        if(players.containsKey(player.getUniqueId()))
            players.remove(player.getUniqueId());
    }

    public void registerAll(ScoreBoard scoreBoard){
        players.put(null, scoreBoard);
    }

    public void join(Player player){
        if(players.containsKey(null))
            players.get(null).setPlayer(player);
        if(players.containsKey(player.getUniqueId()))
            players.get(player.getUniqueId()).setPlayer(player);
    }
}
