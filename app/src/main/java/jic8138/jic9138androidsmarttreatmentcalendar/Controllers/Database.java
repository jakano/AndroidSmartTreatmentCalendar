package jic8138.jic9138androidsmarttreatmentcalendar.Controllers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public final class Database {

    public static FirebaseAuth mAuth;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    private static FirebaseDatabase database;
    public static FirebaseUser currentUser;
    public static boolean initialized = false;


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
            database = FirebaseDatabase.getInstance();
        }
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