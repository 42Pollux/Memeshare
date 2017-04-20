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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static android.content.Context.MODE_WORLD_READABLE;
import static android.support.v7.recyclerview.R.attr.layoutManager;
import static com.pollux.memeshare.DatabaseListStatic.addElement;
import static com.pollux.memeshare.DatabaseListStatic.addTag;
import static com.pollux.memeshare.DatabaseListStatic.getLinesToWrite;
import static com.pollux.memeshare.DatabaseListStatic.isin;
import static com.pollux.memeshare.DatabaseListStatic.length;

/**
 * Created by pollux on 11.04.17.
 */

public class tabAll extends Fragment {

    DatabaseListStatic tagList = new DatabaseListStatic();

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
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        10);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        10);
            }
        }

        ArrayList<CreateList> theimage = new ArrayList<>();
        String path = "/storage/emulated/0/Pictures/9GAG";
        String path2 = Environment.getExternalStorageDirectory().toString() + "/Pictures/9GAG";

        File fptagList = new File(getContext().getFilesDir(), "taglist.dat");
        //Log.d("DATABASE", "deleted taglist.dat: " + fptagList.delete());
        File fp = new File(path);
        File file[] = fp.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return ((name.toLowerCase().endsWith(".png"))||(name.toLowerCase().endsWith(".jpg"))||(name.toLowerCase().endsWith(".jpeg"))||name.toLowerCase().endsWith(".bmp"));
            }
        });

        // TODO elemente von taglist.dat in tagList laden + überprüfen ob noch vorhanden in lokalem ordner ggf entfernen
        // TODO zusätzlich alle lokalen elemente die neu sind in tagList laden
        // TODO + removeAdditionalfunction mit nem array argument in der klasse selbst -- eher doch nicht

        // Datei erstellen, bzw. kurz öffnen, falls noch nicht vorhanden
        try {
            FileOutputStream fOut = new FileOutputStream(fptagList, true);
            fOut.close();
            Log.d("DATABASE", "loaded/created taglist.dat");
            Log.d("DATABASE", getContext().getFilesDir().toString());
        } catch (Exception e){
            e.printStackTrace();
            Log.d("DATABASE", "failed to load/create taglist.dat");
        }

        // Datei öffnen, Elemente auslesen und in eine DatabaseList(tagList) übertragen
        try {
            FileInputStream fIn = new FileInputStream(fptagList);
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader br = new BufferedReader(isr);

            String line = "";
            while((line = br.readLine())!=null){
                String filepath = line;
                tagList = addElement(filepath, tagList);
                if((line = br.readLine())==null){
                    break;
                }
                int i = 0;
                if(!line.equals("$notag")){
                    for(String tag : line.split(";")){
                        //if(tag!="") tagList.addTag(filepath, tag);
                        if(tag!="") addTag(filepath, tag, tagList);
                        i++;
                    }
                }
                Log.d("DATABASE", "tagList, loaded entry: " + filepath + " (with " + i + " tags)");
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        // Neue Elemente im lokalen Ordner zur DatabaseList hinzufügen falls nicht doppelt
        try {
            for(File f : file){
                //if(!DatabaseListStatic.isin(f.getCanonicalPath(), tagList)) tagList = tagList.addElement(f.getCanonicalPath());
                if(!isin(f.getCanonicalPath(), tagList)) {
                    tagList = addElement(f.getCanonicalPath(), tagList);
                    Log.d("DATABASE", "tagList, added entry: " + f.getCanonicalPath());
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }

        Log.d("DATABASE", "final size = " + length(tagList));

        // datei schreiben
        writeToLocalFile(tagList);

        // lokale dateien in arraylist laden
        ArrayList<File> fileList = new ArrayList<File>();
        for(File f : file){
            fileList.add(f);
        }

        // lokale Elemente in die RecyclerView-Liste laden
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

    // writes the tagList to the taglist.dat file
    // returns -1 on failure, 0 on success
    private int writeToLocalFile(DatabaseListStatic tagList){
        try {
            File fptagList = new File(getContext().getFilesDir(), "taglist.dat");
            FileOutputStream fOut = new FileOutputStream(fptagList);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fOut));

            ArrayList<String> lines =  getLinesToWrite(tagList);
            Log.d("DATABASE", "successfully grabbed ArrayList with " + lines.size() + " lines");
            if(!lines.isEmpty()){
                for(String elem : lines){
                    bw.write(elem);
                    bw.newLine();
                }
            } else {
                Log.d("DATABASE", "saving tagList failed (empty)");
                return 0;
            }
            bw.close();
            fOut.close();
            Log.d("DATABASE", "saved tagList successfully");
            return 0;
        } catch (Exception e){
            Log.d("DATABASE", "saving tagList failed");
            e.printStackTrace();
            return -1;
        }
    }

}
