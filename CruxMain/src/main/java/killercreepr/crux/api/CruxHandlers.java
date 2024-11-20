package killercreepr.crux.api;

import killercreepr.crux.api.handler.BlockHandler;
import killercreepr.crux.api.handler.EntityHandler;
import killercreepr.crux.api.handler.ItemHandler;
import killercreepr.crux.api.handler.SkullProvider;
import org.jetbrains.annotations.NotNull;

public interface CruxHandlers {
    @NotNull BlockHandler block();
    void setBlock(@NotNull BlockHandler block);
    @NotNull ItemHandler item();
    void setItem(@NotNull ItemHandler item);

    @NotNull
    EntityHandler entity();

    void setEntity(@NotNull EntityHandler entity);

    @NotNull
    SkullProvider skullProvider();
    void setSkullProvider(@NotNull SkullProvider provider);

    class Generic implements CruxHandlers{
        protected @NotNull ItemHandler item = new ItemHandler.Dummy();
        protected @NotNull BlockHandler block = new BlockHandler.Dummy();
        protected @NotNull SkullProvider skullProvider = new SkullProvider.Dummy();
        protected @NotNull EntityHandler entity = new EntityHandler.Dummy();

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
        public @NotNull EntityHandler entity() {
            return entity;
        }

        @Override
        public void setEntity(@NotNull EntityHandler entity) {
            this.entity = entity;
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
