package com.pollux.memeshare;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

import static android.support.v7.recyclerview.R.attr.layoutManager;

/**
 * Created by pollux on 11.04.17.
 */

public class tabAll extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        // imagegallery
        //setContentView(R.layout.activity_main);
        View rootView = inflater.inflate(R.layout.tab_all, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(),3);
        recyclerView.setLayoutManager(layoutManager);

        // Rahmen
        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                //getResources().getConfiguration().orientation);
        //recyclerView.addItemDecoration(dividerItemDecoration);

        ArrayList<CreateList> createLists = prepareData();
        MyAdapter adapter = new MyAdapter(getActivity().getApplicationContext(), createLists);
        recyclerView.setAdapter(adapter);

        return rootView;
    }


    private ArrayList<CreateList> prepareData(){

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        10);


                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

        ArrayList<CreateList> theimage = new ArrayList<>();

        String path = "/storage/emulated/0/Pictures/9GAG";
        String path2 = Environment.getExternalStorageDirectory().toString() + "/Pictures/9GAG";


        File f = new File(path);
        File file[] = f.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return ((name.toLowerCase().endsWith(".png"))||(name.toLowerCase().endsWith(".jpg"))||(name.toLowerCase().endsWith(".jpeg"))||name.toLowerCase().endsWith(".bmp"));
            }
        });
        if(file==null){

        } else {
            for (int i = 0; i < file.length; i++) {
                CreateList createList = new CreateList();
                createList.setImage_Location(path + "/" + file[i].getName());
                theimage.add(createList);
            }
            return theimage;
        }
        return theimage;
    }

}
