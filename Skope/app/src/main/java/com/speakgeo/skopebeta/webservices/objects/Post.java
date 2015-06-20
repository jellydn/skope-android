/**
* Skope
*
* Created by Vo Hoang San - hoangsan.762@gmai.com
* Copyright (c) 2015 San Vo. All right reserved.
*/

package com.speakgeo.skopebeta.webservices.objects;

import java.util.ArrayList;

/**
* Response of Login API.
*
*/
public class Post {
    private String id;
    private String content;
    private ArrayList<Media> media;
    private User user;
    private Like like;
    private Dislike dislike;
    private String voted_type;
    private long created_at;
    private Comment comment;
    private Location location;

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public ArrayList<Media> getMedia() {
        return media;
    }

    public User getUser() {
        return user;
    }

    public Like getLike() {
        return like;
    }

    public Dislike getDislike() {
        return dislike;
    }

    public String getVoted_type() {
        return voted_type;
    }

    public long getCreated_at() {
        return created_at;
    }

    public Comment getComment() {
        return comment;
    }

    public Location getLocation() {
        return location;
    }

    public class Like {
        private int total;

        public int getTotal() {
            return total;
        }
    }

    public class Dislike {
        private int total;

        public int getTotal() {
            return total;
        }
    }
}
