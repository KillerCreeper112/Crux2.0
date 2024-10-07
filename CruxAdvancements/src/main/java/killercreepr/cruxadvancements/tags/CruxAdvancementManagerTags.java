package killercreepr.cruxadvancements.tags;

import killercreepr.crux.tags.TagParser;
import killercreepr.crux.tags.container.TagContainer;
import killercreepr.crux.tags.context.FormatPrefix;
import killercreepr.crux.tags.hook.ObjectTag;
import killercreepr.crux.tags.resolver.StringResolver;
import killercreepr.crux.tags.resolver.Tag;
import killercreepr.cruxadvancements.advancement.CruxAdvancement;
import killercreepr.cruxadvancements.advancement.progression.CruxAdvancementProgress;
import killercreepr.cruxadvancements.manager.CruxAdvancementManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CruxAdvancementManagerTags implements ObjectTag<CruxAdvancementManager<?>> {
    @Override
    public @NotNull Class<CruxAdvancementManager<?>> getObjectType() {
        return (Class<CruxAdvancementManager<?>>) (Class<?>) CruxAdvancementManager.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("advancement_manager_");
    }

    @Override
    public @Nullable TagContainer<StringResolver> requestStrings(@NotNull CruxAdvancementManager<?> object, @NotNull TagParser tags) {
        return TagContainer.string(tags)
            .add(Tag.string("advancements", (args, ctx) -> object.getAdvancements().values().size() + ""))
            .add(Tag.string("advancements_completed", (args, ctx) ->{
                UUID uuid = UUID.fromString(ctx.deserializeString(args.get(0)));
                int amount = 0;
                for(CruxAdvancement a : object){
                    CruxAdvancementProgress progress = a.getProgressIfPresent(uuid);
                    if(progress == null || !progress.isDone()) continue;
                    amount++;
                }
                return amount + "";
            }))
            ;
    }
}
