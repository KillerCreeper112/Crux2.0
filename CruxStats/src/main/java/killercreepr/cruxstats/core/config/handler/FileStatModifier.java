package killercreepr.cruxstats.core.config.handler;

import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxstats.api.stat.CruxStat;
import killercreepr.cruxstats.api.stat.CruxStatModifier;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class FileStatModifier implements FileObjectHandler<CruxStatModifier> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CruxStatModifier o) {
        FileRegistry reg = ctx.getRegistry();
        return new FileObject()
            .add("key", reg.serializeToFile(o.key()))
            .addProperty("amount", o.getAmount())
            .add("operation", reg.serializeToFile(o.getOperation()))
            ;
    }

    @Override
    public @Nullable CruxStatModifier deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry reg = ctx.getRegistry();
        Key key = reg.deserializeFromFile(Key.class, o.get("key"));
        Number number = reg.deserializeFromFile(Number.class, o.get("amount"));
        CruxStat.Operation operation = reg.deserializeFromFile(CruxStat.Operation.class, o.get("operation"));
        if(key == null || number == null || operation == null) return null;
        return CruxStatModifier.modifier(key, number.doubleValue(), operation);
    }
}
