package com.pollux.memeshare;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.pollux.memeshare.DatabaseListStatic.getTags;
import static com.pollux.memeshare.MainActivity.latestClick;
import static com.pollux.memeshare.MainActivity.latestClickImage;
import static com.pollux.memeshare.MainActivity.latestClickImageObject;
import static com.pollux.memeshare.MainActivity.tagList;

// TODO - handle error: "E/class com.pchmn.materialchips.adapter.ChipsAdapter: null" happens onCreate prolly
public class PopupEditActivity extends Activity {
    private boolean newTagSet = false;
    private boolean allTagsRemoved = false;
    private Context context;
    private int tagCounter = 0;
    public ChipsInput chipsInputAdded;
    public ImageView imgView;
    public List<Chip> chipsAdded = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);

        chipsInputAdded = (ChipsInput) findViewById(R.id.chips_input_added);
        Button btn_share = (Button) findViewById(R.id.btn_share);
        Button btn_cancel = (Button) findViewById(R.id.btn_cancel);
        int width = getResources().getDisplayMetrics().widthPixels/5;
        LinearLayout llayout = (LinearLayout) findViewById(R.id.linlayout);
        llayout.setLayoutParams(new FrameLayout.LayoutParams(width*4, FrameLayout.LayoutParams.WRAP_CONTENT));

        chipsInputAdded.setFilterableList(chipsAdded);


        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String filepath = latestClickImageObject.getImage_Location();
                final Intent shareIntent = new Intent(Intent.ACTION_SEND);
                if(filepath.contains(".jpg")==true){
                    shareIntent.setType("image/jpg");
                } else if (filepath.contains(".jpeg")){
                    shareIntent.setType("image/jpeg");
                } else if (filepath.contains(".png")){
                    shareIntent.setType("image/png");
                } else if (filepath.contains(".gif")){
                    shareIntent.setType("image/gif");
                }
                final File photoFile = new File(latestClickImageObject.getImage_Location());

                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));

                startActivity(Intent.createChooser(shareIntent, "Share image using"));
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        for(String str : getTags(latestClick, tagList)){
            chipsInputAdded.addChip(str, "");
            chipsAdded.add(new Chip(str, ""));
            tagCounter++;
        }

        chipsInputAdded.addChipsListener(new ChipsInput.ChipsListener(){
            @Override
            public void onChipAdded(ChipInterface chip, int newSize) {
                latestClickImage.setVisibility(View.VISIBLE);

            }

            @Override
            public void onChipRemoved(ChipInterface chip, int newSize) {
                chipsAdded.remove(chip);
                tagCounter--;
                if(tagCounter==0) latestClickImage.setVisibility(View.INVISIBLE);
                if(!chip.getLabel().equals("none")) {
                    TaglistAdapter.mRemTag(chip.getLabel());
                }
                Log.d("CHIPS", "removed " + chip.getLabel());

            }

            @Override
            public void onTextChanged(CharSequence text) {
                String str = text.toString();
                if(str.length()>2){
                    if(str.contains(" ")) {
                        str = text.subSequence(0, text.length()-1).toString();
                        chipsInputAdded.addChip(str, "");
                        chipsAdded.add(new Chip(str, ""));
                        TaglistAdapter.mAddTag(str);
                        TaglistAdapter.writeToLocalFile();
                    }

                }
            }
        });


    }

    @Override
    protected void onResume(){
        super.onResume();

    }
    @Override
    protected void onStop(){
        super.onStop();
        TaglistAdapter.writeToLocalFile();
    }

}
