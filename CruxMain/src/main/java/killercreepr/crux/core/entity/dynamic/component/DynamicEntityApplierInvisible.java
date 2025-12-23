package killercreepr.crux.core.entity.dynamic.component;

import killercreepr.crux.api.entity.CruxEntity;
import killercreepr.crux.api.entity.dynamic.DynamicEntityApplierComponent;
import killercreepr.crux.api.entity.dynamic.DynamicEntityApplierSingleComponent;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.util.CruxString;
import org.jetbrains.annotations.NotNull;

public class DynamicEntityApplierInvisible extends DynamicEntityApplierSingleComponent {

    public DynamicEntityApplierInvisible(@NotNull Object value) {
        super(value);
    }

    @Override
    public @NotNull String name() {
        return "invisible";
    }

    @Override
    public void apply(@NotNull CruxEntity entity, @NotNull TextParserContext ctx) {
        entity.entity().setInvisible(CruxString.parseBoolean(
            ctx.input(value + "")
        ));
    }
}
