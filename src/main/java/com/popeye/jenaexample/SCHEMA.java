package com.popeye.jenaexample;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;

public class SCHEMA {
    /**
     * The namespace of the vocabulary as a string
     */
    public static final String uri ="https://schema.org/";

    /** returns the URI for this schema
     * @return the URI for this schema
     */
    public static String getURI() {
        return uri;
    }

    private static final Model m = ModelFactory.createDefaultModel();
    public static final Property WIDTH = m.createProperty(uri + "width");
    public static final Property HEIGHT = m.createProperty(uri + "height");
    public static final Property URL = m.createProperty(uri + "url");
    public static final Property PHOTO = m.createProperty(uri + "photo");
    public static final Property CREATED_TIME = m.createProperty(uri + "created_time");
    public static final Property MESSAGE = m.createProperty(uri + "message");
    public static final Property CREATED_BY = m.createProperty(uri + "created_by");
    public static final Property PERMALINK_URL = m.createProperty(uri + "permalink_url");
    public static final Property NAME = m.createProperty(uri + "name");
    public static final Property ANCHOR_LINK = m.createProperty(uri + "anchor_link");
    public static final Property STATUS_TYPE = m.createProperty(uri + "status_type");
    public static final Property CAPTION = m.createProperty(uri + "caption");
    public static final Property PICTURE = m.createProperty(uri + "picture");
    public static final Property COMMENT = m.createProperty(uri + "comment");
    public static final Property ID = m.createProperty(uri + "id");
    public static final Property ALBUM = m.createProperty(uri + "album");
    public static final Property POST = m.createProperty(uri + "post");
    public static final Property FRIEND = m.createProperty(uri + "friend");
}
