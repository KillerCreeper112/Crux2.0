package killercreepr.cruxconfig.config.bukkit.handler.impl.component;

import killercreepr.crux.component.CruxComponents;
import killercreepr.crux.component.TypedDataComponent;
import killercreepr.crux.registry.MappedRegistry;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StandardCfgDataComponentTypes {
    public static void register(@NotNull MappedRegistry<String, FileDataComponentType<?>> registry){
        registry.register("hardness", new FileDataComponentType<Float>() {
            @Override
            public @Nullable TypedDataComponent<Float> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                return TypedDataComponent.create(
                    CruxComponents.HARDNESS,
                    e.getObject(Float.class, "hardness", 1f)
                );
            }
        });
    }
}
