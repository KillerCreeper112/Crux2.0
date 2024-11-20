package killercreepr.cruxadvancements.core.config.handler;

import killercreepr.crux.api.registry.MappedRegistry;
import killercreepr.crux.core.registry.SimpleMappedRegistry;
import killercreepr.cruxadvancements.api.advancement.reward.CruxAdvanceReward;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FileCruxAdvanceReward implements FileObjectHandler<CruxAdvanceReward> {
    public static final MappedRegistry<String, CustomFileCruxAdvanceReward<?>> CUSTOM_HANDLERS = new SimpleMappedRegistry<>();
    public static void registerCustomHandler(@NotNull CustomFileCruxAdvanceReward<?> handler){
        CUSTOM_HANDLERS.register(handler.getType(), handler);
    }
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull CruxAdvanceReward object) {
        throw new UnsupportedOperationException("unsupported");
    }

    @Override
    public @Nullable CruxAdvanceReward deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileObject base)) return null;

        String type = base.getObject(String.class, "reward");
        if(type==null) return null;
        type = type.toLowerCase();
        CustomFileCruxAdvanceReward<?> handler = CUSTOM_HANDLERS.get(type);
        if(handler==null) throw new IllegalStateException("CruxAdvanceReward type " + type + " does not exist!");

        return handler.deserializeFromFile(ctx, base);
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "crux_advance_reward";
    }
}
