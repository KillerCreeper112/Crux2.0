package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import killercreepr.crux.data.CreateSound;
import killercreepr.cruxconfig.config.common.yaml.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import net.kyori.adventure.key.Key;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class YamlCreateSound implements YamlObjectHandler<CreateSound> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull CreateSound object) {
        return new YamlObject()
                .addProperty("sound", object.getSound().name().asString())
                .addProperty( "volume", object.getSound().volume())
                .addProperty( "pitch", object.getSound().pitch())
                .addProperty("source", object.getSound().source().toString())
                ;
    }

    @Override
    public @Nullable CreateSound deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(!(e instanceof YamlObject o)) return null;
        final String keyName = o.getObject("sound");
        if(keyName == null || keyName.isBlank()) return null;
        net.kyori.adventure.sound.Sound.Source source;
        try{
            source = net.kyori.adventure.sound.Sound.Source.valueOf(
                    o.getOrDefaultObject("source", "").toUpperCase());
        }
        catch (IllegalArgumentException ignored){ source = net.kyori.adventure.sound.Sound.Source.MASTER; }
        float volume = o.getOrDefaultObject("volume", 2f);
        float pitch = o.getOrDefaultObject("pitch", 1f);
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
