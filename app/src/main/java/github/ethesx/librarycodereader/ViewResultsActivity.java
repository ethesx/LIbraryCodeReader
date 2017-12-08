package github.ethesx.librarycodereader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class ViewResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView txtTitle = findViewById(R.id.txtResultsTitle);
        TextView txtIsbn = findViewById(R.id.txtResultsISBN);
        TextView txtAuthor = findViewById(R.id.txtResultsAuthor);
        TextView txtAge = findViewById(R.id.txtResultsAge);
        TextView txtPublisher = findViewById(R.id.txtResultsPublisher);
        TextView btnMarked = findViewById(R.id.btnResultsMarkTitle);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String isbn = intent.getStringExtra("isbn");

        //FiXME REMOVE
        System.out.println("ISBN = " + isbn);

        ProgressBar pBar = (ProgressBar) findViewById(R.id.progressSpinner);
        pBar.setVisibility(View.VISIBLE);

        //Perform the actual lookup and populate the display
        try {
            NetworkService.lookupISBN(isbn, getApplicationContext(), new ResultsCallbacks() {
                @Override
                public void onResponseCallback(String response) {
                        pBar.setVisibility(View.GONE);
                        JSONObject bookReport = parseJson(response);
                        JSONObject firstBook = getFirstBookObject(bookReport);
                    try {
                        txtTitle.setText(firstBook.getString("title"));
                        txtIsbn.setText(firstBook.getString("isbn"));
                        txtAuthor.setText(firstBook.getString("author"));
                        txtAge.setText(firstBook.getString("age"));
                        txtPublisher.setText(firstBook.getString("publisher"));
                        btnMarked.setText(bookReport.getBoolean("marked") ? "Marked" : "Mark");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onResponseErrorCallback(String response) {
                    //TODO create a toast with the error
                }
            });
        } catch(IOException e) {
            System.out.println(e.getMessage());
            //TODO create a toast with the error
        }




    }

    private JSONObject parseJson(String data){


        try {
             return new JSONObject(data);
        }
        catch(Exception e){
            System.out.println("Unable to parse JSON response");
            return null;
        }
    }

    private JSONObject getFirstBookObject(JSONObject jsonObject){
        try {
            return jsonObject.getJSONArray("books").getJSONObject(0);
        } catch (JSONException e) {
            System.out.println("Unable to extract first book data from book report");
            e.printStackTrace();
            return null;
        }
    }

}
