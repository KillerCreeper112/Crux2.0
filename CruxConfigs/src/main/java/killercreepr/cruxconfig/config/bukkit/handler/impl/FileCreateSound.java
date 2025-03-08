package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.communication.CreateSound;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import net.kyori.adventure.key.Key;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
@JsonSerializer(id = "create_sound")
public class FileCreateSound extends SimpleFileHandler<CreateSound> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull CreateSound object) {
        return new FileObject()
                .addProperty("sound", object.getSound().name().asString())
                .addProperty( "volume", object.getSound().volume())
                .addProperty( "pitch", object.getSound().pitch())
                .addProperty("source", object.getSound().source().toString())
                ;
    }

    @Override
    public @Nullable CreateSound deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)){
            if(!(e instanceof FilePrimitive prim)) return null;
            String keyName = prim.getAsString();
            return CreateSound.sound(keyName);
        }
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
        Key key = Key.key(keyName);
        Sound s = Registry.SOUNDS.get(key);
        if(s != null) return CreateSound.sound(s, source, volume, pitch);
        return CreateSound.sound(
            key,
            source,
            volume, pitch
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "create_sound";
    }
}
