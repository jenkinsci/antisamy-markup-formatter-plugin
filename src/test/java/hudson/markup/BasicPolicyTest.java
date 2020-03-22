package hudson.markup;

import com.google.common.base.Throwables;
import org.junit.Assert;
import org.junit.Test;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.HtmlStreamRenderer;

import java.io.IOException;

public class BasicPolicyTest extends Assert {
    @Test
    public void testPolicy() {
        assertSanitize("<a href='http://www.cloudbees.com' rel='nofollow'>CB</a>", "<a href='http://www.cloudbees.com'>CB</a>");
        assertSanitize("<a href='relative/link' rel='nofollow'>relative</a>", "<a href='relative/link'>relative</a>");
        assertSanitize("<a href='mailto:kk&#64;kohsuke.org' rel='nofollow'>myself</a>", "<a href='mailto:kk&#64;kohsuke.org'>myself</a>");
        assertReject("javascript","<a href='javascript:alert(5)'>test</a>");

        assertIntact("<img src='http://www.cloudbees.com' />");
        assertIntact("<img src='relative/test.png' />");
        assertIntact("<img src='relative/test.png' />");
        assertReject("onerror","<img src='x' onerror='alert(5)'>");
        assertReject("javascript","<img src='javascript:alert(5)'>");

        assertIntact("<b><i><u><strike>basic tag</strike></u></i></b>");
        assertIntact("<div><p>basic block tags</p></div>");

        assertIntact("<ul><li>1</li><li>2</li><li>3</li></ul>");
        assertIntact("<ol><li>x</li></ol>");
        assertIntact("<dl><dt>abc</dt><dd>foo</dd></dl>");
        assertIntact("<table><tbody><tr><th>header</th></tr><tr><td>something</td></tr></tbody></table>");
        assertIntact("<h1>title</h1><blockquote>blurb</blockquote>");
        assertReject("style", "<div style='background-color: expression(alert(123));'>inline CSS</div>");

        assertReject("iframe", "<iframe src='nested'></iframe>");

        assertReject("script","<script>window.alert(5);</script>");
        assertReject("script","<script src='http://foo/evil.js'></script>");
        assertReject("script","<script src='relative.js'></script>");

        assertReject("form", "<form/>");

        assertReject("style", "<style>H1 { display:none; }</style>");
        assertReject("link", "<link rel='stylesheet' type='text/css' href='http://www.microsoft.com/'>");
        assertIntact("<div style='background-color:white'>inline CSS</div>");
        assertIntact("<br /><hr />");

        assertReject("sun.com", "<form method='post' action='http://sun.com/'><input type='text' name='foo'><input type='password' name='pass'></form>");
    }

    @Test
    public void testProtocolRelativeUrl() {
        assertReject("action", "<form action='//example.org/evil.php'><input type='submit'/></form>");
    }

    private void assertIntact(String input) {
        input = input.replace('\'','\"');
        assertSanitize(input,input);
    }
    
    private void assertReject(String problematic, String input) {
        String out = sanitize(input);
        assertFalse(out, out.contains(problematic));
    }
    
    private void assertSanitize(String expected, String input) {
        assertEquals(expected.replace('\'','\"'),sanitize(input));
    }

    private String sanitize(String input) {
        try {
            return new RawHtmlMarkupFormatter(false).translate(input);
        } catch (IOException ex) {
            throw new AssertionError(ex);
        }
    }
}
