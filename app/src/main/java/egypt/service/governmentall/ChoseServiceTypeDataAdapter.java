package egypt.service.governmentall;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by falcon on 02/10/2017.
 */

public class ChoseServiceTypeDataAdapter extends RecyclerView.Adapter<ChoseServiceTypeDataAdapter.ViewHolder> {
    private ArrayList<String> androidList;
    private Context context;
    private int lastPosition=-1;
    String typeServic ;

    public ChoseServiceTypeDataAdapter(ArrayList<String> android,Context c , String typeService) {
        this.androidList = android;
        this.context=c;
        this.typeServic=typeService;
    }

    @Override
    public ChoseServiceTypeDataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout. row_layout, viewGroup, false);


        return new ChoseServiceTypeDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChoseServiceTypeDataAdapter.ViewHolder viewHolder, final int i) {


        viewHolder.cintery_name.setText(androidList.get(i));



        viewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,WhatDoyouWantToKnow.class);
                intent.putExtra("serviceName",typeServic);
                intent.putExtra("ChoseTypeService",androidList.get(i));
                context.startActivity(intent);
            }
        });
        setAnimation(viewHolder.card, i);

    }



    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return androidList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView factory_name,cintery_name,email_factory;
        private LinearLayout card;
        public ViewHolder(View view) {
            super(view);
            card=(LinearLayout)view.findViewById(R.id.cardView);
            cintery_name = (TextView)view.findViewById(R.id.title);

        }
    }

}