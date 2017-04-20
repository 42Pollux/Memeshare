package com.pollux.memeshare;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plumillonforge.android.chipview.Chip;
import com.plumillonforge.android.chipview.ChipView;
import com.plumillonforge.android.chipview.ChipViewAdapter;
import com.plumillonforge.android.chipview.OnChipClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pollux on 11.04.17.
 */

public class tabEdit extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.tab_edit, container, false);

        ChipView chipRecently = (ChipView) rootView.findViewById(R.id.chipview_recently);
        ChipView chipAdded = (ChipView) rootView.findViewById(R.id.chipview_added);
        ChipViewAdapter adapter1 = new MainChipViewAdapter(getContext());
        ChipViewAdapter adapter2 = new SecChipViewAdapter(getContext());
        chipRecently.setAdapter(adapter1);
        chipAdded.setAdapter(adapter2);

        List<Chip> chipList_recently = new ArrayList<>();
        chipList_recently.add(new Tag("pepe"));
        chipList_recently.add(new Tag("mlg edition"));
        chipList_recently.add(new Tag("rofl"));
        chipList_recently.add(new Tag("reaction"));
        chipList_recently.add(new Tag("sample text"));
        chipRecently.setChipList(chipList_recently);

        List<Chip> chipList_added = new ArrayList<>();
        chipList_added.add(new Tag("butterfly", 1));
        chipList_added.add(new Tag("rofl", 1));
        chipList_added.add(new Tag("kind of", 1));
        chipAdded.setChipList(chipList_added);

        //((TextView) rootView.findViewById(R.id.text1)).setTextColor(getResources().getColor(R.color.colorWhite));
        //((TextView) rootView.findViewById(R.id.text2)).setTextColor(getResources().getColor(R.color.colorWhite));
        //((TextView) rootView.findViewById(android.R.id.text1)).setTextSize(16.0f);
        //((TextView) rootView.findViewById(android.R.id.text2)).setTextSize(16.0f);

        chipRecently.setOnChipClickListener(new OnChipClickListener() {
            @Override
            public void onChipClick(Chip chip) {
                // Action here !
            }
        });

        chipAdded.setOnChipClickListener(new OnChipClickListener() {
            @Override
            public void onChipClick(Chip chip) {

            }
        });


        return rootView;
    }


}
