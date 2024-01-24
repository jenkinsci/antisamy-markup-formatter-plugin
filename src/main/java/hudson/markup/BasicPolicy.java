package hudson.markup;

import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;
import org.owasp.html.AttributePolicy;
import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class BasicPolicy {
    public static final PolicyFactory POLICY_DEFINITION;

    public static final String property = "safe-html-class-prefix";

    /* Disallow empty string since we use startsWith() in our policy which
     * would match _all_ strings */
    public static final String prefix = (
        System.getProperty( property ) != null
	    && !System.getProperty( property ).trim().isEmpty()
    ) ? System.getProperty( property ) : "safe-html-";

    @Restricted(NoExternalUse.class)
    public static final PolicyFactory ADDITIONS = new HtmlPolicyBuilder()
            .allowElements("dl", "dt", "dd", "hr", "pre")
            .allowAttributes("id").globally()
            .allowAttributes("class").matching(new AttributePolicy() {
                public String apply( String elementName, String attributeName, String value) {
                    return value.startsWith( prefix ) ? value : null;
                }
            }).globally()
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
