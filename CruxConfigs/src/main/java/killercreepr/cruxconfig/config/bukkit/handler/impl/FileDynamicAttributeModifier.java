package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.item.dynamic.component.attribute.DynamicAttributeModifier;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileDynamicAttributeModifier extends SimpleFileHandler<DynamicAttributeModifier> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull DynamicAttributeModifier object) {
        FileRegistry reg = ctx.getRegistry();
        FileObject o = new FileObject();
        Object value = object.getKey();
        if(value != null) o.add("key", reg.serializeToFile(value));
        o.add("amount", reg.serializeToFile(object.getAmount()));
        value = object.getOperation();
        if(value != null) o.add("operation", reg.serializeToFile(value));
        value = object.getEquipmentSlotGroup();
        if(value != null) o.add("slot", reg.serializeToFile(value));
        return o;
    }

    @Override
    public @Nullable DynamicAttributeModifier deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry reg = ctx.getRegistry();

        NumberProvider amount = reg.deserializeFromFileOrDefault(NumberProvider.class, o.get("amount"), NumberProvider.zero());
        /*FileElement amountElement = o.get("amount");
        if(amountElement instanceof FilePrimitive prim){
            if(prim.isNumber()) amount = prim.getAsNumber();
            else amount = prim.getAsString();
        }else amount = reg.deserializeFromFile(String.class, amountElement);*/

        return new DynamicAttributeModifier(
            reg.deserializeFromFile(String.class, o.get("key")),
            amount,
            reg.deserializeFromFile(String.class, o.get("operation")),
            reg.deserializeFromFile(String.class, o.get("slot"))
        );
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "dynamic_attribute_modifier";
    }
}
