package jic8138.jic9138androidsmarttreatmentcalendar;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CalendarActivity extends AppCompatActivity {

    private Button mAddEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_screen);

        mAddEventButton = findViewById(R.id.calendar_add_event_button);
        mAddEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddEventTap();
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.calendar_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment calendar_fragment =  null;
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
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.calendar_view_frame, calendar_fragment);
                transaction.commit();
                return true;
            }
        });

        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.calendar_view_frame, MonthViewFragment.newInstance());
        transaction.commit();

    }

    private void onAddEventTap() {
        DialogFragment addEventDialog = new AddEventDialog();
        addEventDialog.show(getSupportFragmentManager(), "Add Event");

    }

}
