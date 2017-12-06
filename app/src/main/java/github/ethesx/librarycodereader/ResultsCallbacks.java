package github.ethesx.librarycodereader;

/**
 * Created by ethesx on 12/5/2017.
 */

public abstract interface ResultsCallbacks {

    void onResponseCallback(String response);

    void onResponseErrorCallback(String response);

}
