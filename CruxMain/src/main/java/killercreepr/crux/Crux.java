package killercreepr.crux;

import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.data.tick.CruxTick;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.CruxRegistries;
import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.tags.CruxTags;
import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.format.Format;
import killercreepr.crux.tags.standard.CruxStandardTags;
import killercreepr.crux.tags.standard.minimessage.*;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//todo Make a translatable system
public final class Crux {
    public static final String NAMESPACE = "crux";
    public static final TagParser TAGS = new CruxTags.Builder()
        .addTags(CruxStandardTags.buildObjectTags())
        .build();
    public static final Format FORMAT = new Format.Builder()
        .miniMessage(MiniMessage.builder()
            .tags(TagResolver.builder()
                .resolvers(TagResolver.standard(),
                    new CheckBoolTag(),
                    new StringFormatTag(),
                    new DateFormatTag(),
                    new DurationTag(),
                    new ConvertBoolTag(),
                    new UnicodeSpacingTag())
                .build()
            ).build())
        .tagParser(TAGS)

        .stringPattern(CruxStandardTags.buildStringPattern())
        .lorePattern(CruxStandardTags.buildLorePattern())
        .equationPattern(CruxStandardTags.buildEquationPattern())
        .bEquationPattern(CruxStandardTags.buildBEquationPattern())

        .addGlobalStringTags(CruxStandardTags.buildGlobalStringTags())
        .addGlobalStringListTags(CruxStandardTags.buildGlobalStringListTags())
        .build();
    private static final Logger log = Logger.getLogger(Crux.class.getName());

    private static @Nullable CruxPlugin mainPlugin;
    public static @Nullable CruxPlugin getMainPlugin() {
        return mainPlugin;
    }
    public static void setMainPlugin(@Nullable CruxPlugin mainPlugin) {
        Crux.mainPlugin = mainPlugin;
    }

    public static Server getServer(){
        return getMainPlugin().getServer();
    }

    private static final @NotNull CruxHandlers handlers = new CruxHandlers.Generic();
    public static @NotNull CruxHandlers handlers(){ return handlers; }

    public static @NotNull BukkitRunnable buildTickRunnable(){
        return buildTickRunnable(CruxRegistries.TICKS, true);
    }

    public static @NotNull BukkitRunnable buildTickRunnable(@NotNull KeyedRegistry<CruxTick> registry, boolean includeEntityMemory){
        if(!includeEntityMemory){
            return new BukkitRunnable(){
                @Override
                public void run() {
                    for(CruxTick t : new HashSet<>(registry.values())){
                        if(t.markedForRemoval()){
                            registry.remove(t.key());
                            continue;
                        }
                        t.tick();
                    }
                }
            };
        }
        return new BukkitRunnable(){
            @Override
            public void run() {
                for(CruxTick t : new HashSet<>(registry.values())){
                    if(t.markedForRemoval()){
                        registry.remove(t.key());
                        continue;
                    }
                    t.tick();
                }
                EntityMemory.REGISTRY.values().removeIf(EntityMemory::tick);
            }
        };
    }

    public static void log(@NotNull Level level, @NotNull String msg){
        log.log(level, msg);
    }

    public static @NotNull NamespacedKey key(@NotNull String key){
        return key(key.split(":"));
    }

    public static @NotNull NamespacedKey key(@NotNull String[] key){
        return key.length > 1 ? new NamespacedKey(key[0], key[1]) : new NamespacedKey(NAMESPACE, key[0]);
    }

    public static @NotNull List<String> playerList(){
        List<String> list = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            list.add(p.getName());
        }
        return list;
    }
}
