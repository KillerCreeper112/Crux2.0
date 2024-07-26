package killercreepr.crux;

import killercreepr.crux.handler.BlockHandler;
import killercreepr.crux.handler.ItemHandler;
import killercreepr.crux.handler.SkullProvider;
import org.jetbrains.annotations.NotNull;

public interface CruxHandlers {
    @NotNull BlockHandler block();
    void setBlock(@NotNull BlockHandler block);
    @NotNull ItemHandler item();
    void setItem(@NotNull ItemHandler item);

    @NotNull
    SkullProvider skullProvider();
    void setSkullProvider(@NotNull SkullProvider provider);

    class Generic implements CruxHandlers{
        protected @NotNull ItemHandler item = new ItemHandler.Dummy();
        protected @NotNull BlockHandler block = new BlockHandler.Dummy();
        protected @NotNull SkullProvider skullProvider = new SkullProvider.Dummy();

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

        @Override
        public @NotNull SkullProvider skullProvider() {
            return skullProvider;
        }

        @Override
        public void setSkullProvider(@NotNull SkullProvider provider) {
            this.skullProvider = provider;
        }
    }
}
