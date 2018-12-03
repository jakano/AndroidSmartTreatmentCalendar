package jic8138.jic9138androidsmarttreatmentcalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class CalendarActivity extends AppCompatActivity implements FilterDialogFragment.FilterDialogListener {

    private final static String UPDATE_EVENT = "update_events";
    private final static String FILTER_APPLIED = "filter_applied";


    private FloatingActionButton mAddEventButton;
    private ArrayList<Event> e = new ArrayList<>();
    private Fragment mCalendarFragment;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        mDrawerLayout = findViewById(R.id.dl);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAddEventButton = findViewById(R.id.calendar_floating_add_button);
        mAddEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddEventTap();
            }
        });
        NavigationView drawerNavigationView = findViewById(R.id.nav_view);
        drawerNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.navigation_account:
                                Intent ProfileIntent = new Intent(CalendarActivity.this, ProfileActivity.class);
                                startActivity(ProfileIntent);
                                break;
                            case R.id.navigation_logout:
                                Intent HomescreenIntent = new Intent(CalendarActivity.this, HomeScreenActivity.class);
                                HomescreenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                                startActivity(HomescreenIntent);
                                FirebaseAuth.getInstance().signOut();
                                break;
                            case R.id.navigation_filter:
                                DialogFragment filterDialog = new FilterDialogFragment();
                                filterDialog.show(getSupportFragmentManager(), "Filter Dialog");
                                mDrawerLayout.closeDrawers();
                                break;
                        }
                        return true;
                    }
                });
        BottomNavigationView bottomNavigationView = findViewById(R.id.calendar_navigation);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Events", e);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("Events", e);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_month:
                        mCalendarFragment = MonthViewFragment.newInstance();
                        break;
                    case R.id.navigation_week:
                        mCalendarFragment = WeekViewFragment.newInstance();
                        break;
                    case R.id.navigation_day:
                        mCalendarFragment = DayViewFragment.newInstance();
                        break;
                }
                mCalendarFragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.calendar_view_frame, mCalendarFragment);
                transaction.commit();
                return true;
            }
        });

        //Manually displaying the first fragment - one time only
        mCalendarFragment =  MonthViewFragment.newInstance();
        mCalendarFragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.calendar_view_frame, mCalendarFragment);
        transaction.commit();

    }

    private void onAddEventTap() {
        DialogFragment addEventDialog = new AddEventDialog();
        ((AddEventDialog) addEventDialog).setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sendBroadcast(new Intent(UPDATE_EVENT));
            }
        });
        addEventDialog.show(getSupportFragmentManager(), "Add Event");

    }

    @Override
    public void onFilterSelected(String filter) {
        Intent intent = new Intent(FILTER_APPLIED);
        intent.putExtra("filter_option", filter);
        sendBroadcast(intent);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

}
