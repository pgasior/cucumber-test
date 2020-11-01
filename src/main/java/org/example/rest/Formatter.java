package org.example.rest;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.translate.AggregateTranslator;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.apache.commons.lang3.text.translate.EntityArrays;
import org.apache.commons.lang3.text.translate.LookupTranslator;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class Formatter {
    public static final String UTF_8_NEW_LINE = "␤";
    public static final String NEW_LINE_ON_ANY_OS = "\\r?\\n";

    public static final String FOUR_SPACES = "&nbsp; &nbsp; &nbsp; &nbsp;";
    public static final String TAB = "\\t";

    public static String[][] UNICODE_CHARS_ESCAPE = new String[][]{{"\\u", "&#92;"}};

    public String renderText(String text) {
        if (isEmpty(text)) {
            return "";
        }

        return concatLines(BASIC_XML.translate(text), "<br>")
                .replaceAll(TAB, FOUR_SPACES);
    }

    public String renderHeaders(String text) {
        if (text == null) {
            return "";
        }
        return concatLines(BASIC_XML.translate(stringFormOf(text)), "<br>")
                .replaceAll("\\t", "");
    }

    private static String stringFormOf(Object fieldValue) {
        if (Iterable.class.isAssignableFrom(fieldValue.getClass())) {
            return "[" + StringUtils.join((Iterable) fieldValue, ", ") + "]";
        } else {
            return fieldValue.toString();
        }
    }

    private static String concatLines(String message, String newLine) {
        message = StringUtils.replace(message, UTF_8_NEW_LINE, newLine);
        List<String> lines = Splitter.onPattern(NEW_LINE_ON_ANY_OS).splitToList(message);
        return StringUtils.join(lines, newLine);
    }

    private final CharSequenceTranslator BASIC_XML = new AggregateTranslator(
            new LookupTranslator(EntityArrays.BASIC_ESCAPE()),
            new LookupTranslator(UNICODE_CHARS_ESCAPE)
    );
}
