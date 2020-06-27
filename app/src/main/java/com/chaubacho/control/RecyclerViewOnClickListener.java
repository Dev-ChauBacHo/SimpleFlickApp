package com.chaubacho.control;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GestureDetectorCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewOnClickListener extends RecyclerView.SimpleOnItemTouchListener {
    private static final String TAG = "RecyclerViewOnClickList";
    private final GestureDetectorCompat gestureDetectorCompat;

    public RecyclerViewOnClickListener(Context context, final RecyclerView recyclerView, final OnRecyclerClickListener listener) {
        gestureDetectorCompat = new GestureDetectorCompat(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                Log.d(TAG, "onSingleTapUp: starts");
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && listener != null) {
                    Log.d(TAG, "onSingleTapUp: called listener.onItemClick");
                    listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d(TAG, "onLongPress: starts");
                View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && listener != null) {
                    Log.d(TAG, "onLongPress: called listener.onItemLongClick");
                    listener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
        Log.d(TAG, "onInterceptTouchEvent: starts");
        if (gestureDetectorCompat != null) {
            boolean result = gestureDetectorCompat.onTouchEvent(e);
            Log.d(TAG, "onInterceptTouchEvent: return " + result);
            return result;
        } else {
            Log.d(TAG, "onInterceptTouchEvent: return false");
            return false;
        }
    }

    public interface OnRecyclerClickListener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }
}
