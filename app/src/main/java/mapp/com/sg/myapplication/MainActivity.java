package mapp.com.sg.myapplication;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private HomeFragment homeFragment;
    private StoreFragment storeFragment;
    private PostFragment postFragment;
    private AccountFragment accountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing views
        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);

        //inistialising Frame
        homeFragment = new HomeFragment();
        storeFragment = new StoreFragment();
        postFragment = new PostFragment();
        accountFragment = new AccountFragment();

        //setting the default fragment
        setFragment(homeFragment);

        //setting onClickListener for nav bar
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(homeFragment);
                        return true;

                    case R.id.nav_store:
                        mMainNav.setItemBackgroundResource(R.color.colorPrimaryDark);
                        setFragment(storeFragment);
                        return true;

                    case R.id.nav_notif:
                        mMainNav.setItemBackgroundResource(R.color.colorAccent);
                        setFragment(postFragment);
                        return true;

                    case R.id.nav_account:
                        mMainNav.setItemBackgroundResource(R.color.silver);
                        setFragment(accountFragment);
                        return true;

                    default:
                        return false;
                }//end of switch case

            }
        });
    }//end of onCreate method

    private void setFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }//what setFragment does

}//end of MainActivity class
