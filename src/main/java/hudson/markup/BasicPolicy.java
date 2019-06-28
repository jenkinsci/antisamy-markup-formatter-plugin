package hudson.markup;

import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class BasicPolicy {
    public static final PolicyFactory POLICY_DEFINITION;


    public static final PolicyFactory ADDITIONS = new HtmlPolicyBuilder2() {{
        tag("dl,dt,dd");
        tag("hr");
    }}.toFactory();

    static {
        POLICY_DEFINITION = Sanitizers.BLOCKS.
                and(Sanitizers.FORMATTING).
                and(Sanitizers.IMAGES).
                and(Sanitizers.LINKS).
                and(Sanitizers.STYLES).
                and(Sanitizers.TABLES).
                and(ADDITIONS);
    }
}
