package killercreepr.crux.tags.resolver;

import killercreepr.crux.context.TextParserContext;
import killercreepr.crux.tags.format.FormatArgs;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Tag {
    public static @NotNull StringResolver parsed(@NotNull String identifier, @NotNull String value){
        return new StringResolver() {
            @Override
            public @NotNull String identifier() {
                return identifier;
            }

            @Override
            public @NotNull String resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
                return value;
            }
        };
    }

    public static @NotNull StringResolver string(@NotNull String identifier, @NotNull StringParser parser){
        return new StringResolver() {
            @Override
            public @NotNull String identifier() {
                return identifier;
            }

            @Override
            public @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
                return parser.resolve(args, context);
            }
        };
    }

    public static @NotNull StringListResolver parsed(@NotNull String identifier, @NotNull List<String> value){
        return new StringListResolver() {
            @Override
            public @NotNull String identifier() {
                return identifier;
            }

            @Override
            public @NotNull List<String> resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
                return value;
            }
        };
    }

    public static @NotNull StringListResolver stringList(@NotNull String identifier, @NotNull StringListParser parser){
        return new StringListResolver() {
            @Override
            public @NotNull String identifier() {
                return identifier;
            }

            @Override
            public @Nullable List<String> resolve(@NotNull FormatArgs args, @NotNull TextParserContext context) {
                return parser.resolve(args, context);
            }
        };
    }

    public interface StringListParser{
        @Nullable List<String> resolve(@NotNull FormatArgs args, @NotNull TextParserContext context);
    }
    public interface StringParser{
        @Nullable String resolve(@NotNull FormatArgs args, @NotNull TextParserContext context);
    }
}
