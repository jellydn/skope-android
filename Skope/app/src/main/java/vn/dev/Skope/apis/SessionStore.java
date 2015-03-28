package vn.dev.Skope.apis;

import android.content.Context;
import android.content.SharedPreferences.Editor;

/**
 * Created by tientun on 3/9/15.
 */
public class SessionStore {
    
    private static final String TOKEN = "access_token";
    private static final String USER = "access_user";
    private static final String USER_ID = "access_user_id";
    private static final String KEY = "ast-session";
    
    public static boolean save(ApiRequest apiRequest, Context context) {
        Editor editor =
            context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
//        editor.putString(TOKEN, apiRequest.getAccessToken());
//        editor.putString(USER, apiRequest.getAccessName());
//        editor.putString(USER_ID, apiRequest.getAccessId());
        return editor.commit();
    }

//    public static boolean restore(ApiRequest apiRequest, Context context) {
//        SharedPreferences savedSession =
//            context.getSharedPreferences(KEY, Context.MODE_PRIVATE);
//        apiRequest.setAccessName(savedSession.getString(USER, null));
//        apiRequest.setAccessId(savedSession.getString(USER_ID, null));
//        return apiRequest.setAccessToken(savedSession.getString(TOKEN, null));
//    }

    public static void clear(Context context) {
        Editor editor = 
            context.getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
    
}

