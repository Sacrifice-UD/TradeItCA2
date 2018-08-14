package mapp.com.sg.myapplication;



import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;

    private StoreFragment storeFragment;
    private PostFragment postFragment;
    private AccountFragment accountFragment;
    private WatchlistFragment watchlistFragment;
    private ViewPostFragment viewPostFragment;

    private static final int REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initializing views
        mMainFrame = (FrameLayout) findViewById(R.id.main_frame);
        mMainNav = (BottomNavigationView) findViewById(R.id.main_nav);

        verifyPermissions();

    }//end of onCreate method

    private void setupViewPager(){
        //initialising Frame
        storeFragment = new StoreFragment();
        postFragment = new PostFragment();
        accountFragment = new AccountFragment();
        watchlistFragment = new WatchlistFragment();
        viewPostFragment = new ViewPostFragment();

        //setting the default fragment
        setFragment(watchlistFragment);

        //setting onClickListener for nav bar
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_watchlist:
                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(watchlistFragment);
                        return true;

                    case R.id.nav_store:
                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(storeFragment);
                        return true;

                    case R.id.nav_notif:
                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(postFragment);
                        return true;

                    case R.id.nav_account:
                        mMainNav.setItemBackgroundResource(R.color.colorPrimary);
                        setFragment(accountFragment);
                        return true;

                    default:
                        return false;
                }//end of switch case

            }
        });
    }


    private void verifyPermissions(){
        Log.d(TAG, "verifyPermissions: asking user for permissions");

        //asking permissions
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA};

        //if all permissions are allowed, if not ask for the permission
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED){
            setupViewPager();
        }else{
            ActivityCompat.requestPermissions(MainActivity.this,
                    permissions,
                    REQUEST_CODE);
        }
    }//end of verifyPermission method

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }

    private void setFragment(Fragment fragment) {
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }//what setFragment does

}//end of MainActivity class
