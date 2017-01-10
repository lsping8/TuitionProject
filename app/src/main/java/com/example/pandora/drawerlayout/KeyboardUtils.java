package com.example.pandora.drawerlayout;

import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import java.util.HashMap;

/**
 * Created by Pandora on 9/4/2016.
 */
public class KeyboardUtils implements ViewTreeObserver.OnGlobalLayoutListener {

    int heightDiff;
    private SoftKeyboardToggleListener mCallback;
    private View mRootView;
    private float mScreenDensity = 1;
    private static HashMap<SoftKeyboardToggleListener, KeyboardUtils> sListenerMap = new HashMap<>();

    public KeyboardUtils(){

    }

    @Override
    public void onGlobalLayout() {
        Rect r = new Rect();
        //r will be populated with the coordinates of your view that area still visible.
        mRootView.getWindowVisibleDisplayFrame(r);

        heightDiff = mRootView.getRootView().getHeight() - (r.bottom - r.top);
        float dp = heightDiff / mScreenDensity;

        if (mCallback != null)
            mCallback.onToggleSoftKeyboard(dp > 200,heightDiff);
    }

    public interface SoftKeyboardToggleListener {
        void onToggleSoftKeyboard(boolean isVisible,int heightDiff);
    }

    public static void addKeyboardToggleListener(Activity act, SoftKeyboardToggleListener listener) {
        removeKeyboardToggleListener(listener);

        sListenerMap.put(listener, new KeyboardUtils(act, listener));
    }

    public static void removeKeyboardToggleListener(SoftKeyboardToggleListener listener) {
        if (sListenerMap.containsKey(listener)) {
            KeyboardUtils k = sListenerMap.get(listener);
            k.removeListener();

            sListenerMap.remove(listener);
        }
    }

    public static void removeAllKeyboardToggleListeners() {
        for (SoftKeyboardToggleListener l : sListenerMap.keySet())
            sListenerMap.get(l).removeListener();

        sListenerMap.clear();
    }

    private void removeListener() {
        mCallback = null;

        mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    public int getKeyboardSize(){
        return heightDiff;
    }

    private KeyboardUtils(Activity act, SoftKeyboardToggleListener listener) {
        mCallback = listener;

        mRootView = ((ViewGroup) act.findViewById(android.R.id.content)).getChildAt(0);
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        mScreenDensity = act.getResources().getDisplayMetrics().density;
    }

}
