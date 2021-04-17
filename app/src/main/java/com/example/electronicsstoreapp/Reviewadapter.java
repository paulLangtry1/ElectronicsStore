package com.example.electronicsstoreapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class Reviewadapter  extends RecyclerView.Adapter<Reviewadapter.MyViewHolder>
{
    ArrayList<feedback> contractssFromDB;

    private Reviewadapter.OnContractListener monContractListener;

    public Reviewadapter(ArrayList<Comment> allcomments)
    {
    }

    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtView;
        public TextView activejob;

        public RatingBar rb;
        //public Button btnAcceptContract;
        Reviewadapter.OnContractListener onContractListener;

        public MyViewHolder(View itemView, Reviewadapter.OnContractListener onContractListener){
            super(itemView);
            rb = itemView.findViewById(R.id.ratingBarlayout);
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

    public Reviewadapter(ArrayList<feedback>myDataset, Reviewadapter.OnContractListener onContractListener)
    {
        contractssFromDB=myDataset;
        this.monContractListener = onContractListener;

    }
    @Override
    public Reviewadapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.card_layout_rating,parent,false);
        Reviewadapter.MyViewHolder viewHolder=new Reviewadapter.MyViewHolder(itemView, monContractListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Reviewadapter.MyViewHolder holder, int position) {
        final feedback contract=contractssFromDB.get(position);
        holder.txtView.setText("Category :"+contract.getCategory()+"\n" + "\n" + "Title :"+contract.getTitle());
        holder.rb.setRating(Float.parseFloat(contract.getRating()));

    }

    public interface OnContractListener
    {
        void onContractClick(int position);
    }

    public void add(int position, feedback contract){
        contractssFromDB.add(position, contract);
        notifyItemInserted(position);
    }
    public void remove(int position){
        contractssFromDB.remove(position);
        notifyItemRemoved(position);
    }
    public void update(feedback contract,int position){
        contractssFromDB.set(position,contract);
        notifyItemChanged(position);
    }
    public void addItemtoEnd(feedback contract){
        //these functions are user-defined
        contractssFromDB.add(contract);
        notifyItemInserted(contractssFromDB.size());
    }


    @Override
    public int getItemCount() {
        return contractssFromDB.size();
    }
}
