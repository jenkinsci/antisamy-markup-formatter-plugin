package hudson.markup;

import static org.junit.Assert.assertTrue;

import io.jenkins.plugins.casc.misc.RoundTripAbstractTest;
import jenkins.model.Jenkins;
import org.jvnet.hudson.test.RestartableJenkinsRule;

public class JCasCCompatibilityTest extends RoundTripAbstractTest {

    @Override
    protected void assertConfiguredAsExpected(RestartableJenkinsRule restartableJenkinsRule, String s) {
        Jenkins jenkins = restartableJenkinsRule.j.jenkins;

        assertTrue(
                "Safe HTML markup formatter should be configured",
                jenkins.getMarkupFormatter() instanceof RawHtmlMarkupFormatter);
    }

    @Override
    protected String stringInLogExpected() {
        return "Setting class hudson.markup.RawHtmlMarkupFormatter.disableSyntaxHighlighting = true";
    }
}
