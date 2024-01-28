package fr.redbuild.models.spigot.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import fr.redbuild.models.spigot.utils.injector.Injector;
import lombok.Setter;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Map;

public class ScoreBoard {
    private final ScoreBoardManager scoreBoardManager;
    @Setter
    private Scoreboard scoreBoard;
    @Setter
    private Objective objective;

    /**
     * Constructeur de la classe ScoreBoard.
     *
     * @param name        Le nom de l'objectif.
     * @param displayName Le nom affiché de l'objectif.
     */
    public ScoreBoard(String name, Component displayName) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        scoreBoard = manager.getNewScoreboard();
        objective = scoreBoard.registerNewObjective(name, Criteria.DUMMY, displayName);
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        scoreBoardManager = Injector.getInstance(ScoreBoardManager.class);
    }

    /**
     * Constructeur de la classe ScoreBoard.
     *
     * @param scoreBoard Le Scoreboard à utiliser.
     * @param objective  L'objectif à utiliser.
     */
    public ScoreBoard(Scoreboard scoreBoard, Objective objective) {
        this.scoreBoard = scoreBoard;
        this.objective = objective;
        scoreBoardManager = Injector.getInstance(ScoreBoardManager.class);
    }

    /**
     * Définit le nom affiché de l'objectif.
     *
     * @param name Le nom affiché de l'objectif.
     * @return L'instance de ScoreBoard.
     */
    public ScoreBoard setDisplayName(Component name) {
        objective.displayName(name);
        return this;
    }

    /**
     * Ajoute une ligne à l'objectif.
     *
     * @param position La position de la ligne.
     * @param text     Le texte de la ligne.
     * @return L'instance de ScoreBoard.
     */
    public ScoreBoard addLine(int position, String text) {
        objective.getScore(text).setScore(position);
        return this;
    }

    /**
     * Ajoute plusieurs lignes à l'objectif.
     *
     * @param lines Une map contenant les positions et les textes des lignes.
     * @return L'instance de ScoreBoard.
     */
    public ScoreBoard addLine(Map<Integer, String> lines) {
        lines.keySet().forEach(pos -> addLine(pos, lines.get(pos)));
        return this;
    }

    /**
     * Ajoute plusieurs lignes à l'objectif.
     *
     * @param lines Une liste contenant les textes des lignes.
     * @return L'instance de ScoreBoard.
     */
    public ScoreBoard addLines(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            addLine(i, lines.get(i));
        }
        return this;
    }

    /**
     * Modifie une ligne de l'objectif.
     *
     * @param position La position de la ligne à modifier.
     * @param text     Le nouveau texte de la ligne.
     * @return L'instance de ScoreBoard.
     */
    public ScoreBoard editLine(int position, String text) {
        removeLine(position);
        addLine(position, text);
        return this;
    }

    /**
     * Modifie plusieurs lignes de l'objectif.
     *
     * @param lines Une map contenant les positions et les nouveaux textes des
     *              lignes.
     * @return L'instance de ScoreBoard.
     */
    public ScoreBoard editLine(Map<Integer, String> lines) {
        lines.keySet().forEach(pos -> editLine(pos, lines.get(pos)));
        return this;
    }

    /**
     * Modifie plusieurs lignes de l'objectif.
     *
     * @param lines Une liste contenant les nouveaux textes des lignes.
     * @return L'instance de ScoreBoard.
     */
    public ScoreBoard editLines(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            editLine(i, lines.get(i));
        }
        return this;
    }

    /**
     * Supprime une ligne de l'objectif.
     *
     * @param position La position de la ligne à supprimer.
     * @return L'instance de ScoreBoard.
     */
    public ScoreBoard removeLine(int position) {
        scoreBoard.resetScores(objective.getScoreboard().getObjective(DisplaySlot.SIDEBAR).getScoreboard().getEntries()
                .stream().filter(entry -> objective.getScore(entry).getScore() == position).findFirst().get());
        return this;
    }

    /**
     * Supprime une ligne de l'objectif.
     *
     * @param text Le texte de la ligne à supprimer.
     * @return L'instance de ScoreBoard.
     */
    public ScoreBoard removeLine(String text) {
        scoreBoard.resetScores(text);
        return this;
    }

    /**
     * Supprime plusieurs lignes de l'objectif.
     *
     * @param lines Une map contenant les positions et les textes des lignes à
     *              supprimer.
     * @return L'instance de ScoreBoard.
     */
    public ScoreBoard removeLine(Map<Integer, String> lines) {
        lines.keySet().forEach(this::removeLine);
        return this;
    }

    /**
     * Supprime plusieurs lignes de l'objectif.
     *
     * @param lines Une liste contenant les textes des lignes à supprimer.
     * @return L'instance de ScoreBoard.
     */
    public ScoreBoard removeLines(List<String> lines) {
        lines.forEach(this::removeLine);
        return this;
    }

    /**
     * Enregistre un joueur avec cet objectif.
     *
     * @param player Le joueur à enregistrer.
     * @return L'instance de ScoreBoard.
     */
    public ScoreBoard registerPlayer(Player player) {
        player.setScoreboard(scoreBoard);
        scoreBoardManager.registerPlayers(player, this);
        return this;
    }

    /**
     * Enregistre plusieurs joueurs avec cet objectif.
     *
     * @param players Une liste de joueurs à enregistrer.
     * @return L'instance de ScoreBoard.
     */
    public ScoreBoard registerPlayers(List<Player> players) {
        players.forEach(this::registerPlayer);
        return this;
    }

    /**
     * Enregistre tous les joueurs avec cet objectif.
     *
     * @return L'instance de ScoreBoard.
     */
    public ScoreBoard registerAll() {
        scoreBoardManager.registerAll(this);
        return this;
    }

    /**
     * Définit le Scoreboard pour un joueur.
     *
     * @param player Le joueur.
     */
    public void setPlayer(Player player) {
        player.setScoreboard(scoreBoard);
    }
}
