package killercreepr.cruxmenus.menu.bukkit.module.standard;

import killercreepr.crux.context.DummyInputContext;
import killercreepr.crux.context.InputContext;
import killercreepr.crux.context.SimpleInputContext;
import killercreepr.crux.valueproviders.number.NumberProvider;
import killercreepr.crux.valueproviders.number.UniformNumber;
import killercreepr.crux.valueproviders.number.UniformNumberArray;
import killercreepr.cruxmenus.menu.bukkit.CfgMenu;
import killercreepr.cruxmenus.menu.bukkit.Menu;
import killercreepr.cruxmenus.menu.bukkit.module.ActiveMenuModule;
import killercreepr.cruxmenus.menu.bukkit.module.config.CfgMenuModule;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class PagedMenuModule<T> extends CfgMenuModule {
    protected final @NotNull NumberProvider indexes;
    public PagedMenuModule(@NotNull Key key, @NotNull String id, @NotNull NumberProvider indexes) {
        super(key, id);
        this.indexes = indexes;
    }

    @Override
    public @Nullable ActiveMenuModule build(@NotNull Menu menu, @NotNull String id) {
        return build(menu, id, parseIndexes(menu));
    }

    public abstract @Nullable ActiveMenuModule build(@NotNull Menu menu, @NotNull String id, @NotNull List<Integer> indexes);

    public @NotNull List<Integer> parseIndexes(@NotNull Menu menu){
        List<Integer> list = new ArrayList<>();
        InputContext ctx;
        if(menu instanceof CfgMenu cfg){
            ctx = new SimpleInputContext(cfg.getHolder().getRegistry().getFormat(), cfg.buildTags());
        }else ctx = new DummyInputContext();

        if(indexes instanceof UniformNumberArray array){
            for(NumberProvider p : array.getNumbers()){
                list.add(p.sample(ctx).intValue());
            }
            return list;
        }
        if(indexes instanceof UniformNumber random){
            for(int i = random.getMinInclusive().sample(ctx).intValue(); i <= random.getMaxInclusive().sample(ctx).intValue(); i++){
                list.add(i);
            }
            return list;
        }
        list.add(indexes.sample(ctx).intValue());
        return list;
    }
}
