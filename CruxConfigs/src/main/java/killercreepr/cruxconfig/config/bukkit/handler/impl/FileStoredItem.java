package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.item.StoredItem;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FilePrimitive;
import killercreepr.cruxconfig.config.common.handler.FileObjectHandler;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Base64;

public class FileStoredItem implements FileObjectHandler<StoredItem> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> ctx, @NotNull StoredItem object) {
        return new FilePrimitive(
            Base64.getEncoder().encodeToString(object.item().serializeAsBytes())
        );
    }

    @Override
    public @Nullable StoredItem deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
        if(!(e instanceof FileGeneric p)) return null;
        String string = p.getAsString();
        byte[] bytes;
        try{
            bytes = Base64.getDecoder().decode(string);
        }catch (IllegalArgumentException ignored){
            ignored.printStackTrace();
            return null;
        }
        return new StoredItem(ItemStack.deserializeBytes(bytes));
    }

    @Override
    public @NotNull String jsonSerializerID() {
        return "stored_item";
    }
}
