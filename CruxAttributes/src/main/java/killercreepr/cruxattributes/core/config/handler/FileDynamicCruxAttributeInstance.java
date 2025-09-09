package killercreepr.cruxattributes.core.config.handler;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.core.util.CruxObjects;
import killercreepr.cruxattributes.api.attribute.CruxAttribute;
import killercreepr.cruxattributes.api.attribute.CruxAttributeInstance;
import killercreepr.cruxattributes.api.attribute.CruxAttributeModifier;
import killercreepr.cruxattributes.api.attribute.DynamicCruxAttributeInstance;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

public class FileDynamicCruxAttributeInstance implements FileObjectHandler<DynamicCruxAttributeInstance> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull DynamicCruxAttributeInstance instance) {
        FileRegistry reg = ctx.getRegistry();
        FileObject o = new FileObject();
        o.add("attribute", reg.serializeToFile(instance.getAttribute().key()));
        o.add("modifiers", reg.serializeToFile(instance.getModifiers()));
        return o;
    }

    @Override
    public @Nullable DynamicCruxAttributeInstance deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry reg = ctx.getRegistry();
        CruxAttribute attribute = reg.deserializeFromFile(CruxAttribute.class, o.get("attribute"));
        Collection<CruxAttributeModifier> modifiers = reg.deserializeFromFile(
            new TypeToken<Collection<CruxAttributeModifier>>(){}.getType(),
            o.get("modifiers")
        );

        if(o.has("base_amount") && o.get("base_amount").isFilePrimitive()){
            if(modifiers == null) modifiers = new ArrayList<>();
            modifiers.add(CruxAttributeModifier.baseModifier(o.get("base_amount").getAsDouble()));
        }

        if(CruxObjects.checkNull(attribute, modifiers)) return null;
        return CruxAttributeInstance.dynamicInstance(attribute, modifiers);
    }
}
