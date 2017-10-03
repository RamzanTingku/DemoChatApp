package com.example.ramzanullah.demochatapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ViewMomentActivity extends AppCompatActivity {

    private RecyclerView momentListView;
    private Toolbar toolbar;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_moment);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("moments");

        toolbar = (Toolbar) findViewById(R.id.main_page_appbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Moments");

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setReverseLayout(true);
        llm.setStackFromEnd(true);

        momentListView = (RecyclerView) findViewById(R.id.recyclerview_moment);
        momentListView.setHasFixedSize(true);
        momentListView.setLayoutManager(llm);


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Moment,MomentViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Moment,MomentViewHolder>(
                Moment.class, R.layout.single_row_moment, MomentViewHolder.class, mDatabase
        ) {
            @Override
            protected void populateViewHolder(MomentViewHolder viewHolder, Moment model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage());
            }
        };

        momentListView.setAdapter(firebaseRecyclerAdapter);

    }

    public static class MomentViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public MomentViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title){
            TextView viewTitle = (TextView)mView.findViewById(R.id.tv_view_title);
            viewTitle.setText(title);
        }

        public void setDesc(String desc){
            TextView viewDesc = (TextView)mView.findViewById(R.id.tv_view_description);
            viewDesc.setText(desc);
        }

        public void setImage(Context imgContext, String image){
            ImageView viewImage = (ImageView)mView.findViewById(R.id.iv_view_image);
            Picasso.with(imgContext).load(image).into(viewImage);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.view_moment,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId()== R.id.add_moment){

            startActivity(new Intent(ViewMomentActivity.this,AddMomentActivity.class));
        }

        return true;
    }

  /*  @Override
    public void onBackPressed() {
        //super.onBackPressed();
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exiting App");
        builder.setMessage("Are sure to exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }*/




   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseRecyclerAdapter.cleanup();
    }*/

}
