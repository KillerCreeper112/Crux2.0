package killercreepr.crux.core.communication.boss;

import killercreepr.crux.api.communication.boss.ActiveBossBar;
import killercreepr.crux.api.communication.boss.CreateBossBar;
import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.core.util.CruxDuration;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public class SimpleActiveBossBar implements ActiveBossBar {
    protected final @NotNull Key key;
    protected @NotNull CreateBossBar bossBar;
    protected final @NotNull BossBar bar;
    protected final @Nullable Duration duration;
    protected long ticksRemaining;

    public SimpleActiveBossBar(@NotNull Key key, @NotNull CreateBossBar bossBar, @NotNull BossBar bar, @Nullable Duration duration) {
        this.key = key;
        this.bossBar = bossBar;
        this.bar = bar;
        this.duration = duration;
        this.ticksRemaining = CruxDuration.toTicks(duration);
    }

    @Override
    public @NotNull CreateBossBar createBossBar() {
        return bossBar;
    }

    @Override
    public @NotNull BossBar bossBar() {
        return bar;
    }

    @Override
    public ActiveBossBar createBossBar(@NotNull CreateBossBar bossBar) {
        this.bossBar = bossBar;
        return this;
    }

    @Override
    public void update(@NotNull TextParserContext ctx) {
        bossBar.updateBossBar(bar, ctx);
        /*var name = bar.name();
        var overlay = bar.overlay();
        var color = bar.color();*/
        /*if(!name.equals(bar.name()) || !overlay.equals(bar.overlay()) || !color.equals(bar.color())){
            for (BossBarViewer viewer : bar.viewers()) {
                if(!(viewer instanceof Audience audience)) continue;
                bar.removeViewer(audience);
                bar.addViewer(audience);
                Bukkit.broadcastMessage("changed");
            }
        }*/
    }

    @Override
    public void added(@NotNull Audience viewer) {
        bar.addViewer(viewer);
    }

    @Override
    public void removed(@NotNull Audience viewer) {
        bar.removeViewer(viewer);
    }

    @Override
    public void tick(@NotNull Audience viewer) {
        if(ticksRemaining == -1) return;
        ticksRemaining--;
    }

    @Override
    public long ticksRemaining() {
        return ticksRemaining;
    }

    @Override
    public void ticksRemaining(long amount) {
        this.ticksRemaining = amount;
    }

    @Override
    public void resetTimer() {
        this.ticksRemaining = CruxDuration.toTicks(duration);
    }

    @Override
    public boolean isActive() {
        return ticksRemaining == -1 || ticksRemaining > 0;
    }

    @Override
    public @Nullable Duration duration() {
        return duration;
    }

    @Override
    public @NotNull Key key() {
        return key;
    }
}
