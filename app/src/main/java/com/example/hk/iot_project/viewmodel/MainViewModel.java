package com.example.hk.iot_project.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayMap;
import android.util.Log;

import com.example.hk.iot_project.model.GetDataModel;


public class MainViewModel extends ViewModel{
    // TODO: Implement the ViewModel
    private GetDataModel getDataModel;

    public final ObservableArrayMap<String, String> getData = new ObservableArrayMap<>();
    public final ObservableArrayMap<String, String> postData = new ObservableArrayMap<>();

    public MainViewModel() {
        Log.d("onMainViewModel", "iot complete");

        getDataModel = new GetDataModel();
        setGetDataModel();
    }

    public void onCreate() { }
    public void onPause() { }
    public void onResume() { }
    public void onDestroy() { }

    public void setGetDataModel(){
        Log.d("onMainViewModel", "in setGetDataModel");
        if(getDataModel.isData()){
            String str = getDataModel.getData();
            Log.d("onMainViewModel", "this data is : "+str);
        }
    }

    /**
     * An Action, callable by the view.  This action will pass a message to the model
     * for the cell clicked and then update the observable fields with the current
     * model state.
     */
    public void onClickedCellAt(int row, int col) {
//        Player playerThatMoved = getDataModel.mark(row, col);
//        cells.put("" + row + col, playerThatMoved == null ?
//                null : playerThatMoved.toString());
//        winner.set(getDataModel.getWinner() == null ? null : getDataModel.getWinner().toString());
    }

    /**
     * An Action, callable by the view.  This action will pass a message to the model
     * to restart and then clear the observable data in this ViewModel.
     */
    public void onResetSelected() {
//        getDataModel.restart();
//        getData.clear();
//        postData.clear();
    }

    public void onCleared(){
        getData.clear();
        postData.clear();
    }
}
