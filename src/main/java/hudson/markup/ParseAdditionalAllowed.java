package hudson.markup;

import java.util.ArrayList;
import java.text.ParseException;
import java.util.Iterator;

import hudson.markup.ElementInfo;


public class ParseAdditionalAllowed implements Iterable<ElementInfo> {
    private ArrayList<ElementInfo> elements = new ArrayList<ElementInfo>();

    public static ArrayList<ElementInfo> validateAdditionalAllowed(final String additionalAllowed) throws ParseException {
        ArrayList<ElementInfo> elements = new ArrayList<ElementInfo>();
        String tag = null;
        ArrayList<String> attributes = null;

        if (additionalAllowed.trim().equals("")) {
            return elements;
        }

        if (additionalAllowed.endsWith(",")) {
            throw new java.text.ParseException("Could not parse: '" + additionalAllowed + "', found trailing ','", 0);
        }

        for ( String word : additionalAllowed.split("(?!^)\\b") ) {
            // System.out.println("word: '" + word + "'");
            word = word.trim();

            if (word.startsWith(",")) {
                if (!word.equals(",")) {
                    throw new java.text.ParseException("Could not parse: '" + additionalAllowed + "', unexpected '" + word + "'", 0);
                }

                if (attributes == null ) {
                    elements.add(new ElementInfo(tag));
                    // System.out.println(tag + attributes.toString());
                    tag = null;
                }
            } else if (word.startsWith("[")) {
                if (!word.equals("[") || attributes != null) {
                    throw new java.text.ParseException("Could not parse: '" + additionalAllowed + "', unexpected '" + word + "'", 0);
                }

                attributes = new ArrayList<String>();
                // System.out.println("Start attr:" + tag + attributes.toString());
            } else if (word.startsWith("]")) {
                if (!(word.equals("]") || word.equals("],")) || attributes == null) {
                    throw new java.text.ParseException("Could not parse: '" + additionalAllowed + "', unexpected '" + word + "'", 0);
                }

                elements.add(new ElementInfo(tag, attributes));
                // System.out.println(tag + attributes.toString());
                tag = null;
                attributes = null;
            } else {
                // System.out.println("not delim: " + word);
                if ( attributes != null ) {
                    attributes.add(word);
                } else {
                    tag = word;
                }
            }
        }

        if (tag != null) {
            elements.add(new ElementInfo(tag));
            // System.out.println(tag + attributes.toString());
        }

        if (attributes != null) {
            throw new java.text.ParseException("Could not parse: '" + additionalAllowed + "', expected ']'", 0);
        }

        return elements;
    }

    public ParseAdditionalAllowed(final String additionalAllowed) {
        try {
            this.elements = validateAdditionalAllowed(additionalAllowed);
        } catch (ParseException ex) {
            // This should never happen as validateAdditionalAllowed should have been called by Jenkins
            System.err.println("Plugin initialization error:" + ex);
        }
    }

    @Override
    public Iterator<ElementInfo> iterator() {
        return this.elements.iterator();
    }
}
