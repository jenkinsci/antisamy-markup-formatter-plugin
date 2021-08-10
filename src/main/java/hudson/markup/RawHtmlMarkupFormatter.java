package hudson.markup;

import com.google.common.base.Throwables;
import hudson.util.FormValidation;
import hudson.Extension;
import hudson.markup.ParseAdditionalAllowed;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.HtmlStreamRenderer;

import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.lang.UnsupportedOperationException;


/**
 * {@link MarkupFormatter} that sanitizes HTML, allowing some safe (formatting) HTML.
 *
 * Before SECURITY-26 was fixed in Jenkins 1.454, this allowed all HTML without restriction.
 * Since then, the class name is a misnomer, but kept for backwards compatibility.
 *
 */
public class RawHtmlMarkupFormatter extends MarkupFormatter {
    final boolean disableSyntaxHighlighting;
    final String additionalAllowed;
    private transient BasicPolicy policy;

    @DataBoundConstructor
    public RawHtmlMarkupFormatter(final boolean disableSyntaxHighlighting, final String additionalAllowed) {
        this.disableSyntaxHighlighting = disableSyntaxHighlighting;
        this.additionalAllowed = additionalAllowed;
        this.policy = new BasicPolicy(additionalAllowed);
    }

    /**
     * Restores the configuration after deserialization.
     *
     * @return this instance
     */
    protected Object readResolve() {
        this.policy = new BasicPolicy(this.additionalAllowed);
        return this;
    }

    public boolean isDisableSyntaxHighlighting() {
        return disableSyntaxHighlighting;
    }

    public String getAdditionalAllowed() {
        return additionalAllowed;
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
        // BUG: policy is null here until the config page has been visited
        HtmlSanitizer.sanitize(markup, this.policy.POLICY_DEFINITION.apply(renderer));
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

        public FormValidation doCheckAdditionalAllowed(@QueryParameter String value) throws IOException {
            try {
                ParseAdditionalAllowed.validateAdditionalAllowed(value);
                return FormValidation.ok();
            } catch (ParseException ex) {
                return FormValidation.error(ex.toString());
            }
        }
    }
}
