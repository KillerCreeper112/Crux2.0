package killercreepr.cruxmenus.menu.bukkit;

import killercreepr.crux.item.dynamic.DynamicItem;
import killercreepr.crux.registry.Registry;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.container.MultiTagContainer;
import killercreepr.crux.tags.context.FormatParserContext;
import killercreepr.crux.tags.format.Format;
import killercreepr.crux.tags.provider.StringTagProvider;
import killercreepr.crux.util.CruxItem;
import killercreepr.crux.util.CruxMath;
import killercreepr.crux.util.CruxString;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.cruxmenus.menu.bukkit.actions.ActionContext;
import killercreepr.cruxmenus.menu.bukkit.actions.MenuAction;
import killercreepr.cruxmenus.menu.bukkit.api.events.menu.MenuItemClickEvent;
import killercreepr.cruxmenus.menu.bukkit.holder.ClickActions;
import killercreepr.cruxmenus.menu.bukkit.holder.MenuItemHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MenuItem {
    protected final @NotNull MenuItemHolder base;
    protected final @NotNull MenuContext inputtedContext;
    protected final @NotNull MenuContext evaluatedContext;
    public MenuItem(@NotNull MenuItemHolder base, @NotNull MenuContext inputtedContext, @NotNull MenuContext evaluatedContext) {
        this.base = base;
        this.inputtedContext = inputtedContext;
        this.evaluatedContext = evaluatedContext;
    }

    public @NotNull Format getFormat(){
        return evaluatedContext.getMenu().getHolder().getRegistry().getFormat();
    }


    public @NotNull Optional<Integer> getSlot(){
        NumberProvider provider = base.info().getObject("slot", NumberProvider.class).orElse(null);
        if(provider != null) return Optional.of(
                provider.sample(text -> setPlaceholders(text)).intValue()
        );
        return Optional.empty();
    }

    public boolean canDisplay(){
        String viewRequirement = base.info().getObject("view_requirement", String.class).orElse(null);
        if(viewRequirement == null) return true;
        return CruxString.parseBoolean(
            CruxMath.evaluateEvalEx(setPlaceholders(viewRequirement))
        );
    }

    public @NotNull String setPlaceholders(@NotNull String text){
        return getFormat().deserializeString(text, buildTags());
    }

    public @NotNull MergedTagContainer buildTags(){
        MergedTagContainer resolvers = new MultiTagContainer(evaluatedContext.getResolvers().getTagParser());

        resolvers.addAll(inputtedContext.getResolvers());

        resolvers.hookAll(evaluatedContext.getMenu().getHolder().info());
        resolvers.addAll(evaluatedContext.getMenu().buildTags());

        resolvers.hook(base);
        resolvers.hook(MenuItem.this);

        resolvers.hookAll(base.info());
        resolvers.hookAll(inputtedContext.getInfo());
        resolvers.hookAll(evaluatedContext.getInfo());
        return resolvers;
    }

    public @Nullable ItemStack buildItem(@NotNull Player p){
        DynamicItem item = base.getItem().value();
        if(item == null) return null;
        item = item.clone();
        MergedTagContainer tags = buildTags();

        FormatParserContext context = new FormatParserContext.Builder(getFormat())
            .viewer(p)
            .tags(tags)
            .build();
        return item.buildItem(context);
    }

    public @NotNull CompletableFuture<CruxItem> buildItemCompletely(@NotNull Player p){
        DynamicItem item = base.getItem().value();
        if(item == null) return null;
        item = item.clone();
        MergedTagContainer tags = buildTags();

        FormatParserContext context = new FormatParserContext.Builder(getFormat())
            .viewer(p)
            .tags(tags)
            .build();
        return item.buildCompletely(context);
    }

    public @NotNull MenuItemClickEvent click(@NotNull Player p, @NotNull InventoryClickEvent event){
        ActionContext actionInfo = new ActionContext(
            evaluatedContext.getMenu(),
            inputtedContext.getInfo().append(evaluatedContext.getInfo()),
            buildTags(), this, event
        );
        MenuItemClickEvent clickEvent = new MenuItemClickEvent(p, actionInfo.getMenu(), this, actionInfo, base.getClickActions());
        if(!clickEvent.callEvent()) return clickEvent;

        ClickActions actions = clickEvent.getClickActions();
        if(actions == null) return clickEvent;

        ActionContext context = clickEvent.getContext();
        actions.clickOrDefault(event, List.of()).forEach(s -> performAction(p, s, context));
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
        return performAction(p, action, actionInfo, evaluatedContext.getMenu().getHolder().getRegistry().MENU_ACTIONS);
    }

    public boolean performAction(@NotNull Player p, @NotNull String action,
                                 @NotNull ActionContext actionInfo, @NotNull Registry<MenuAction> actions){
        String actionName = extractAction(action);
        if(actionName == null) return false;
        String result = getFormat().deserializeString(action.replaceFirst("\\[.*?]\\s*", ""),
                actionInfo.getAllMergedResolvers());
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


    public @NotNull MenuContext getInputtedContext() {
        return inputtedContext;
    }

    public @NotNull MenuContext getEvaluatedContext() {
        return evaluatedContext;
    }
}
