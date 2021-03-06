package com.android.server.wm;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.Slog;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceControl;
import com.android.server.display.OppoBrightUtils;

class CircularDisplayMask {
    private static final String TAG = "WindowManager";
    private boolean mDimensionsUnequal = false;
    private boolean mDrawNeeded;
    private int mLastDH;
    private int mLastDW;
    private int mMaskThickness;
    private Paint mPaint;
    private int mRotation;
    private int mScreenOffset = 0;
    private Point mScreenSize;
    private final Surface mSurface = new Surface();
    private final SurfaceControl mSurfaceControl;
    private boolean mVisible;

    public CircularDisplayMask(DisplayContent dc, int zOrder, int screenOffset, int maskThickness) {
        Display display = dc.getDisplay();
        this.mScreenSize = new Point();
        display.getSize(this.mScreenSize);
        if (this.mScreenSize.x != this.mScreenSize.y + screenOffset) {
            Slog.w("WindowManager", "Screen dimensions of displayId = " + display.getDisplayId() + "are not equal, circularMask will not be drawn.");
            this.mDimensionsUnequal = true;
        }
        SurfaceControl ctrl = null;
        try {
            ctrl = dc.makeOverlay().setName("CircularDisplayMask").setBufferSize(this.mScreenSize.x, this.mScreenSize.y).setFormat(-3).build();
            ctrl.setLayerStack(display.getLayerStack());
            ctrl.setLayer(zOrder);
            ctrl.setPosition(OppoBrightUtils.MIN_LUX_LIMITI, OppoBrightUtils.MIN_LUX_LIMITI);
            ctrl.show();
            this.mSurface.copyFrom(ctrl);
        } catch (Surface.OutOfResourcesException e) {
        }
        this.mSurfaceControl = ctrl;
        this.mDrawNeeded = true;
        this.mPaint = new Paint();
        this.mPaint.setAntiAlias(true);
        this.mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.mScreenOffset = screenOffset;
        this.mMaskThickness = maskThickness;
    }

    private void drawIfNeeded() {
        if (this.mDrawNeeded && this.mVisible && !this.mDimensionsUnequal) {
            this.mDrawNeeded = false;
            Canvas c = null;
            try {
                c = this.mSurface.lockCanvas(new Rect(0, 0, this.mScreenSize.x, this.mScreenSize.y));
            } catch (Surface.OutOfResourcesException | IllegalArgumentException e) {
            }
            if (c != null) {
                int i = this.mRotation;
                if (i == 0 || i == 1) {
                    this.mSurfaceControl.setPosition(OppoBrightUtils.MIN_LUX_LIMITI, OppoBrightUtils.MIN_LUX_LIMITI);
                } else if (i == 2) {
                    this.mSurfaceControl.setPosition(OppoBrightUtils.MIN_LUX_LIMITI, (float) (-this.mScreenOffset));
                } else if (i == 3) {
                    this.mSurfaceControl.setPosition((float) (-this.mScreenOffset), OppoBrightUtils.MIN_LUX_LIMITI);
                }
                int circleRadius = this.mScreenSize.x / 2;
                c.drawColor(-16777216);
                c.drawCircle((float) circleRadius, (float) circleRadius, (float) (circleRadius - this.mMaskThickness), this.mPaint);
                this.mSurface.unlockCanvasAndPost(c);
            }
        }
    }

    public void setVisibility(boolean on) {
        if (this.mSurfaceControl != null) {
            this.mVisible = on;
            drawIfNeeded();
            if (on) {
                this.mSurfaceControl.show();
            } else {
                this.mSurfaceControl.hide();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void positionSurface(int dw, int dh, int rotation) {
        if (this.mLastDW != dw || this.mLastDH != dh || this.mRotation != rotation) {
            this.mLastDW = dw;
            this.mLastDH = dh;
            this.mDrawNeeded = true;
            this.mRotation = rotation;
            drawIfNeeded();
        }
    }
}
