package killercreepr.cruxmenus.core.menu.item;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.item.CruxItem;
import killercreepr.crux.api.item.dynamic.DynamicItem;
import killercreepr.crux.api.registry.Registry;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.format.FormatSerializer;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.crux.api.text.tags.container.TagContainer;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.text.format.FormatParserContext;
import killercreepr.crux.core.text.resolver.Tag;
import killercreepr.crux.core.util.CruxString;
import killercreepr.cruxmenus.api.event.MenuItemClickEvent;
import killercreepr.cruxmenus.api.menu.action.MenuAction;
import killercreepr.cruxmenus.api.menu.action.click.ClickActions;
import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import killercreepr.cruxmenus.api.menu.contex.MenuContext;
import killercreepr.cruxmenus.api.menu.holder.MenuItemHolder;
import killercreepr.cruxmenus.api.menu.item.MenuItem;
import killercreepr.cruxmenus.api.menu.item.requirement.ViewCondition;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleMenuItem implements MenuItem {
    public static final String FORMAT_DATA_PREFIX = "menu_item/";

    protected final @NotNull MenuItemHolder base;
    protected final @NotNull MenuContext inputtedContext;
    protected final @NotNull MenuContext evaluatedContext;
    public SimpleMenuItem(@NotNull MenuItemHolder base, @NotNull MenuContext inputtedContext, @NotNull MenuContext evaluatedContext) {
        this.base = base;
        this.inputtedContext = inputtedContext;
        this.evaluatedContext = evaluatedContext;
    }

    public @NotNull FormatSerializer getFormat(){
        return evaluatedContext.getMenu().getHolder().getRegistry().getFormat();
    }


    public @NotNull Optional<List<Number>> getSlots(){
        NumberProvider provider = base.info().getObject("slot", NumberProvider.class).orElse(null);
        if(provider != null){
            MergedTagContainer tags = buildTags();
            return Optional.of(provider.sampleList(text -> getFormat().deserializeString(text, tags)));
        }
        return Optional.empty();
    }

    public boolean canDisplay(){
        ViewCondition viewCondition = base.info().getAny(ViewCondition.class, "view_requirement", "view_requirements", "view_condition", "view_conditions");
        if(viewCondition == null) return true;
        return viewCondition.test(TextParserContext.builder(getFormat())
            .tags(buildTags())
            .build());
        /*return CruxString.parseBoolean(
            CruxMath.evaluateEvalEx(setPlaceholders(viewRequirement))
        );*/
    }

    public @NotNull String setPlaceholders(@NotNull String text){
        return getFormat().deserializeString(text, buildTags());
    }

    public @NotNull MergedTagContainer buildTags(){
        MergedTagContainer resolvers = TagContainer.merged(evaluatedContext.getResolvers().getTagParser());

        resolvers.addAll(inputtedContext.getResolvers());

        resolvers.hookAll(evaluatedContext.getMenu().getHolder().info());
        resolvers.addAll(evaluatedContext.getMenu().buildTags());

        resolvers.hook(base);
        resolvers.hook(SimpleMenuItem.this);

        resolvers.addAll(buildPrimitiveTags(base.info()));
        resolvers.hookAll(base.info());
        resolvers.addAll(buildPrimitiveTags(inputtedContext.info()));
        resolvers.hookAll(inputtedContext.info());
        resolvers.hookAll(evaluatedContext.info());
        return resolvers;
    }

    public MergedTagContainer buildPrimitiveTags(DataExchange info){
        if(info.isEmpty()) return null;
        MergedTagContainer tags = TagContainer.merged();

        info.forEach((id, holder) ->{
            Object o = holder.value();
            if(o instanceof List<?> e){
                List<String> stringed;
                try{
                    stringed = (List<String>) o;
                }catch (Exception ignored){
                    stringed = new ArrayList<>();
                    List<String> finalStringed = stringed;
                    e.forEach(ee -> finalStringed.add(e + ""));
                }
                tags.add(Tag.parsed(FORMAT_DATA_PREFIX + id, stringed));
                return;
            }
            if(!isPrimitive(o)) return;
            tags.add(Tag.parsed(FORMAT_DATA_PREFIX + id, o + ""));
        });
        return tags;
    }
    public boolean isPrimitive(Object o){
        if(o instanceof String) return true;
        if(o instanceof Boolean) return true;
        if(o instanceof Number) return true;
        if(o instanceof Character) return true;
        return false;
    }

    public @Nullable ItemStack buildItem(@NotNull Entity p){
        DynamicItem item = base.getItem().value();
        if(item == null) return null;
        item = item.clone();
        MergedTagContainer tags = buildTags();

        FormatParserContext context = new FormatParserContext.Builder(getFormat())
            .viewer(p instanceof OfflinePlayer d ? d : null)
            .tags(tags)
            .build();
        return item.buildItem(context);
    }

    public @NotNull CompletableFuture<CruxItem> buildItemCompletely(@NotNull Entity p){
        DynamicItem item = base.getItem().value();
        if(item == null) return CompletableFuture.completedFuture(null);
        item = item.clone();
        MergedTagContainer tags = buildTags();

        FormatParserContext context = new FormatParserContext.Builder(getFormat())
            .viewer(p instanceof OfflinePlayer d ? d : null)
            .tags(tags)
            .build();
        return item.buildCompletely(context);
    }

    public @NotNull MenuItemClickEvent click(@NotNull Entity p, @NotNull InventoryClickEvent event){
        ActionContext actionInfo = ActionContext.context(
            evaluatedContext.getMenu(),
            inputtedContext.info().append(evaluatedContext.info()),
            buildTags(), this, event
        );
        MenuItemClickEvent clickEvent = new MenuItemClickEvent(p, actionInfo.getMenu(), this, actionInfo, base.getClickActions());
        if(!clickEvent.callEvent()) return clickEvent;

        ClickActions actions = clickEvent.getClickActions();
        if(actions == null) return clickEvent;

        ActionContext context = clickEvent.getContext();
        Collection<String> parsedActions = actions.clickOrDefault(event, List.of());
        if(parsedActions.isEmpty()) return clickEvent;
        actionInfo.parserCtx().deserializeStringList(parsedActions).forEach(s -> performAction(p, s, context));
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

    public boolean performAction(@NotNull Entity p, @NotNull String action, @NotNull ActionContext actionInfo){
        return performAction(p, action, actionInfo, evaluatedContext.getMenu().getHolder().getRegistry().menuActions());
    }

    public boolean performAction(@NotNull Entity p, @NotNull String action,
                                 @NotNull ActionContext actionInfo, @NotNull Registry<MenuAction> actions){
        String actionName = extractAction(action);
        if(actionName == null) return false;
        MergedTagContainer tags = actionInfo.getAllMergedResolvers();
        String result = getFormat().deserializeString(action.replaceFirst("\\[.*?]\\s*", ""), tags);
        for(MenuAction a : actions){
            if(!a.has(actionName)) continue;
            String[] args = CruxString.quoteSplit(result, " ");
            /*for(int i = 0; i < args.length; i++){
                String s = args[i];
                args[i] = getFormat().deserializeString(s, tags);
            }*/
            if(a.execute(actionInfo, args)) return true;
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
