package com.example.todolist.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.AddNewTask;
import com.example.todolist.MainActivity;
import com.example.todolist.R;
import com.example.todolist.Utils.DatabaseHandler;
import com.example.todolist.model.ToDoModel;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter <ToDoAdapter.ViewHolder>
{
    private List<ToDoModel> toDoModelList;
    private MainActivity activity;
    private DatabaseHandler db;

    public  ToDoAdapter(DatabaseHandler db,MainActivity activity){
        this.db = db;
        this.activity = activity;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent , false);
        return  new ViewHolder(itemView);

    }
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        db.openDatabase();
         ToDoModel item = toDoModelList.get(position);
         holder.task.setText(item.getTask());
         holder.task.setChecked(toBoolean(item.getStatus()));
         holder.task.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 if(isChecked)
                 {
                     db.updateStatus(item.getId(), 1);
                 }
                 else
                 {
                 db.updateStatus(item.getId(), 0);
                 }
             }
         });
    }
    public int getItemCount(){
        return toDoModelList.size();
    }
    public void setTasks(List<ToDoModel> todoList)
    {
        this.toDoModelList= todoList;
        notifyDataSetChanged();
    }
    private boolean toBoolean(int n)
    {
        return n != 0;
    }
    public Context getContext()
    {
        return activity;
    }
    public void editItem(int position)
    {
        ToDoModel item = toDoModelList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id" , item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragmet = new AddNewTask();
        fragmet.setArguments(bundle);
        fragmet.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox task;

        ViewHolder(View view)
        {
            super(view);
            task = view.findViewById(R.id.todoCheckbox);
        }
    }
}
