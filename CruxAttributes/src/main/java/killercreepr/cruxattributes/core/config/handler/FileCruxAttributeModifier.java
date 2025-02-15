package killercreepr.cruxattributes.core.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.core.util.CruxObjects;
import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.api.equipment.CruxSlotGroup;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class FileCruxAttributeModifier implements FileObjectHandler<CruxAttributeModifier> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CruxAttributeModifier mod) {
        FileRegistry reg = ctx.getRegistry();
        FileObject o = new FileObject();
        o.add("key", reg.serializeToFile(mod.key()));
        o.addProperty("amount", mod.getAmount());
        o.add("slot", reg.serializeToFile(mod.getSlotGroup()));
        o.add("operation", reg.serializeToFile(mod.getOperation()));
        Key[] path = mod.getPath();
        if(path != null && path.length > 0){
            o.add("path", reg.serializeToFile(Arrays.asList(path)));
        }
        return o;
    }

    @Override
    public @Nullable CruxAttributeModifier deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry reg = ctx.getRegistry();
        Key key = reg.deserializeFromFile(Key.class, o.get("key"));
        Number amount = reg.deserializeFromFile(Number.class, o.get("amount"));
        CruxSlotGroup slot = reg.deserializeFromFile(CruxSlotGroup.class, o.get("slot"));
        CruxAttribute.Operation operation = reg.deserializeFromFile(CruxAttribute.Operation.class, o.get("operation"));
        if(CruxObjects.checkNull(key, amount, slot, operation)) return null;

        List<Key> path = reg.deserializeFromFile(new TypeToken<List<Key>>(){}.getType(), o.get("path"));

        CruxAttributeModifier mod = CruxAttributeModifier.modifier(
            key, amount.doubleValue(), operation, slot
        );
        if(path != null && !path.isEmpty()) mod = mod.withPath(path.toArray(new Key[0]));
        return mod;
    }
}
