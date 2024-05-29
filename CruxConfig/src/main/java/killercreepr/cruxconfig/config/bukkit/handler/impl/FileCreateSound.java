package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.data.CreateSound;
import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Key;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileCreateSound extends SimpleFileHandler<CreateSound> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull CreateSound object) {
        return new FileObject()
                .addProperty("sound", object.getSound().name().asString())
                .addProperty( "volume", object.getSound().volume())
                .addProperty( "pitch", object.getSound().pitch())
                .addProperty("source", object.getSound().source().toString())
                ;
    }

    @Override
    public @Nullable CreateSound deserializeFromFile(@NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        final String keyName = o.getObject("sound");
        if(keyName == null || keyName.isBlank()) return null;
        net.kyori.adventure.sound.Sound.Source source;
        try{
            source = net.kyori.adventure.sound.Sound.Source.valueOf(
                    o.getOrDefaultObject("source", "").toUpperCase());
        }
        catch (IllegalArgumentException ignored){ source = net.kyori.adventure.sound.Sound.Source.MASTER; }
        float volume = (float) (double) o.getOrDefaultObject("volume", 2D);
        float pitch = (float) (double) o.getOrDefaultObject("pitch", 1D);
        try{
            Sound s = Sound.valueOf(keyName.toUpperCase());
            return new CreateSound(s, source,
                    volume, pitch
            );
        }catch (IllegalArgumentException ignored){
            String[] args = keyName.split(":");
            return new CreateSound(
                    net.kyori.adventure.sound.Sound.sound(
                            args.length > 1 ? Key.key(args[0], args[1]) : Key.key(Key.MINECRAFT_NAMESPACE, args[0]),
                            source,
                            volume, pitch
                    ));
        }
    }
}
