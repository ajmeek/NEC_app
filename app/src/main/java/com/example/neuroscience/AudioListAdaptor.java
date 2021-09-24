package com.example.neuroscience;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class AudioListAdaptor extends RecyclerView.Adapter<AudioListAdaptor.AudioViewHolder> {

    private File[] allFiles;
    private TimeCapture timeCapture;
    private onItemListClick onItemListClick;

    public AudioListAdaptor(File[] allFiles, onItemListClick onItemListClick){

        this.allFiles = allFiles;
        this.onItemListClick = onItemListClick;

    }

    @NonNull
    @Override
    public AudioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Initialize view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_list_item, parent, false);
        timeCapture = new TimeCapture();
        return new AudioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AudioViewHolder holder, int position) {
        //set text on text view
        holder.list_title.setText(allFiles[position].getName());
        holder.list_date.setText(timeCapture.getTimeCapture(allFiles[position].lastModified()));

    }

    @Override
    public int getItemCount() {

        return allFiles.length;
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView list_image;
        private TextView list_title, list_date;

        public AudioViewHolder(@NonNull View itemView) {
            super(itemView);

            list_image = itemView.findViewById(R.id.list_image_view);
            list_title = itemView.findViewById(R.id.list_file);
            list_date = itemView.findViewById(R.id.list_date);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onItemListClick.onClickListener(allFiles[getAdapterPosition()], getAdapterPosition());
        }
    }//End of AudioViewHolder


    public interface onItemListClick{
        void onClickListener(File file, int position);

    }//End of onItmeListCLick

}//End of AudioListAdaptor
