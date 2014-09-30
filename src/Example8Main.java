// EECE 210 Example 8
//
// When it prompts you for a file name, if you type a simple string such
// as "test1.html" (without the quotes) it will just look on your hard disk
// in the same directory as your code or Eclipse project.
//
// If you type a string such as "http://www.google.com/index.html", it will
// connect to that URL and download the HTML content from it.
//
// Then: all the HTML tags in that file will be printed to the console.

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.Scanner;
import java.util.Queue;

public class Example8Main {
    /**
     * This method takes a filename or URL and then proceeds to print all the
     * HTML tags in the specified file.
     * 
     * @param args
     *            No arguments are needed for this method
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        String pageText = "";
        Scanner console = new Scanner(System.in);
        String choice = "s";

        // prompt for page, then download it if it's a URL
        System.out.print("Page URL or file name (blank for empty): ");
        String url = console.nextLine().trim();

        if (url.length() > 0) {
            if (isURL(url)) {
                System.out.println("Downloading from " + url + " ...");
            }

            try {
                pageText = readCompleteFileOrURL(url);

            } catch (MalformedURLException mfurle) {
                System.out.println("Badly formatted URL: " + url);
            } catch (FileNotFoundException fnfe) {
                System.out.println("Web page or file not found: " + url);
            } catch (IOException ioe) {
                System.out.println("I/O error: " + ioe.getMessage());
            }
        } else {
            pageText = "No page text (starting from empty queue)";
        }

        StringBuffer sb = new StringBuffer(pageText);

        System.out.println("Printing tags:");

        // Illustrates how to use an iterator.
        // The hasNext( ) and nextTag( ) methods help define the iterator.
        while (HtmlTag.hasNext(sb)) {
            HtmlTag tag = HtmlTag.nextTag(sb);
            System.out.println("+ " + tag.toString());
        }

        System.out.println("Done!");
    }

    /**
     * 
     * Returns an input stream to read from the given address. Works with URLs
     * or normal file names.
     * 
     * @param address
     *            A filename or URL.
     * @return An InputStream for the file/URL specified as an argument.
     * @throws IOException
     *             if there was a problem reading the file.
     * @throws MalformedURLException
     *             if the URL is not well-formed.
     */
    public static InputStream getInputStream(String address)
            throws IOException, MalformedURLException {
        if (isURL(address)) {
            return new URL(address).openStream();
        } else {
            // local file
            return new FileInputStream(address);
        }
    }

    /**
     * Returns true if the given string represents a URL.
     * 
     * @param address
     *            A URL or filename.
     * @return True if the argument is a URL, false otherwise.
     */
    public static boolean isURL(String address) {
        return address.startsWith("http://") || address.startsWith("https://")
                || address.startsWith("www.") || address.endsWith("/")
                || address.endsWith(".com") || address.contains(".com/")
                || address.endsWith(".org") || address.contains(".org/")
                || address.endsWith(".edu") || address.contains(".edu/")
                || address.endsWith(".ca") || address.contains(".ca/")
                || address.endsWith(".gov") || address.contains(".gov/");
    }

    /**
     * Opens the given address for reading input, and reads it until the end of
     * the file, and returns the entire file contents as a big String.
     * 
     * If address starts with http[s]:// , assumes address is a URL and tries to
     * download the data from the web. Otherwise, assumes the address is a local
     * file and tries to read it from the disk.
     * 
     * @param address
     *            An input file/URL.
     * @return All the contents in the file or at the URL.
     * @throws IOException
     *             if there was a problem reading the file or accessing the URL.
     */
    public static String readCompleteFileOrURL(String address)
            throws IOException {
        InputStream stream = getInputStream(address); // open file

        // read each letter into a buffer
        StringBuffer buffer = new StringBuffer();
        while (true) {
            int ch = stream.read();
            if (ch < 0) {
                break;
            }

            buffer.append((char) ch);
        }

        return buffer.toString();
    }
}
