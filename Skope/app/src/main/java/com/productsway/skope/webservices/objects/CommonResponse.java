/**
* Skope
*
* Created by Vo Hoang San - hoangsan.762@gmai.com
* Copyright (c) 2015 San Vo. All right reserved.
*/

package com.productsway.skope.webservices.objects;

/**
* Common response class for all responses.
*
*/
public class CommonResponse {
    protected Meta meta;

	public boolean hasError() {
		return meta.getCode() != 200;
	}
}
