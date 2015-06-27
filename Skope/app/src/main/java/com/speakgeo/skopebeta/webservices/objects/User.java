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
public class User implements Serializable {
    private String id;
    private String name;
    private String avatar;
    private String email;
    private String first_name;
    private String last_name;
    private String gender;
    private String timezone;
    private String created_at;
    private Ejabberd ejabberd;
    private Location location;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getGender() {
        return gender;
    }

    public String getTimezone() {
        return timezone;
    }

    public String getCreated_at() {
        return created_at;
    }

    public Ejabberd getEjabberd() {
        return ejabberd;
    }

    public Location getLocation() {
        return location;
    }
}
