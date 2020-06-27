package com.chaubacho.control;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chaubacho.model.Photo;
import com.chaubacho.newflickbrowser.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ImageViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";
    private List<Photo> photoList;
    private Context context;
    private SwipeRefreshLayout swipeRefreshLayout;

    public RecyclerViewAdapter(List<Photo> photoList, Context context, SwipeRefreshLayout swipeRefreshLayout) {
        this.photoList = photoList;
        this.context = context;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: create new views");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_in_list, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        if (photoList == null || photoList.size() == 0) {
            holder.thumbnail.setImageResource(android.R.drawable.ic_menu_gallery);
            holder.title.setText(R.string.no_photo);
        } else {
            Photo photoItem = photoList.get(position);
            Log.d(TAG, "onBindViewHolder: " + photoItem.getTitle() + " ==> " + position);
            Picasso.get().load(createImgUri(photoItem.getFarm_id(),
                                            photoItem.getServer_id(),
                                            photoItem.getId(),
                                            photoItem.getSecret(),
                                            "m"))
                    .error(android.R.drawable.ic_menu_gallery)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .into(holder.thumbnail);
            holder.title.setText(photoList.get(position).getTitle());
        }
    }

    public Photo getPhoto(int position) {
        return (photoList != null) ? photoList.get(position) : null;
    }

    public void updateAdapter(GetRawData getRawData, ProcessJsonData jsonData) {

        jsonData.createUri(jsonData.getSearchTag(), jsonData.getPage())
                .map(getRawData::prepareData)
                .flatMap(json -> {
                    try {
                        return Observable.defer(() -> Observable.just(jsonData.processData(json)));
                    } catch (Exception e){
                        return Observable.error(e);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Photo>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {
                        Log.d(TAG, "onSubscribe: called");
                    }

                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull List<Photo> pList) {
                        Log.d(TAG, "onNext: called");
                        photoList.clear();
                        photoList.addAll(pList);
                        Log.d(TAG, "onNext: size: " + photoList.size());
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        Log.d(TAG, "onError: called" + Observable.error(e));
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete: called");
                        notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }


    @Override
    public int getItemCount() {
        return ((photoList == null) || photoList.size() == 0) ? 1 : photoList.size();
    }

    private String createImgUri(String farm, String server, String id, String secret, String size) {
//        https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
        return String.format("https://farm%s.staticflickr.com/%s/%s_%s_%s.jpg",
                farm, server, id, secret, size);
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "ImageViewHolder";
        ImageView thumbnail;
        TextView title;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            Log.d(TAG, "ImageViewHolder: constructor");
            this.thumbnail = itemView.findViewById(R.id.item_photo);
            this.title = itemView.findViewById(R.id.item_textview);
        }
    }
}
