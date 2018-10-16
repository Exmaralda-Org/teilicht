package de.ids.mannheim.clarin.normalverbraucher;

import java.io.IOException;
import java.io.InputStream;

import javax.ws.rs.Consumes;
//import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.korpora.useful.Anonymize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

import de.ids.mannheim.clarin.mime.*;

@Path("")
public class OrthoNormal {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(
            OrthoNormal.class.getName());
    
    private static DictionaryNormalizer dictNormalizer;
    private static TEINormalizer teiDictNormalizer; 
    static {
        dictNormalizer = new DictionaryNormalizer();
        teiDictNormalizer = new TEINormalizer(dictNormalizer);
    }
    
    
    /**
     * normalize using an EXMARaLDA-OrthoNormal-based normalizer:
     * @param input, a TEI-encoded speech transcription
     * @return a TEI-encoded speech transcription with normalization in &lt;w&gt;
     */
    @POST
    @Path("normalize")
    @Consumes({
        MIMETypes.TEI_SPOKEN,
        MIMETypes.DTA,
        MIMETypes.TEI,
        MIMETypes.XML
    })
    @Produces({
        MIMETypes.TEI_SPOKEN,
        MIMETypes.DTA,
        MIMETypes.TEI,
        MIMETypes.XML
    })

    public Response normalize(InputStream input,
            @Context HttpServletRequest request) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            LOGGER.info("Processing <{}> of length {} for {}.",
                    request.getHeader(HttpHeaders.CONTENT_TYPE),
                    request.getHeader(HttpHeaders.CONTENT_LENGTH),
                    Anonymize.getRemoteAddr(request));
            teiDictNormalizer.normalize(doc);
            DOMImplementationLS domImplementation =
                    (DOMImplementationLS) doc.getImplementation();
            LSSerializer lsSerializer = domImplementation.createLSSerializer();
            return Response.ok(
                    lsSerializer.writeToString(doc),
                    request.getContentType()).build();
        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw new WebApplicationException(e, Response
                    .status(400).entity(e.getMessage()).build());             
        }
    }

    /**
     * reload dictionaries
     * @param content – will be disregarded
     */
    @PUT
    @Path("reload")
    public void reloadDict(String content) {
        dictNormalizer.reloadDict();
    }

}
