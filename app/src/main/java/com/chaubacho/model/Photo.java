package com.chaubacho.model;

import java.io.Serializable;

public class Photo implements Serializable {
    //should have this to define UID for declare version of serializable
    private static final long serialVersionUID = 1L;

    private String title;
    private String farm_id;
    private String server_id;
    private String id;
    private String secret;

    public Photo(String title, String farm_id, String server_id, String id, String secret) {
        this.title = title;
        this.farm_id = farm_id;
        this.id = id;
        this.secret = secret;
        this.server_id = server_id;
    }

    public String getTitle() {
        return title;
    }

    public String getFarm_id() {
        return farm_id;
    }

    public String getServer_id() {
        return server_id;
    }

    public String getId() {
        return id;
    }


    public String getSecret() {
        return secret;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "title='" + title + '\n' +
                ", author='" + farm_id + '\n' +
                ", author_id='" + server_id + '\n' +
                ", link='" + id + '\n' +
                ", image='" + secret + '\n' +
                '}';
    }
}
