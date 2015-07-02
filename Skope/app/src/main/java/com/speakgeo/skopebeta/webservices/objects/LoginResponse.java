/**
* Skope
*
* Created by Vo Hoang San - hoangsan.762@gmai.com
* Copyright (c) 2015 San Vo. All right reserved.
*/

package com.speakgeo.skopebeta.webservices.objects;

/**
* Response of Login API.
*
*/
public class LoginResponse extends CommonResponse {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data extends CommonResponse.Data{
        private AccessToken accessToken;
        private User user;

        public AccessToken getAccessToken() {
            return accessToken;
        }

        public User getUser() {
            return user;
        }

        public class AccessToken {
            private String token;
            private String expired_at;

            public String getToken() {
                return token;
            }

            public String getExpired_at() {
                return expired_at;
            }
        }
    }
}
