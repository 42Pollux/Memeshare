package com.pollux.memeshare;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import net.rdrei.android.dirchooser.DirectoryChooserConfig;
import net.rdrei.android.dirchooser.DirectoryChooserFragment;

import java.io.File;
import java.io.IOException;

import static android.R.attr.data;
import static com.pollux.memeshare.MainActivity.delete_tagList_onStartup;
import static com.pollux.memeshare.MainActivity.memeFolderPath;

public class SettingsActivity extends AppCompatActivity implements DirectoryChooserFragment.OnFragmentInteractionListener{
    public static final String PREFS_NAME = "PrefsFile";
    private Uri folderPath;
    private EditText mDirectoryTextView;
    private DirectoryChooserFragment mDialog;
    private Switch deleteSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        String pathToFolder = settings.getString("memefolder", "");
        boolean deleteSwitchState = settings.getBoolean("deleteTagList", false);
        mDirectoryTextView = (EditText) findViewById(R.id.editTextFolder);
        mDirectoryTextView.setText(pathToFolder);


        final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                .newDirectoryName("DialogSample")
                .build();
        mDialog = DirectoryChooserFragment.newInstance(config);

        Button btn_apply = (Button) findViewById(R.id.buttonapply);
        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memeFolderPath = mDirectoryTextView.getText().toString();
                try {
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("memefolder", mDirectoryTextView.getText().toString());
                    editor.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button btn_change = (Button) findViewById(R.id.buttonchange);
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.show(getFragmentManager(), null);
            }
        });

        deleteSwitch = (Switch) findViewById(R.id.switchDelete);
        if(deleteSwitchState) deleteSwitch.setChecked(true);
        deleteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                delete_tagList_onStartup = isChecked;
                try {
                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("deleteTagList", isChecked);
                    editor.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onSelectDirectory(@NonNull String path) {
        mDirectoryTextView.setText(path);
        memeFolderPath = path;
        mDialog.dismiss();
        try {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("memefolder", path);
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCancelChooser() {
        mDialog.dismiss();
    }



}
