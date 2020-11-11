package com.dev.words;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button buttonInsert, buttonClear;

    WordViewModel wordViewModel;
    MyAdapter myAdapter1, myAdapter2;
    RecyclerView recyclerView;
    Switch aSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wordViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(WordViewModel.class);
        buttonInsert = findViewById(R.id.buttonInsert);

        buttonClear = findViewById(R.id.buttonClear);

        recyclerView = findViewById(R.id.recylerView);
        aSwitch = findViewById(R.id.switch1);
        myAdapter1 = new MyAdapter(false,wordViewModel);
        myAdapter2 = new MyAdapter(true,wordViewModel);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    recyclerView.setAdapter(myAdapter2);
                } else {

                    recyclerView.setAdapter(myAdapter1);
                }
            }
        });




        //观察数据库的内容变化
        wordViewModel.getListLiveDataWords().observe(this, new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                int temp = myAdapter1.getItemCount();
                myAdapter1.setAllWords(words);
                myAdapter2.setAllWords(words);
                if(temp != words.size()){
                    myAdapter1.notifyDataSetChanged();
                    myAdapter2.notifyDataSetChanged();
                }
            }
        });


        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String english[] = {
                        "Hello",
                        "World",
                        "Google",
                        "Japan",
                        "Chronometer",
                        "Repository",
                        "Android",
                        "Apple",
                        "USA",
                        "NEW",
                        "Recycler",
                        "Project"
                };
                String chineseMeaing[] = {
                        "你好",
                        "世界",
                        "谷歌",
                        "日本",
                        "计时器",
                        "仓库",
                        "安卓",
                        "苹果",
                        "美国",
                        "新世界",
                        "回收站",
                        "项目"
                };
                int i = 0;
                for (String string : english) {
                    wordViewModel.insertWords(new Word(english[i], chineseMeaing[i]));
                    i++;
                }

            }
        });
        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wordViewModel.deleteAllWords();

            }
        });


    }


}