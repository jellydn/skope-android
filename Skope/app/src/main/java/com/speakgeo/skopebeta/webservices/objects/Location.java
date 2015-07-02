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
public class Location implements Serializable {
    private String distance;
    private String longitude;
    private String latitude;

    public String getDistance() {
        return distance;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }
}
