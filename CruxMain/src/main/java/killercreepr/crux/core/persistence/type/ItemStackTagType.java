package killercreepr.crux.core.persistence.type;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;

public class ItemStackTagType implements PersistentDataType<String, ItemStack> {
    /**
     * Returns the primitive data type of this tag.
     *
     * @return the class
     */
    @Override
    public @NotNull Class<String> getPrimitiveType() {
        return String.class;
    }

    /**
     * Returns the complex object type the primitive value resembles.
     *
     * @return the class type
     */
    @Override
    public @NotNull Class<ItemStack> getComplexType() {
        return ItemStack.class;
    }

    /**
     * Returns the primitive data that resembles the complex object passed to
     * this method.
     *
     * @param complex the complex object instance
     * @param context the context this operation is running in
     * @return the primitive value
     */
    @Override
    public @NotNull String toPrimitive(@NotNull ItemStack complex, @NotNull PersistentDataAdapterContext context) {
        return Base64.getEncoder().encodeToString(complex.serializeAsBytes());
    }

    /**
     * Creates a complex object based of the passed primitive value
     *
     * @param primitive the primitive value
     * @param context   the context this operation is running in
     * @return the complex object instance
     */
    @Override
    public @NotNull ItemStack fromPrimitive(@NotNull String primitive, @NotNull PersistentDataAdapterContext context) {
        return ItemStack.deserializeBytes(Base64.getDecoder().decode(primitive));
    }
}
