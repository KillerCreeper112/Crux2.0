package killercreepr.crux;

import killercreepr.crux.handler.BlockHandler;
import killercreepr.crux.handler.ItemHandler;
import org.jetbrains.annotations.NotNull;

public interface CruxHandlers {
    @NotNull BlockHandler block();
    void setBlock(@NotNull BlockHandler block);
    @NotNull ItemHandler item();
    void setItem(@NotNull ItemHandler item);

    class Generic implements CruxHandlers{
        protected @NotNull ItemHandler item = new ItemHandler.Dummy();
        protected @NotNull BlockHandler block = new BlockHandler.Dummy();

        public @NotNull BlockHandler block() {
            return block;
        }

        public void setBlock(@NotNull BlockHandler block) {
            this.block = block;
        }

        public @NotNull ItemHandler item() {
            return item;
        }

        public void setItem(@NotNull ItemHandler item) {
            this.item = item;
        }
    }
}
