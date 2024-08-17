package killercreepr.crux.data.entity;

import net.kyori.adventure.key.Key;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PlayerTickedDataHolder extends PlayerDataHolder implements PlayerTickDataHolder{
    public PlayerTickedDataHolder(@NotNull Key key, @NotNull PlayerMemory parent) {
        super(key, parent);
    }

    @Override
    public final boolean shouldRemoveFromMemory(@Nullable Entity e) {
        throw new IllegalStateException("Entity function used on a Player-Only DataHolder");
    }

    @Override
    public final void removing(@Nullable Entity e) {
        throw new IllegalStateException("Entity function used on a Player-Only DataHolder");
    }

    @Override
    public final void tick(@NotNull Entity e) {
        throw new IllegalStateException("Entity function used on a Player-Only DataHolder");
    }

    @Override
    public boolean shouldRemoveFromMemory(@Nullable Player e) {
        return false;
    }

    @Override
    public void removing(@Nullable Player e) {
        removingFromMemory(e);
    }

    @Override
    protected void removingFromMemory(@Nullable Entity e) {
        super.removingFromMemory(e);
        if(!(e instanceof Player p)) return;
        removingFromMemory(p);
    }

    protected void removingFromMemory(@Nullable Player e){}


    /**
     * Most ticked holders probably won't want to tick after
     * the player has left.
     */
    @Override
    public void tick(@NotNull Player e) {
        if(parent.quit() != null) return;
        onTick(e);
    }

    protected void onTick(@NotNull Player e){
    }
}
