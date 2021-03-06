package android.widget;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import java.util.Calendar;

@Deprecated
public class DigitalClock extends TextView {
    Calendar mCalendar;
    String mFormat;
    private FormatChangeObserver mFormatChangeObserver;
    /* access modifiers changed from: private */
    public Handler mHandler;
    /* access modifiers changed from: private */
    public Runnable mTicker;
    /* access modifiers changed from: private */
    public boolean mTickerStopped = false;

    public DigitalClock(Context context) {
        super(context);
        initClock();
    }

    public DigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock();
    }

    private void initClock() {
        if (this.mCalendar == null) {
            this.mCalendar = Calendar.getInstance();
        }
    }

    /* access modifiers changed from: protected */
    @Override // android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        this.mTickerStopped = false;
        super.onAttachedToWindow();
        this.mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, this.mFormatChangeObserver);
        setFormat();
        this.mHandler = new Handler();
        this.mTicker = new Runnable() {
            /* class android.widget.DigitalClock.AnonymousClass1 */

            public void run() {
                if (!DigitalClock.this.mTickerStopped) {
                    DigitalClock.this.mCalendar.setTimeInMillis(System.currentTimeMillis());
                    DigitalClock digitalClock = DigitalClock.this;
                    digitalClock.setText(DateFormat.format(digitalClock.mFormat, DigitalClock.this.mCalendar));
                    DigitalClock.this.invalidate();
                    long now = SystemClock.uptimeMillis();
                    DigitalClock.this.mHandler.postAtTime(DigitalClock.this.mTicker, (1000 - (now % 1000)) + now);
                }
            }
        };
        this.mTicker.run();
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mTickerStopped = true;
        getContext().getContentResolver().unregisterContentObserver(this.mFormatChangeObserver);
    }

    /* access modifiers changed from: private */
    public void setFormat() {
        this.mFormat = DateFormat.getTimeFormatString(getContext());
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean selfChange) {
            DigitalClock.this.setFormat();
        }
    }

    @Override // android.widget.TextView, android.view.View
    public CharSequence getAccessibilityClassName() {
        return DigitalClock.class.getName();
    }
}
