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
import com.example.mytriptracker.entities.Vacation;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    public class VacationViewHolder extends RecyclerView.ViewHolder{
        private final TextView vacationItemView;
        private VacationViewHolder(View itemView){
            super(itemView);
            vacationItemView = itemView.findViewById(R.id.textView2);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Vacation current = mVacations.get(position);
                    String myFormat = "MM/dd/yyyy";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                    String startDateString=sdf.format(current.getStartDate().getTime());
                    String endDateString=sdf.format(current.getEndDate().getTime());
                    Intent intent = new Intent(context,VacationDetails.class);
                    intent.putExtra("vacationID", current.getVacationId());
                    intent.putExtra("vacationTitle", current.getVacationTitle());
                    intent.putExtra("hotel", current.getHotel());
                    intent.putExtra("startDate", startDateString);
                    intent.putExtra("endDate", endDateString);
                    context.startActivity(intent);
                }
            });
        }

    }
    private List<Vacation> mVacations;
    private final Context context;
    private final LayoutInflater mInflater;
    public VacationAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }
    @NonNull
    @Override
    public VacationAdapter.VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.vacation_list_item,parent, false);
        return new VacationViewHolder((itemView));
    }

    @Override
    public void onBindViewHolder(@NonNull VacationAdapter.VacationViewHolder holder, int position) {
        if(mVacations!=null){
            Vacation current = mVacations.get(position);
            String name = current.getVacationTitle();
            holder.vacationItemView.setText(name);
        }else{
            holder.vacationItemView.setText("No Vacations to show");
        }
    }

    @Override
    public int getItemCount() {
        return mVacations.size();
    }

    public void setVacations(List<Vacation> vacations){

        mVacations = vacations;
        notifyDataSetChanged();
    }


}
