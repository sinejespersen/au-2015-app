package itsmap.SV;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/*
We have used the following link to make this class
https://developer.android.com/training/volley/index.html
We have also used a previous project done in another course (Pervasive Positioning)
 */
public class ServerCom {
    private static ServerCom mInstance;
    private static Context context;
    private RequestQueue mRequestQueue;

    private ServerCom(Context context) {
        this.context = context;
    }

    public static synchronized ServerCom getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ServerCom(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}

