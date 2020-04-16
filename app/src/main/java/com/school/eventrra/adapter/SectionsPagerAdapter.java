package com.school.eventrra.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.school.eventrra.R;
import com.school.eventrra.fragment.AllFragment;
import com.school.eventrra.fragment.ForYouFragment;
import com.school.eventrra.fragment.ThisWeekendFragment;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.tab_title_all,
            R.string.tab_title_for_you,
            R.string.tab_title_this_weekend
    };
    private final Context CONTEXT;
    private final Fragment[] FRAGMENTS = new Fragment[]{
            new AllFragment(),
            new ForYouFragment(),
            new ThisWeekendFragment()
    };

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        CONTEXT = context;
    }

    @Override
    public Fragment getItem(int position) {
        return FRAGMENTS[position];
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return CONTEXT.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}
