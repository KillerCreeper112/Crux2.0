package killercreepr.crux.tags.container;

import killerceepr.crux.tags.tag.LoreResolver;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class LoreHookContainer implements TagsContainer<LoreResolver> {
    private final Map<String, LoreResolver> tags = new HashMap<>();
    public LoreHookContainer(@Nullable LoreHookContainer container){
        if(container == null) return;
        this.tags.putAll(container.get());
    }

    public LoreHookContainer(){}

    public TagsContainer<LoreResolver> putAll(@Nullable LoreHookContainer container){
        if(container != null) tags.putAll(container.get());
        return this;
    }

    @Override
    public TagsContainer<LoreResolver> put(@NotNull String id, @Nullable LoreResolver tag) {
        if(tag == null) return this;
        tags.put(id, tag);
        return this;
    }

    @Override
    public TagsContainer<LoreResolver> putIfAbsent(@NotNull String id, @Nullable LoreResolver tag) {
        if(tag == null) return this;
        tags.putIfAbsent(id, tag);
        return this;
    }

    @Override
    public TagsContainer<LoreResolver> remove(@NotNull String id) {
        tags.remove(id);
        return this;
    }

    @Override
    public boolean has(@NotNull String id) {
        return tags.containsKey(id);
    }

    @Override
    public @Nullable LoreResolver get(@NotNull String id) {
        return tags.getOrDefault(id, null);
    }

    @Override
    public @NotNull Map<String, LoreResolver> get() {
        return tags;
    }

    @Override
    public @NotNull LoreHookContainer clone() {
        return new LoreHookContainer(this);
    }
}
