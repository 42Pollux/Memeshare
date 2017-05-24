package com.pollux.memeshare;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.util.SortedList;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

import static com.pollux.memeshare.DatabaseListStatic.addElement;
import static com.pollux.memeshare.DatabaseListStatic.addTag;
import static com.pollux.memeshare.DatabaseListStatic.getLinesToWrite;
import static com.pollux.memeshare.DatabaseListStatic.getTags;
import static com.pollux.memeshare.DatabaseListStatic.isin;
import static com.pollux.memeshare.DatabaseListStatic.length;


public class MainActivity extends AppCompatActivity {
    public static String latestClick = "";
    public static ImageView latestClickImage;
    public static ImageObject latestClickImageObject;
    public static DatabaseListStatic tagList = new DatabaseListStatic(); //evtl init()?
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private tabEditInterface listener;
    private Toolbar mToolbar;
    public TaglistAdapter tladapter;
    public static Menu search_menu;

    public ViewPager getViewPager(){ return mViewPager; }
    public void setListener(tabEditInterface listener){ this.listener = listener; }

    /* ----------------------------------------------------------------------------------------------------*/
    /* public functions to access the UI from other activities/fragments*/
    public void swipeRight(){ mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true); }
    public void clickAnimation(ImageObject iwobj, ImageView iw, String filepath){
        Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.image_click);
        iw.startAnimation(animFadein);
        latestClick = filepath;
        latestClickImageObject = iwobj;
    }
    public void showPopupEdit(ImageView img){
        latestClickImage = img;
        Intent pop = new Intent(this, PopupEditActivity.class);
        startActivity(pop);
    }

    /* ----------------------------------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tladapter = new TaglistAdapter(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    /* ----------------------------------------------------------------------------------------------------*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        search_menu = menu;
        listener.afterMenuCreation();
        return true;
    }

    /* ----------------------------------------------------------------------------------------------------*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }




    /* ----------------------------------------------------------------------------------------------------
     * Usage   : function that creates/grabs the info file and adds data, asks for permissions first
     * Returns : ArrayList containing ImageObject-objects
     * TODO - add function to remove every entry in tagList which does not occur as local file
     */
    public ArrayList<ImageObject> prepareData(){
        ArrayList<ImageObject> images = new ArrayList<>();
        String image_folder_path = "/storage/emulated/0/Pictures/9GAG";
        String path_opt = Environment.getExternalStorageDirectory().toString() + "/Pictures/9GAG";
        boolean delete_tagList_onStartup = true;


        // ask for permissions to store data
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        10);
            }
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        10);
            }
        }

        // read in all local files into a File Array
        File fp = new File(image_folder_path);
        File local_files[] = fp.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return ((name.toLowerCase().endsWith(".png"))||(name.toLowerCase().endsWith(".jpg"))||(name.toLowerCase().endsWith(".jpeg"))||name.toLowerCase().endsWith(".bmp"));
            }
        });

        // create local info file if not created yet
        File fpTagList = new File(getFilesDir(), "taglist.dat");
        if(delete_tagList_onStartup) Log.d("DATABASE", "deleted taglist.dat: " + fpTagList.delete());
        try {
            FileOutputStream fOut = new FileOutputStream(fpTagList, true);
            fOut.close();
            Log.d("DATABASE", "loaded/created taglist.dat");
            Log.d("DATABASE", getFilesDir().toString());
        } catch (Exception e){
            e.printStackTrace();
            Log.d("DATABASE", "failed to load/create taglist.dat");
        }

        // open taglist.dat and read all the elements into the DatabaseListStatic-tagList
        try {
            FileInputStream fIn = new FileInputStream(fpTagList);
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

        // add the new images to the tagList (only the new)
        try {
            for(File f : local_files){
                if(!isin(f.getCanonicalPath(), tagList)) {
                    tagList = addElement(f.getCanonicalPath(), tagList);
                    Log.d("DATABASE", "tagList, added entry: " + f.getCanonicalPath());
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        Log.d("DATABASE", "final size = " + length(tagList));
        TaglistAdapter.writeToLocalFile();

        // load image paths into the images-ArrayList
        try {
            if (local_files == null) {
                //TODO handle errors properly
            } else {
                for (int i = 0; i < local_files.length; i++) {
                    String filepath = image_folder_path + "/" + local_files[i].getName();
                    ImageObject imgobj = new ImageObject();
                    imgobj.setImage_Location(filepath);
                    imgobj.setImageTags(getTags(local_files[i].getCanonicalPath(), tagList));
                    images.add(imgobj);
                }
                return images;
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        return images;
    }

    /* ----------------------------------------------------------------------------------------------------
     * Usage    : standard tabbed activity template stuff
     * Returns  : returns a fragment corresponding to one of the sections/tabs/pages
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0 :    tabAll tab1 = new tabAll();
                            return tab1;
                case 1 :    tabLatest tab2 = new tabLatest();
                            return tab2;
                default :   return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "All";
                case 1:
                    return "Latest";
            }
            return null;
        }
    }
}
