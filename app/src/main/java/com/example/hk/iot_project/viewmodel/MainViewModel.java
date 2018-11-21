package com.example.hk.iot_project.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.databinding.ObservableArrayMap;

import com.example.hk.iot_project.model.GetDataModel;
import com.example.hk.iot_project.model.PostDataModel;

public class MainViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    private GetDataModel getDataModel;
    private PostDataModel postDataModel;

    public final ObservableArrayMap<String, String> getData = new ObservableArrayMap<>();
    public final ObservableArrayMap<String, String> postData = new ObservableArrayMap<>();

    public MainViewModel() {
        getDataModel = new GetDataModel();
        postDataModel = new PostDataModel();
    }

    public void onCreate() { }
    public void onPause() { }
    public void onResume() { }
    public void onDestroy() { }

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
