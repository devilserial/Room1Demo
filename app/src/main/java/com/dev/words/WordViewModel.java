package com.dev.words;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private WordRepository wordRepository;

    public WordViewModel(@NonNull Application application) {
        super(application);
        wordRepository = new WordRepository(application);
    }

    public LiveData<List<Word>> getListLiveDataWords() {
        return wordRepository.getLiveDataWords();
    }

    //封装方法，执行每个对应的线程
    public void insertWords(Word... words) {
        wordRepository.insertWords(words);
    }

    public void deleteAllWords() {
        wordRepository.deleteAllWords();
    }

    public void updateWords(Word... words) {
        wordRepository.updateWords(words);
    }

    public void deleteWords(Word... words) {
        wordRepository.deleteWords(words);
    }


}
