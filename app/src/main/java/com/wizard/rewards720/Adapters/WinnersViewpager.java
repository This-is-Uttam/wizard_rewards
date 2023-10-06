package com.wizard.rewards720.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.wizard.rewards720.Fragments.ProductWinFragment;
import com.wizard.rewards720.Fragments.VoucherWinFragment;

public class WinnersViewpager extends FragmentPagerAdapter {
    public WinnersViewpager(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return new ProductWinFragment();
        }else if (position == 1){
            return new VoucherWinFragment();
        }else {
            return new ProductWinFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        if (position==0){
            title = "Products";
        } else {
            title = "Vouchers";
        }
        return title;
    }
}
