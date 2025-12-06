package killercreepr.cruxconfig.config.bukkit.handler.impl.text.animation;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.communication.animation.TextAnimation;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.communication.animation.LetterCycleTextAnimation;
import killercreepr.cruxconfig.config.bukkit.handler.impl.FileTextAnimation;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.GetterFileHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StandardTextAnimations {
    public static void register(FileTextAnimation file){
        file.registerCustomHandler(Crux.key("letter_cycle"), new GetterFileHandler<>() {
            @Override
            public @Nullable TextAnimation deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                if(!(e instanceof FileObject o)) return null;
                var r = ctx.getRegistry();
                return new LetterCycleTextAnimation(
                    r.deserializeFromFile(String.class, o.get("raw_text")),
                    r.deserializeFromFile(String.class, o.get("text")),
                    r.deserializeFromFile(new TypeToken<List<String>>(){}.getType(), o.get("colors")),
                    r.deserializeFromFileOrDefault(Integer.class, o.get("highlight_width"), 1),
                    r.deserializeFromFileOrDefault(Integer.class, o.get("delay"), 1),
                    r.deserializeFromFileOrDefault(Boolean.class, o.get("ignore_blank_spaces"), true)
                );
            }
        });
    }
}
