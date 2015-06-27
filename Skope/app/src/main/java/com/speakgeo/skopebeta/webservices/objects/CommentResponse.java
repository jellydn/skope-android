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
public class CommentResponse extends CommonResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data extends CommonResponse.Data{
        private CommentItem comment;

        public CommentItem getComment() {
            return comment;
        }
    }
}
