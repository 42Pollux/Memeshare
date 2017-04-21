package com.pollux.memeshare;

import android.Manifest;
import android.app.SearchManager;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

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
import static com.pollux.memeshare.DatabaseListStatic.removeTag;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private tabEditInterface listener;
    public String latestClick = "";
    public DatabaseListStatic tagList = new DatabaseListStatic(); //evtl init()?

    public ViewPager getViewPager(){
        return mViewPager;
    }

    public void setListener(tabEditInterface listener){
        this.listener = listener;
    }

    public void swipeRight(){
        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1, true);

    }

    public void clickAnimation(ImageView iw, String filepath){
        Animation animFadein = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.image_click);
        iw.startAnimation(animFadein);
        latestClick = filepath;

        //hier tabEdit Elemente laden
        listener.chipRemAllTags();
        Log.d("CHIPS", "length of tags: " + getTags(latestClick, tagList).size());
        for(String str : getTags(latestClick, tagList)){
            listener.chipAddTag(str, "");
        }
    }

    public void setPreviewImage(){
        listener.setPreviewImage(latestClick);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void mAddTag(String _tag){
        addTag(latestClick, _tag, tagList);
    }

    public void mRemTag(String _tag){
        removeTag(latestClick, _tag, tagList);
    }

    public ArrayList<CreateList> prepareData(){

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
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

        ArrayList<CreateList> theimage = new ArrayList<>();
        String path = "/storage/emulated/0/Pictures/9GAG";
        String path2 = Environment.getExternalStorageDirectory().toString() + "/Pictures/9GAG";

        File fptagList = new File(getFilesDir(), "taglist.dat");
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
            Log.d("DATABASE", getFilesDir().toString());
        } catch (Exception e){
            e.printStackTrace();
            Log.d("DATABASE", "failed to load/create taglist.dat");
        }

        // Datei öffnen, Elemente auslesen und in eine DatabaseListStatic(tagList) übertragen
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

        // Neue Elemente im lokalen Ordner zur DatabaseListStatic hinzufügen falls nicht doppelt
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
        writeToLocalFile();

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
    public int writeToLocalFile(){
        try {
            File fptagList = new File(getFilesDir(), "taglist.dat");
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












    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position){
                case 0 :    tabAll tab1 = new tabAll();
                            return tab1;
                case 1 :    tabEdit tab2 = new tabEdit();
                            return tab2;
                default :   return null;
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "All";
                case 1:
                    return "Edit";
            }
            return null;
        }
    }
}
