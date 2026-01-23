package killercreepr.cruxconfig.config.bukkit.handler.impl.communication;

import com.google.common.reflect.TypeToken;
import killercreepr.crux.api.communication.Communicator;
import killercreepr.crux.api.communication.animation.TextAnimation;
import killercreepr.crux.api.valueproviders.number.NumberProvider;
import killercreepr.crux.core.Crux;
import killercreepr.crux.core.communication.animation.AnimatedMsg;
import killercreepr.cruxconfig.config.bukkit.handler.impl.FileCommunicator;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.GetterFileHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StandardCommunicators {
    public static void register(FileCommunicator file){
        file.registerCustomHandler(Crux.key("animation"), new GetterFileHandler<>() {
            @Override
            public @Nullable Communicator deserializeFromFile(@NotNull FileContext<?> ctx, @NotNull FileElement e) {
                if(!(e instanceof FileObject o)) return null;
                var r = ctx.getRegistry();
                List<AnimatedMsg.Entry> entries = new ArrayList<>();
                if(o.get("entries") instanceof FileArray a){
                    a.forEach(ele ->{
                        if(!(ele instanceof FileObject oo)) return;
                        Integer sendRate = r.deserializeFromFileOrDefault(Integer.class, oo.get("send_rate"), null);
                        entries.add(new AnimatedMsg.Entry(
                            r.deserializeFromFile(Communicator.class, oo.get("msg")),
                            sendRate == null ? (oo.has("frame") ? -1 : 0) : sendRate,
                            r.deserializeFromFileOrDefault(Integer.class, oo.get("frame"), 0)
                        ));
                    });
                }
                return new AnimatedMsg(
                    entries,
                    r.deserializeFromFile(new TypeToken<Map<String, TextAnimation>>(){}.getType(), o.get("animations")),
                    r.deserializeFromFileOrDefault(NumberProvider.class, o.get("animation_length"), NumberProvider.equation("<max_animation_length>"))
                );
            }
        });
    }
}
