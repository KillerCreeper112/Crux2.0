package killercreepr.cruxitems.core.config.handler;

import killercreepr.crux.api.component.DataComponentAccessor;
import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.component.parser.DataComponentDecoder;
import killercreepr.cruxconfig.config.bukkit.handler.impl.component.FileDataComponentAccessor;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import killercreepr.cruxitems.core.item.CfgItemType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FileCfgItemType implements FileObjectHandler<CfgItemType> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull CfgItemType object) {
        throw new UnsupportedOperationException("nope");
    }

    @Override
    public @Nullable CfgItemType deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject o)) return null;
        FileRegistry reg = ctx.getRegistry();
        Collection<TypedDataComponent<?>> parsedComponents = Set.of();
        if(o.get("crux_components") instanceof FileArray){
            var mappedComps = reg.deserializeFromFile(DataComponentAccessor.class, o.get("crux_components"));
            if(mappedComps != null){
                parsedComponents = new ArrayList<>();
                for(TypedDataComponent<?> comp : mappedComps){
                    parsedComponents.add(comp);
                }
            }
        }else{
            String unparsedComponents = reg.deserializeFromFile(String.class, o.get("crux_components"));
            if(unparsedComponents != null){
                parsedComponents = DataComponentDecoder.componentDecoder().parseComponents(unparsedComponents);
            }
        }

        DataComponentAccessor components = parsedComponents.isEmpty() ? null : new DataComponentAccessor.SimpleImmutable(parsedComponents);
        return new CfgItemType(components);
    }
}
