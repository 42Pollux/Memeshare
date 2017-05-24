package com.pollux.memeshare;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

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
import static com.pollux.memeshare.MainActivity.search_menu;

/**
 * Created by pollux on 11.04.17.
 */

public class tabAll extends Fragment implements tabEditInterface{
    DatabaseListStatic tagList = new DatabaseListStatic();
    String latestClick = "";
    ViewPager mActViewPager;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;
    ArrayList<ImageObject> imgobj;

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

    public void clickAnim(ImageObject iwobj, ImageView iw, String filepath){
        MainActivity ma = (MainActivity) getActivity();
        ma.clickAnimation(iwobj, iw, filepath);
    }

    public void showPopEdit(ImageView img){
        MainActivity ma = (MainActivity) getActivity();
        ma.showPopupEdit(img);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.tab_all, container, false);

        MainActivity mAct = (MainActivity) getActivity();
        mAct.setListener(this);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity().getApplicationContext(),3);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration hLine = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.HORIZONTAL);
        DividerItemDecoration vLine = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.VERTICAL);

        hLine.setDrawable(getActivity().getResources().getDrawable(R.drawable.divider_white));
        vLine.setDrawable(getActivity().getResources().getDrawable(R.drawable.divider_white));
        recyclerView.addItemDecoration(hLine);
        recyclerView.addItemDecoration(vLine);

        mActViewPager = mAct.getViewPager();

        imgobj = ((MainActivity)getActivity()).prepareData();
        adapter = new RecyclerViewAdapter(this, getActivity().getApplicationContext(), imgobj);
        recyclerView.setAdapter(adapter);
        adapter.add(imgobj);

        return rootView;
    }

    public void afterMenuCreation(){
        final MenuItem searchItem = search_menu.findItem(R.id.search_view);
        adapter.setSearchViewFilterLogic(searchItem);
    }



}
