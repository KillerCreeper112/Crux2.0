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

public class AddIfAllLoreResolver implements StringListResolver {
    @Override
    public @NotNull String identifier() {
        return "add_if_all";
    }

    @Override
    public @Nullable List<String> resolve(@NotNull FormatArgs args, @NotNull TextParserContext ctx) {
        int ifChecks = (int) CruxMath.evaluate(ctx.deserializeString(args.get(0)));
        int index = 1;
        while(index <= ifChecks){
            String ifCheck = ctx.deserializeString(args.get(index));
            if(!CruxString.parseBoolean(CruxMath.evaluateEvalEx(ifCheck))) return List.of();
            index++;
        }

        List<String> list = new ArrayList<>();
        while(args.has(index)){
            String text = ctx.deserializeString(args.get(index));
            list.add(text);
            index++;
        }
        return list;
    }
}
