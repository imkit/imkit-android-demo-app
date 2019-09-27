package com.imkit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.imkit.sdk.ApiResponse;
import com.imkit.sdk.BuildConfig;
import com.imkit.sdk.IMKit;
import com.imkit.sdk.IMRestCallback;
import com.imkit.sdk.model.Client;
import com.imkit.sdk.model.Room;
import com.imkit.widget.fragment.ChatFragment;
import com.imkit.widget.fragment.RoomInfoFragment;
import com.imkit.widget.fragment.RoomListFragment;
import com.imkit.widget.fragment.VideoPlayerFragment;
import com.imkit.widget.utils.Utils;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (TextUtils.isEmpty(IMKit.instance().getToken())) {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//            return;
//        }

        drawerLayout = findViewById(R.id.drawer_layout);
        View logoutButton = findViewById(R.id.nav_logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_sidemenu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager fm = getSupportFragmentManager();
                if (fm.getBackStackEntryCount() > 0) {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_back);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                } else {
                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_nav_sidemenu);
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);
                    getSupportActionBar().setDisplayShowHomeEnabled(true);
                }

                Fragment topFragment = getTopFragment();
                topFragment.setHasOptionsMenu(true);

                if (topFragment instanceof VideoPlayerFragment) {
                    getSupportActionBar().hide();
                } else {
                    getSupportActionBar().show();
                }

                hideKeyboard();
            }
        });

        IMKit.instance().loadCurrentClient(new IMRestCallback<Client>() {
            @Override
            public void onResult(Client result) {
                // Connect socket, start receiving real-time messages
                IMKit.instance().connect();

                pushFragment(instantiateRoomListFragment(), false);
            }

            @Override
            public void onFailure(Call<ApiResponse<Client>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Register broadcast receiver
        this.registerReceiver(badgeBroadcastReceiver, new IntentFilter(IMKit.BROADCAST_ACTION_BADGE));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            this.unregisterReceiver(badgeBroadcastReceiver);
        } catch (Exception e) {
            //Log.e(TAG, "onDestroy", e);
        }
    }

    private void logout() {
        // Unsubscribe push notification
        IMKit.instance().unsubscribe(null);
        IMKit.instance().clear();
        IMKit.instance().setToken("");

        this.unregisterReceiver(badgeBroadcastReceiver);

//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
//
//        finish();
    }

    public CustomRoomListFragment instantiateRoomListFragment() {
        CustomRoomListFragment roomListFragment = CustomRoomListFragment.newInstance();
        roomListFragment.setListener(new RoomListFragment.RoomListFragmentListener() {
            /**
             * On user select a room in list
             * @param room
             */
            @Override
            public void onRoomSelect(Room room) {
                pushFragment(instantiateChatFragment(room));
            }

            /**
             * Override to custom the hint view which shows when room list is empty
             * @param parent The container which will holds the hint view
             * @return Custom hint view.
             */
            @Override
            public View onCreateRoomListEmptyView(ViewGroup parent) {
                View view = LayoutInflater.from(MainActivity.this).inflate(com.imkit.widget.R.layout.im_room_list_empty, parent, false);
                ImageView iconView = (ImageView) ((ViewGroup) view).getChildAt(0);
                iconView.setColorFilter(ContextCompat.getColor(MainActivity.this, com.imkit.widget.R.color.colorPrimary));
                return view;
            }
        });
        return roomListFragment;
    }

    public ChatFragment instantiateChatFragment(Room room) {
        return CustomChatFragment.newInstance(room.getId(), Utils.getDisplayRoomTitle(room));
    }

    public RoomInfoFragment instantiateRoomInfoFragment(final ChatFragment chatFragment, final Room room) {
        RoomInfoFragment roomInfoFragment = RoomInfoFragment.newInstance(room.getId(), Utils.getDisplayRoomTitle(room));
        roomInfoFragment.setListener(new RoomInfoFragment.RoomInfoFragmentListener() {
            @Override
            public void roomLeaved() {
                chatFragment.leaveRoom();
            }
        });
        return roomInfoFragment;
    }

    public VideoPlayerFragment instantiateVideoPlayerFragment(String videoUrl) {
        return VideoPlayerFragment.newInstance().setVideoUrl(videoUrl);
    }

    public void pushFragment(Fragment fragment) {
        pushFragment(fragment, true);
    }

    /**
     * @param fragment
     * @param addToBackStack Note: it has strange behavior while repeating push push pop when addToBackStack=false
     */
    public void pushFragment(Fragment fragment, boolean addToBackStack) {

        Log.d(TAG, "pushFragment: " + fragment.getClass().getName() + ", " + addToBackStack);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.container, fragment, fragment.getClass().getCanonicalName());

        Fragment topFragment = getTopFragment();

        if (addToBackStack) {
            String tag = null;
            if (topFragment != null) {
                // Using full name because the simple name may be duplicated after obscured by ProGuard
                tag = topFragment.getClass().getCanonicalName();
                Log.i(TAG, "push to BackStack: " + tag);
            }
            ft.addToBackStack(tag);
        }

        ft.commit();
    }

    public Fragment getTopFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.container);
    }

    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
            return;
        }

        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onStart() {
        super.onStart();

        IMKit.instance().connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        IMKit.instance().disconnect();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected");

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                onBackPressed();
                return true;
            }
            Log.d(TAG, "open drawer");
            drawerLayout.openDrawer(Gravity.LEFT, true);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            View view = getCurrentFocus();
            if (view != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                view.clearFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver badgeBroadcastReceiver = new BroadcastReceiver() {

        private final String TAG = "BadgeBroadcastReceiver";

        @Override
        public void onReceive(final Context context, final Intent intent) {
            final PendingResult pendingResult = goAsync();

            AsyncTask<Void, Integer, Integer> asyncTask = new AsyncTask<Void, Integer, Integer>() {
                @Override
                protected Integer doInBackground(Void... params) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Action: " + intent.getAction() + "\n");

                    int badge = intent.getIntExtra(IMKit.BROADCAST_EXTRA_BADGE, 0);
                    sb.append("badge = " + badge);
                    Log.d(TAG, sb.toString());
                    // Must call finish() so the BroadcastReceiver can be recycled.
                    pendingResult.finish();
                    return badge;
                }

                @Override
                protected void onPostExecute(Integer badge) {
                    Log.i(TAG, "badge=" + badge);

                    // TODO: update UI
                }
            };
            if (BuildConfig.VERSION_CODE > Build.VERSION_CODES.HONEYCOMB) {
                asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                asyncTask.execute();
            }
        }
    };
}
