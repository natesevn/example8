Example 8: Custom Iterators
========


This example illustrates how to create and use an iterator to move through some HTML text and work with the HTML tags in the provided text.

The iterator is implemented using the methods `hasNext( )` and `nextTag( )` that are part of the `HtmlTag` class.

```java
    /**
     * Checks if argument buf has another HTML tag or not.
     * 
     * @param buf
     *            the StringBuffer to check for HTML tags.
     * @return true if buf has another HTML tag, false otherwise.
     */
    public static boolean hasNext(StringBuffer buf) {
        int index1 = buf.indexOf("<");
        int index2 = buf.indexOf(">");

        if (index1 >= 0 && index2 > index1) {
            // check for HTML comments: <!-- -->
            if (index1 + 4 <= buf.length()
                    && buf.substring(index1 + 1, index1 + 4).equals("!--")) {
                // a comment; look for closing comment tag -->
                index2 = buf.indexOf("-->", index1 + 4);
                if (index2 < 0) {
                    return false;
                } else {
                    buf.insert(index1 + 4, " "); // fixes things like <!--hi-->
                    index2 += 3; // advance to the closing >
                }
            }

            return true;

        } else
            return false;

    }

   /**
     * This method advances to next tag in the input StreamBuffer; probably not
     * a perfect HTML tag tokenizer, but it will do for now.
     * 
     * This method should be used if hasNext( ) returns true. Otherwise this
     * method will return null and that can be problematic.
     * 
     * @param buf
     *            the StreamBuffer to obtain a tag from
     * @return the next tag or null if there is no other tag. This method
     *         modifies buf by removing the next tag, if it should exist.
     */
    public static HtmlTag nextTag(StringBuffer buf) {
        int index1 = buf.indexOf("<");
        int index2 = buf.indexOf(">");

        if (index1 >= 0 && index2 > index1) {
            // check for HTML comments: <!-- -->
            if (index1 + 4 <= buf.length()
                    && buf.substring(index1 + 1, index1 + 4).equals("!--")) {
                // a comment; look for closing comment tag -->
                index2 = buf.indexOf("-->", index1 + 4);
                if (index2 < 0) {
                    return null;
                } else {
                    buf.insert(index1 + 4, " "); // fixes things like <!--hi-->
                    index2 += 3; // advance to the closing >
                }
            }

            String element = buf.substring(index1 + 1, index2).trim();

            // remove attributes
            for (int i = 0; i < WHITESPACE.length(); i++) {
                int index3 = element.indexOf(WHITESPACE.charAt(i));
                if (index3 >= 0) {
                    element = element.substring(0, index3);
                }
            }

            // determine whether opening or closing tag
            boolean isOpenTag = true;
            if (element.indexOf("/") == 0) {
                isOpenTag = false;
                element = element.substring(1);
            }
            element = element.replaceAll("[^a-zA-Z0-9!-]+", "");

            buf.delete(0, index2 + 1);
            return new HtmlTag(element, isOpenTag);
        } else {
            return null;
        }
    }
```

The iterator is used, in this example, to print all the tags in a provided piece of HTML text to the console. This is done through the following code segment in `Example8Main.java`:
```java
        // Illustrates how to use an iterator.
        // The hasNext( ) and nextTag( ) methods help define the iterator.
        while (HtmlTag.hasNext(sb)) {
            HtmlTag tag = HtmlTag.nextTag(sb);
            System.out.println("+ " + tag.toString());
        }
```

While the central purpose of this example is to illustrate iterators, the provided code also demonstrates how one could get data from a URL (see the `getInputStream( )` method in `Example8Main.java`).