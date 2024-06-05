package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.data.CreateSound;
import killercreepr.crux.data.CreateTitle;
import killercreepr.crux.data.MsgContainer;
import killercreepr.cruxconfig.config.bukkit.handler.SimpleFileHandler;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.json.annotation.JsonSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
@JsonSerializer(id = "msg_container")
public class FileMsgContainer extends SimpleFileHandler<MsgContainer> {
    @Override
    public @NotNull FileElement serializeToFile(@NotNull FileContext<?> context, @NotNull MsgContainer msg) {
        FileRegistry registry = context.getRegistry();
        FileObject map = new FileObject();
        List<String> chat = msg.getChat();
        if(chat != null) map.add("chat", registry.serializeToFileElement(chat));
        String s = msg.getActionBar();
        if(s != null) map.add("action_bar", registry.serializeToFileElement(s));
        CreateTitle title = msg.getTitle();
        if(title != null) map.add("title", registry.serializeToFileElement(title));
        CreateSound sound = msg.getSound();
        if(sound != null) map.add("sound", registry.serializeToFileElement(sound));
        return map;
    }

    @Override
    public @Nullable MsgContainer deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(e instanceof FileGeneric chat){
            return new MsgContainer(chat.getAsString());
        }
        if(e instanceof FileArray a){
            if(a.isEmpty()) return new MsgContainer(List.of());
            List<String> list = new ArrayList<>();
            for(FileElement ee : a){
                list.add(ee.getAsString());
            }
            return new MsgContainer(list);
        }
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        List<String> chat = registry.deserialize(List.class, o.get("chat"));
        String actionBar = o.getObject("action_bar");
        CreateTitle title = registry.deserialize(CreateTitle.class, o.get("title"));
        CreateSound sound = registry.deserialize(CreateSound.class, o.get("sound"));
        MsgContainer container = new MsgContainer(chat.isEmpty() ? null : chat,
                actionBar,
                title, sound);
        container.setBroadcast(o.getOrDefaultObject("broadcast", false));
        return container.isEmpty() ? null : container;
    }
    @Override
    public @NotNull String jsonSerializerID() {
        return "msg_container";
    }
}
