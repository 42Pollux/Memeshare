package com.pollux.memeshare;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by pollux on 11.04.17.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private ArrayList<CreateList> galleryList;
    private Context context;
    private tabAll activity;

    public MyAdapter(tabAll activity, Context context, ArrayList<CreateList> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
        this.activity = activity;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    private static class MyHandler extends Handler {}
    private final MyHandler mHandler = new MyHandler();


    public static class MyRunnable implements Runnable {
        private final WeakReference<tabAll> mActivity;
        private final tabAll ta;

        public MyRunnable(tabAll activity2) {
            mActivity = new WeakReference<>(activity2);
            ta = activity2;
        }

        @Override
        public void run() {
            tabAll activity3 = mActivity.get();
                ta.scrollRight();
                Log.d("ADAPTER", "successfully swiped");
        }
    }




    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder viewHolder, int i) {
        //viewHolder.title.setText(galleryList.get(i).getImage_title());
        viewHolder.img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        final File f = new File(galleryList.get(i).getImage_Location());
        Picasso.with(context).load(f).resize(275, 275).into(viewHolder.img);
        //viewHolder.img.setImageResource((galleryList.get(i).getImage_Location()));

        //click event
        viewHolder.img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Animation animFadein = AnimationUtils.loadAnimation(context,R.anim.image_click);
                //viewHolder.img.startAnimation(animFadein);
                try {
                    Log.d("CLICK", "clicked on: " + f.getCanonicalPath());
                    activity.clickAnim(viewHolder.img, f.getCanonicalPath());
                    activity.setPrevImg();
                } catch(Exception e){
                    e.printStackTrace();
                }
                MyRunnable mRunnable = new MyRunnable(activity);
                mHandler.postDelayed(mRunnable, 275);
                //activity.scrollRight();
            }

        });

    }


    @Override
    public int getItemCount() {
        return galleryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView img;
        public ViewHolder(View view) {
            super(view);

            //title = (TextView)view.findViewById(R.id.title);
            img = (ImageView) view.findViewById(R.id.img);
        }
    }
}
