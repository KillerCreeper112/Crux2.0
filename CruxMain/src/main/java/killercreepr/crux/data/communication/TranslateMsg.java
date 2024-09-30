package killercreepr.crux.data.communication;

import killercreepr.crux.data.StringIdentifiable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TranslateMsg implements StringIdentifiable {
    protected final @NotNull String id;
    protected final @Nullable Communicator defaultValue;

    public TranslateMsg(@NotNull String id){
        this(id, null);
    }

    public TranslateMsg(@NotNull String id, @Nullable Communicator defaultValue) {
        this.id = id;
        this.defaultValue = defaultValue;
    }

    @ApiStatus.Experimental
    public TranslateMsg(@Nullable Communicator defaultValue) {
        this.id = null;
        this.defaultValue = defaultValue;
    }

    public @Nullable Communicator defaultValue() {
        return defaultValue;
    }

    @Override
    public @NotNull String id() {
        return id;
    }

    @Override
    public String toString() {
        return "TranslateMsg={id=" + id + ", defaultValue=" + defaultValue + "}";
    }
}
