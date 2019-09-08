package com.example.socketmessage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.socketmessage.ImageUpload.ImageProcess;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListAdapter adapter;
    private ArrayList<ListModel> msgArrayList;
    private SocketInit socketInit;

    // ---- Image

        private int PICK_IMAGE_REQUEST = 1;
        private Bitmap bitmap;
        private Uri filePath;
    private boolean sendImage = false;
    private ImageProcess imageProcess;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.socketInit = new SocketInit();
        this.imageProcess = new ImageProcess(this);
        imageView = findViewById(R.id.imagePreview);

        setList();
        sendClick();
        socketListener();
    }

    void setList(){
        msgArrayList = new ArrayList<>();
        adapter = new ListAdapter(msgArrayList, this);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    void sendClick() {
        Button btn = findViewById(R.id.send);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView msg = findViewById(R.id.editText);
                String msgTxt = msg.getText().toString();

                if(sendImage) imageProcess.uploadImage(bitmap, msgTxt);
                else socketInit.mSocket.emit("messagedetection", "some guy", msgTxt, null);

                // ---- Purge

                msg.setText("");
                sendImage = false;
                imageView.setImageResource(android.R.color.transparent);
            }
        });

        Button imgBtn = findViewById(R.id.selectImageButton);
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
    }

    // ------ Upload Img

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            sendImage = true;
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // ------- Add & Refresh Data

    void addMessage(String username, String message, String imgUrl){
        msgArrayList.add(new ListModel(username + ": " + message, imgUrl));
        adapter.notifyDataSetChanged();
    }


    // ---- Listener socket message received

    public void socketListener(){
        Emitter.Listener onNewMessage = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        String username;
                        String message;
                        String imgUrl;
                        try {
                            username = data.getString("sendername");
                            message = data.getString("message");
                            imgUrl = data.getString("imgUrl");
                            addMessage(username, message, imgUrl);
                        } catch (JSONException e) {
                            Log.e("Received socket error", e.toString());
                        }
                    }
                });
            }
        };

        socketInit.mSocket.on("newmessage", onNewMessage);
    }

    // ---- Destroy

    @Override
    public void onDestroy() {
        super.onDestroy();
        socketInit.mSocket.disconnect();
    }

}