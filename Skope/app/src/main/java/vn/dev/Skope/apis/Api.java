package vn.dev.Skope.apis;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tientun on 3/9/15.
 */
public class Api {
    public static final String API_BASE_URL = "http://192.168.10.78/patchfilm/api/public/api/";

    public static final String PATH_AUTH = "user/login";
    public static final String PATH_REGISTER = "user/register";
    public static final String PATH_UPDATE_TOKEN = "user/update-device-token";
    public static final String PATH_LOGIN_FACEBOOK = "user/facebook-login";
    public static final String PATH_LOGIN_TWITTER = "user/twitter-login";
    public static final String PATH_GET_NEW_IMAGES = "image/new-images";
    public static final String PATH_GET_RECOMMENT_TAG_IMAGES = "image/recommended-tags-images";
    public static final String PATH_GET_MAP_IMAGES = "image/map";
    public static final String PATH_GET_MY_PAGE = "image/my-page-images";
    public static final String PATH_GET_MY_PAGE_ALBUM = "image/my-page-albums";
    public static final String PATH_UPDATE_PROFILE = "user/update-profile";
    public static final String PATH_UPLOAD_PROFILE_PHOTO = "user/upload-profile-photo";
    public static final String PATH_LIKE_PHOTO = "image/like-unlike-image";
    public static final String PATH_POST_IMAGE = "image/post-image";
    public static final String PATH_FORGOT_PASSWORD = "user/forgot-password";
    public static final String PATH_GET_IMAGE_DETAIL = "image/image-detail";

    private static Api instance;
    private RequestQueue mRequestQueue;
    private static Context mContext;
    private String mAccessToken = null;

    private static final String API_DEVICE_TYPE = "1";

    public static void init(Context context) {
        instance = new Api(context);
    }

    private Api(Context context) {
        this.mContext = context;
    }

    public static Api getInstance() {
        if (mContext == null) {
            throw new IllegalStateException("You must call init(Context context) first.");
        }
        return instance;
    }
//    public boolean isAuthed(){
//        return apiRequest !=null&& apiRequest.getAccessToken()!=null;
//    }

    public void auth(Map<String, String> params, final Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, final vn.dev.Skope.apis.ApiErrorListener errorListener) {
        params.put("device_id", getDeviceId(mContext));
        params.put("device_token", getDevicePushToken(mContext));
        params.put("device_type", getDeviceType());
        post(PATH_AUTH, params, new Response.Listener<vn.dev.Skope.apis.ResponseData>() {
            @Override
            public void onResponse(vn.dev.Skope.apis.ResponseData response) {
                responseListener.onResponse(response);
            }
        }, new vn.dev.Skope.apis.ApiErrorListener() {
            @Override
            public void onErrorResponse(vn.dev.Skope.apis.ApiError error) {
                errorListener.onErrorResponse(error);
            }
        });
    }

    /**
     * @param params nick_name
     *               password
     *               email
     *               gender
     *               device_id
     *               device_token
     *               device_type
     */
    public void register(Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        params.put("device_id", getDeviceId(mContext));
        params.put("device_token", getDevicePushToken(mContext));
        params.put("device_type", getDeviceType());
        post(PATH_REGISTER, params, responseListener, errorListener);
    }

    /**
     * @param params device_token
     *               device_id
     *               device_type
     */
    public void updateDeviceToken(Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        post(PATH_UPDATE_TOKEN, params, responseListener, errorListener);
    }

    /**
     * @param params facebook_id
     *               email
     *               name
     *               gender
     *               nick_name
     *               avatar_link
     *               device_id
     *               device_token
     *               device_type
     */
    public void loginFacebook(Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        post(PATH_LOGIN_FACEBOOK, params, responseListener, errorListener);
    }

    /**
     * @param params facebook_id
     *               email
     *               name
     *               gender
     *               nick_name
     *               avatar_link
     *               device_id
     *               device_token
     *               device_type
     */
    public void loginTwitter(Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        post(PATH_LOGIN_TWITTER, params, responseListener, errorListener);
    }

    /**
     * @param params start
     *               limit
     */
    public void getNewImages(Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        get(PATH_GET_NEW_IMAGES, params, responseListener, errorListener);
    }

    /**
     * @param params start
     *               limit
     *               tags
     */
    public void getRecommentTagImages(Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        get(PATH_GET_RECOMMENT_TAG_IMAGES, params, responseListener, errorListener);
    }

    /**
     * @param params map_scale
     *               center_location_lat
     *               center_location_lng
     *               tags
     *               time_filter
     */
    public void getMapImages(Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        get(PATH_GET_MAP_IMAGES, params, responseListener, errorListener);
    }

    /**
     * @param params start
     *               limit
     */
    public void getMyPage(Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        get(PATH_GET_MY_PAGE, params, responseListener, errorListener);
    }

    /**
     * @param params start
     *               limit
     */
    public void getMyPagealbums(Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        get(PATH_GET_MY_PAGE_ALBUM, params, responseListener, errorListener);
    }

    /**
     * @param params nick_name
     *               gender
     *               email
     *               country_id
     *               is_push
     *               is_email_push
     *               connected_facebook
     *               connected_twitter
     *               connected_instagram
     *               is_deleted_image
     */
    public void updateProfile(Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        post(PATH_UPDATE_PROFILE, params, responseListener, errorListener);
    }

    /**
     * @param params image_id
     *               status
     */
    public void likePhoto(Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        post(PATH_LIKE_PHOTO, params, responseListener, errorListener);
    }

    /**
     * @param params image
     */
    public void uploadProfilePhoto(Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        upload(PATH_UPLOAD_PROFILE_PHOTO, params, responseListener, errorListener);
    }

    /**
     * @param params image
     *               tags
     *               lat
     *               lng
     *               subject
     *               width
     *               height
     */
    public void postImage(Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        post(PATH_POST_IMAGE, params, responseListener, errorListener);
    }

    /**
     * @param params email
     */
    public void forgotPassword(Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        post(PATH_FORGOT_PASSWORD, params, responseListener, errorListener);
    }

    /**
     * @param params image_id
     */
    public void getImageDetail(Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        post(PATH_GET_IMAGE_DETAIL, params, responseListener, errorListener);
    }


    /**
     * Thread request to servece
     *
     * @param pathMethod
     * @param params
     */
    public vn.dev.Skope.apis.ApiRequest post(final String pathMethod, final Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        return request(Request.Method.POST, pathMethod, params, responseListener, errorListener);
    }

    public vn.dev.Skope.apis.ApiRequest get(final String pathMethod, final Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        return request(Request.Method.GET, pathMethod, params, responseListener, errorListener);
    }

    public vn.dev.Skope.apis.ApiRequest upload(final String pathMethod, final Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        return request(Request.Method.POST, pathMethod, params, responseListener, errorListener);
    }

    public vn.dev.Skope.apis.ApiRequest request(int method, final String pathMethod, final Map<String, String> params, Response.Listener<vn.dev.Skope.apis.ResponseData> responseListener, vn.dev.Skope.apis.ApiErrorListener errorListener) {
        String url = API_BASE_URL + pathMethod;
        Map<String, String> headers = new HashMap<String, String>();
        if (getAccessToken() != null) {
            headers.put("Authorization", String.format("Bearer {%s}", getAccessToken()));
        }
        vn.dev.Skope.apis.ApiRequest request = new vn.dev.Skope.apis.ApiRequest(method, url, params, headers, responseListener, errorListener);
        getRequestQueue().add(request);
        return request;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }

    public static String getDeviceType() {
        return API_DEVICE_TYPE;
    }

    public static String getDevicePushToken(Context context) {
        return "test_push_token";
    }

    public static String getDeviceId(Context context) {
        return "test_device_token";
    }

    public String getAccessToken() {
        return null;
    }

}
