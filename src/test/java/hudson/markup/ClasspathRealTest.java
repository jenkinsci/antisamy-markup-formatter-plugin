package hudson.markup;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jenkins.core.PluginExcerptSanitizer;
import jenkins.model.Jenkins;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.RealJenkinsRule;

public class ClasspathRealTest {
    public static final String CLASS_NAME = "org.owasp.shim.ForJava9AndLater";

    @Rule
    public RealJenkinsRule j = new RealJenkinsRule();

    @Test
    public void testClasspath() throws Throwable {
        j.then(ClasspathRealTest::_testClasspath);
    }

    static void _testClasspath(JenkinsRule j) throws ClassNotFoundException {
        assertThrows(
                ClassNotFoundException.class,
                () -> BasicPolicy.class.getClassLoader().loadClass(CLASS_NAME));
        assertThrows(
                ClassNotFoundException.class,
                () -> ClasspathRealTest.class.getClassLoader().loadClass(CLASS_NAME));
        // control:
        Jenkins.get()
                .getCoreLibrary(jenkins.core.PluginExcerptSanitizer.class)
                .getClass()
                .getClassLoader()
                .loadClass(CLASS_NAME);

        // control:
        final PluginExcerptSanitizer sanitizer = Jenkins.get().getCoreLibrary(PluginExcerptSanitizer.class);
        assertThat(
                sanitizer.sanitize("<p><strong>Hello world</strong></p><script>alert(1);</script>"),
                equalTo("<strong>Hello world</strong>"));
    }
}
