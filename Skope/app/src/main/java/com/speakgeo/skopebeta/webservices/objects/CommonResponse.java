/**
* Skope
*
* Created by Vo Hoang San - hoangsan.762@gmai.com
* Copyright (c) 2015 San Vo. All right reserved.
*/

package com.speakgeo.skopebeta.webservices.objects;

/**
* Common response class for all responses.
*
*/
public class CommonResponse {
    private Meta meta;

	public boolean hasError() {
		return meta.getCode() != 200;
	}

    public Meta getMeta() {
        return meta;
    }

    public class Meta {
        private String message;
        private int code;
        private String version;

        public String getMessage() {
            return message;
        }

        public int getCode() {
            return code;
        }

        public String getVersion() {
            return version;
        }
    }

    public class Data {
        private String message;

        public String getMessage() {
            return message;
        }
    }
}
