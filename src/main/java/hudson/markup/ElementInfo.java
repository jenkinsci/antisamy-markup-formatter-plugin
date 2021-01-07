package hudson.markup;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class ElementInfo {
    public final String tag;
    public final List<String> attributes;

    public ElementInfo(String tag, List<String> attributes) {
        this.tag = tag;
        this.attributes = attributes;
    }

    public ElementInfo(String tag, String[] attributes) {
        this.tag = tag;
        this.attributes = Arrays.asList(attributes);
    }

    public ElementInfo(String tag) {
        this.tag = tag;
        this.attributes = new ArrayList<String>();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ElementInfo)) return false;
        ElementInfo other = (ElementInfo)obj;
        if (other.tag.equals(this.tag) && other.attributes.equals(this.attributes)) {
            return true;
        }

        return false;
    }

    public String dump() {
        return this.toString() + "\n  " + tag + "\n  " + attributes;
    }
}
