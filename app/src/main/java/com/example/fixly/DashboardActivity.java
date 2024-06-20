package com.example.fixly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    private RecyclerView recyclerView1, recyclerView2;
    private MyAdapter adapter1;
    private MySecondAdapter adapter2;
    TextView greetingTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.dashboard);

        greetingTextView = findViewById(R.id.greeting);
        String username = getIntent().getStringExtra("username");
        if (username != null) {
            greetingTextView.setText("Hey " + username);
        }

        // Initialize RecyclerViews
        recyclerView1 = findViewById(R.id.recycler_view);
        recyclerView2 = findViewById(R.id.rercycler_view);

        // Set LayoutManager for each RecyclerView
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager1);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);

        // Sample data
        List<ItemModel> dataList1 = new ArrayList<>();
        dataList1.add(new ItemModel(R.drawable.icon1, "Plumber"));
        dataList1.add(new ItemModel(R.drawable.icon2, "Electrician"));
        dataList1.add(new ItemModel(R.drawable.icon3, "Carpenter"));
        dataList1.add(new ItemModel(R.drawable.icon4, "Builder"));
        dataList1.add(new ItemModel(R.drawable.icon5, "Painter"));
        List<String> dataList2 = Arrays.asList("Dan Omondi", "John Karanja", "Geoffrey Bisley ", "Francis Akatu", "Meresia Akinyi");

        // Set Adapter for each RecyclerView
        adapter1 = new MyAdapter(dataList1);
        recyclerView1.setAdapter(adapter1);

        adapter2 = new MySecondAdapter(dataList2);
        recyclerView2.setAdapter(adapter2);
    }

    public class ItemModel {
        private int imageResId;
        private String text;

        public ItemModel(int imageResId, String text) {
            this.imageResId = imageResId;
            this.text = text;
        }

        public int getImageResId() {
            return imageResId;
        }

        public String getText() {
            return text;
        }
    }


    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        // Inside MyAdapter class
        private List<ItemModel> dataList;

        // Constructor
        public MyAdapter(List<ItemModel> dataList) {
            this.dataList = dataList;
        }




        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.itemlayout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            // Retrieve data for the current position
            ItemModel item = dataList.get(position);

            // Set image and text for the current item
            holder.imageView.setImageResource(item.getImageResId());
            holder.textView.setText(item.getText());
        }


        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;
            public ImageView imageView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }

    private class MySecondAdapter extends RecyclerView.Adapter<MySecondAdapter.ViewHolder> {
        private List<String> dataList;

        public MySecondAdapter(List<String> dataList) {
            this.dataList = dataList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.itemmlayout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String data = dataList.get(position);
            holder.textView.setText(data);
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textViewM);
            }
        }
    }
}
