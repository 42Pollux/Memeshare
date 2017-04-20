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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.content.Context.MODE_WORLD_READABLE;
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
        DatabaseList tagList = new DatabaseList("init");
        String path = "/storage/emulated/0/Pictures/9GAG";
        String path2 = Environment.getExternalStorageDirectory().toString() + "/Pictures/9GAG";


        File fp = new File(path);
        File file[] = fp.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return ((name.toLowerCase().endsWith(".png"))||(name.toLowerCase().endsWith(".jpg"))||(name.toLowerCase().endsWith(".jpeg"))||name.toLowerCase().endsWith(".bmp"));
            }
        });

        // elemente von taglist.dat in tagList laden + überprüfen ob noch vorhanden in lokalem ordner ggf entfernen
        // zusätzlich alle lokalen elemente die neu sind in tagList laden
        // + removeAdditionalfunction mit nem array argument in der klasse selbst

        try {
            FileInputStream fIn = new FileInputStream("taglist.dat"); // was wenn datei noch nicht vorhanden?
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader br = new BufferedReader(isr);

            String line = new String();
            while((line = br.readLine())!=null){
                String filepath = new String(line);
                tagList.addElement(line);
                Log.d("DATABASE", "tagList, loaded entry: " + line);
                if((line = br.readLine())==null){
                    break;
                }
                if(line=="$notag") continue;
                int i = 0;
                for(String tag : line.split(";")){
                    if(tag!="") tagList.addTag(filepath, tag);
                    i++;
                }
                Log.d("DATABASE", "tagList, loaded tags: " + i);
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        try {
            for(File f : file){
                if(!tagList.isin(f.getCanonicalPath())) tagList.addElement(f.getCanonicalPath());
                Log.d("DATABASE", "tagList, added entry: " + f.getCanonicalPath());
            }
        } catch(IOException e){
            e.printStackTrace();
        }

        ArrayList<File> fileList = new ArrayList<File>();
        for(File f : file){
            fileList.add(f);
        }

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
