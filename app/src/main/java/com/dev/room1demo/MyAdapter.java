package com.dev.room1demo;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<Word> allWords = new ArrayList<>();
    boolean cardView;
    WordViewModel wordViewModel;

    public void setAllWords(List<Word> allWords) {
        this.allWords = allWords;
    }

    public MyAdapter(boolean cardView,WordViewModel wordViewModel) {
        this.cardView = cardView;
        this.wordViewModel = wordViewModel;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //加载layout文件的类
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        //实例化itemView ,通过加载自己创建的layout文件

        View itemView;
        if (cardView) {
            itemView = layoutInflater.inflate(R.layout.cell_card, parent, false);
        } else {
            itemView = layoutInflater.inflate(R.layout.cell_normal_2, parent, false);
        }

        //返回实例化的自定义的MyViewHolder
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Word word = allWords.get(position);
        holder.textViewNum.setText(String.valueOf(position));
        holder.textViewEnglish.setText(word.getWord());
        holder.textViewChinese.setText(word.getChineseMeaning());
        holder.chineseInvisible.setOnCheckedChangeListener(null);
        if(word.isBar()){
            holder.textViewChinese.setVisibility(View.GONE);
            holder.chineseInvisible.setChecked(true);
        }else{
            holder.textViewChinese.setVisibility(View.VISIBLE);
            holder.chineseInvisible.setChecked(false);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://m.youdao.com/dict?le=eng&q=" + holder.textViewEnglish.getText());
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        holder.chineseInvisible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    holder.textViewChinese.setVisibility(View.GONE);
                    word.setBar(true);
                    wordViewModel.updateWords(word);
                }else {
                    holder.textViewChinese.setVisibility(View.VISIBLE);
                   word.setBar(false);
                   wordViewModel.updateWords(word);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return allWords.size();
    }

    //首先定义自己的ViewHolder
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNum, textViewEnglish, textViewChinese;
        Switch chineseInvisible;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            chineseInvisible = itemView.findViewById(R.id.chineseInvisible);
            textViewNum = itemView.findViewById(R.id.textViewNum);
            textViewEnglish = itemView.findViewById(R.id.textViewEnglish);
            textViewChinese = itemView.findViewById(R.id.textViewChinese);
        }
    }
}
