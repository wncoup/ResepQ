package com.alvin.resepqu.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alvin.resepqu.DetailActivity;
import com.alvin.resepqu.Object.ResepObject;
import com.alvin.resepqu.R;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ResepAdapter extends RecyclerView.Adapter<ResepAdapter.CardViewHolder> {

    private Context context;
    private List<ResepObject> resepObjectsList;

    public ResepAdapter(Context context, List<ResepObject> resepObjectsList) {
        this.context = context;
        this.resepObjectsList = resepObjectsList;
    }


    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);

        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewHolder holder, int position) {
        final ResepObject resepObject = resepObjectsList.get(position);

        holder.ivImage.setImageURI(Uri.parse(resepObject.getImg()));
//        System.out.println("FOTOXX : " + Uri.parse(resepObject.getImg()));
        holder.tvTime.setText(formatDate(resepObject.getTime()));
        holder.tvJudul.setText(resepObject.getJudul());
        holder.tvDeskripsi.setText(resepObject.getDeskripsi());
        //
//        holder.tvCara.setText(resepObject.getCara());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), DetailActivity.class);
                i.putExtra(DetailActivity.KEY_JUDUL , resepObject.getJudul());
                i.putExtra(DetailActivity.KEY_DESKRIPSI, resepObject.getDeskripsi());
                i.putExtra(DetailActivity.KEY_IMG, resepObject.getImg());
                i.putExtra(DetailActivity.KEY_TIME, resepObject.getTime());
                i.putExtra(DetailActivity.KEY_CARA, resepObject.getCara());

                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return resepObjectsList.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        ImageView ivImage;
        TextView tvTime, tvJudul, tvDeskripsi, tvCara;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.iv_imgCardItem);
            tvTime = itemView.findViewById(R.id.tv_timeCardItem);
            tvJudul = itemView.findViewById(R.id.tv_judulResepCardItem);
            tvDeskripsi = itemView.findViewById(R.id.tv_deskripsiResepCardItem);
//            tvCara = itemView.findViewById(R.id.tv_caraResepCardItem);
        }
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("MMM d");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }
        return "";
    }
}
