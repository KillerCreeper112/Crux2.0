package killercreepr.cruxmenu.menu.bukkit;

import killercreepr.crux.item.dynamic.DynamicItem;
import killercreepr.crux.registry.Registry;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.container.MultiTagContainer;
import killercreepr.crux.tags.context.FormatParserContext;
import killercreepr.crux.tags.format.Format;
import killercreepr.crux.util.CruxMath;
import killercreepr.crux.util.CruxString;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxmenu.menu.bukkit.actions.ActionContext;
import killercreepr.cruxmenu.menu.bukkit.actions.MenuAction;
import killercreepr.cruxmenu.menu.bukkit.api.events.menu.MenuItemClickEvent;
import killercreepr.cruxmenu.menu.bukkit.holder.ClickActions;
import killercreepr.cruxmenu.menu.bukkit.holder.MenuItemHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuItem {
    protected final @NotNull MenuItemHolder base;
    protected final @NotNull MenuContext info;
    public MenuItem(@NotNull MenuItemHolder base, @NotNull MenuContext info) {
        this.base = base;
        this.info = info;
    }

    public @NotNull Format getFormat(){
        return info.getMenu().getHolder().getRegistry().getFormat();
    }

    public @NotNull Optional<Integer> getSlot(){
        NumberProvider provider = base.info().getObject("slot", NumberProvider.class).orElse(null);
        if(provider != null) return Optional.of(
                provider.value(this::setPlaceholders).intValue()
        );
        return Optional.empty();
    }

    public boolean canDisplay(){
        String viewRequirement = base.info().getObject("view_requirement", String.class).orElse(null);
        if(viewRequirement == null) return true;
        return CruxMath.evaluateEvalEx(setPlaceholders(viewRequirement));
    }

    public @NotNull String setPlaceholders(@NotNull String text){
        return info.getMenu().getHolder().getRegistry().getFormat().deserializeString(text, buildTags());//todo may need to check this
    }

    public @NotNull MergedTagContainer buildTags(){
        MergedTagContainer resolvers = new MultiTagContainer(info.getResolvers().getTagParser());
        resolvers.hookAll(info.getMenu().getHolder().info());
        resolvers.addAll(info.getMenu().buildTags());
        resolvers.addAll(info.getMenu().buildTags());
        resolvers.hookAll(base.info());
        resolvers.hookAll(info.getInfo());
        return resolvers;
    }

    public @Nullable ItemStack buildItem(@NotNull Player p){
        DynamicItem item = base.getItem().value();
        if(item == null) return null;
        item = item.clone();
        MergedTagContainer tags = buildTags();
        tags.hookAll(info.getInfo());

        FormatParserContext context = new FormatParserContext.Builder(getFormat())
            .viewer(p)
            .tags(tags)
            .build();
        return item.buildItem(context);
    }

    public @NotNull MenuItemClickEvent click(@NotNull Player p, @NotNull InventoryClickEvent event){
        ActionContext actionInfo = new ActionContext(info.getMenu(), info.getInfo(), info.getResolvers(), this, event);
        MenuItemClickEvent clickEvent = new MenuItemClickEvent(p, actionInfo.getMenu(), this, actionInfo, base.getClickActions());
        if(!clickEvent.callEvent()) return clickEvent;

        ClickActions actions = clickEvent.getClickActions();
        if(actions == null) return clickEvent;

        ActionContext context = clickEvent.getContext();
        actions.getOrDefault(event.getClick(), List.of()).forEach(s -> performAction(p, s, context));
        return clickEvent;
    }

    public static final Pattern ACTION_PATTERN = Pattern.compile("\\[(.*?)]");
    private static @Nullable String extractAction(@NotNull String input) {
        Matcher matcher = ACTION_PATTERN.matcher(input);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return null;
    }

    public boolean performAction(@NotNull Player p, @NotNull String action, @NotNull ActionContext actionInfo){
        return performAction(p, action, actionInfo, info.getMenu().getHolder().getRegistry().MENU_ACTIONS);
    }

    public boolean performAction(@NotNull Player p, @NotNull String action,
                                 @NotNull ActionContext actionInfo, @NotNull Registry<MenuAction> actions){
        String actionName = extractAction(action);
        if(actionName == null) return false;
        String result = getFormat().deserializeString(action.replaceFirst("\\[.*?]\\s*", ""),
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

    public @NotNull MenuContext info() {
        return info;
    }

}
