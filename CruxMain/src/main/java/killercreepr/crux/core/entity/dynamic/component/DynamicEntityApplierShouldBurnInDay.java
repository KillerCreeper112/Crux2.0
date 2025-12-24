package killercreepr.crux.core.entity.dynamic.component;

import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.api.entity.dynamic.DynamicEntityApplierSingleComponent;
import killercreepr.crux.api.text.context.TextParserContext;
import org.bukkit.entity.*;
import org.jetbrains.annotations.NotNull;

public class DynamicEntityApplierShouldBurnInDay extends DynamicEntityApplierSingleComponent {
    public DynamicEntityApplierShouldBurnInDay(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "should_burn_in_day";
    }

    @Override
    public void apply(@NotNull CruxEntity entity, @NotNull TextParserContext ctx) {
        var e = entity.entity();
        if(e instanceof Zombie z){
            z.setShouldBurnInDay(parseBool(ctx));
        }else if(e instanceof AbstractSkeleton z){
            z.setShouldBurnInDay(parseBool(ctx));
        }
    }
}
