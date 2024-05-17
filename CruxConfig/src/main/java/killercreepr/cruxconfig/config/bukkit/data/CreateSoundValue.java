package killercreepr.cruxconfig.config.bukkit.data;

import killercreepr.crux.data.CreateSound;
import killercreepr.cruxconfig.config.bukkit.file.CruxConfig;
import killercreepr.cruxconfig.config.bukkit.value.ConfigValue;
import net.kyori.adventure.key.Key;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateSoundValue extends ConfigValue<CreateSound> {


    public CreateSoundValue(@Nullable CreateSound defaultValue) {
        super(CreateSound.class, defaultValue);
    }

    public CreateSoundValue() {
        super(CreateSound.class);
    }

    @Override
    public @Nullable CreateSound get(@NotNull CruxConfig crux, @NotNull String path) {
        FileConfiguration cfg = crux.config();
        final String keyName = cfg.getString(path + "sound");
        if(keyName == null || keyName.isBlank()) return null;
        net.kyori.adventure.sound.Sound.Source source;
        try{ source = net.kyori.adventure.sound.Sound.Source.valueOf(cfg.getString("source", "").toUpperCase()); }
        catch (IllegalArgumentException e){ source = net.kyori.adventure.sound.Sound.Source.MASTER; }
        try{
            Sound s = Sound.valueOf(keyName.toUpperCase());
            return new CreateSound(s, source,
                    (float) cfg.getDouble(path + "volume", 2f),
                    (float) cfg.getDouble(path + "pitch", 1f));
        }catch (IllegalArgumentException e){
            String[] args = keyName.split(":");
            return new CreateSound(
                    net.kyori.adventure.sound.Sound.sound(
                            args.length > 1 ? Key.key(args[0], args[1]) : Key.key(Key.MINECRAFT_NAMESPACE, args[0]),
                            source,
                            (float) cfg.getDouble(path + "volume", 2f),
                            (float) cfg.getDouble(path + "pitch", 1f))
            );
        }
    }

    @Override
    public void set(@NotNull CruxConfig cfg, @NotNull String path, @Nullable CreateSound object) {
        if(object == null) cfg.set(removeDot(path), null);
        else{
            cfg.set(path + "sound", object.getSound().name().asString());
            cfg.set(path + "volume", object.getSound().volume());
            cfg.set(path + "pitch", object.getSound().pitch());
            cfg.set(path + "source", object.getSound().source().toString());
        }
    }
}
