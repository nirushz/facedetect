package il.co.nnz.facedetect;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 15/11/2016.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        fragmentList = new ArrayList<>();
    }

    // getting the fragment using his position
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    // return how many fragmants we have
    @Override
    public int getCount() {
        return fragmentList.size();
    }

    // adding a fragmant to our list - this is the
    // only method that we didnt override
    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }
}
