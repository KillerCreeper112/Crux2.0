package killercreepr.crux.core.text.tags.standard.object;

import killercreepr.crux.api.data.User;
import killercreepr.crux.api.text.format.FormatPrefix;
import killercreepr.crux.api.text.hook.ObjectTag;
import killercreepr.crux.api.text.tags.TagParser;
import killercreepr.crux.core.text.container.StringTagContainer;
import killercreepr.crux.core.text.resolver.Tag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UserTags implements ObjectTag<User> {
    @Override
    public @NotNull Class<User> getObjectType() {
        return User.class;
    }

    @Override
    public @NotNull FormatPrefix defaultPrefix() {
        return FormatPrefix.simple("user_");
    }

    @Override
    public @Nullable StringTagContainer requestStrings(@NotNull User user, @NotNull TagParser tags) {
        return new StringTagContainer(tags)
            .add(Tag.string("uuid", (args, context) -> user.uuid().toString()))
            .add(Tag.string("name", (args, context) -> user.name() + ""))
            ;
    }
}
