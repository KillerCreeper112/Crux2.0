package killercreepr.crux.location;

import killerceepr.crux.Crux;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public class CompareLocation {
    public enum LocationFlag{
        IGNORE_X,
        IGNORE_Y,
        IGNORE_Z,
        IGNORE_PITCH,
        IGNORE_YAW,
        IGNORE_WORLD
    }

    private Location pos1;
    private Location pos2;

    private final Set<LocationFlag> flags = new HashSet<>();

    public CompareLocation(@NotNull Location pos1, @NotNull Location pos2){
        this.pos1 = pos1;
        this.pos2 = pos2;
    }

    public CompareLocation(@NotNull Location pos1, @NotNull Location pos2, boolean ignoreRotation){
        this.pos1 = pos1;
        this.pos2 = pos2;
        if(ignoreRotation){
            flags.add(LocationFlag.IGNORE_YAW);
            flags.add(LocationFlag.IGNORE_PITCH);
        }
    }

    public CompareLocation(@NotNull Location pos1, @NotNull Location pos2, boolean ignoreRotation, boolean ignoreWorld){
        this.pos1 = pos1;
        this.pos2 = pos2;
        if(ignoreRotation){
            flags.add(LocationFlag.IGNORE_YAW);
            flags.add(LocationFlag.IGNORE_PITCH);
        }
        if(ignoreWorld) flags.add(LocationFlag.IGNORE_WORLD);
    }

    public boolean moved(Location to, Location from){
        return to.getX() != from.getX() ||
                to.getY() != from.getY() ||
                to.getZ() != from.getZ();
    }

    public boolean moved(){
        return moved(pos1, pos2);
    }

    public boolean compare(Location pos1, Location pos2, Set<LocationFlag> flags){
        if(flags.size() >= 6){
            Crux.log(Level.WARNING, "CompareLocation is using all 6 LocationFlags! Result will always return true when comparing: " +
                    pos1 + " & " + pos2 + ".");
        }
        if(!flags.contains(LocationFlag.IGNORE_X)){
            if(pos1.getX() != pos2.getX()) return false;
        }
        if(!flags.contains(LocationFlag.IGNORE_Y)){
            if(pos1.getY() != pos2.getY()) return false;
        }
        if(!flags.contains(LocationFlag.IGNORE_Z)){
            if(pos1.getZ() != pos2.getZ()) return false;
        }
        if(!flags.contains(LocationFlag.IGNORE_PITCH)){
            if(pos1.getPitch() != pos2.getPitch()) return false;
        }
        if(!flags.contains(LocationFlag.IGNORE_YAW)){
            if(pos1.getYaw() != pos2.getYaw()) return false;
        }
        if(!flags.contains(LocationFlag.IGNORE_WORLD)){
            return pos1.getWorld().equals(pos2.getWorld());
        }
        return true;
    }

    public boolean compare(LocationFlag... flags){
        return compare(pos1, pos2, new HashSet<>(Arrays.asList(flags)));
    }

    public boolean compare(){
        return compare(pos1, pos2, flags);
    }

    public boolean compare(Location tempPos2){
        return compare(pos1, tempPos2, flags);
    }

    public boolean compare(Location tempPos2, boolean ignoreRotation){
        Set<LocationFlag> newFlags = new HashSet<>(flags);
        if(ignoreRotation){
            newFlags.add(LocationFlag.IGNORE_PITCH);
            newFlags.add(LocationFlag.IGNORE_YAW);
        }else{
            newFlags.remove(LocationFlag.IGNORE_PITCH);
            newFlags.remove(LocationFlag.IGNORE_YAW);
        }
        return compare(pos1, tempPos2, newFlags);
    }

    public boolean compare(boolean ignoreRotation){
        Set<LocationFlag> newFlags = new HashSet<>(flags);
        if(ignoreRotation){
            newFlags.add(LocationFlag.IGNORE_PITCH);
            newFlags.add(LocationFlag.IGNORE_YAW);
        }else{
            newFlags.remove(LocationFlag.IGNORE_PITCH);
            newFlags.remove(LocationFlag.IGNORE_YAW);
        }
        return compare(pos1, pos2, newFlags);
    }

    public boolean compare(boolean ignoreX, boolean ignoreY, boolean ignoreZ){
        Set<LocationFlag> newFlags = new HashSet<>(flags);
        if(ignoreX) newFlags.add(LocationFlag.IGNORE_X);
        else newFlags.remove(LocationFlag.IGNORE_X);

        if(ignoreX) newFlags.add(LocationFlag.IGNORE_Y);
        else newFlags.remove(LocationFlag.IGNORE_Y);

        if(ignoreX) newFlags.add(LocationFlag.IGNORE_Z);
        else newFlags.remove(LocationFlag.IGNORE_Z);
        return compare(pos1, pos2, newFlags);
    }

    public boolean compare(boolean ignoreX, boolean ignoreY, boolean ignoreZ, boolean ignoreRotation){
        Set<LocationFlag> newFlags = new HashSet<>(flags);
        if(ignoreX) newFlags.add(LocationFlag.IGNORE_X);
        else newFlags.remove(LocationFlag.IGNORE_X);

        if(ignoreX) newFlags.add(LocationFlag.IGNORE_Y);
        else newFlags.remove(LocationFlag.IGNORE_Y);

        if(ignoreX) newFlags.add(LocationFlag.IGNORE_Z);
        else newFlags.remove(LocationFlag.IGNORE_Z);

        if(ignoreRotation){
            newFlags.add(LocationFlag.IGNORE_PITCH);
            newFlags.add(LocationFlag.IGNORE_YAW);
        }else{
            newFlags.remove(LocationFlag.IGNORE_PITCH);
            newFlags.remove(LocationFlag.IGNORE_YAW);
        }
        return compare(pos1, pos2, newFlags);
    }

    public boolean compare(boolean ignoreX, boolean ignoreY, boolean ignoreZ, boolean ignoreRotation, boolean ignoreWorld){
        Set<LocationFlag> newFlags = new HashSet<>(flags);
        if(ignoreX) newFlags.add(LocationFlag.IGNORE_X);
        else newFlags.remove(LocationFlag.IGNORE_X);

        if(ignoreX) newFlags.add(LocationFlag.IGNORE_Y);
        else newFlags.remove(LocationFlag.IGNORE_Y);

        if(ignoreX) newFlags.add(LocationFlag.IGNORE_Z);
        else newFlags.remove(LocationFlag.IGNORE_Z);

        if(ignoreRotation){
            newFlags.add(LocationFlag.IGNORE_PITCH);
            newFlags.add(LocationFlag.IGNORE_YAW);
        }else{
            newFlags.remove(LocationFlag.IGNORE_PITCH);
            newFlags.remove(LocationFlag.IGNORE_YAW);
        }

        if(ignoreX) newFlags.add(LocationFlag.IGNORE_WORLD);
        else newFlags.remove(LocationFlag.IGNORE_WORLD);
        return compare(pos1, pos2, newFlags);
    }

    public Location getPos1() {
        return pos1;
    }

    public CompareLocation setPos1(Location l) {
        this.pos1 = l;
        return this;
    }

    public Location getPos2() {
        return pos2;
    }

    public CompareLocation setPos2(Location l1) {
        this.pos2 = l1;
        return this;
    }

    public CompareLocation setFlags(LocationFlag... flags){
        this.flags.clear();
        this.flags.addAll(Arrays.asList(flags));
        return this;
    }

    public CompareLocation addFlag(LocationFlag flag){
        flags.add(flag);
        return this;
    }

    public CompareLocation addFlags(LocationFlag... flags){
        this.flags.addAll(Arrays.asList(flags));
        return this;
    }

    public CompareLocation removeFlag(LocationFlag flag){
        flags.remove(flag);
        return this;
    }

    public Set<LocationFlag> getFlags() {
        return flags;
    }
}
