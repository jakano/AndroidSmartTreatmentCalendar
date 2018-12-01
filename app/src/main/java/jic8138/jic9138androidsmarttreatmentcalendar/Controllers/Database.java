package jic8138.jic9138androidsmarttreatmentcalendar.Controllers;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import jic8138.jic9138androidsmarttreatmentcalendar.Event;


public final class Database {

    public static FirebaseAuth mAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    private static FirebaseDatabase database;
    public static FirebaseUser currentUser;
    public static boolean initialized = false;
    private static ArrayList<Event> events;


    public static void initialize() {
        if (!initialized) {
            mAuth = FirebaseAuth.getInstance();
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    currentUser = firebaseAuth.getCurrentUser();
                    if (currentUser != null) {
                        // User is signed in
                        Log.d("DatabaseAuth", "onAuthStateChanged:signed_in:" + currentUser.getUid());
                    } else {
                        // User is signed out
                        Log.d("DatabaseAuth", "onAuthStateChanged:signed_out");
                    }
                }
            };
            initialized = true;
            events = new ArrayList<>();
            database = FirebaseDatabase.getInstance();

            DatabaseReference ref = Database.getReference("events");

            ref.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Event event = dataSnapshot.getValue(Event.class);
                    events.add(event);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Event event = dataSnapshot.getValue(Event.class);
                    for (int i = 0; i < events.size(); i++) {
                        if (event.getEventID().equals(events.get(i).getEventID())) {
                            events.set(i, event);
                            break;
                        }
                    }
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Event event = dataSnapshot.getValue(Event.class);
                    for (int i = 0; i < events.size(); i++) {
                        if (event.getEventID().equals(events.get(i).getEventID())) {
                            events.remove(i);
                            break;
                        }
                    }
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
    }



    public static ArrayList<Event> getEvents() {
        initialize();
        return events;
    }

    public static DatabaseReference getReference(String ref) {
        if (!initialized) {
            initialize();
        }
        return database.getReference(ref);
    }

    public static void addListener() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    public static void removeListener() {
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private Database() {}
}