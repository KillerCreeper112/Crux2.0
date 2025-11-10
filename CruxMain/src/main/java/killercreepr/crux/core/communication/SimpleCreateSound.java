package killercreepr.crux.core.communication;

import killercreepr.crux.api.communication.CreateSound;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.Registry;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class SimpleCreateSound implements CreateSound {
    protected final Sound sound;
    public SimpleCreateSound(@NotNull Sound sound) {
        this.sound = sound;
    }

    public SimpleCreateSound(@NotNull org.bukkit.Sound sound, float volume, float pitch) {
        this(sound, Sound.Source.MASTER, volume, pitch);
    }


    public SimpleCreateSound(@NotNull org.bukkit.Sound sound, float pitch) {
        this(sound, 2f, pitch);
    }

    public SimpleCreateSound(@NotNull org.bukkit.Sound sound) {
        this(sound,1f);
    }

    public SimpleCreateSound(@NotNull org.bukkit.Sound sound, @NotNull Sound.Source source, float volume, float pitch) {
        this.sound = Sound.sound(Registry.SOUNDS.getKeyOrThrow(sound), source, volume, pitch);
    }

    @Override
    public String toString() {
        return "SimpleCreateSound{" + "sound=" + sound + "}";
    }

    public @NotNull Sound getSound() { return sound; }

    public CreateSound playAt(@NotNull Location l){
        l.getWorld().playSound(sound, l.getX(), l.getY(), l.getZ());
        return this;
    }

    public CreateSound playAt(@NotNull Entity l){
        l.getWorld().playSound(sound, l.getLocation().getX(), l.getLocation().getY(), l.getLocation().getZ());
        return this;
    }

    public CreateSound playFor(@NotNull Audience p){
        p.playSound(sound, Sound.Emitter.self());
        return this;
    }

    public CreateSound playFrom(@NotNull Entity l){
        l.getWorld().playSound(sound, l);
        return this;
    }
}
