package killercreepr.crux.menu.bukkit.actions.custom;

import killercreepr.crux.menu.bukkit.actions.ActionInfo;
import killercreepr.crux.menu.bukkit.actions.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundAction extends SimpleMenuAction {
    public SoundAction(@NotNull NamespacedKey key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionInfo actionInfo, @NotNull String[] args) {
        String[] keyArgs = args[0].split(":");
        Key key = keyArgs.length > 1 ? Key.key(keyArgs[0], keyArgs[1]) : Key.key("minecraft", keyArgs[0]);
        float pitch = 1f;
        if(args.length > 1){
            try{ pitch = Float.parseFloat(args[1]); } catch (IllegalArgumentException ignored){}
        }
        p.playSound(Sound.sound(key, Sound.Source.MASTER, 2f, pitch));
        return true;
    }
}
