package hudson.markup;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.jenkins.plugins.casc.misc.junit.jupiter.AbstractRoundTripTest;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
class JCasCCompatibilityTest extends AbstractRoundTripTest {

    @Override
    protected void assertConfiguredAsExpected(JenkinsRule jenkinsRule, String s) {
        assertInstanceOf(
                RawHtmlMarkupFormatter.class,
                jenkinsRule.jenkins.getMarkupFormatter(),
                "Safe HTML markup formatter should be configured");
        assertTrue(
                ((RawHtmlMarkupFormatter) jenkinsRule.jenkins.getMarkupFormatter()).isDisableSyntaxHighlighting(),
                "Safe HTML markup formatter should be configured with disable syntax highlighting = true");
    }

    @Override
    protected String stringInLogExpected() {
        return "Setting class hudson.markup.RawHtmlMarkupFormatter.disableSyntaxHighlighting = true";
    }
}
