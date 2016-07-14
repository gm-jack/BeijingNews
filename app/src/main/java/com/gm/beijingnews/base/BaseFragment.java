package com.gm.beijingnews.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Administrator on 2016/6/28.
 */
public abstract class BaseFragment extends Fragment {
    /**
     * 存储fragment数据
     */
    public static final String SAVEFRAGMENT = "savefragment";
    Bundle savedState;

    /**
     * 基类Fragment
     */
    public Context context;

    public BaseFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView();
    }

    public abstract View initView();

    /**
     * 当activity的oncreate()方法返回时调用
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //初始化数据
        // Restore State Here
        if (!restoreStateFromArguments()) {
            // First Time, Initialize something here
            onFirstTimeLaunched();
        }
        initData();
    }

    /**
     * 子类重写此方法加载数据
     */
    public void initData() {
    }

    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save State Here
        saveStateToArguments();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Save State Here
        saveStateToArguments();
    }

    ////////////////////
    // Don't Touch !!
    ////////////////////

    private void saveStateToArguments() {
        if (getView() != null)
            savedState = saveState();
        if (savedState != null) {
            Bundle b = getArguments();
            if (b != null) {
                b.putBundle(SAVEFRAGMENT, savedState);
            }
        }
    }

    ////////////////////
    // Don't Touch !!
    ////////////////////

    private boolean restoreStateFromArguments() {
        Bundle b = getArguments();
        if (b != null) {
            savedState = b.getBundle(SAVEFRAGMENT);
            if (savedState != null) {
                restoreState();
                return true;
            }
        }

        return false;
    }

    /////////////////////////////////
    // Restore Instance State Here
    /////////////////////////////////

    private void restoreState() {
        if (savedState != null) {
            // For Example
            //tv1.setText(savedState.getString(text));
            onRestoreState(savedState);
        }
    }

    protected void onRestoreState(Bundle savedInstanceState) {

    }

    //////////////////////////////
    // Save Instance State Here
    //////////////////////////////

    private Bundle saveState() {
        Bundle state = new Bundle();
        // For Example
        //state.putString(text, tv1.getText().toString());
        onSaveState(state);
        return state;
    }

    protected void onSaveState(Bundle outState) {

    }
}
//如果你使用这个模版，你只需继承StatedFragment类然后在onSaveState()保存数据，在onRestoreState()中取出数据，其余的事情上面的代码已经为你做好了，我相信覆盖了我所知道的所有情况。
//        库现在本文描述的StatedFragment已经被做成了一个易于使用的库，并且发布到了jcenter，你现在只需在build.gradle中添加依赖就行了：
//        dependencies {
//        compile 'com.inthecheesefactory.thecheeselibrary:stated-fragment-support-v4:0.9.1'
//        }
//
//        继承StatedFragment，同时分别在onSaveState(Bundle outState)和onRestoreState(Bundle savedInstanceState)中保存和取出状态数据。如果你想在fragment第一次启动的时候做点什么，你也可以重写onFirstTimeLaunched()，它只会在第一次启动的时候被调用。
//public class MainFragment extends StatedFragment {
//
//    ...
//
//    /**
//     * Save Fragment's State here
//     */
//    @Override
//    protected void onSaveState(Bundle outState) {
//        super.onSaveState(outState);
//        // For example:
//        //outState.putString(text, tvSample.getText().toString());
//    }
//
//    /**
//     * Restore Fragment's State here
//     */
//    @Override
//    protected void onRestoreState(Bundle savedInstanceState) {
//        super.onRestoreState(savedInstanceState);
//        // For example:
//        //tvSample.setText(savedInstanceState.getString(text));
//    }
//
//}
