package de.ids.mannheim.clarin.normalverbraucher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

@Path("orthonormal")
public class OrthoNormal {
    
    
    private static DictionaryNormalizer dictNormalizer;
    private static TEINormalizer teiDictNormalizer; 
    static {
        dictNormalizer = new DictionaryNormalizer();
        teiDictNormalizer = new TEINormalizer(dictNormalizer);
    }
    
    @POST
    @Path("normalize")
    @Consumes("application/tei+xml;format-variant=tei-iso-spoken;tokenized=1")
    @Produces("application/tei+xml;format-variant=tei-iso-spoken;tokenized=1")

    public Response normalize(InputStream input) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            System.err.format("Have got %d <w> nodes.\n",
                    doc.getElementsByTagName("w").getLength());
            teiDictNormalizer.normalize(doc);
            DOMImplementationLS domImplementation = (DOMImplementationLS) doc.getImplementation();
            LSSerializer lsSerializer = domImplementation.createLSSerializer();
            return Response.ok(lsSerializer.writeToString(doc)).build();
        } catch (SAXException e) {
            throw new WebApplicationException(e, Response
                    .status(400).entity(e.getStackTrace()).build());             
        } catch (IOException e) {
            throw new WebApplicationException(e, Response
                    .status(400).entity(e.getStackTrace()).build());             
        } catch (Exception e) {
            throw new WebApplicationException(e, Response
                    .status(400).entity(e.getStackTrace()).build());             
        }
    }

    @PUT
    @Path("reload")
    public void reloadDict(String content) {
        dictNormalizer.reloadDict();
    }

}
