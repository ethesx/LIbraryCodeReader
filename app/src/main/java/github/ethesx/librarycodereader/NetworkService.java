package github.ethesx.librarycodereader;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    public static void lookupISBN (String isbn, Activity activity) throws IOException{//Context context, final TextView resultTextView) throws IOException {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        TextView resultTextView = activity.findViewById(R.id.code_info);

        //TODO finish appending isbn for search
        String url = "https://my-json-server.typicode.com/ethesx/testjson/books";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ProgressBar pBar = (ProgressBar)activity.findViewById(R.id.progressSpinner);
                pBar.setVisibility(View.GONE);
                resultTextView.setText("Response is: "
                        + response);

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultTextView.setText("That didn't work!");
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
