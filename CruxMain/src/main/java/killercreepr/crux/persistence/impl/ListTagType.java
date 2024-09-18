package killercreepr.crux.persistence.impl;

import com.google.common.collect.Lists;
import net.minecraft.nbt.ListTag;
import org.bukkit.persistence.ListPersistentDataType;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ListTagType<P, C> implements ListPersistentDataType<P, C> {
    public static ListTagType<?, ?> listUnchecked(@NotNull PersistentDataType<?,  ?> typeType){
        return new ListTagType<>(typeType);
    }

    protected final @NotNull PersistentDataType<P, C> tagType;
    public ListTagType(@NotNull PersistentDataType<P, C> tagType) {
        this.tagType = tagType;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Class<List<P>> getPrimitiveType() {
        return (Class<List<P>>) (Object) List.class;
    }

    @NotNull
    @Override
    @SuppressWarnings("unchecked")
    public Class<List<C>> getComplexType() {
        return (Class<List<C>>) (Object) List.class;
    }

    @NotNull
    @Override
    public List<P> toPrimitive(@NotNull final List<C> complex, @NotNull final PersistentDataAdapterContext context) {
        return Lists.transform(complex, s -> tagType.toPrimitive(s, context));
    }

    @NotNull
    @Override
    public List<C> fromPrimitive(@NotNull final List<P> primitive, @NotNull final PersistentDataAdapterContext context) {
        return Lists.transform(primitive, s -> tagType.fromPrimitive(s, context));
    }

    @NotNull
    @Override
    public PersistentDataType<P, C> elementType() {
        return this.tagType;
    }
}
