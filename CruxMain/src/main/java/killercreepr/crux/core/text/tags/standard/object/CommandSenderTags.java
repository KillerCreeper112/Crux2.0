package killercreepr.crux.core.text.tags.standard.object;

import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.text.container.StringTagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CommandSenderTags implements ObjectTag<CommandSender> {
    @Override
    public @NotNull Class<CommandSender> getObjectType() {
        return CommandSender.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("command_sender_");
    }

    @Override
    public @Nullable StringTagContainer requestStrings(@NotNull CommandSender p, @NotNull TagParser tags) {
        return new StringTagContainer(tags)
            .add(Tag.string("name", (args, context) -> p.getName() + ""))
            .add(Tag.string("has_permission", (args, ctx) ->{
                String permission = args.get(0);
                return p.hasPermission(permission) + "";
            }))
            ;
    }
}
