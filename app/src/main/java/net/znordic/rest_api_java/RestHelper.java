package net.znordic.rest_api_java;

import android.util.Log;

import org.apache.http.params.HttpParams;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class RestHelper {


    private static final String TAG = "RestHelper";
    static URL mUrl;

    public RestHelper(){

        try {
            mUrl = new URL("https://znordic.net/poc/test.php/");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public void rawTest() {
        Log.d(TAG,"@rawTest");
        HttpURLConnection urlConnection = null;

        try {
            urlConnection = (HttpURLConnection) mUrl.openConnection();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (urlConnection.getInputStream())));

           // InputStream in = new BufferedInputStream(urlConnection.getInputStream());
           Log.d(TAG, ""+ readStream(br));
            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        } finally {
            Log.d(TAG,"@rawTest - disconnect");
            urlConnection.disconnect();
        }

    }

    public String readStream(BufferedReader in) {
        StringBuilder result = new StringBuilder();
        try {
             for (String line; (line = in.readLine()) != null; ) {
                    result.append(line).append('\n');
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return result.toString();
    }

    /*

     */

    public void send(String method,  HashMap<String, String> postDataParams) {

        try {
            HttpsURLConnection urlConnection = (HttpsURLConnection) mUrl.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod(method);  //POST GET etc.
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundry=---SEPERATORXYZ001");
            urlConnection.setRequestProperty("Accept", "*/*");


        OutputStream os = null;

            os = urlConnection.getOutputStream();

            BufferedWriter writer = null;
            writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));


            Log.d(TAG,""+getPostDataString(postDataParams));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            urlConnection.connect();
            String response = "";
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                response = readResponseStream(urlConnection.getInputStream());
                Log.d(TAG, response);
            }

        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String readResponseStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }




}
