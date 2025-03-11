package killercreepr.cruxitems.core.item.consume;

import killercreepr.cruxitems.api.item.consume.ItemConsumeResult;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemConsumeResultImpl implements ItemConsumeResult {
    public ItemConsumeResultImpl(Boolean cancel, @Nullable ItemStack resultItem, boolean successful, boolean replaceResultItem, @Nullable ItemStack item, boolean replaceItem) {
        this.cancel = cancel;
        this.resultItem = resultItem;
        this.successful = successful;
        this.replaceResultItem = replaceResultItem;
        this.item = item;
        this.replaceItem = replaceItem;
    }

    protected final Boolean cancel;
    protected final @Nullable ItemStack resultItem;
    protected final boolean successful;
    protected final boolean replaceResultItem;
    protected final @Nullable ItemStack item;
    protected final boolean replaceItem;

    @Override
    public @Nullable Boolean getCancelled() {
        return cancel;
    }

    @Override
    public boolean successful() {
        return successful;
    }

    @Override
    public boolean replaceResultItem() {
        return replaceResultItem;
    }

    @Override
    public boolean replaceItem() {
        return replaceItem;
    }

    @Override
    public ItemStack resultItem() {
        return resultItem;
    }

    @Override
    public ItemStack item() {
        return item;
    }

    public static class Builder implements ItemConsumeResult.Builder {
        protected Boolean cancel;
        protected boolean successful;
        protected ItemStack resultItem;
        protected ItemStack item;
        @Override
        public ItemConsumeResult.Builder cancel(Boolean cancel) {
            this.cancel = cancel;
            return this;
        }

        @Override
        public ItemConsumeResult.Builder successful(boolean successful) {
            this.successful = successful;
            return this;
        }

        @Override
        public ItemConsumeResult.Builder resultItem(ItemStack resultItem) {
            this.resultItem = resultItem;
            return this;
        }

        @Override
        public ItemConsumeResult.Builder item(ItemStack item) {
            this.item = item;
            return this;
        }

        @Override
        public ItemConsumeResult build() {
            return new ItemConsumeResultImpl(
                cancel, resultItem, successful, resultItem != null, item, item != null
            );
        }
    }
}
