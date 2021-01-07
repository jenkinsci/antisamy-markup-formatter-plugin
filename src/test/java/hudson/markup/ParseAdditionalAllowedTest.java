package hudson.markup;

import com.google.common.base.Throwables;
import org.junit.Assert;
import org.junit.Test;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.HtmlStreamRenderer;

import java.io.IOException;
import java.util.ArrayList;
import java.text.ParseException;
import java.util.Arrays;

public class ParseAdditionalAllowedTest extends Assert {
    @Test
    public void testGood() throws ParseException {
        ElementInfo[] expect = {
            new ElementInfo("a", new String[]{}),
            new ElementInfo("span", new String[]{"title"}),
            new ElementInfo("spam", new String[]{"a", "b", "c"}),
        };

        int index = 0;
        for (ElementInfo elt : new ParseAdditionalAllowed("a,span[title],spam[a,b,c]")) {
            // System.out.println("Got:\n" + elt.dump());
            // System.out.println("Expect:\n" + expect[index].dump());
            assertTrue(elt.equals(expect[index++]));
        }
    }

    @Test
    public void testInputErrorDoubleCommaInElementsList() {
        Exception exception = assertThrows(ParseException.class, () -> {
                ParseAdditionalAllowed.validateAdditionalAllowed("a,,span[title]");
            });

        String expectedMessage = "Could not parse: 'a,,span[title]', unexpected ',,'";
        String actualMessage = exception.getMessage();
        System.out.println(actualMessage);
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testInputErrorTrailingCommaInElementsList() {
        Exception exception = assertThrows(ParseException.class, () -> {
                ParseAdditionalAllowed.validateAdditionalAllowed("a,");
            });
    }

    @Test
    public void testInputErrorDoubleCommaInAttributesList() {
        Exception exception = assertThrows(ParseException.class, () -> {
                ParseAdditionalAllowed.validateAdditionalAllowed("span[a,,b]");
            });
    }

    @Test
    public void testInputErrorTrailingCommaInAttributesList() {
        Exception exception = assertThrows(ParseException.class, () -> {
                ParseAdditionalAllowed.validateAdditionalAllowed("span[a,b,]");
            });
    }

    @Test
    public void testInputErrorUnmatchedLeftBracket() {
        Exception exception = assertThrows(ParseException.class, () -> {
                ParseAdditionalAllowed.validateAdditionalAllowed("a[title");
            });
    }

    @Test
    public void testInputErrorUnmatchedRightBracket() {
        Exception exception = assertThrows(ParseException.class, () -> {
                ParseAdditionalAllowed.validateAdditionalAllowed("title]");
            });
    }
}
