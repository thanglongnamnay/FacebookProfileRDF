/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.popeye.jenaexample;

import com.github.jsonldjava.utils.Obj;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.VCARD;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * Tutorial 4 - create a model and write it in XML form to standard out
 */
public class Tutorial04 extends Object {

    // some definitions
    static String rootUri = "https://graph.facebook.com/";

    public static Resource getPhoto(Model model, JSONObject photo) {
        Resource resource = model.createResource(rootUri + photo.get("id"));
        resource.addProperty(SCHEMA.WIDTH, String.valueOf(photo.get("width")))
                .addProperty(SCHEMA.HEIGHT, String.valueOf(photo.get("height")))
                .addProperty(SCHEMA.URL, (String) photo.get("picture"));
        return resource;
    }

    public static Resource getAlbum(Model model, JSONObject album) {
        Resource resource = model.createResource(rootUri + album.get("id"))
                .addProperty(VCARD.NAME, (String) album.get("name"));
        JSONObject photos1 = (JSONObject) album.get("photos");
        if (photos1 == null) return resource;
        JSONArray photos = (JSONArray) photos1.get("data");
        for (Object photo : photos) {
            resource.addProperty(SCHEMA.PHOTO, getPhoto(model, (JSONObject) photo));
        }
        return resource;
    }

    public static Resource getComment(Model model, JSONObject comment) {
        Resource resource = model.createResource(rootUri + comment.get("id"));
        String created_time = (String) comment.get("created_time");
        String message = (String) comment.get("message");
        String fromId = (String) ((JSONObject) comment.get("from")).get("id");
        if (created_time != null)
            resource.addProperty(SCHEMA.CREATED_TIME, created_time);
        if (message != null) resource.addProperty(SCHEMA.MESSAGE, message);
        resource.addProperty(SCHEMA.CREATED_BY, model.createResource(rootUri + fromId));
        return resource;
    }

    public static Resource getPost(Model model, JSONObject post) {
        Resource resource = model.createResource(rootUri + post.get("id"));
        String url = (String) post.get("permalink_url");
        String name = (String) post.get("name");
        String anchor_link = (String) post.get("link");
        String status_type = (String) post.get("status_type");
        String caption = (String) post.get("caption");
        String picture = (String) post.get("picture");
        if (url != null) resource.addProperty(SCHEMA.PERMALINK_URL, url);
        if (name != null) resource.addProperty(SCHEMA.NAME, name);
        if (anchor_link != null)
            resource.addProperty(SCHEMA.ANCHOR_LINK, anchor_link);
        if (status_type != null)
            resource.addProperty(SCHEMA.STATUS_TYPE, status_type);
        if (caption != null) resource.addProperty(SCHEMA.CAPTION, caption);
        if (picture != null) resource.addProperty(SCHEMA.PICTURE, picture);

        JSONObject commentObject = (JSONObject) post.get("comments");
        if (commentObject != null) {
            JSONArray comments = (JSONArray) commentObject.get("data");
            for (Object comment : comments) {
                resource.addProperty(SCHEMA.COMMENT, getComment(model, (JSONObject) comment));
            }
        }
        return resource;
    }

    public static Resource getProfile(Model model, JSONObject profile) {
        Resource resource = model.createResource(rootUri + profile.get("id"))
                .addProperty(VCARD.FN, (String) profile.get("name"))
                .addProperty(VCARD.BDAY, (String) profile.get("birthday"))
                .addProperty(FOAF.gender, (String) profile.get("gender"))
                .addProperty(VCARD.ADR, "Hanoi, Vietnam")
                .addProperty(SCHEMA.ID, "1887831738007020");
        JSONArray albums = (JSONArray) ((JSONObject) profile.get("albums")).get("data");
        for (Object album : albums) {
            resource.addProperty(SCHEMA.ALBUM, getAlbum(model, (JSONObject) album));
        }

        JSONArray posts = (JSONArray) ((JSONObject) profile.get("feed")).get("data");
        for (Object post : posts) {
            resource.addProperty(SCHEMA.POST, getPost(model, (JSONObject) post));
        }
        return resource;
    }

    public static void linkFriend(Model model, Resource r1, Resource r2) {
        r1.addProperty(SCHEMA.FRIEND, r2);
        r2.addProperty(SCHEMA.FRIEND, r1);
    }

    public static void main(String[] args) {
        JSONParser parser = new JSONParser();

        try {
            JSONObject json = (JSONObject) parser.parse(new FileReader("resources/data.json"));
            JSONObject person2 = (JSONObject) parser.parse(new FileReader("resources/person2.json"));
            JSONObject person3 = (JSONObject) parser.parse(new FileReader("resources/person3.json"));
            Model model = ModelFactory.createDefaultModel();
            Resource me = getProfile(model, json);
            Resource friend1 = getProfile(model, person2);
            Resource friend2 = getProfile(model, person3);
            linkFriend(model, me, friend2);
            linkFriend(model, me, friend1);

            model.write(System.out);
            FileOutputStream fileOutputStream = new FileOutputStream("resources/data.rdf");
            model.write(fileOutputStream);
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
