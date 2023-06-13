package com.example.mytriptracker.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mytriptracker.R;
import com.example.mytriptracker.entities.Excursion;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder>{

    public class ExcursionViewHolder extends RecyclerView.ViewHolder{
        private final TextView excursionItemView1;
        private final TextView excursionItemView2;
        private ExcursionViewHolder(View itemView){
            super(itemView);
            excursionItemView1 = itemView.findViewById(R.id.ExcursionTitle);
            excursionItemView2 = itemView.findViewById(R.id.ExcursionDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Excursion current = mExcursions.get(position);
                    String myFormat = "MM/dd/yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                    String dateString=sdf.format(current.getDate());

                    Intent intent = new Intent(context,ExcursionDetails.class);
                    intent.putExtra("excursionId", current.getExcursionId());
                    intent.putExtra("title", current.getTitle());
                    intent.putExtra("date", dateString);
                    intent.putExtra("vacationID", current.getVacationID());
                    context.startActivity(intent);
                }
            });
        }
    }
    private List<Excursion> mExcursions;
    private final Context context;
    private final LayoutInflater mInflater;

    public ExcursionAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }
    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.excursion_list_item,parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if(mExcursions!=null){
            Excursion current = mExcursions.get(position);
            String name = current.getTitle();
            Date date = current.getDate();
            holder.excursionItemView1.setText(name);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            String dateString = dateFormat.format(date);
            holder.excursionItemView2.setText(dateString);
        }else{
            holder.excursionItemView1.setText("No Excursions name to show");
            holder.excursionItemView2.setText("No Vacations ID to show");
        }
    }

    @Override
    public int getItemCount() {
        if(mExcursions != null){
            return mExcursions.size();
        }
        return 0;
    }

    public void setExcursions(List<Excursion> excursions){

        mExcursions = excursions;
        notifyDataSetChanged();
    }

    public Excursion getExcursionAtPosition(int position) {
        return mExcursions.get(position);
    }


}
