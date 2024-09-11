package killercreepr.cruxentities.listener;

import killercreepr.crux.Crux;
import killercreepr.crux.data.communication.CreateSound;
import killercreepr.cruxentities.entity.mob.goal.sound.CruxGoalSounds;
import killercreepr.cruxentities.entity.mob.goal.sound.SoundedMob;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.function.Consumer;

public class CustomEntitySoundsListener implements Listener {
    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event){
        if(!(event.getEntity() instanceof Mob mob)) return;
        sound(mob, sounds ->{
            double health = mob.getHealth() - event.getFinalDamage();
            CreateSound sound = health <= 0D ? sounds.death() : sounds.hurt();
            if(sound==null) return;
            sound.playAt(mob.getLocation());
        });
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Mob mob)) return;
        sound(mob, sounds ->{
            CreateSound sound = sounds.attack();
            if(sound==null) return;
            sound.playAt(mob.getLocation());
        });
    }

    public void sound(Mob mob, Consumer<CruxGoalSounds> consumer){
        Crux.getServer().getMobGoals().getRunningGoals(mob).forEach(goal ->{
            if(!(goal instanceof SoundedMob sounded)) return;
            CruxGoalSounds sounds = sounded.getGoalSounds();
            if(sounds == null) return;
            consumer.accept(sounds);
        });
    }
}
