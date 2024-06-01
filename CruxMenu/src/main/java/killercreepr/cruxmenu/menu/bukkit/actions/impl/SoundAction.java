package killercreepr.cruxmenu.menu.bukkit.actions.impl;

import killercreepr.cruxmenu.menu.bukkit.actions.ActionContext;
import killercreepr.cruxmenu.menu.bukkit.actions.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundAction extends SimpleMenuAction {
    public SoundAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull Player p, @NotNull ActionContext actionInfo, @NotNull String[] args) {
        Key key = Key.key(args[0]);
        float pitch = 1f;
        if(args.length > 1){
            try{ pitch = Float.parseFloat(args[1]); } catch (IllegalArgumentException ignored){}
        }
        p.playSound(Sound.sound(key, Sound.Source.MASTER, 2f, pitch));
        return true;
    }
}
