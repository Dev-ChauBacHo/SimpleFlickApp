package com.chaubacho.control;

import android.net.Uri;
import android.util.Log;

import com.chaubacho.Const;
import com.chaubacho.model.Photo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class ProcessJsonData {
    private static final String TAG = "ProcessJsonData";

    private List<Photo> photoList = null;
    private DownloadStatus downloadStatus;

    private String searchTag;
    private String page;

    public ProcessJsonData(String searchTag, String page, RecyclerViewAdapter adapter) {
        this.searchTag = searchTag;
        this.page = page;
        this.photoList = new ArrayList<>();
        adapter.updateAdapter(new GetRawData(), this);
    }

    Observable<String> createUri(String searchTag, String page) {
        Log.d(TAG, "createUri: starts");

        return Observable.just(Uri.parse(Const.BASE_URL).buildUpon()
                .appendQueryParameter("api_key", Const.API_KEY)
                .appendQueryParameter("tags", searchTag)
                .appendQueryParameter("media", "photo")
                .appendQueryParameter("per_page", "50")
                .appendQueryParameter("page", page)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .build().toString());
    }

    public List<Photo> processData(String data) {
        Log.d(TAG, "processData: starts");

        try {
            List<Photo> newPhotoList = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(data);
            JSONArray itemArray;
            itemArray = jsonObject.getJSONObject("photos").getJSONArray("photo");
            for (int i = 0; i < itemArray.length(); i++) {
                JSONObject jsonPhoto = itemArray.getJSONObject(i);
                String title = jsonPhoto.getString("title");
                String farm_id = jsonPhoto.getString("farm");
                String server_id = jsonPhoto.getString("server");
                String id = jsonPhoto.getString("id");
                String secret = jsonPhoto.getString("secret");

                newPhotoList.add(new Photo(title, farm_id, server_id, id, secret));
            }
            Log.d(TAG, "processData: processed data successfully");
            return newPhotoList;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "processData: error processing json data" + e.getMessage());
            return null;
        }
    }

    public String getSearchTag() {
        return searchTag;
    }

    public String getPage() {
        return page;
    }
}
