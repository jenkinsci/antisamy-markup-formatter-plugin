package hudson.markup;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jenkins.core.PluginExcerptSanitizer;
import jenkins.model.Jenkins;
import org.junit.jupiter.api.Test;
import org.jvnet.hudson.test.JenkinsRule;
import org.jvnet.hudson.test.junit.jupiter.WithJenkins;

@WithJenkins
public class ClasspathTest {

    public static final String CLASS_NAME = "org.owasp.shim.ForJava9AndLater";

    @Test
    public void testClasspath(JenkinsRule j) {
        assertThrows(
                ClassNotFoundException.class,
                () -> BasicPolicy.class.getClassLoader().loadClass(CLASS_NAME));
        assertThrows(
                ClassNotFoundException.class,
                () -> ClasspathTest.class.getClassLoader().loadClass(CLASS_NAME));

        // control:
        final PluginExcerptSanitizer sanitizer = Jenkins.get().getCoreLibrary(PluginExcerptSanitizer.class);
        assertThat(
                sanitizer.sanitize("<p><strong>Hello world</strong></p><script>alert(1);</script>"),
                equalTo("<strong>Hello world</strong>"));
    }
}
