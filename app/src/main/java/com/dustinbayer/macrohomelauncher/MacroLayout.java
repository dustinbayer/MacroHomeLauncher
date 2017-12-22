package com.dustinbayer.macrohomelauncher;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import java.util.BitSet;

/**
 * Created by dusti on 12/21/2017.
 */

public class MacroLayout  extends LinearLayout {

    final int NUM_KEYS = 8;
    final int MACRO_MIN_KEYS = 3;

    Context context;
    View view;
    Rect hitRect;
    BitSet macro = new BitSet(NUM_KEYS);
    int lastKey = -1;

    Boolean canToggleKeys = false;
    public void setCanToggleKeys(Boolean canToggleKeys) { this.canToggleKeys = canToggleKeys; }

    int timeVisibleInMil = 1000;
    public void setTimeVisibleInMil(int timeVisibleInMil) { this.timeVisibleInMil = timeVisibleInMil; }

    Handler handler = new Handler();
    Runnable r;

    public MacroLayout(Context context) {
        this(context, null);
    }

    public MacroLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MacroLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        this.context = context;

        view = View.inflate(context, R.layout.layout_macro, this);
        setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        hitRect = new Rect();
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN ) {
                    if(r != null)
                        stopTimer();

                    for(int i = 0; i < NUM_KEYS; i ++) {
                        View keyView = view.findViewById(getKeyId(i));
                        keyView.getHitRect(hitRect);
                        if (hitRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                            if(i != lastKey)
                                toggleKey(i);
                        }
                    }
                    return true;
                } else if ( motionEvent.getAction() == MotionEvent.ACTION_MOVE ) {
                    for(int i = 0; i < NUM_KEYS; i ++) {
                        View keyView = view.findViewById(getKeyId(i));
                        keyView.getHitRect(hitRect);
                        if (hitRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
                            if(i != lastKey)
                                toggleKey(i);
                        }
                    }
                } else if ( motionEvent.getAction() == MotionEvent.ACTION_UP ||
                            motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    startTimer();
                }
               return false;
            }
        });

        // Assign custom attributes
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs,
                    R.styleable.MacroLayout,
                    0, 0);

            boolean canToggle = true;
            int time = 1000;

            try {
                canToggle = a.getBoolean(R.styleable.MacroLayout_canToggleKeys, false);
                time = a.getInteger(R.styleable.MacroLayout_timeVisibleInMil, 1000);
            } catch (Exception e) {
                Log.e("MACROLAYOUT", "There was an error loading attributes.");
            } finally {
                a.recycle();
            }

            setCanToggleKeys(canToggle);
            setTimeVisibleInMil(time);
        }

        startTimer();
    }

    private void checkMacro() {
        if(macro.cardinality() >= MACRO_MIN_KEYS) {
            Log.d("MACROLAYOUT", "Macro: " + macro.toString());
        }

        fadeOut();
    }

    private void toggleKey(int key) {
        View keyView = view.findViewById(getKeyId(key));
        if(keyView == null)
            return;

        if(macro.get(key)) {
            if(canToggleKeys) {
                keyView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.keyOff, null));
                macro.clear(key);
            }
        } else {
            keyView.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.keyOn, null));
            macro.set(key);
        }
    }

    private void startTimer() {
        handler.postDelayed(r = new Runnable() {
            @Override
            public void run() {
                checkMacro();
            }
        }, timeVisibleInMil);
    }

    private void stopTimer() {
        view.clearAnimation();
        view.setAlpha(1.0f);
        if(handler != null) {
            handler.removeCallbacks(r);
            r = null;
        }
    }

    private void fadeOut() {
        view.animate()
                .translationY(view.getHeight())
                .alpha(0.0f)
                .setDuration(timeVisibleInMil)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.GONE);
                    }
                });
    }

    private int getKeyId(int key) {
        switch (key) {
            case 0:
                return R.id.key_1;
            case 1:
                return R.id.key_2;
            case 2:
                return R.id.key_3;
            case 3:
                return R.id.key_4;
            case 4:
                return R.id.key_5;
            case 5:
                return R.id.key_6;
            case 6:
                return R.id.key_7;
            case 7:
                return R.id.key_8;
            default:
                return 0;
        }
    }

    public void cleanUp() {
        stopTimer();
        handler = null;
        context = null;
        view = null;
        hitRect = null;
        macro = null;
    }
}
