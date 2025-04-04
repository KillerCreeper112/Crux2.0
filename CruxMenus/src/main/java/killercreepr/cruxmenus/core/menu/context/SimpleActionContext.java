package killercreepr.cruxmenus.core.menu.context;

import killercreepr.crux.api.data.DataExchange;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.tags.container.MergedTagContainer;
import killercreepr.cruxmenus.api.menu.CfgMenu;
import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import killercreepr.cruxmenus.api.menu.item.MenuItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;

public class SimpleActionContext extends SimpleMenuContext implements ActionContext {
    protected final @NotNull MenuItem item;
    protected final @NotNull InventoryClickEvent event;
    protected final @NotNull HumanEntity player;
    protected final @NotNull TextParserContext parserCtx;
    public SimpleActionContext(@NotNull CfgMenu menu, @NotNull DataExchange info, @NotNull MergedTagContainer resolvers,
                               @NotNull MenuItem item, @NotNull InventoryClickEvent event, @NotNull TextParserContext parserCtx) {
        super(menu, info, resolvers);
        this.item = item;
        this.event = event;
        this.player = event.getWhoClicked();
        this.parserCtx = parserCtx;
    }
    public SimpleActionContext(@NotNull CfgMenu menu, @NotNull DataExchange info, @NotNull MergedTagContainer resolvers,
                               @NotNull MenuItem item, @NotNull InventoryClickEvent event) {
        super(menu, info, resolvers);
        this.item = item;
        this.event = event;
        this.player = event.getWhoClicked();
        this.parserCtx = TextParserContext.builder()
            .format(menu.getHolder().getRegistry().getFormat())
            .tags(getAllMergedResolvers())
            .build();
    }

    @Override
    public @NotNull HumanEntity getPlayer() {
        return player;
    }

    public @NotNull InventoryClickEvent getEvent() {
        return event;
    }

    public @NotNull MenuItem getItem() {
        return item;
    }

    @Override
    public @NotNull MergedTagContainer getAllMergedResolvers() {
        return super.getAllMergedResolvers().addAll(item.buildTags());
    }

    @Override
    public @NotNull TextParserContext parserCtx() {
        return parserCtx;
    }
}
