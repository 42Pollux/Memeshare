package com.pollux.memeshare;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.plumillonforge.android.chipview.ChipViewAdapter;

/**
 * Created by pollux on 20.04.17.
 */

public class MainChipViewAdapter extends ChipViewAdapter {
    public MainChipViewAdapter(Context context) {
        super(context);
    }

    @Override
    public int getLayoutRes(int position) {
        Tag tag = (Tag) getChip(position);
        switch (tag.getType()) {
            case 0: return R.layout.chip_close;
            case 1: return R.layout.chip_add;
            default: return R.layout.chip_close;
        }
    }

    @Override
    public int getBackgroundColor(int position) {
        Tag tag = (Tag) getChip(position);
        return getColor(R.color.colorTag);
    }

    @Override
    public int getBackgroundColorSelected(int position) {
        return getColor(R.color.colorTagSelected);
    }

    @Override
    public int getBackgroundRes(int position) {
        return 0;
    }

    @Override
    public void onLayout(View view, int position) {
        Tag tag = (Tag) getChip(position);

        ((TextView) view.findViewById(android.R.id.text1)).setTextColor(getColor(R.color.colorWhite));
        ((TextView) view.findViewById(android.R.id.text1)).setTextSize(16.0f);

    }
}