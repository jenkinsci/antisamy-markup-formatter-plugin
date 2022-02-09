package hudson.markup;

import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class BasicPolicy {
    public static final PolicyFactory POLICY_DEFINITION;

    @Restricted(NoExternalUse.class)
    public static final PolicyFactory ADDITIONS = new HtmlPolicyBuilder()
        .allowElements("dl", "dt", "dd", "hr", "pre")
        .allowAttributes("title").globally()
        .toFactory();

    @Restricted(NoExternalUse.class)
    public static final PolicyFactory LINK_TARGETS = new HtmlPolicyBuilder()
            .allowElements("a")
            .requireRelsOnLinks("noopener", "noreferrer")
            .allowAttributes("target")
            .matching(false, "_blank")
            .onElements("a")
            .toFactory();

    static {
        POLICY_DEFINITION = Sanitizers.BLOCKS.
                and(Sanitizers.FORMATTING).
                and(Sanitizers.IMAGES).
                and(Sanitizers.LINKS).
                and(Sanitizers.STYLES).
                and(Sanitizers.TABLES).
                and(ADDITIONS).and(LINK_TARGETS);
    }
}
