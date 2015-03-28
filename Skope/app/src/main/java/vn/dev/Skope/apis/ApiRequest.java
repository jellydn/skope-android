package vn.dev.Skope.apis;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by tientun on 3/6/15.
 */
public class ApiRequest extends Request<vn.dev.Skope.apis.ResponseData> {
    private static final String TAG = "ApiRequest";
    private final Map<String, String> headers;
    private final Map<String, String> params;
    private final Response.Listener<vn.dev.Skope.apis.ResponseData> listener;
    private final ApiErrorListener errorListener;
    private ApiError apiError;

    public ApiRequest(int method, String url, Map<String, String> params, Map<String, String> headers, Response.Listener<vn.dev.Skope.apis.ResponseData> listener, ApiErrorListener errorListener) {
        super(method, url, null);
        this.headers = headers;
        this.params = params;
        this.listener = listener;
        this.errorListener = errorListener;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse response) {
        final Gson gson = new Gson();
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            Log.d(TAG, "Response: " + json);
            vn.dev.Skope.apis.ResponseData rd = gson.fromJson(json, vn.dev.Skope.apis.ResponseData.class);
            if (!rd.isSuccess()){
                return Response.error(new VolleyError(response));
            }
            return Response.success(rd, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }

    }

    /*@Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            errorListener = new VolleyError(new String(volleyError.networkResponse.data));
        }
        return volleyError;
    }*/

    @Override
    protected void deliverResponse(vn.dev.Skope.apis.ResponseData response) {
        listener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        super.deliverError(error);
        String msg = null;
        int statusCode = 0;
        if (error.networkResponse != null && error.networkResponse.data != null) {
            JsonParser parser = new JsonParser();
            JsonObject jo = parser.parse(new String(error.networkResponse.data)).getAsJsonObject();
            msg = jo.get("message").getAsString();
            statusCode = jo.get("status_code").getAsInt();
        }
        ApiError e = new ApiError(error.networkResponse, msg, statusCode);
        errorListener.onErrorResponse(e);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> h = headers != null ? headers : super.getHeaders();
        return h;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params != null ? params : super.getParams();
    }
}
