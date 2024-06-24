package com.example.fixly;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class handymanActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.handyman);
        View backLink = findViewById(R.id.back);
        backLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(handymanActivity.this, DashboardActivity.class);
                startActivity(intent);
            }

        });

        // Get the passed data
        String handymanData = getIntent().getStringExtra("HANDYMAN_DATA");

        // Find the TextViews and set the data
        TextView nameTextView = findViewById(R.id.dan_omondi);
        TextView aboutTextView = findViewById(R.id.hello_i_m_d);

        if (handymanData != null) {
            // For this example, we're assuming the data is split by a separator. Adjust as needed.
            String[] dataParts = handymanData.split(";"); // Adjust the separator based on your data format
            if (dataParts.length > 0) nameTextView.setText(dataParts[0]); // Name
            if (dataParts.length > 1) aboutTextView.setText(dataParts[1]); // About
        }
    }



}
