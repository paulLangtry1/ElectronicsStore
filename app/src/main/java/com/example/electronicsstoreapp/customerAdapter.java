package com.example.electronicsstoreapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Comment;

import java.util.ArrayList;

public class customerAdapter  extends RecyclerView.Adapter<customerAdapter.MyViewHolder>
{
    ArrayList<Customer> contractssFromDB;

    private customerAdapter.OnContractListener monContractListener;

    public customerAdapter(ArrayList<Comment> allcomments)
    {
    }

    //Inner class - Provide a reference to each item/row
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView txtView;
        public TextView activejob;

        //public Button btnAcceptContract;
        customerAdapter.OnContractListener onContractListener;

        public MyViewHolder(View itemView, customerAdapter.OnContractListener onContractListener){
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

    public customerAdapter(ArrayList<Customer>myDataset, customerAdapter.OnContractListener onContractListener)
    {
        contractssFromDB=myDataset;
        this.monContractListener = onContractListener;

    }
    @Override
    public customerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //create new view - create a row - inflate the layout for the row
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View itemView =inflater.inflate(R.layout.card_layout,parent,false);
        customerAdapter.MyViewHolder viewHolder=new customerAdapter.MyViewHolder(itemView, monContractListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Customer contract=contractssFromDB.get(position);
        holder.txtView.setText("Name :"+contract.getUsername()+"\n" + "\n" + "Phone Number :"+contract.getPhoneNo()+"\n" + "Email:"+contract.getEmail());
    }

    public interface OnContractListener
    {
        void onContractClick(int position);
    }

    public void add(int position, Customer contract){
        contractssFromDB.add(position, contract);
        notifyItemInserted(position);
    }
    public void remove(int position){
        contractssFromDB.remove(position);
        notifyItemRemoved(position);
    }
    public void update(Customer contract,int position){
        contractssFromDB.set(position,contract);
        notifyItemChanged(position);
    }
    public void addItemtoEnd(Customer contract){
        //these functions are user-defined
        contractssFromDB.add(contract);
        notifyItemInserted(contractssFromDB.size());
    }


    @Override
    public int getItemCount() {
        return contractssFromDB.size();
    }

}
