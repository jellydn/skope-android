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
public class Comment {
    private int total;
    private ArrayList<CommentItem> items;

    public int getTotal() {
        return total;
    }

    public ArrayList<CommentItem> getItems() {
        return items;
    }

    public class CommentItem {
        private String id;
        private String content;
        private User user;
        private long created_at;

        public String getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        public User getUser() {
            return user;
        }

        public long getCreated_at() {
            return created_at;
        }
    }
}
