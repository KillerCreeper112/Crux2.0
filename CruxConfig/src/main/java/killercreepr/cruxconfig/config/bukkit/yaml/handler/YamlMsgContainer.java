package killercreepr.cruxconfig.config.bukkit.yaml.handler;

import killercreepr.crux.data.CreateSound;
import killercreepr.crux.data.CreateTitle;
import killercreepr.crux.data.MsgContainer;
import killercreepr.cruxconfig.config.common.yaml.YamlContext;
import killercreepr.cruxconfig.config.common.yaml.element.YamlArray;
import killercreepr.cruxconfig.config.common.yaml.element.YamlElement;
import killercreepr.cruxconfig.config.common.yaml.element.YamlGeneric;
import killercreepr.cruxconfig.config.common.yaml.element.YamlObject;
import killercreepr.cruxconfig.config.common.yaml.handler.YamlObjectHandler;
import killercreepr.cruxconfig.config.common.yaml.registry.YamlRegistry;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class YamlMsgContainer implements YamlObjectHandler<MsgContainer> {
    @Override
    public @NotNull YamlElement serializeToYaml(@NotNull YamlContext context, @NotNull MsgContainer msg) {
        YamlRegistry registry = context.getRegistry();
        YamlObject map = new YamlObject();
        List<String> chat = msg.getChat();
        if(chat != null) map.add("chat", registry.serializeObject(chat));
        String s = msg.getActionBar();
        if(s != null) map.add("action_bar", registry.serializeObject(s));
        CreateTitle title = msg.getTitle();
        if(title != null) map.add("title", registry.serializeObject(title));
        CreateSound sound = msg.getSound();
        if(sound != null) map.add("sound", registry.serializeObject(sound));
        return map;
    }

    @Override
    public @Nullable MsgContainer deserializeFromYaml(@NotNull YamlContext context, @Nullable YamlElement e) {
        if(e instanceof YamlGeneric chat){
            return new MsgContainer(chat.getAsString());
        }
        if(e instanceof YamlArray a){
            if(a.isEmpty()) return new MsgContainer(List.of());
            List<String> list = new ArrayList<>();
            for(YamlElement ee : a){
                list.add(ee.getAsString());
            }
            return new MsgContainer(list);
        }
        if(!(e instanceof YamlObject o)) return null;
        YamlRegistry registry = context.getRegistry();
        List<String> chat = registry.deserialize(List.class, o.get("chat"));
        Bukkit.getLogger().warning("CHAT MAN? " + new ArrayList<>(chat));
        String actionBar = o.getObject("action_bar");
        CreateTitle title = registry.deserialize(CreateTitle.class, o.get("title"));
        CreateSound sound = registry.deserialize(CreateSound.class, o.get("sound"));
        MsgContainer container = new MsgContainer(chat.isEmpty() ? null : chat,
                actionBar,
                title, sound);
        container.setBroadcast(o.getOrDefaultObject("broadcast", false));
        return container.isEmpty() ? null : container;
    }
}
