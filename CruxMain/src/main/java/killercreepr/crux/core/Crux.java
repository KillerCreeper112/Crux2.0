package killercreepr.crux.core;

import killercreepr.crux.api.CruxHandlers;
import killercreepr.crux.api.communication.lang.CreateLang;
import killercreepr.crux.api.data.CruxTick;
import killercreepr.crux.api.entity.memory.EntityMemory;
import killercreepr.crux.api.entity.memory.TickedDataHolder;
import killercreepr.crux.api.registry.KeyedRegistry;
import killercreepr.crux.api.scheduler.CruxScheduler;
import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.communication.lang.SimpleCreateLang;
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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

//todo maybe eventually separate bukkit/paper and crux
public final class Crux {
    public static short debug = 0;

    public final static CreateLang lang = new SimpleCreateLang();
    public static CreateLang lang(){
        return lang;
    }

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
            EntityMemory[] removeBuffer = new EntityMemory[64];
            int removeCount = 0;

            CruxTick[] tickedRemoveBuffer = new CruxTick[16];
            int tickedRemoveCount = 0;

            @Override
            public void run() {
                tickedRemoveCount = 0;
                for (CruxTick value : registry) {
                    if(value.markedForRemoval()){
                        tickedAddRemove(value);
                        continue;
                    }
                    value.tick();
                }
                tickedBufferRemove();

                removeCount = 0;
                for (EntityMemory holder : EntityMemory.REGISTRY) {
                    if(holder.tick()){
                        addRemove(holder);
                    }
                }
                bufferRemove();

                //EntityMemory.REGISTRY.values().removeIf(EntityMemory::tick);
            }
            void bufferRemove(){
                for (int i = 0; i < removeCount; i++) {
                    EntityMemory.REGISTRY.unregister(removeBuffer[i]);
                    removeBuffer[i] = null;
                }
                removeCount = 0;
            }
            void addRemove(EntityMemory holder) {
                if (removeCount == removeBuffer.length) {
                    EntityMemory[] newBuf =
                        new EntityMemory[removeBuffer.length * 2];
                    System.arraycopy(removeBuffer, 0, newBuf, 0, removeBuffer.length);
                    removeBuffer = newBuf;
                }
                removeBuffer[removeCount++] = holder;
            }

            void tickedBufferRemove(){
                for (int i = 0; i < tickedRemoveCount; i++) {
                    registry.unregister(tickedRemoveBuffer[i]);
                    tickedRemoveBuffer[i] = null;
                }
                tickedRemoveCount = 0;
            }
            void tickedAddRemove(CruxTick holder) {
                if (tickedRemoveCount == tickedRemoveBuffer.length) {
                    CruxTick[] newBuf =
                        new CruxTick[tickedRemoveBuffer.length * 2];
                    System.arraycopy(tickedRemoveBuffer, 0, newBuf, 0, tickedRemoveBuffer.length);
                    tickedRemoveBuffer = newBuf;
                }
                tickedRemoveBuffer[tickedRemoveCount++] = holder;
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
            EntityMemory[] removeBuffer = new EntityMemory[64];
            int removeCount = 0;

            CruxTick[] tickedRemoveBuffer = new CruxTick[16];
            int tickedRemoveCount = 0;
            @Override
            public void run() {
                tickedRemoveCount = 0;
                for (CruxTick value : registry) {
                    if(value.markedForRemoval()){
                        tickedAddRemove(value);
                        continue;
                    }
                    value.tick();
                }
                tickedBufferRemove();

                removeCount = 0;
                for (EntityMemory holder : EntityMemory.MAIN_THREAD_REGISTRY) {
                    if(holder.tick()){
                        addRemove(holder);
                    }
                }
                bufferRemove();

                /*registry.values().removeIf(t ->{
                    if(t.markedForRemoval()){
                        return true;
                    }
                    t.tick();
                    return false;
                });
                EntityMemory.MAIN_THREAD_REGISTRY.values().removeIf(EntityMemory::tick);*/
            }

            void bufferRemove(){
                for (int i = 0; i < removeCount; i++) {
                    EntityMemory.MAIN_THREAD_REGISTRY.unregister(removeBuffer[i]);
                    removeBuffer[i] = null;
                }
                removeCount = 0;
            }
            void addRemove(EntityMemory holder) {
                if (removeCount == removeBuffer.length) {
                    EntityMemory[] newBuf =
                        new EntityMemory[removeBuffer.length * 2];
                    System.arraycopy(removeBuffer, 0, newBuf, 0, removeBuffer.length);
                    removeBuffer = newBuf;
                }
                removeBuffer[removeCount++] = holder;
            }

            void tickedBufferRemove(){
                for (int i = 0; i < tickedRemoveCount; i++) {
                    registry.unregister(tickedRemoveBuffer[i]);
                    tickedRemoveBuffer[i] = null;
                }
                tickedRemoveCount = 0;
            }
            void tickedAddRemove(CruxTick holder) {
                if (tickedRemoveCount == tickedRemoveBuffer.length) {
                    CruxTick[] newBuf =
                        new CruxTick[tickedRemoveBuffer.length * 2];
                    System.arraycopy(tickedRemoveBuffer, 0, newBuf, 0, tickedRemoveBuffer.length);
                    tickedRemoveBuffer = newBuf;
                }
                tickedRemoveBuffer[tickedRemoveCount++] = holder;
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

    public static @NotNull NamespacedKey key(@NotNull String key) {
        int colon = key.indexOf(':');
        return colon == -1
          ? new NamespacedKey(NAMESPACE, key)
          : new NamespacedKey(key.substring(0, colon), key.substring(colon + 1));
    }

    public static @NotNull NamespacedKey key(@NotNull String[] key) {
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
