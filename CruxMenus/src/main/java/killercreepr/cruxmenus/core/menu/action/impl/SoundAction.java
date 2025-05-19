package killercreepr.cruxmenus.core.menu.action.impl;

import killercreepr.cruxmenus.api.menu.contex.ActionContext;
import killercreepr.cruxmenus.core.menu.action.SimpleMenuAction;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.entity.HumanEntity;
import org.jetbrains.annotations.NotNull;

public class SoundAction extends SimpleMenuAction {
    public SoundAction(@NotNull Key key) {
        super(key);
    }

    @Override
    public boolean execute(@NotNull ActionContext ctx, @NotNull String[] args) {
        HumanEntity p = ctx.getPlayer();
        Key key = Key.key(args[0]);
        float pitch = 1f;
        if(args.length > 1){
            try{ pitch = Float.parseFloat(args[1]); } catch (IllegalArgumentException ignored){}
        }
        float volume = 0.3f;
        if(args.length > 2){
            try{ volume = Float.parseFloat(args[2]); } catch (IllegalArgumentException ignored){}
        }
        p.playSound(Sound.sound(key, Sound.Source.MASTER, volume, pitch));
        return true;
    }
}
