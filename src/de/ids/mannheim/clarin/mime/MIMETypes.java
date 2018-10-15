package de.ids.mannheim.clarin.mime;

/**
 * MIME Types for CLARIN projects dealing with XML formats
 */
public class MIMETypes {

    /**
     * <a href="http://www.tei-c.org/release/doc/tei-p5-doc/en/html/TS.html">transcriptions of speech</a>
     */
    public final static String TEI_SPOKEN =
            "application/tei+xml;format-variant=tei-iso-spoken;tokenized=1";

    /**
     * <a href="http://www.deutschestextarchiv.de">Data by Deutsches Textarchiv</a>
     */
    public final static String DTA =
            "application/tei+xml;format-variant=tei-dta;tokenized=1";

    /**
     * any <a href="http://www.tei-c.org/release/doc">TEI</a> XML
     */
    public final static String TEI = "application/tei+xml";

    /**
     * <a href="https://www.w3.org/XML/">any XML</a>
     */
    public final static String XML = "application/xml";
}
