package com.pollux.memeshare;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pchmn.materialchips.ChipView;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.ChipInterface;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static com.pollux.memeshare.DatabaseListStatic.addElement;
import static com.pollux.memeshare.DatabaseListStatic.addTag;
import static com.pollux.memeshare.DatabaseListStatic.getLinesToWrite;
import static com.pollux.memeshare.DatabaseListStatic.isin;
import static com.pollux.memeshare.DatabaseListStatic.length;

/**
 * Created by pollux on 11.04.17.
 */

public class tabEdit extends Fragment implements tabEditInterface{
    private boolean funcall = false;
    public ChipsInput chipsInputAdded;
    public ImageView imgView;
    public List<Chip> chipsAdded = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.tab_edit, container, false);
        ((MainActivity)getActivity()).setListener(this);
        chipsInputAdded = (ChipsInput) rootView.findViewById(R.id.chips_input_added);
        imgView = (ImageView) rootView.findViewById(R.id.imagepreview);

        Button btnClear = (Button) rootView.findViewById(R.id.btnClear);
        Button btnSome = (Button) rootView.findViewById(R.id.btnSome);
        Button btnShare = (Button) rootView.findViewById(R.id.btnShare);
        int width = getResources().getDisplayMetrics().widthPixels/3;
        btnClear.setLayoutParams(new LinearLayout.LayoutParams(width, 90));
        btnSome.setLayoutParams(new LinearLayout.LayoutParams(width, 90));
        btnShare.setLayoutParams(new LinearLayout.LayoutParams(width, 90));
        //btnShare.getBackground().setColorFilter(0x00000000, PorterDuff.Mode.MULTIPLY);

        chipsInputAdded.setFilterableList(chipsAdded);

        chipsInputAdded.addChipsListener(new ChipsInput.ChipsListener(){
            @Override
            public void onChipAdded(ChipInterface chip, int newSize) {
                // chip added
                // newSize is the size of the updated selected chip list

            }

            @Override
            public void onChipRemoved(ChipInterface chip, int newSize) {
                if(!funcall){
                    chipsAdded.remove(chip);
                    if(!chip.getLabel().equals("none")) {
                        ((MainActivity)getActivity()).mRemTag(chip.getLabel());
                    }
                    Log.d("CHIPS", "removed " + chip.getLabel());
                    ((MainActivity)getActivity()).writeToLocalFile();
                }
            }

            @Override
            public void onTextChanged(CharSequence text) {
                String str = text.toString();
                if(str.length()>2){
                    if(str.contains(" ")) {
                        str = text.subSequence(0, text.length()-1).toString();
                        chipsInputAdded.addChip(str, "");
                        chipsAdded.add(new Chip(str, ""));
                        ((MainActivity)getActivity()).mAddTag(str);
                        ((MainActivity)getActivity()).writeToLocalFile();
                    }

                }
            }
        });

        return rootView;
    }

    public void setPreviewImage(String _filepath){
        File image = new File(_filepath);
        Bitmap myBitmap = BitmapFactory.decodeFile(image.getAbsolutePath());
        imgView.setImageBitmap(myBitmap);
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    public void chipAddTag(String tag, String info){
        chipsInputAdded.addChip(tag, info);
        chipsAdded.add(new Chip(tag, info));
        //Log.d("CHIPS", "added tag");
    }
    public void chipRemAllTags(){
        funcall = true;
        for(Chip c : chipsAdded){
            chipsInputAdded.removeChipByLabel(c.getLabel());
            //Log.d("CHIPS", "removing " + c.getLabel());
        }
        chipsAdded.clear();
        funcall = false;
        //Log.d("CHIPS", "removed all tags");
    }

}
