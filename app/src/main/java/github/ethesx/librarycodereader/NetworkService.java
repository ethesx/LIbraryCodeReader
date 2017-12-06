package github.ethesx.librarycodereader;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.IOException;

/**
 * Created by ethesx on 11/24/2017.
 */

public class NetworkService {

    public static void lookupISBN (String isbn, Context context, final ResultsCallbacks resultsCallbacks) throws IOException{//Context context, final TextView resultTextView) throws IOException {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);


        //TODO finish appending isbn for search
        String url = "https://my-json-server.typicode.com/ethesx/testjson/bookreport";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                resultsCallbacks.onResponseCallback(response);



            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultsCallbacks.onResponseCallback(error.getMessage());
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
