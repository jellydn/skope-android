package vn.dev.Skope.apis;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by tientun on 3/6/15.
 */
public class ResponseData {
    private boolean success = false;
    private JsonObject data = null;

    public boolean isSuccess(){
        return success;
    }

    public String getData(){
        return this.data.toString();
    }

    public <T> T getData(Class<T> clazz){
        final Gson gson = new Gson();
        return gson.fromJson(data, clazz);
    }
    public <T> T getData(String key, Class<T> clazz){
        final Gson gson = new Gson();
        JsonObject jo = data.getAsJsonObject(key);
        return gson.fromJson(jo, clazz);
    }
}
