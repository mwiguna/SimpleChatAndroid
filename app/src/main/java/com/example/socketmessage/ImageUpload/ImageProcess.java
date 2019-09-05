package com.example.socketmessage.ImageUpload;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.example.socketmessage.SocketInit;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageProcess {

    private SocketInit socketInit = new SocketInit();
    private VolleyAPI api = new VolleyAPI();
    public static final String UPLOAD_URL = "http://192.168.100.6/project/myproject/socketimg/index.php";
    public static final String UPLOAD_KEY = "image";

    private Context context;

    public ImageProcess(Context context){
        this.context = context;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage(Bitmap bitmap, final String msgTxt) {
        Map<String, String> data = new HashMap<>();
        data.put(UPLOAD_KEY, getStringImage(bitmap));

        api.postDataVolley(data,UPLOAD_URL, context, new VolleyResponseListener() {

            @Override
            public void onResponse(String response) {
                Log.e("result", String.valueOf(response));
                socketInit.mSocket.emit("messagedetection", "some guy", msgTxt, response);
            }
        });
    }

}
