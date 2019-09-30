package de.ids.mannheim.clarin.teispeech.service;

import de.ids.mannheim.clarin.mime.MIMETypes;
import de.ids.mannheim.clarin.teispeech.data.DocUtilities;
import de.ids.mannheim.clarin.teispeech.data.GATParser;
import de.ids.mannheim.clarin.teispeech.data.LanguageDetect;
import de.ids.mannheim.clarin.teispeech.tools.ProcessingLevel;
import de.ids.mannheim.clarin.teispeech.utilities.ServiceUtilities;
import de.ids.mannheim.clarin.teispeech.workflow.*;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.jdom2.JDOMException;
import org.korpora.useful.Anonymize;
import org.korpora.useful.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;


/**
 * Webservices for dealing with TEI-encoded documents
 *
 * @author bfi
 */


@Path("")
public class TEILicht {

    private final static Logger LOGGER = LoggerFactory
            .getLogger(TEILicht.class.getName());

    /**
     * convert to a TEI ISO transcription:
     *
     * @param input
     *         the input document – plain text!
     * @param language
     *         the presumed language, preferably a ISO 639 code
     * @param request
     *         the HTTP request
     * @return a TEI-encoded speech transcription
     */
    @POST
    @Path("text2iso")
    @Consumes({MIMETypes.PLAIN_TEXT})
    @Produces({MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML})

    public Response text2iso(InputStream input,
                             @QueryParam("lang") String language,
                             @Context HttpServletRequest request) {
        try {
            ServiceUtilities.checkLanguage(language);
            LOGGER.info("Processing <{}> of length {} for {}.",
                    request.getHeader(HttpHeaders.CONTENT_TYPE),
                    request.getHeader(HttpHeaders.CONTENT_LENGTH),
                    Anonymize.anonymizeAddress(request));
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
     *         a TEI-encoded speech transcription
     * @param language
     *         the presumed language, preferably a ISO 639 code
     * @param keepCase
     *         if true, do not convert to lower case when normalizing and
     *         effectively skip capitalized words
     * @param force
     *         whether to force normalization
     * @param request
     *         the HTTP request
     * @return a TEI-encoded speech transcription with normalization in
     * &lt;w&gt;
     */
    @POST
    @Path("normalize")
    @Consumes({MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML})
    @Produces({MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML})

    public Response normalize(InputStream input,
                              @QueryParam("lang") String language,
                              @QueryParam("keep_case") boolean keepCase,
                              @QueryParam("force") boolean force,
                              @Context HttpServletRequest request) {
        try {
            ServiceUtilities.checkLanguage(language);
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            DictionaryNormalizer diNo = new DictionaryNormalizer(keepCase,
                    false);
            TEINormalizer teiDictNormalizer = new TEINormalizer(diNo, language);
            LOGGER.info("Processing <{}> of length {} for {}.",
                    request.getHeader(HttpHeaders.CONTENT_TYPE),
                    request.getHeader(HttpHeaders.CONTENT_LENGTH),
                    Anonymize.anonymizeAddress(request));
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
     *         a TEI-encoded speech transcription
     * @param language
     *         the presumed language, preferably a ISO 639 code
     * @param force
     *         whether to force normalization
     * @param request
     *         the HTTP request
     * @return a TEI-encoded speech transcription with POS tags
     */
    @POST
    @Path("pos")
    @Consumes({MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML})
    @Produces({MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML})

    public Response pos(InputStream input, @QueryParam("lang") String language,
                        @QueryParam("force") boolean force,
                        @Context HttpServletRequest request) {
        try {
            ServiceUtilities.checkLanguage(language);
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            TEIPOS teipo = new TEIPOS(doc, language);
            LOGGER.info("Processing <{}> of length {} for {}.",
                    request.getHeader(HttpHeaders.CONTENT_TYPE),
                    request.getHeader(HttpHeaders.CONTENT_LENGTH),
                    Anonymize.anonymizeAddress(request));
            teipo.posTag(force);
            return Response.ok(doc, request.getContentType()).build();
        } catch (IllegalArgumentException | SAXException
                | ParserConfigurationException | IOException e) {
            throw new WebApplicationException(e,
                    Response.status(400).entity(e.getMessage()).build());
        }
    }

    /**
     * Detect languages in a a TEI ISO transcription:
     *
     * @param input
     *         a TEI-encoded speech transcription
     * @param expected
     *         the languages expected in the document
     * @param language
     *         the presumed language, preferably a ISO 639 code
     * @param force
     *         whether to force normalization
     * @param minimalLength
     *         the minimal length of an utterance to attempt language
     *         detection
     * @param request
     *         the HTTP request
     * @return a TEI-encoded speech transcription with languages detected
     */
    @POST
    @Path("guess")
    @Consumes({MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML})
    @Produces({MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML})

    public Response guess(InputStream input,
                          @QueryParam("lang") String language,
                          @QueryParam("expected") List<String> expected,
                          @QueryParam("force") boolean force,
                          @QueryParam("minimal_length") @DefaultValue("5") int minimalLength,
                          @Context HttpServletRequest request) {
        try {
            ServiceUtilities.checkLanguage(language);
            String[] expectedLangs = expected.stream()
                    .map(ServiceUtilities::checkLanguage)
                    .toArray(String[]::new);
            if (expectedLangs.length == 1){
                expectedLangs = expectedLangs[0].split("[, ]");
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            LanguageDetect guesser = new LanguageDetect(doc, language,
                    expectedLangs, minimalLength);
            LOGGER.info("Processing <{}> of length {} for {}.",
                    request.getHeader(HttpHeaders.CONTENT_TYPE),
                    request.getHeader(HttpHeaders.CONTENT_LENGTH),
                    Anonymize.anonymizeAddress(request));
            guesser.detect(force);
            return Response.ok(doc, request.getContentType()).build();
        } catch (IllegalArgumentException | SAXException
                | ParserConfigurationException | IOException e) {
            throw new WebApplicationException(e,
                    Response.status(400).entity(e.getMessage()).build());
        }
    }

    /**
     * segmentize TEI ISO document according to transcription conventions:
     *
     * @param input
     *         a TEI-encoded speech transcription
     * @param level
     *         the parsing level: generic, minimal, basic
     * @param request
     *         the HTTP request
     * @return a TEI-encoded speech transcription with annotation parsed
     */
    @POST
    @Path("segmentize")
    @Consumes({MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML})
    @Produces({MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML})

    public Response segmentize(InputStream input,
                               @DefaultValue("generic") @QueryParam("level") ProcessingLevel level,
                               @Context HttpServletRequest request) {
        if (level == ProcessingLevel.generic)
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory
                        .newInstance();
                factory.setNamespaceAware(true);
                DocumentBuilder builder;
                builder = factory.newDocumentBuilder();
                Document doc = builder.parse(input);
                GenericParsing.process(doc);
                return Response.ok(doc, request.getContentType()).build();
            } catch (SAXException | IOException
                    | ParserConfigurationException e) {
                throw new WebApplicationException(e,
                        Response.status(400).entity(e.getMessage()).build());
            }
        else
            try {
                org.jdom2.Document doc = Utilities.parseXMLviaJDOM(input);
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

    /**
     * Detect languages in a a TEI ISO transcription:
     *
     * @param input
     *         a TEI-encoded speech transcription
     * @param language
     *         the presumed language, preferably a ISO 639 code
     * @param transcribe
     *         whether to add a phonetic transcription to the utterances if
     *         possible
     * @param usePhones
     *         whether to use (pseudo)phones to determine relative duration
     *         of words
     * @param force
     *         whether to force normalization
     * @param request
     *         the HTTP request
     * @param time
     *         the time length of the conversation in seconds
     * @param offset
     *         the time offset in seconds
     * @param every
     *         number of items after which to insert an orientation anchor
     * @return a TEI-encoded speech transcription with languages detected
     */
    @POST
    @Path("align")
    @Consumes({MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML})
    @Produces({MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML})

    public Response align(InputStream input,
                          @QueryParam("lang") String language,
                          @QueryParam("transcribe") @DefaultValue("true") boolean transcribe,
                          @QueryParam("use_phones") @DefaultValue("true") boolean usePhones,
                          @QueryParam("force") boolean force,
                          @QueryParam("time") double time,
                          @QueryParam("offset")  @DefaultValue("5") double offset,
                          @QueryParam("every") int every,
                          @Context HttpServletRequest request) {
        try {
            ServiceUtilities.checkLanguage(language);
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            PseudoAlign aligner = new PseudoAlign(doc, language, usePhones,
                    transcribe, force, time, offset, every);
            LOGGER.info("Processing <{}> of length {} for {}.",
                    request.getHeader(HttpHeaders.CONTENT_TYPE),
                    request.getHeader(HttpHeaders.CONTENT_LENGTH),
                    Anonymize.anonymizeAddress(request));
            aligner.calculateUtterances();
            return Response.ok(aligner.getDoc(),
                    request.getContentType()).build();
        } catch (IllegalArgumentException | SAXException
                | ParserConfigurationException | IOException e) {
            throw new WebApplicationException(e,
                    Response.status(400).entity(e.getMessage()).build());
        }
    }

    /**
     * add IDs to XML elements for roundtripping
     *
     * @param input
     *         a TEI-encoded speech transcription
     * @param request
     *         the HTTP request
     * @return a TEI-encoded speech transcription with IDs
     */
    @POST
    @Path("identify")
    @Consumes({MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML})
    @Produces({MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML})

    public Response identify(InputStream input,
                             @Context HttpServletRequest request) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            DocumentIdentifier.makeIDs(doc);
            LOGGER.info("Processing <{}> of length {} for {}.",
                    request.getHeader(HttpHeaders.CONTENT_TYPE),
                    request.getHeader(HttpHeaders.CONTENT_LENGTH),
                    Anonymize.anonymizeAddress(request));
            return Response.ok(doc, request.getContentType()).build();
        } catch (IllegalArgumentException | SAXException
                | ParserConfigurationException | IOException e) {
            throw new WebApplicationException(e,
                    Response.status(400).entity(e.getMessage()).build());
        }
    }

    /**
     * remove IDs from XML elements which are only used in roundtripping
     *
     * @param input
     *         a TEI-encoded speech transcription
     * @param request
     *         the HTTP request
     * @return a TEI-encoded speech transcription without IDs
     */
    @POST
    @Path("unidentify")
    @Consumes({MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML})
    @Produces({MIMETypes.TEI_SPOKEN, MIMETypes.DTA, MIMETypes.TEI,
            MIMETypes.XML})

    public Response unidentify(InputStream input,
                               @Context HttpServletRequest request) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder;
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            DocumentIdentifier.removeIDs(doc);
            LOGGER.info("Processing <{}> of length {} for {}.",
                    request.getHeader(HttpHeaders.CONTENT_TYPE),
                    request.getHeader(HttpHeaders.CONTENT_LENGTH),
                    Anonymize.anonymizeAddress(request));
            return Response.ok(doc, request.getContentType()).build();
        } catch (IllegalArgumentException | SAXException
                | ParserConfigurationException | IOException e) {
            throw new WebApplicationException(e,
                    Response.status(400).entity(e.getMessage()).build());
        }
    }
}
