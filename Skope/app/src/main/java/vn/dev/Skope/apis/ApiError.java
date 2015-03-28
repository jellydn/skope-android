package vn.dev.Skope.apis;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

/**
 * Created by tientun on 3/9/15.
 */
public class ApiError extends VolleyError {
    private String mMessage = null;
    private int mStatusCode = 0;

    public ApiError(NetworkResponse response, String message, int statusCode) {
        super(response);
        this.mMessage = message;
        this.mStatusCode = statusCode;
    }

    @Override
    public String getMessage() {
        return mMessage !=null? mMessage :super.getMessage();
    }
    public int getStatusCode(){
        return mStatusCode;
    }
}
