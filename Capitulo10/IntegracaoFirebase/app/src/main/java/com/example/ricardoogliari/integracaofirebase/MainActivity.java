package com.example.ricardoogliari.integracaofirebase;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends WearableActivity {

    private TextView txtStatus;
    private boolean value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtStatus = (TextView) findViewById(R.id.txtStatus);

        setAmbientEnabled();

        LifeCycle.myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                value = dataSnapshot.getValue(Boolean.class);
                txtStatus.setText(value ? "Ligado" : "Desligado");
            }

            @Override
            public void onCancelled(DatabaseError error) {
            }
        });

    }

    public void changeStatus(View view) {
        value = !value;
        LifeCycle.myRef.setValue(value);
    }


}
