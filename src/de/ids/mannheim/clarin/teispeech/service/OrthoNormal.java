package de.ids.mannheim.clarin.teispeech.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.jdom2.JDOMException;
import org.korpora.useful.Anonymize;
import org.korpora.useful.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import de.ids.mannheim.clarin.mime.MIMETypes;
import de.ids.mannheim.clarin.teispeech.data.GATParser;
import de.ids.mannheim.clarin.teispeech.tools.CLI.Level;
import de.ids.mannheim.clarin.teispeech.tools.DocUtilities;
import de.ids.mannheim.clarin.teispeech.tools.GenericParsing;
import de.ids.mannheim.clarin.teispeech.tools.LanguageDetect;
import de.ids.mannheim.clarin.teispeech.tools.TEINormalizer;
import de.ids.mannheim.clarin.teispeech.tools.TEIPOS;
import de.ids.mannheim.clarin.teispeech.tools.TextToTEIConversion;
import de.ids.mannheim.clarin.teispeech.utilities.ServiceUtilities;

/**
 * Webservices for dealing with TEI-encodded documents
 *
 * @author bfi
 *
 */
@Path("")
public class OrthoNormal {

    private final static Logger LOGGER = LoggerFactory
            .getLogger(OrthoNormal.class.getName());

    /**
     * convert to a TEI ISO transcription:
     *
     * @param input
     *            the input document – plain text!
     * @param language
     *            the presumed language, preferably a ISO 639 code
     * @param request
     *            the HTTP request
     * @return a TEI-encoded speech transcription with normalization in
     *         &lt;w&gt;
     */
    @POST
    @Path("text2iso")
    @Consumes({ MIMETypes.PLAIN_TEXT })
    @Produces({ MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML })

    public Response text2iso(InputStream input,
            @QueryParam("lang") String language,
            @Context HttpServletRequest request) {
        try {
            ServiceUtilities.checkLanguage(language);
            LOGGER.info("Processing <{}> of length {} for {}.",
                    request.getHeader(HttpHeaders.CONTENT_TYPE),
                    request.getHeader(HttpHeaders.CONTENT_LENGTH),
                    Anonymize.anonymizeAddr(request));
            CharStream inputCS;
            inputCS = CharStreams.fromStream(input);
            Document doc = TextToTEIConversion.process(inputCS, language);
            return Response.ok(doc, request.getContentType()).build();
        } catch (IOException e) {
            throw new WebApplicationException(e,
                    Response.status(400).entity(e.getMessage()).build());
        }
    }

    /**
     * normalize using an EXMARaLDA-OrthoNormal-based normalizer:
     *
     * @param input
     *            a TEI-encoded speech transcription
     * @param language
     *            the presumed language, preferably a ISO 639 code
     * @param force
     *            whether to force normalization
     * @param request
     *            the HTTP request
     * @return a TEI-encoded speech transcription with normalization in
     *         &lt;w&gt;
     */
    @POST
    @Path("normalize")
    @Consumes({ MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML })
    @Produces({ MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML })

    public Response normalize(InputStream input,
            @QueryParam("lang") String language,
            @QueryParam("force") boolean force,
            @Context HttpServletRequest request) {
        try {
            ServiceUtilities.checkLanguage(language);
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            TEINormalizer teiDictNormalizer = new TEINormalizer(language);
            LOGGER.info("Processing <{}> of length {} for {}.",
                    request.getHeader(HttpHeaders.CONTENT_TYPE),
                    request.getHeader(HttpHeaders.CONTENT_LENGTH),
                    Anonymize.anonymizeAddr(request));
            teiDictNormalizer.normalize(doc, force);
            return Response.ok(doc, request.getContentType()).build();
        } catch (IllegalArgumentException | SAXException
                | ParserConfigurationException | IOException e) {
            throw new WebApplicationException(e,
                    Response.status(400).entity(e.getMessage()).build());
        }
    }

    /**
     * POS-tag a TEI ISO transcription:
     *
     * @param input
     *            a TEI-encoded speech transcription
     * @param language
     *            the presumed language, preferably a ISO 639 code
     * @param force
     *            whether to force normalization
     * @param request
     *            the HTTP request
     * @return a TEI-encoded speech transcription with normalization in
     *         &lt;w&gt;
     */
    @POST
    @Path("pos")
    @Consumes({ MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML })
    @Produces({ MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML })

    public Response pos(InputStream input, @QueryParam("lang") String language,
            @QueryParam("force") boolean force,
            @Context HttpServletRequest request) {
        try {
            ServiceUtilities.checkLanguage(language);
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            TEIPOS teipo = new TEIPOS(doc, language);
            LOGGER.info("Processing <{}> of length {} for {}.",
                    request.getHeader(HttpHeaders.CONTENT_TYPE),
                    request.getHeader(HttpHeaders.CONTENT_LENGTH),
                    Anonymize.anonymizeAddr(request));
            teipo.posTag(force);
            return Response.ok(doc, request.getContentType()).build();
        } catch (IllegalArgumentException | SAXException
                | ParserConfigurationException | IOException e) {
            throw new WebApplicationException(e,
                    Response.status(400).entity(e.getMessage()).build());
        }
    }

    /**
     * pos-tag a TEI ISO transcription:
     *
     * @param input
     *            a TEI-encoded speech transcription
     * @param expected
     *            the languages expected in the document
     * @param language
     *            the presumed language, preferably a ISO 639 code
     * @param force
     *            whether to force normalization
     * @param request
     *            the HTTP request
     * @return a TEI-encoded speech transcription with normalization in
     *         &lt;w&gt;
     */
    @POST
    @Path("guess")
    @Consumes({ MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML })
    @Produces({ MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML })

    public Response guess(InputStream input,
            @QueryParam("lang") String language,
            @QueryParam("expected") List<String> expected,
            @QueryParam("force") boolean force,
            @Context HttpServletRequest request) {
        try {
            ServiceUtilities.checkLanguage(language);
            String[] expectedLangs = expected.stream()
                    .map(ServiceUtilities::checkLanguage)
                    .toArray(String[]::new);
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            LanguageDetect guesser = new LanguageDetect(doc, language,
                    expectedLangs);
            LOGGER.info("Processing <{}> of length {} for {}.",
                    request.getHeader(HttpHeaders.CONTENT_TYPE),
                    request.getHeader(HttpHeaders.CONTENT_LENGTH),
                    Anonymize.anonymizeAddr(request));
            guesser.detect(force);
            return Response.ok(doc, request.getContentType()).build();
        } catch (IllegalArgumentException | SAXException
                | ParserConfigurationException | IOException e) {
            throw new WebApplicationException(e,
                    Response.status(400).entity(e.getMessage()).build());
        }
    }

    /**
     * normalize using an EXMARaLDA-OrthoNormal-based normalizer:
     *
     * @param input
     *            a TEI-encoded speech transcription
     * @param language
     *            the presumed language, preferably a ISO 639 code
     * @param level
     *            the parsing level: generic, minimal, basic
     * @param request
     *            the HTTP request
     * @return a TEI-encoded speech transcription with normalization in
     *         &lt;w&gt;
     */
    @POST
    @Path("segmentize")
    @Consumes({ MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML })
    @Produces({ MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML })

    public Response segmentize(InputStream input,
            @QueryParam("lang") String language,
            @QueryParam("level") Level level,
            @Context HttpServletRequest request) {
        if (level == Level.generic) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                Document doc = builder.parse(input);
                GenericParsing.process(doc);
                return Response.ok(doc, request.getContentType()).build();
            } catch (SAXException | IOException
                    | ParserConfigurationException e) {
                throw new RuntimeException(e);
            }

        } else {
            try {
                ServiceUtilities.checkLanguage(language);
                org.jdom2.Document doc = Utilities.parseXMLviaJDOM(input);
                // TODO: language?
                GATParser parser = new GATParser();
                parser.parseDocument(doc, level.ordinal() + 1);
                DocUtilities.makeChange(doc, String.format(
                        "utterances parsed to %s conventions", level.name()));
                return Response.ok(Utilities.convertJDOMToDOM(doc),
                        request.getContentType()).build();
            } catch (IllegalArgumentException | IOException | JDOMException e) {
                throw new WebApplicationException(e,
                        Response.status(400).entity(e.getMessage()).build());
            }
        }
    }

}
