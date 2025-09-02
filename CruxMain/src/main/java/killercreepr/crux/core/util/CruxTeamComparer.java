package killercreepr.crux.core.util;

import killercreepr.crux.core.Crux;
import org.bukkit.entity.Entity;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;

public class CruxTeamComparer {
    public static CruxTeamComparer teamComparer(boolean trueIfNull){
        return teamComparer(trueIfNull, null);
    }
    public static CruxTeamComparer teamComparer(boolean trueIfNull, Predicate<Team> additionalCheck){
        return teamComparer(Crux.getServer().getScoreboardManager().getMainScoreboard(), trueIfNull, additionalCheck);
    }

    public static CruxTeamComparer teamComparer(Scoreboard board, boolean trueIfNull){
        return teamComparer(board, trueIfNull, null);
    }
    public static CruxTeamComparer teamComparer(Scoreboard board, boolean trueIfNull, Predicate<Team> additionalCheck){
        return new CruxTeamComparer(board, trueIfNull, additionalCheck);
    }

    protected final Scoreboard board;
    protected final boolean trueIfNull;
    protected final Predicate<Team> additionalCheck;

    public CruxTeamComparer(Scoreboard board, boolean trueIfNull, Predicate<Team> additionalCheck) {
        this.board = board;
        this.additionalCheck = additionalCheck;
        this.trueIfNull = trueIfNull;
    }

    public Scoreboard getBoard() {
        return board;
    }

    public boolean isTrueIfNull() {
        return trueIfNull;
    }

    public Predicate<Team> getAdditionalCheck() {
        return additionalCheck;
    }

    public Team getTeam(Entity entity){
        return board.getEntityTeam(entity);
    }

    public Team getTeam(UUID entity){
        return getTeam(entity.toString());
    }

    public Team getTeam(String entry){
        return board.getEntryTeam(entry);
    }

    public boolean areOnSameTeam(Entity first, Entity second){
        Team team1 = getTeam(first);
        Team team2 = getTeam(second);
        return compareTeams(team1, team2);
    }

    public boolean areOnSameTeam(Entity first, UUID second){
        Team team1 = getTeam(first);
        Team team2 = getTeam(second);
        return compareTeams(team1, team2);
    }

    public boolean areOnSameTeam(UUID first, UUID second){
        Team team1 = getTeam(first);
        Team team2 = getTeam(second);
        return compareTeams(team1, team2);
    }

    public boolean areOnSameTeam(String first, String second){
        Team team1 = getTeam(first);
        Team team2 = getTeam(second);
        return compareTeams(team1, team2);
    }

    public boolean compareAdditionCheck(Team team){
        return (additionalCheck == null || additionalCheck.test(team));
    }

    public boolean compareTeams(Team team1, Team team2){
        if(trueIfNull){
            return Objects.equals(team1, team2) && compareAdditionCheck(team1);
        }
        if(team1 == null || team2 == null) return false;

        return Objects.equals(team1, team2) && compareAdditionCheck(team1);
    }
}
