package killercreepr.cruxmenus.menu.bukkit;

import killercreepr.crux.data.DataExchange;
import killercreepr.crux.tags.container.MergedTagContainer;
import killercreepr.crux.tags.container.MultiTagContainer;
import org.jetbrains.annotations.NotNull;

public class MenuContext {
    protected final @NotNull CfgMenu menu;
    protected final @NotNull DataExchange info;
    protected final @NotNull MergedTagContainer resolvers;

    public MenuContext(@NotNull CfgMenu menu, @NotNull DataExchange info, @NotNull MergedTagContainer resolvers) {
        this.menu = menu;
        this.info = info;
        this.resolvers = resolvers;
    }

    public @NotNull CfgMenu getMenu() {
        return menu;
    }

    public @NotNull DataExchange getInfo() {
        return info;
    }

    public @NotNull MergedTagContainer getResolvers() {
        return resolvers;
    }

    public @NotNull DataExchange getAllMergedInfo(){
        return menu.getHolder().info()
            .append(menu.info())
            .append(info)
            ;
    }

    public @NotNull MergedTagContainer getAllMergedResolvers(){
        MergedTagContainer tags = new MultiTagContainer(menu.getHolder().getRegistry().getFormat().tags());
        tags.addAll(resolvers);
        tags.hookAll(getAllMergedInfo());
        return tags;
    }
}
