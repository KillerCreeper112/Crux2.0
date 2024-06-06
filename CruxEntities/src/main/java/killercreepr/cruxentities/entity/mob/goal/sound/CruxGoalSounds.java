package killercreepr.cruxentities.entity.mob.goal.sound;

import killercreepr.crux.data.CreateSound;
import killercreepr.crux.data.tick.Ticked;
import killercreepr.crux.util.CruxMath;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CruxGoalSounds implements Ticked {
    protected final Entity e;
    protected int ambient;
    public CruxGoalSounds(@NotNull Entity e){
        this.e = e;
    }

    @Override
    public final void tick(){
        if(ambient() ==  null) return;
        if(ambient > 0){
            ambient--;
            return;
        }
        ambient = CruxMath.random(ambientMin(), ambientMax());
        playSound(ambient());
    }

    public int ambientMin(){ return 60; }
    public int ambientMax(){ return 100; }

    public final void playSound(@Nullable CreateSound a){
        if(a == null) return;
        a.playAt(e);
    }

    public @Nullable CreateSound ambient(){ return null; }
    public @Nullable CreateSound attack(){ return null; }
    public @Nullable CreateSound hurt(){ return null; }
    public @Nullable CreateSound death(){ return null; }
}
