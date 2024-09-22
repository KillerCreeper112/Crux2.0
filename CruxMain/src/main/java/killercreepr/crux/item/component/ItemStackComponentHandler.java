package killercreepr.crux.item.component;

import killercreepr.crux.persistence.PersistenceComponentHandler;
import killercreepr.crux.util.CruxTag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public interface ItemStackComponentHandler extends PersistenceComponentHandler {
    @NotNull ItemStack getItem();

    @Override
    default @NotNull PersistentDataContainer getComponentsPersistentContainer() {
        ItemStack item = getItem();
        PersistentDataContainer container = CruxTag.get(item, "components", PersistentDataType.TAG_CONTAINER, null);
        if(container == null) container = item.getItemMeta().getPersistentDataContainer().getAdapterContext().newPersistentDataContainer();
        return container;
    }

    @Override
    default void onComponentsPersistentContainerChanged(@NotNull PersistentDataContainer data) {
        ItemStack item = getItem();
        CruxTag.set(item, "components", PersistentDataType.TAG_CONTAINER, data.isEmpty() ? null : data);
    }
}
