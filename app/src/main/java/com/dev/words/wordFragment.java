package com.dev.words;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link wordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class wordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private WordViewModel wordViewModel;
    private MyAdapter myAdapter1, myAdapter2;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private LiveData<List<Word>>    filterWords;
    public static final String VIEW_TYPE_SHP="view_type_shp";
    public static final String IS_USING_CARDVIEW = "is_using_cardview";

    public wordFragment() {
        // Required empty public constructor
        setHasOptionsMenu(true);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment wordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static wordFragment newInstance(String param1, String param2) {
        wordFragment fragment = new wordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 搜索菜单
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(750);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String patten = newText.trim();
                filterWords.removeObservers(requireActivity());
                filterWords = wordViewModel.getSeachWords(patten);
                filterWords.observe(requireActivity(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int temp = myAdapter1.getItemCount();
                        myAdapter1.setAllWords(words);
                        myAdapter2.setAllWords(words);
                        if (temp != words.size()) {
                            myAdapter1.notifyDataSetChanged();
                            myAdapter2.notifyDataSetChanged();
                        }
                    }
                });
                return true;
            }
        });
    }

    /**
     * 切换视图菜单,清除数据菜单
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.clearData:
                AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("清除数据");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wordViewModel.deleteAllWords();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
                break;
            case R.id.changedView:
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(VIEW_TYPE_SHP, Context.MODE_PRIVATE);
                boolean viewType = sharedPreferences.getBoolean(IS_USING_CARDVIEW,false);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(viewType){
                    recyclerView.setAdapter(myAdapter1);
                    editor.putBoolean(IS_USING_CARDVIEW,false);
                }else{
                    recyclerView.setAdapter(myAdapter2);
                    editor.putBoolean(IS_USING_CARDVIEW,true);
                }
                editor.apply();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_word, container, false);
    }

    /**
     * 开始的时候初始化
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wordViewModel = new ViewModelProvider(requireActivity(),
                new ViewModelProvider.AndroidViewModelFactory(requireActivity().getApplication())).get(WordViewModel.class);
        myAdapter1 = new MyAdapter(false, wordViewModel);
        myAdapter2 = new MyAdapter(true, wordViewModel);
        recyclerView = requireActivity().findViewById(R.id.recyclerView);
        floatingActionButton = requireActivity().findViewById(R.id.floatingActionButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(VIEW_TYPE_SHP, Context.MODE_PRIVATE);
        boolean viewType = sharedPreferences.getBoolean(IS_USING_CARDVIEW,false);
        if(viewType){
            recyclerView.setAdapter(myAdapter2);
        }else{
            recyclerView.setAdapter(myAdapter1);
        }
        filterWords = wordViewModel.getListLiveDataWords();
        filterWords.observe(requireActivity(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                int temp = myAdapter1.getItemCount();
                myAdapter1.setAllWords(words);
                myAdapter2.setAllWords(words);
                if (temp != words.size()) {
                    myAdapter1.notifyDataSetChanged();
                    myAdapter2.notifyDataSetChanged();
                }
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_wordFragment_to_addFragment);
            }
        });
    }

    @Override
    public void onResume() {
        InputMethodManager inputMethodManager1 = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager1.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        super.onResume();
    }
}