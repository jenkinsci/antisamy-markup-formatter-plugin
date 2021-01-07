package hudson.markup;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

import hudson.markup.ParseAdditionalAllowed;
import hudson.markup.ElementInfo;

public class BasicPolicy {
    public static final PolicyFactory ADDITIONS = new HtmlPolicyBuilder().allowElements("dl", "dt", "dd", "hr", "pre").toFactory();
    public final PolicyFactory POLICY_DEFINITION;

    public BasicPolicy(final String additionalAllowed) {
        // User defined additional elements and attributes
        PolicyFactory USER_ADDITIONS = new HtmlPolicyBuilder().toFactory();
        String[] oneElt = new String[1];

        for (ElementInfo eltInf : new ParseAdditionalAllowed(additionalAllowed)) {
            System.out.println("ELTINF:" + eltInf.dump());
            oneElt[0] = eltInf.tag;
            if (eltInf.attributes.isEmpty()) {
                USER_ADDITIONS = USER_ADDITIONS.and(new HtmlPolicyBuilder().allowElements(oneElt).toFactory());
            } else {
                String[] attributes = new String[eltInf.attributes.size()];
                eltInf.attributes.toArray(attributes);
                USER_ADDITIONS = USER_ADDITIONS.and(new HtmlPolicyBuilder().allowElements(oneElt).allowAttributes(attributes).onElements(oneElt).toFactory());
            }
        }

        // Deafult allowed + all additions
        this.POLICY_DEFINITION = Sanitizers.BLOCKS.
            and(Sanitizers.FORMATTING).
            and(Sanitizers.IMAGES).
            and(Sanitizers.LINKS).
            and(Sanitizers.STYLES).
            and(Sanitizers.TABLES).
            and(ADDITIONS).
            and(USER_ADDITIONS);
    }
}
