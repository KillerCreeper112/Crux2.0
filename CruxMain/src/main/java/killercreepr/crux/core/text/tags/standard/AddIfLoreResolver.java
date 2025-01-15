package killercreepr.crux.core.text.tags.standard;

import killercreepr.crux.api.text.context.TextParserContext;
import killercreepr.crux.api.text.resolver.StringListResolver;
import killercreepr.crux.core.text.format.FormatArgs;
import killercreepr.crux.core.util.CruxMath;
import killercreepr.crux.core.util.CruxString;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AddIfLoreResolver implements StringListResolver {
    @Override
    public @NotNull String identifier() {
        return "add_if";
    }

    @Override
    public @Nullable List<String> resolve(@NotNull FormatArgs args, @NotNull TextParserContext ctx) {
        String ifCheck = ctx.deserializeString(args.get(0));
        if(!CruxString.parseBoolean(CruxMath.evaluateEvalEx(ifCheck))) return List.of();
        int index = 1;
        List<String> list = new ArrayList<>();
        while(args.has(index)){
            String text = ctx.deserializeString(args.get(index));
            list.add(text);
            index++;
        }
        return list;
    }
}
