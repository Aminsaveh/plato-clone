package com.example.ap_plato;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class game_adapter extends RecyclerView.Adapter<game_adapter.RecyclerViewHolder> {
    private ArrayList<GameHandler> games;
    private ArrayList<View> items = new ArrayList<>();
    public class RecyclerViewHolder extends  RecyclerView.ViewHolder{
        TextView textView;
        ProgressBar progressBar;
        de.hdodenhof.circleimageview.CircleImageView imageView;
        TextView level;
        public RecyclerViewHolder(@NonNull View itemView){
            super(itemView);
            this.textView = itemView.findViewById(R.id.game_name);
            this.progressBar = itemView.findViewById(R.id.progress_bar);
            this.imageView = itemView.findViewById(R.id.game_img);
            this.level = itemView.findViewById(R.id.level_text);
        }
    }
    public game_adapter(ArrayList<GameHandler> games){
        this.games = games;
    }
    @NonNull
    @Override
    public game_adapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_bio_card_view , parent , false);
        game_adapter.RecyclerViewHolder recyclerViewHolder = new game_adapter.RecyclerViewHolder(view);
        items.add(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull game_adapter.RecyclerViewHolder holder, final int position) {
        final String user = this.games.get(position).name;
        final int score = this.games.get(position).Score;
        holder.progressBar.setMax(10);
        holder.progressBar.setProgress(score % 10);
        holder.level.setText("Level : " + (score / 10));
        holder.textView.setText(user);
        if (user.equals("XO")){
            holder.imageView.setBackgroundResource(R.drawable.xo);
        }
        else if (user.equals("DotsAndBoxes")){
            holder.imageView.setBackgroundResource(R.drawable.dotsboxes);
        }
        else if (user.equals("Connect 3")){
            holder.imageView.setBackgroundResource(R.drawable.connect3);
        }else if (user.equals("Word Guess")){
            holder.imageView.setBackgroundResource(R.drawable.guessword);
        }
    }
    @Override
    public int getItemCount() {
        return this.games.size();
    }

}
