package killercreepr.crux.core.util;

import org.jetbrains.annotations.Nullable;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CruxBase64 {
    public static final Pattern BASE64_PATTERN = Pattern.compile("^[a-zA-Z0-9+/]*={0,2}$");
    /**
     * Checks if a string is a valid Base64 encoded string.
     *
     * @param value The string to check.
     * @return true if the string is valid Base64; false otherwise.
     */
    public static boolean isBase64(@Nullable String value) {
        if (value == null || value.isEmpty()) {
            return false;
        }

        // Check if the string matches the Base64 character set pattern
        Matcher matcher = BASE64_PATTERN.matcher(value);
        if (!matcher.matches()) {
            return false;
        }

        try {
            // Attempt to decode the string
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] decodedBytes = decoder.decode(value);

            // Check if decoded bytes can be properly re-encoded to match the original string
            String reEncodedString = Base64.getEncoder().encodeToString(decodedBytes);
            return reEncodedString.equals(value);
        } catch (IllegalArgumentException e) {
            // If decoding fails, it's not a valid Base64 encoded string
            return false;
        }
    }
}
