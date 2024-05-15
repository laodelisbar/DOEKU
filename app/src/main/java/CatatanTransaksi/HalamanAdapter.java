package CatatanTransaksi;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class HalamanAdapter extends FragmentStateAdapter {
    private final int numOfTabs;

    public HalamanAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int numOfTabs) {
        super(fragmentManager, lifecycle);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new Fragment1();
            case 1:
                return new Fragment2();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return numOfTabs;
    }
}
