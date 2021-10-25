package com.publicicat.mismascotastres;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout1;
    private ViewPager2 viewPager2;
    private final int[] tabIcons = {
            R.drawable.ic_baseline_home_24,
            R.drawable.ic_baseline_person_24
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Barra menu declarar botons 1/2
        ImageView rightIcon = findViewById(R.id.right_icon);
        registerForContextMenu(rightIcon);

        ImageView leftIcon = findViewById(R.id.left_icon);
        leftIcon.setVisibility(View.GONE);

        //TabLayout with a couple of tabs i ViewPager 1/2
        tabLayout1 = findViewById(R.id.tabLayout1);
        setupTabIcons();

        viewPager2 = findViewById(R.id.viewPager2);

        viewPager2.setAdapter(new AdaptadorFragment(getSupportFragmentManager(), getLifecycle()));
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout1.selectTab(tabLayout1.getTabAt(position));

            }
        });

        tabLayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    } //onCreate: end

    private void setupTabIcons() {
        tabLayout1.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout1.getTabAt(1).setIcon(tabIcons[1]);
    }
    //TabLayout with a couple of tabs i ViewPager 2/2
    class AdaptadorFragment extends FragmentStateAdapter {
        public AdaptadorFragment(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }


        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch(position) {
                case 0: return new FragmentOne();
                default: return new FragmentTwo();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }

    }

    //Barra menu declarar botons 2/2
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_option, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

         switch (item.getItemId()) {
             case R.id.About:
                 Intent about = new Intent(MainActivity.this, AboutActivity.class);
                 startActivity(about);
                 break;

             case R.id.Form:
                 Intent form = new Intent (MainActivity.this, FormActivity.class);
                 startActivity(form);
                 break;
         }

        return super.onContextItemSelected(item);
    }
}