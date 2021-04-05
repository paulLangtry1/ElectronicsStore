package com.example.electronicsstoreapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
{
    ArrayList<Item> contractssFromDB;

    private OnContractListener monContractListener;

    public MyAdapter(ArrayList<Comment> allcomments)
    {
    }

    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtView;
        public TextView activejob;

        //public Button btnAcceptContract;
        OnContractListener onContractListener;

        public MyViewHolder(View itemView,OnContractListener onContractListener){
            super(itemView);
            txtView= itemView.findViewById(R.id.textView);


            this.onContractListener = onContractListener;

            itemView.setOnClickListener(this);


        }



        @Override
        public void onClick(View view)
        {
            onContractListener.onContractClick(getAdapterPosition());

            //int position=this.getLayoutPosition();
            //Contract selectedContract =contractssFromDB.get(position);
            //Toast.makeText(view.getContext(),"This worked", Toast.LENGTH_LONG).show();
            //Intent intent= new Intent(view.getContext(),CurrentContract.class);
            //view.getContext().startActivity(intent);

        }
    }

    public MyAdapter(ArrayList<Item>myDataset, OnContractListener onContractListener)
    {
        contractssFromDB=myDataset;
        this.monContractListener = onContractListener;

    }
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.card_layout,parent,false);
        MyViewHolder viewHolder=new MyViewHolder(itemView, monContractListener);
        return viewHolder;
    }

    public interface OnContractListener
    {
        void onContractClick(int position);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Item contract=contractssFromDB.get(position);
        holder.txtView.setText("Title :"+contract.getTitle()+"\n" + "\n" + "Manufacturer :"+contract.getManufacturer()+"\n" + "Price:"+contract.getPrice());

    }
    public void add(int position, Item contract){
        contractssFromDB.add(position, contract);
        notifyItemInserted(position);
    }
    public void remove(int position){
        contractssFromDB.remove(position);
        notifyItemRemoved(position);
    }
    public void update(Item contract,int position){
        contractssFromDB.set(position,contract);
        notifyItemChanged(position);
    }
    public void addItemtoEnd(Item contract){
        //these functions are user-defined
        contractssFromDB.add(contract);
        notifyItemInserted(contractssFromDB.size());
    }


    @Override
    public int getItemCount() {
        return contractssFromDB.size();
    }

}
