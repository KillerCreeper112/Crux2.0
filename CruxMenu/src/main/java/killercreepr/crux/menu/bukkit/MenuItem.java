package killercreepr.crux.menu.bukkit;

import killercreepr.crux.Crux;
import killercreepr.crux.menu.bukkit.actions.ActionInfo;
import killercreepr.crux.menu.bukkit.actions.MenuAction;
import killercreepr.crux.menu.holder.MenuItemHolder;
import killercreepr.crux.tags.container.ObjectLoreHookContainer;
import killercreepr.crux.tags.container.ObjectStringHookContainer;
import killercreepr.crux.util.CruxMath;
import killercreepr.crux.util.CruxString;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuItem {
    protected final @NotNull MenuItemHolder base;
    protected final @NotNull MenuInfo info;
    public MenuItem(@NotNull MenuItemHolder base, @NotNull MenuInfo info) {
        this.base = base;
        this.info = info;
    }

    public @NotNull Optional<Integer> getSlot(){
        Number x = base.info().getObject("slot", Number.class).orElse(null);
        if(x != null) return Optional.of(x.intValue());
        String eq = base.info().getObject("slot", String.class).orElse(null);
        if(eq == null) return Optional.empty();
        return Optional.of((int) CruxMath.evaluate(setPlaceholders(eq)));
    }

    public boolean canDisplay(){
        String viewRequirement = base.info().getObject("view_requirement", String.class).orElse(null);
        if(viewRequirement == null) return true;
        return CruxMath.testExpression(setPlaceholders(viewRequirement));
    }

    public @NotNull String setPlaceholders(@NotNull String text){
        return info.getMenu().getHolder().getRegistry().getFormat().setPlaceholders(text, buildTags());
    }

    public @NotNull ObjectStringHookContainer buildTags(){
        ObjectStringHookContainer resolvers = new ObjectStringHookContainer(info.getResolvers().getTags());
        resolvers.hookAll(info.getMenu().getHolder().info());
        resolvers.putAll(info.getMenu().getTags());
        resolvers.hookAll(base.info());
        resolvers.hookAll(info.getInfo());
        return resolvers;
    }

    public @Nullable ItemStack buildItem(@NotNull Player p){
        ItemStack item = base.getItem().value();
        if(item == null) return null;
        item = item.clone();
        if(base.getDisplayName() != null){
            ItemMeta meta = item.getItemMeta();
            if(meta != null){
                meta.displayName(
                        Component.empty().decoration(TextDecoration.ITALIC, false)
                                .append(Crux.FORMAT.deserialize(p, null, base.getDisplayName(), buildTags()))
                );
                item.setItemMeta(meta);
            }
        }
        if(base.getDisplayLore() != null){
            List<Component> lore = item.lore();
            if(lore == null) lore = new ArrayList<>();
            ObjectLoreHookContainer loreResolvers = new ObjectLoreHookContainer(info.getResolvers().getTags(), buildTags());
            loreResolvers.hookAll(info.getInfo());
            List<Component> add = new ArrayList<>();
            for(String s : base.getDisplayLore()){
                List<String> list = Crux.FORMAT.deserializeLore(p, null, s, loreResolvers);
                if(list == null){
                    list = new ArrayList<>();
                    list.add(s);
                }
                for(String format : list){
                    add.add(Component.empty().decoration(TextDecoration.ITALIC, false)
                            .append(Crux.FORMAT.deserialize(p, null, format, buildTags())));
                }
            }
            lore.addAll(add);
            item.lore(lore);
        }
        return item;
    }

    public void click(@NotNull Player p, @NotNull InventoryClickEvent event){
        if(base.getClickActions() == null) return;
        ActionInfo actionInfo = new ActionInfo(info.getMenu(), info.getInfo(), info.getResolvers(), this, event);
        base.getClickActions().getOrDefault(event.getClick(), List.of()).forEach(s -> performAction(p, s, actionInfo));
    }

    public static final Pattern ACTION_PATTERN = Pattern.compile("\\[(.*?)]");
    private static @NotNull String extractAction(@NotNull String input) {
        Matcher matcher = ACTION_PATTERN.matcher(input);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    public boolean performAction(@NotNull Player p, @NotNull String action, @NotNull ActionInfo actionInfo){
        return performAction(p, action, actionInfo, info.getMenu().getHolder().getRegistry().MENU_ACTIONS);
    }

    public boolean performAction(@NotNull Player p, @NotNull String action,
                                 @NotNull ActionInfo actionInfo, @NotNull Registry<MenuAction> actions){
        String actionName = extractAction(action);
        String result = Crux.FORMAT.setPlaceholders(action.replaceFirst("\\[.*?]\\s*", ""),
                actionInfo.getResolvers());
        for(MenuAction a : actions){
            if(!a.has(actionName)) continue;
            String[] args = CruxString.quoteSplit(result, " ");
            if(a.execute(p, actionInfo, args)) return true;
        }
        return false;
    }

    public @NotNull MenuItemHolder getBase() {
        return base;
    }

    public @NotNull MenuInfo info() {
        return info;
    }

}
