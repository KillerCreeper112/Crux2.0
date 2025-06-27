package killercreepr.crux.core;

import killercreepr.crux.api.CruxHandlers;
import killercreepr.crux.api.data.CruxTick;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.api.scheduler.CruxScheduler;
import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.plugin.CruxPlugin;
import killercreepr.crux.core.registries.CruxRegistries;
import killercreepr.crux.core.scheduler.CruxSimpleScheduler;
import killercreepr.crux.core.text.tags.standard.CruxStandardTags;
import killercreepr.crux.core.text.tags.standard.minimessage.*;
import net.kyori.adventure.key.Key;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//todo maybe eventually separate bukkit/paper and crux
public final class Crux {
    public static short debug = 0;


    public static String namespace(){
        return NAMESPACE;
    }
    public static final String NAMESPACE = "crux";
    public static TagParser tags(){
        return TAGS;
    }
    @Deprecated(forRemoval = true, since = "use tags()")
    public static final TagParser TAGS = TagParser.builder()
        .addTags(CruxStandardTags.buildObjectTags())
        .build();
    public static FormatSerializer format(){
        return FORMAT;
    }
    @Deprecated(forRemoval = true, since = "use format()")
    public static final FormatSerializer FORMAT = FormatSerializer.builder()
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
    private static CruxScheduler scheduler = new CruxSimpleScheduler();

    public static CruxScheduler scheduler() {
        return scheduler;
    }

    public static void setScheduler(@NotNull CruxScheduler scheduler) {
        Crux.scheduler = scheduler;
    }

    private static CruxPlugin mainPlugin;
    public static CruxPlugin getMainPlugin() {
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

    public static @NotNull BukkitRunnable buildTickTask(){
        return buildTickTask(CruxRegistries.TICK, true);
    }
    public static @NotNull BukkitRunnable buildMainThreadTickTask(){
        return buildMainThreadTickTask(CruxRegistries.MAIN_THREAD_TICK, true);
    }

    public static int getCurrentTick(){
        return getServer().getCurrentTick();
    }

    public static boolean isPrimaryThread(){
        return Crux.getServer().isGlobalTickThread();
    }

    public static @NotNull BukkitRunnable buildTickTask(@NotNull KeyedRegistry<CruxTick> registry, boolean includeEntityMemory){
        if(!includeEntityMemory){
            return new BukkitRunnable(){
                @Override
                public void run() {
                    registry.values().removeIf(t ->{
                        if(t.markedForRemoval()){
                            return true;
                        }
                        t.tick();
                        return false;
                    });
                }
            };
        }
        return new BukkitRunnable(){
            @Override
            public void run() {
                registry.values().removeIf(t ->{
                    if(t.markedForRemoval()){
                        return true;
                    }
                    t.tick();
                    return false;
                });
                EntityMemory.REGISTRY.values().removeIf(EntityMemory::tick);
            }
        };
    }

    public static @NotNull BukkitRunnable buildMainThreadTickTask(@NotNull KeyedRegistry<CruxTick> registry, boolean includeEntityMemory){
        if(!includeEntityMemory){
            return new BukkitRunnable(){
                @Override
                public void run() {
                    registry.values().removeIf(t ->{
                        if(t.markedForRemoval()){
                            return true;
                        }
                        t.tick();
                        return false;
                    });
                }
            };
        }
        return new BukkitRunnable(){
            @Override
            public void run() {
                registry.values().removeIf(t ->{
                    if(t.markedForRemoval()){
                        return true;
                    }
                    t.tick();
                    return false;
                });
                EntityMemory.MAIN_THREAD_REGISTRY.values().removeIf(EntityMemory::tick);
            }
        };
    }

    public static void log(@NotNull Level level, @NotNull String msg){
        log.log(level, msg);
    }
    public static void logInfo(@NotNull String msg){
        log.info(msg);
    }
    public static void logWarning(@NotNull String msg){
        log(Level.WARNING, msg);
    }
    public static void logError(@NotNull String msg){
        log(Level.SEVERE, msg);
    }

    public static String keyMinimalString(@NotNull Key key){
        if(key.namespace().equals(NAMESPACE)) return key.value();
        return key.asString();
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
