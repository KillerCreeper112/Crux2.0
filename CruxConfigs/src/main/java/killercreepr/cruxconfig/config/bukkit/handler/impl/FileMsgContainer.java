package killercreepr.cruxconfig.config.bukkit.handler.impl;

import killercreepr.crux.api.communication.Communicator;
import killercreepr.crux.api.communication.CreateSound;
import killercreepr.crux.api.communication.CreateTitle;
import killercreepr.crux.api.communication.boss.CreateBossBar;
import killercreepr.crux.core.communication.MsgContainer;
import killercreepr.cruxconfig.config.common.FileContext;
import killercreepr.cruxconfig.config.common.FileRegistry;
import killercreepr.cruxconfig.config.common.element.FileArray;
import killercreepr.cruxconfig.config.common.element.FileElement;
import killercreepr.cruxconfig.config.common.element.FileGeneric;
import killercreepr.cruxconfig.config.common.element.FileObject;
import killercreepr.cruxconfig.config.common.handler.SimpleFileHandler;
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
        if(chat != null) map.add("chat", registry.serializeToFile(chat));
        String s = msg.getActionBar();
        if(s != null) map.add("action_bar", registry.serializeToFile(s));
        CreateTitle title = msg.getTitle();
        if(title != null) map.add("title", registry.serializeToFile(title));
        CreateSound sound = msg.getSound();
        if(sound != null) map.add("sound", registry.serializeToFile(sound));
        CreateBossBar bossBar = msg.getBossBar();
        if(bossBar != null) map.add("boss_bar", registry.serializeToFile(bossBar));
        return map;
    }

    @Override
    public @Nullable MsgContainer deserializeFromFile(@NotNull FileContext<?> context, @NotNull FileElement e) {
        if(e instanceof FileGeneric chat){
            return (MsgContainer) Communicator.chat(chat.getAsString());
        }
        if(e instanceof FileArray a){
            if(a.isEmpty()) return (MsgContainer) Communicator.chat();
            List<String> list = new ArrayList<>();
            for(FileElement ee : a){
                list.add(ee.getAsString());
            }
            return (MsgContainer) Communicator.chat(list);
        }
        if(!(e instanceof FileObject o)) return null;
        FileRegistry registry = context.getRegistry();
        List<String> chat = registry.deserializeFromFile(List.class, o.get("chat"));
        String actionBar = o.getObject("action_bar");
        CreateTitle title = registry.deserializeFromFile(CreateTitle.class, o.get("title"));
        CreateSound sound = registry.deserializeFromFile(CreateSound.class, o.get("sound"));
        CreateBossBar bossBar = registry.deserializeFromFile(CreateBossBar.class, o.get("boss_bar"));
        MsgContainer container = (MsgContainer) Communicator.builder()
            .chat(chat)
            .actionBar(actionBar)
            .title(title)
            .sound(sound)
            .bossBar(bossBar)
            .broadcast(o.getOrDefaultObject("broadcast", false))
            .build();
        return container.isEmpty() ? null : container;
    }
    @Override
    public @NotNull String jsonSerializerID() {
        return "msg_container";
    }
}
