package killercreepr.crux;

import killercreepr.crux.data.entity.EntityMemory;
import killercreepr.crux.data.tick.CruxTick;
import killercreepr.crux.module.CruxModule;
import killercreepr.crux.plugin.CruxPlugin;
import killercreepr.crux.registries.Registries;
import killercreepr.crux.registry.KeyedRegistry;
import killercreepr.crux.registry.Registry;
import killercreepr.crux.tags.Tags;
import killercreepr.crux.tags.format.Format;
import killercreepr.crux.tags.minimessage.*;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Crux {
    public static final String NAMESPACE = "crux";
    public static final Tags TAGS = new Tags();
    public static final Format FORMAT = new Format(
            MiniMessage.builder()
                    .tags(TagResolver.builder()
                            .resolvers(TagResolver.standard(),
                                    new CheckBoolTag(),
                                    new StringFormatTag(),
                                    new DateFormatTag(),
                                    new DurationTag(),
                                    new ConvertBoolTag())
                            .build()
                    ).build(), TAGS
    );
    private static final Logger log = Logger.getLogger(Crux.class.getName());

    public static @Nullable CruxPlugin mainPlugin;

    public static @Nullable CruxPlugin getMainPlugin() {
        return mainPlugin;
    }

    public static void setMainPlugin(@Nullable CruxPlugin mainPlugin) {
        Crux.mainPlugin = mainPlugin;
    }
    /*private final MenuRegistry MENU_REGISTRY = new MenuRegistry(FORMAT);

    public @NotNull MenuRegistry getMenuRegistry() {
        return MENU_REGISTRY;
    }*/

    /*public void onEnable() {
        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")){
            placeholderAPIHook = new PlaceholderAPIHook();
        }else placeholderAPIHook = null;

        *//*Registry<MenuAction> actions = MENU_REGISTRY.MENU_ACTIONS;
        actions.register(new OpenMenuAction(key("menu")));
        actions.register(new UpdateMenuAction(key("update")));
        actions.register(new SoundAction(key("sound")));
        actions.register(new CloseInventoryAction(key("close")));

        new MenuFolder(this, "menu", getMenuRegistry()).register();*//*

        new CClaimTags(this, FORMAT.getTags());

        //getServer().getPluginManager().registerEvents(new MenuListener(), this);
    }*/

    /*@EventHandler(ignoreCancelled = true)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        new MenuFolder(this, "menu", getMenuRegistry()).register();
    }

    @EventHandler(ignoreCancelled = true)
    public void onAsyncChat(AsyncChatEvent event) {
        Player p = event.getPlayer();
        String plain = PlainTextComponentSerializer.plainText().serialize(event.message());
        Bukkit.getScheduler().runTask(this, task ->{
            MenuHolder menu = MENU_REGISTRY.MENU_HOLDERS.get(Crux.key(plain));
            if(menu != null) menu.open(p);
        });
    }*/

    public static @NotNull BukkitRunnable buildTickRunnable(){
        return buildTickRunnable(Registries.TICKS);
    }

    public static @NotNull BukkitRunnable buildTickRunnable(@NotNull KeyedRegistry<CruxTick> registry){
        return new BukkitRunnable(){
            @Override
            public void run() {
                for(CruxTick t : new HashSet<>(registry.values())){
                    if(t.markedForRemoval()){
                        registry.remove(t.getKey());
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
