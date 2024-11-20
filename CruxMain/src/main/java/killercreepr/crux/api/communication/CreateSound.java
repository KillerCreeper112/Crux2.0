package killercreepr.crux.api.communication;


import killercreepr.crux.core.communication.SimpleCreateSound;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface CreateSound {
    static CreateSound sound(@NotNull org.bukkit.Sound sound){
        return new SimpleCreateSound(sound);
    }
    static CreateSound sound(@NotNull Key key){
        return sound(key, 1f);
    }

    static CreateSound sound(@NotNull Key key, float pitch){
        return sound(key, Sound.Source.MASTER, 2f, pitch);
    }

    static CreateSound sound(@NotNull Key key, @NotNull Sound.Source source, float volume, float pitch){
        return sound(Sound.sound(key, source, volume, pitch));
    }

    static CreateSound sound(@NotNull String key){
        return sound(Key.key(key), 1f);
    }

    static CreateSound sound(@NotNull String key, float pitch){
        return sound(Key.key(key), Sound.Source.MASTER, 2f, pitch);
    }

    static CreateSound sound(@NotNull String key, @NotNull Sound.Source source, float volume, float pitch){
        return sound(Key.key(key), source, volume, pitch);
    }

    static CreateSound sound(@NotNull org.bukkit.Sound sound, float pitch){
        return new SimpleCreateSound(sound, pitch);
    }
    static CreateSound sound(@NotNull Sound sound){
        return new SimpleCreateSound(sound);
    }

    static CreateSound sound(@NotNull org.bukkit.Sound sound, float volume, float pitch){
        return new SimpleCreateSound(sound, volume, pitch);
    }
    static CreateSound sound(@NotNull org.bukkit.Sound sound, @NotNull Sound.Source source, float volume, float pitch){
        return new SimpleCreateSound(sound, source, volume, pitch);
    }

    @NotNull Sound getSound();
    CreateSound playAt(@NotNull Location l);

    CreateSound playAt(@NotNull Entity l);

    CreateSound playFor(@NotNull Entity p);

    CreateSound playFrom(@NotNull Entity l);
}
