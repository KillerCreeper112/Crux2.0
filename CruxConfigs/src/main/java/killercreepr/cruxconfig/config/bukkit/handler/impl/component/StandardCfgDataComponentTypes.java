package killercreepr.cruxconfig.config.bukkit.handler.impl.component;

import killercreepr.crux.api.component.TypedDataComponent;
import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.component.CruxComponents;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileObject;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StandardCfgDataComponentTypes {
    public static void register(@NotNull MappedRegistry<Key, FileDataComponentType<?>> registry){
        registry.register(Crux.key("hardness"), new FileDataComponentType<Float>() {
            @Override
            public @Nullable TypedDataComponent<Float> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                return TypedDataComponent.create(
                    CruxComponents.HARDNESS,
                    e.getObject(Float.class, "hardness", 1f)
                );
            }
        });
        registry.register(Crux.key("unbreakable"), new FileDataComponentType<Boolean>() {
            @Override
            public @Nullable TypedDataComponent<Boolean> deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileObject e) {
                return TypedDataComponent.create(
                    CruxComponents.UNBREAKABLE,
                    e.getObject(Boolean.class, "unbreakable", false)
                );
            }
        });
    }
}
