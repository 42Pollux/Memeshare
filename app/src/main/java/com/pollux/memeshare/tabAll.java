package com.pollux.memeshare;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

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
    String latestClick = "";
    ViewPager mActViewPager;

    public DatabaseListStatic getTagList(){
        return tagList;
    }
    public ViewPager getViewPager(){
        return mActViewPager;
    }

    public void scrollRight(){
        MainActivity ma = (MainActivity) getActivity();
        ma.swipeRight();
    }

    public void clickAnim(ImageView iw, String filepath){
        MainActivity ma = (MainActivity) getActivity();
        ma.clickAnimation(iw, filepath);
    }

    public void setPrevImg(){
        MainActivity ma = (MainActivity) getActivity();
        ma.setPreviewImage();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        // imagegallery
        //setContentView(R.layout.activity_main);
        View rootView = inflater.inflate(R.layout.tab_all, container, false);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);


        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(),3);
        recyclerView.setLayoutManager(layoutManager);

        MainActivity mAct = (MainActivity) getActivity();
        mActViewPager = mAct.getViewPager();

        ArrayList<CreateList> createLists = ((MainActivity)getActivity()).prepareData();
        MyAdapter adapter = new MyAdapter(this, getActivity().getApplicationContext(), createLists);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

}
