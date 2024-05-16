package killercreepr.crux;

import io.papermc.paper.event.player.AsyncChatEvent;
import killercreepr.crux.config.MenuFolder;
import killercreepr.crux.config.TestCfg;
import killercreepr.crux.hooks.PlaceholderAPIHook;
import killercreepr.crux.menu.actions.MenuAction;
import killercreepr.crux.menu.actions.custom.CloseInventoryAction;
import killercreepr.crux.menu.actions.custom.OpenMenuAction;
import killercreepr.crux.menu.actions.custom.SoundAction;
import killercreepr.crux.menu.actions.custom.UpdateMenuAction;
import killercreepr.crux.menu.holder.MenuHolder;
import killercreepr.crux.menu.listener.MenuListener;
import killercreepr.crux.menu.registry.MenuRegistry;
import killercreepr.crux.registry.Registry;
import killercreepr.crux.tags.Tags;
import killercreepr.crux.tags.defaults.CClaimTags;
import killercreepr.crux.tags.format.Format;
import killercreepr.crux.tags.minimessage.*;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Crux extends JavaPlugin implements Listener {
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

    private final MenuRegistry MENU_REGISTRY = new MenuRegistry(FORMAT);

    public @NotNull MenuRegistry getMenuRegistry() {
        return MENU_REGISTRY;
    }

    private static Crux instance;
    public static Crux inst(){ return instance; }

    private PlaceholderAPIHook placeholderAPIHook;

    public @Nullable PlaceholderAPIHook getPlaceholderAPIHook(){ return placeholderAPIHook; }

    @Override
    public void onEnable() {
        instance = this;
        if(getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")){
            placeholderAPIHook = new PlaceholderAPIHook();
        }else placeholderAPIHook = null;

        Registry<MenuAction> actions = MENU_REGISTRY.MENU_ACTIONS;
        actions.register(new OpenMenuAction(key("menu")));
        actions.register(new UpdateMenuAction(key("update")));
        actions.register(new SoundAction(key("sound")));
        actions.register(new CloseInventoryAction(key("close")));

        new MenuFolder(this, "menu", getMenuRegistry()).register();

        new CClaimTags(this, FORMAT.getTags());

        //todo remove test
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(new MenuListener(), this);

        TestCfg cfg = new TestCfg(this, "testayo");
        cfg.setup();
        log(Level.WARNING, cfg.TEST.get() + "");
    }

    @EventHandler(ignoreCancelled = true)
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
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);
    }

    public static void log(@NotNull Level level, @NotNull String msg){
        inst().getLogger().log(level, msg);
    }

    public static @NotNull NamespacedKey key(@NotNull String key){
        return key(key.split(":"));
    }

    public static @NotNull NamespacedKey key(@NotNull String[] key){
        return key.length > 1 ? new NamespacedKey(key[0], key[1]) : new NamespacedKey(inst(), key[0]);
    }

    public static @NotNull NamespacedKey minecraftKey(@NotNull String key){
        return minecraftKey(key.split(":"));
    }

    public static @NotNull NamespacedKey minecraftKey(@NotNull String[] key){
        return key.length > 1 ? new NamespacedKey(key[0], key[1]) : NamespacedKey.minecraft(key[0]);
    }

    public static @Nullable Player getPlayer(@NotNull String uuidOrName){
        if(uuidOrName.isBlank()) return null;
        Player p = Bukkit.getPlayer(uuidOrName);
        if(p == null){
            try{
                return Bukkit.getPlayer(UUID.fromString(uuidOrName));
            }catch (IllegalArgumentException ignored){}
        }
        return p;
    }

    public static @NotNull List<String> playerList(){
        List<String> list = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()){
            list.add(p.getName());
        }
        return list;
    }
}
