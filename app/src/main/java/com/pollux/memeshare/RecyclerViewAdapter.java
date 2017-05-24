package com.pollux.memeshare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<ImageObject> galleryList;
    private Context context;
    private tabAll activity;
    private SortedList<ImageObject> mPages;

    public RecyclerViewAdapter(tabAll activity, Context context, ArrayList<ImageObject> galleryList) {
        this.galleryList = galleryList;
        this.context = context;
        this.activity = activity;


        mPages = new SortedList<ImageObject>(ImageObject.class, new SortedList.Callback<ImageObject>() {
            @Override
            public int compare(ImageObject o1, ImageObject o2) {
                return o1.getImage_Location().compareTo(o2.getImage_Location());
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);

            }

            @Override
            public boolean areContentsTheSame(ImageObject oldItem, ImageObject newItem) {
                // return whether the items' visual representations are the same or not.
                return oldItem.getImage_Location().equals(newItem.getImage_Location());
            }

            @Override
            public boolean areItemsTheSame(ImageObject item1, ImageObject item2) {
                return item1.getImage_Location() == item2.getImage_Location();
            }
        });

        for(ImageObject a : this.galleryList){
            mPages.add(a);
        }

    }

    public void add(ImageObject model) {
        mPages.add(model);
    }

    public void remove(ImageObject model) {
        mPages.remove(model);
    }

    public void add(ArrayList<ImageObject> models) {
        mPages.addAll(models);
    }

    public void remove(ArrayList<ImageObject> models) {
        mPages.beginBatchedUpdates();
        for (ImageObject model : models) {
            mPages.remove(model);
        }
        mPages.endBatchedUpdates();
    }
    public void replaceAll(ArrayList<ImageObject> models) {
        mPages.beginBatchedUpdates();
        for (int i = mPages.size() - 1; i >= 0; i--) {
            final ImageObject model = mPages.get(i);
            if (!models.contains(model)) {
                mPages.remove(model);
            }
        }
        mPages.addAll(models);
        mPages.endBatchedUpdates();
        //notifyDataSetChanged();
    }

    public void setSearchViewFilterLogic(MenuItem searchItem){

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final ArrayList<ImageObject> filteredModelList = filter(galleryList, newText);
                replaceAll(filteredModelList);
                //scrollToPosition(0);
                return true;
            }
        });

    }

    private static ArrayList<ImageObject> filter(ArrayList<ImageObject> models, String query) {
        final String lowerCaseQuery = query.toLowerCase();
        final ArrayList<ImageObject> filteredModelList = new ArrayList<>();
        for (ImageObject model : models) {
            for (String tag : model.getImageTags()){
                if (tag.contains(lowerCaseQuery)) {
                    filteredModelList.add(model);
                    break;
                }
            }

        }
        return filteredModelList;
    }

    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
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
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder viewHolder, int i) {
        int imgsize = activity.getResources().getDisplayMetrics().widthPixels/3 -2;
        final ImageObject iwobj = mPages.get(i);

        ViewGroup.LayoutParams params = viewHolder.img.getLayoutParams();
        params.width = imgsize;
        params.height = imgsize;
        viewHolder.img.setScaleType(ImageView.ScaleType.FIT_XY);
        //String path = galleryList.get(i).getImage_Location();
        String path = mPages.get(i).getImage_Location();
        final File f = new File(path);
        Picasso.with(context).load(f).resize(imgsize, imgsize).into(viewHolder.img);
        if(TaglistAdapter.mHasTag(path)) {
            viewHolder.tag.setVisibility(View.VISIBLE);
        } else {
            viewHolder.tag.setVisibility(View.INVISIBLE);
        }
        //viewHolder.img.setImageResource((galleryList.get(i).getImage_Location()));

        //click event
        viewHolder.img.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Animation animFadein = AnimationUtils.loadAnimation(context,R.anim.image_click);
                //viewHolder.img.startAnimation(animFadein);
                try {
                    Log.d("CLICK", "clicked on: " + f.getCanonicalPath());
                    activity.clickAnim(iwobj, viewHolder.img, f.getCanonicalPath());
                    //activity.setPrevImg();
                } catch(Exception e){
                    e.printStackTrace();
                }
                MyRunnable mRunnable = new MyRunnable(activity);
                //mHandler.postDelayed(mRunnable, 275);
                activity.showPopEdit(viewHolder.tag);
            }

        });


    }


    //@Override
   // public int getItemCount() {
    //    return galleryList.size();
   // }
    @Override
    public int getItemCount() {
        return mPages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private ImageView img, tag;
        public ViewHolder(View view) {
            super(view);

            //title = (TextView)view.findViewById(R.id.title);
            img = (ImageView) view.findViewById(R.id.img);
            tag = (ImageView) view.findViewById(R.id.tag);
        }
    }
}
