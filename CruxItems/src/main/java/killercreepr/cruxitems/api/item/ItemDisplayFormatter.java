package killercreepr.cruxitems.api.item;

import killercreepr.cruxitems.api.values.ValuesProvider;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ItemDisplayFormatter {
    @Nullable List<String> getLoreFormat(@NotNull ItemStack item);
    @Nullable String getNameFormat(@NotNull ItemStack item);

    class General implements ItemDisplayFormatter{
        protected final ValuesProvider values;

        public General(ValuesProvider values) {
            this.values = values;
        }

        @Override
        public @Nullable List<String> getLoreFormat(@NotNull ItemStack item) {
            return values.generalItemLoreFormat().value();
        }

        @Override
        public @Nullable String getNameFormat(@NotNull ItemStack item) {
            return values.generalItemNameFormat().value();
        }
    }
}
