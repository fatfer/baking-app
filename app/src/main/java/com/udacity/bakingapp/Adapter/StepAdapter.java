package com.udacity.bakingapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.udacity.bakingapp.Model.Step;
import com.udacity.bakingapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder>{

    private ArrayList<Step> mStepList;
    final private StepAdapter.ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public StepAdapter(ArrayList<Step> stepList, StepAdapter.ListItemClickListener mOnClickListener){
        mStepList = stepList;
        this.mOnClickListener = mOnClickListener;
    }

    @NonNull
    @Override
    public StepViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.step_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);

        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mStepList.size();
    }

    public class StepViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_step_short_description)
        TextView tv_step_short_description;

        @BindView(R.id.iv_video_play_icon)
        ImageView iv_video_play_icon;

        final Context mContext;

        public StepViewHolder(View itemView) {
            super(itemView);

            mContext = itemView.getContext();
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {

            Step step = mStepList.get(listIndex);
            tv_step_short_description.setText(listIndex + ". " + step.getShortDescription());

            String videoURL = step.getVideoURL();
            if(videoURL != null && !videoURL.isEmpty()){
                iv_video_play_icon.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }

}
