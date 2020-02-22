package hudson.markup;

import com.google.common.base.Throwables;
import hudson.Extension;
import org.kohsuke.stapler.DataBoundConstructor;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.HtmlStreamRenderer;

import java.io.IOException;
import java.io.Writer;

/**
 * {@link MarkupFormatter} that sanitizes HTML, allowing some safe (formatting) HTML.
 *
 * Before SECURITY-26 was fixed in Jenkins 1.454, this allowed all HTML without restriction.
 * Since then, the class name is a misnomer, but kept for backwards compatibility.
 *
 */
public class RawHtmlMarkupFormatter extends MarkupFormatter {

    final boolean disableSyntaxHighlighting;

    @DataBoundConstructor
    public RawHtmlMarkupFormatter(final boolean disableSyntaxHighlighting) {
        this.disableSyntaxHighlighting = disableSyntaxHighlighting;
    }

    public boolean isDisableSyntaxHighlighting() {
        return disableSyntaxHighlighting;
    }

    @Override
    public void translate(String markup, Writer output) throws IOException {
        HtmlStreamRenderer renderer = HtmlStreamRenderer.create(
                output,
                // Receives notifications on a failure to write to the output.
                Throwables::propagate, // System.out suppresses IOExceptions
                // Our HTML parser is very lenient, but this receives notifications on
                // truly bizarre inputs.
                x -> {
                    throw new Error(x);
                }
        );
        HtmlSanitizer.sanitize(markup, BasicPolicy.POLICY_DEFINITION.apply(renderer));
    }

    public String getCodeMirrorMode() {
        return disableSyntaxHighlighting ? null : "htmlmixed";
    }

    public String getCodeMirrorConfig() {
        return "mode:'text/html'";
    }

    @Extension
    public static class DescriptorImpl extends MarkupFormatterDescriptor {
        @Override
        public String getDisplayName() {
            return "Safe HTML";
        }
    }

    public static final MarkupFormatter INSTANCE = new RawHtmlMarkupFormatter(false);
}
