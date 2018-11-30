package jic8138.jic9138androidsmarttreatmentcalendar;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

    private FloatingActionButton mAddEventButton;
    private ArrayList<Event> e = new ArrayList<>();
    private ListView mEventsListView;
    private EventsListAdapter mEventsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_screen);
        mEventsListView = findViewById(R.id.calendar_event_list);


        mAddEventButton = findViewById(R.id.calendar_floating_add_button);
        mAddEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddEventTap();
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.calendar_navigation);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Events", e);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment calendar_fragment =  null;
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("Events", e);
                switch (menuItem.getItemId()) {
                    case R.id.navigation_month:
                        calendar_fragment = MonthViewFragment.newInstance();
                        break;
                    case R.id.navigation_week:
                        calendar_fragment = WeekViewFragment.newInstance();
                        break;
                    case R.id.navigation_day:
                        calendar_fragment = DayViewFragment.newInstance();
                        break;
                }
                calendar_fragment.setArguments(bundle);
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.calendar_view_frame, calendar_fragment);
                transaction.commit();
                return true;
            }
        });

        //Manually displaying the first fragment - one time only
        Fragment calendar_fragment =  MonthViewFragment.newInstance();
        calendar_fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.calendar_view_frame, calendar_fragment);
        transaction.commit();

    }

    private void onAddEventTap() {
        DialogFragment addEventDialog = new AddEventDialog();
        addEventDialog.show(getSupportFragmentManager(), "Add Event");

    }

}
