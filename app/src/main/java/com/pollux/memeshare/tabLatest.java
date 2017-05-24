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

public class tabLatest extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.tab_latest, container, false);
        //((MainActivity)getActivity()).setListener(this);

        return rootView;
    }


}
