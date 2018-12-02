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
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.alamkanak.weekview.WeekViewEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import jic8138.jic9138androidsmarttreatmentcalendar.Controllers.Database;

public class CalendarActivity extends AppCompatActivity {

    private final static String UPDATE_EVENT = "update_events";

    private FloatingActionButton mAddEventButton;
    private ArrayList<Event> e = new ArrayList<>();
    private Fragment mCalendarFragment;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle toggler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        mDrawerLayout = findViewById(R.id.dl);
        toggler = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(toggler);
        toggler.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAddEventButton = findViewById(R.id.calendar_floating_add_button);
        mAddEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddEventTap();
            }
        });
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            case R.id.navigation_account:
                                Intent ProfileIntent = new Intent(CalendarActivity.this, ProfileActivity.class);
                                finish();
                                startActivity(ProfileIntent);
                            case R.id.navigation_logout:
                                Intent HomescreenIntent = new Intent(CalendarActivity.this, HomeScreenActivity.class);
                                HomescreenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                finish();
                                startActivity(HomescreenIntent);
                                FirebaseAuth.getInstance().signOut();

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
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
