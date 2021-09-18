package net.znordic.rest_api_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.Headers;
import okio.Buffer;


import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkForPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        checkForPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        print();
    }

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/plain");
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final OkHttpClient client = new OkHttpClient();

    String doPostRequest(String url, String json) throws IOException {

        //sCDcmGfqK9g26udHHFQSOPD3v2gyFOrm", "zebra", "XXZKJ201000689"
// Create an interceptor which catches requests and logs the info you want

/*        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"sn\""),
                        RequestBody.create(null, "XXZKJ201000689"))
                .addFormDataPart("sn", "XXZKJ201000689")
                .addFormDataPart("zpl_file", "my.zpl", RequestBody.create(MEDIA_TYPE_MARKDOWN, new File("/sdcard/Android/data/net.znordic.rest_api_java/files/barcodelabel.zpl")))
                .build();
        RequestBody requestBody2 = new FormBody.Builder()

                .add("sn", "XXZKJ201000689")
                .add("zpl_file", "my.zpl")
                .build();

        Request request = new Request.Builder()
                .addHeader("accept","text/plain")
                .url("https://znordic.net/poc/test.php")

                .url("https://api.zebra.com/v2/devices/printers/send")
                .addHeader("apikey", "sCDcmGfqK9g26udHHFQSOPD3v2gyFOrm")
                .addHeader("tenant", "zebra")
                .post(requestBody)
                .build();

                Log.d("REST__",bodyToString(requestBody));

        String veryLongString = bodyToString(requestBody);
        int maxLogSize = 1000;
        for(int i = 0; i <= veryLongString.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > veryLongString.length() ? veryLongString.length() : end;
            Log.v("REST__", veryLongString.substring(start, end));
        }


        Response response = client.newCall(request).execute();
        return response.body().string();

 */
            return null;
    }
    private static String bodyToString(final RequestBody request){
        try {
            final RequestBody  copy = request;
            final Buffer buffer = new Buffer();

            copy.writeTo(buffer);

            StringBuffer sb = new StringBuffer();
            sb.append(buffer.readUtf8());

          //  sb.replace("Content-Length: 14", "XXZKJ201000689");

            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    void print() {
        Log.d("REST__", "start print");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("REST__", "asdasdas print");

                  //  MultipartUtility multipart = new MultipartUtility("http://znordic.net/poc/test.php", "utf-8");
                    MultipartUtility multipart = new MultipartUtility("https://api.zebra.com/v2/devices/printers/send", "utf-8");
                //    multipart.addHeaderField("apikey", "sCDcmGfqK9g26udHHFQSOPD3v2gyFOrm");
                 //   multipart.addHeaderField("tenant", "zebra");
                    multipart.addFormField("sn","XXZKJ201000689" );

                    multipart.addFilePart("zpl_file", new File("/sdcard/Android/data/net.znordic.rest_api_java/files/barcodelabel.zpl"),getApplicationContext());

                    try {
                        Log.d("REST__", "response");
                        List<String> response = multipart.finish();
                                   for (String line : response) {
                            Log.e("REST__", "Response:" + line);
                        }
                    }catch(Exception e) {
                        e.printStackTrace();
                    }

                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });


    }

    public void checkForPermission(String permissionType) {

        if (ContextCompat.checkSelfPermission(this,
                permissionType)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    permissionType)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                showCustomDialog("Permission", "Permission type is not granted:\n"+permissionType+"\nApp may not support all features.");
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{permissionType},
                        1001);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted

                Toast.makeText(getApplicationContext(), "Permission already granted \n"+permissionType, Toast.LENGTH_SHORT).show();
        }


    }

    public void showCustomDialog(String title, String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


}
