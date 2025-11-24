package killercreepr.crux.api.communication.boss;

import killercreepr.crux.api.text.context.TextParserContext;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.key.Keyed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;

public interface ActiveBossBar extends Keyed {
    @NotNull CreateBossBar createBossBar();
    @Nullable Duration duration();
    @NotNull
    BossBar bossBar();
    ActiveBossBar createBossBar(@NotNull CreateBossBar bossBar);
    void update(@NotNull TextParserContext ctx);
    void added(@NotNull Audience viewer);
    void removed(@NotNull Audience viewer);
    void tick(@NotNull Audience viewer);
    long ticksRemaining();
    void ticksRemaining(long amount);
    void resetTimer();
    boolean isActive();
}
