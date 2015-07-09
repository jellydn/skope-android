/**
* Skope
*
* Created by Vo Hoang San - hoangsan.762@gmai.com
* Copyright (c) 2015 San Vo. All right reserved.
*/

package com.speakgeo.skopebeta.webservices.objects;

import java.io.Serializable;

/**
* Response of Login API.
*
*/
public class Media implements Serializable{
    private String id;
    private String type;
    private String src;
    private String thumb;

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSrc() {
        return src;
    }

    public String getThumb() {
        return thumb;
    }
}
