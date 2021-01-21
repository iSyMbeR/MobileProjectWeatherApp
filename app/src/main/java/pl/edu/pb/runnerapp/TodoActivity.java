package pl.edu.pb.runnerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

import pl.edu.pb.runnerapp.model.TaskModel;


public class TodoActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingAddButton;
    private FloatingActionButton floatingBackButton;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    public static FirebaseUser firebaseUser;
    private String onlineUserID;

    private ProgressDialog loader;
    private String key = "";
    private String task;
    private String description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_todo);
        toolbar = findViewById(R.id.todoToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Todo List");

        recyclerView = findViewById(R.id.todoRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        loader = new ProgressDialog(this);
        firebaseUser = firebaseAuth.getCurrentUser();
        onlineUserID = firebaseUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("tasks").child(onlineUserID);

        floatingAddButton = findViewById(R.id.todoFAB);
        floatingAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTask();
            }
        });
        floatingBackButton = findViewById(R.id.todoBack);
        floatingBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TodoActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

    }

    private void addTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = LayoutInflater.from(this);

        View myView = layoutInflater.inflate(R.layout.todo_input_file, null);
        myDialog.setView(myView);

        final AlertDialog dialog = myDialog.create();
        dialog.setCancelable(false);

        final EditText task = myView.findViewById(R.id.task);
        final EditText description = myView.findViewById(R.id.taskDescription);
        Button save = myView.findViewById(R.id.todoSaveButton);
        Button cancel = myView.findViewById(R.id.todoCancelButton);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mTask = task.getText().toString().trim();
                String mDescription = description.getText().toString().trim();
                String id = databaseReference.push().getKey();
                String date = DateFormat.getDateInstance().format(new Date());
                if (TextUtils.isEmpty(mTask)) {
                    task.setError("Task Required");
                    return;
                }
                if (TextUtils.isEmpty(mDescription)) {
                    description.setError("Description required");
                    return;
                } else {
                    loader.setMessage("Adding your data");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();
                    Log.d("blond", id + " Id < przed task model");
                    TaskModel taskModel = new TaskModel(id, mTask, mDescription, date);
                    Log.d("blond", taskModel.toString() + " task model < przed task model");
                    databaseReference.child(id).setValue(taskModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("blond", "onComplete task model");
                            if (task.isSuccessful()) {
                                Log.d("blond", "onComplete isSuccessful task model");
                                Toast.makeText(TodoActivity.this, "Task has been added ", Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            } else {
                                Log.d("blond", "onComplete error task model");
                                String error = task.getException().toString();
                                Toast.makeText(TodoActivity.this, "Failder " + error, Toast.LENGTH_SHORT).show();
                                loader.dismiss();
                            }
                        }
                    });
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }











    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<TaskModel> options = new FirebaseRecyclerOptions.Builder<TaskModel>()
                .setQuery(databaseReference, TaskModel.class)
                .build();

        FirebaseRecyclerAdapter<TaskModel, MyViewHolder> adapter = new FirebaseRecyclerAdapter<TaskModel, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, final int position, @NonNull final TaskModel model) {
                holder.setDate(model.getDate());
                holder.setTask(model.getTask());
                holder.setDesc(model.getDescription());

            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retreived_layout, parent, false);
                return new MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTask(String task) {
            TextView taskTectView = mView.findViewById(R.id.taskTv);
            taskTectView.setText(task);
        }

        public void setDesc(String desc) {
            TextView descTectView = mView.findViewById(R.id.descriptionTv);
            descTectView.setText(desc);
        }

        public void setDate(String date) {
            TextView dateTextView = mView.findViewById(R.id.dateTv);
        }
    }











//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseRecyclerOptions<TaskModel> options = new FirebaseRecyclerOptions.Builder<TaskModel>()
//                .setQuery(databaseReference, TaskModel.class)
//                .build();
//
//        FirebaseRecyclerAdapter<TaskModel, MyViewHolder> adapter = new FirebaseRecyclerAdapter<TaskModel, MyViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull TaskModel model) {
//                holder.setData(model.getDate());
//                holder.setTask(model.getTask());
//                holder.setDescription(model.getDescription());
//            }
//
//            @NonNull
//            @Override
//            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.retreived_layout, parent, false);
//                return new MyViewHolder(view);
//            }
//        };
//
//        recyclerView.setAdapter(adapter);
//        adapter.startListening();
//    }
//
//    public static class MyViewHolder extends RecyclerView.ViewHolder {
//        View view;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            view = itemView;
//        }
//
//        public void setTask(String task) {
//            TextView taskTextView = view.findViewById(R.id.taskTv);
//            taskTextView.setText(task);
//        }
//
//        public void setDescription(String description) {
//            TextView descriptionTextView = view.findViewById(R.id.descriptionTv);
//            descriptionTextView.setText(description);
//        }
//
//        public void setData(String date) {
//            TextView dateTextView = view.findViewById(R.id.dateTv);
//        }
//    }
}