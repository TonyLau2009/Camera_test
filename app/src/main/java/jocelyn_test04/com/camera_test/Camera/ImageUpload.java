package jocelyn_test04.com.camera_test.Camera;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jocelyn on 12/11/2016.
 */

public class ImageUpload extends AsyncTask<Void,Void,String>{
    Context c;
    public Bitmap image;
    private String img_name;
    private String urlAddress;

    String encoded_str = null;


    ProgressDialog pd;

    public ImageUpload( Context c, Bitmap image, String img_name,String urlAddress) {
        this.c = c;
        this.image = image;
        this.img_name = img_name;
        this.urlAddress = urlAddress;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(c);
        pd.setTitle("Upload");
        pd.setMessage("Images uploading....Please wait");
        pd.show();
    }

    @Override
    protected String doInBackground(Void... voids) {

        return this.ecodeimage();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pd.dismiss();

        if(s != null){
            makeRequest(s);
        }else{
            Toast.makeText(c,
                    "Encodeed fill null",Toast.LENGTH_SHORT).show();
        }
    }


    private String ecodeimage(){

        ByteArrayOutputStream stream = null;
        if(image != null) {
            try {
                stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                stream.flush();
               // stream.close();
                byte[] bitmapbyte = stream.toByteArray();
                encoded_str = Base64.encodeToString(bitmapbyte,Base64.DEFAULT);

                return encoded_str;
            } catch (IOException e) {
                e.getStackTrace();
            } finally {
                if(stream != null){
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    private void makeRequest(final String str) {

        RequestQueue requestQueue = Volley.newRequestQueue(c);
        StringRequest request = new StringRequest(Request.Method.POST, urlAddress,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(c,
                                "SUCCESSFULL",Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c,
                        "UNSUCCESS",Toast.LENGTH_SHORT).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map = new HashMap<>();
                map.put("encoded_str", str);
                map.put("img_name", img_name);
                return map;
            }
        };
        requestQueue.add(request);
    }

}
