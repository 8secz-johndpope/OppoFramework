package com.color.oshare;

import android.bluetooth.BluetoothDevice;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

public class ColorOshareDevice implements Parcelable {
    public static final Parcelable.Creator<ColorOshareDevice> CREATOR = new Parcelable.Creator<ColorOshareDevice>() {
        /* class com.color.oshare.ColorOshareDevice.AnonymousClass1 */

        @Override // android.os.Parcelable.Creator
        public ColorOshareDevice createFromParcel(Parcel in) {
            String name = in.readString();
            String blemac = in.readString();
            String wifimac = in.readString();
            int virtual = in.readInt();
            int progress = in.readInt();
            String remaindTime = in.readString();
            int successNum = in.readInt();
            int totalNum = in.readInt();
            long lastFoundTime = in.readLong();
            int vender = in.readInt();
            ColorOshareDevice set = new ColorOshareDevice();
            set.mName = name;
            set.mBleMac = blemac;
            set.mWifiMac = wifimac;
            set.mState = (ColorOshareState) in.readSerializable();
            set.mVirtual = virtual;
            set.mBluetootchDevice = (BluetoothDevice) in.readParcelable(null);
            set.mProgress = progress;
            set.mRemainTime = remaindTime;
            set.mHeadIcon = (Bitmap) in.readParcelable(null);
            set.mSucceedNum = successNum;
            set.mTotalNum = totalNum;
            set.mLastFoundTime = lastFoundTime;
            set.mVender = vender;
            return set;
        }

        @Override // android.os.Parcelable.Creator
        public ColorOshareDevice[] newArray(int size) {
            return new ColorOshareDevice[size];
        }
    };
    public static final int DEFAULT_VIRTUAL = 8;
    private String mBleMac = "";
    private BluetoothDevice mBluetootchDevice;
    private String mDisplayName = "";
    private Bitmap mHeadIcon;
    private long mLastFoundTime;
    private String mName = "";
    private int mProgress;
    private String mRemainTime;
    private ColorOshareState mState = ColorOshareState.IDLE;
    private int mSucceedNum;
    private int mTotalNum;
    private int mVender;
    private int mVirtual = 8;
    private String mWifiMac = null;

    public long getLastFoundTime() {
        return this.mLastFoundTime;
    }

    public void setLastFoundTime(long lastFoundTime) {
        this.mLastFoundTime = lastFoundTime;
    }

    public String getDisplayName() {
        return this.mDisplayName;
    }

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public int getSucceedNum() {
        return this.mSucceedNum;
    }

    public void setSucceedNum(int succeedNum) {
        this.mSucceedNum = succeedNum;
    }

    public int getTotalNum() {
        return this.mTotalNum;
    }

    public void setTotalNum(int totalNum) {
        this.mTotalNum = totalNum;
    }

    public int getVender() {
        return this.mVender;
    }

    public void setVender(int vender) {
        this.mVender = vender;
    }

    public Bitmap getHeadIcon() {
        return this.mHeadIcon;
    }

    public void setHeadIcon(Bitmap bitmap) {
        this.mHeadIcon = bitmap;
    }

    public String getRemainTime() {
        return this.mRemainTime;
    }

    public void setRemainTime(String remainTime) {
        this.mRemainTime = remainTime;
    }

    public void setProgress(int progress) {
        this.mProgress = progress;
    }

    public int getProgress() {
        return this.mProgress;
    }

    public BluetoothDevice getBluetoothDevice() {
        return this.mBluetootchDevice;
    }

    public boolean isVirtual() {
        return this.mVirtual > 0;
    }

    public void setVirtual(int virtual) {
        this.mVirtual = virtual;
    }

    public int getVirtual() {
        return this.mVirtual;
    }

    public void setBluetootchDevice(BluetoothDevice bluetootchDevice) {
        this.mBluetootchDevice = bluetootchDevice;
    }

    public String getName() {
        return this.mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getBleMac() {
        return this.mBleMac;
    }

    public void setBleMac(String bleMac) {
        this.mBleMac = bleMac;
    }

    public String getWifiMac() {
        return this.mWifiMac;
    }

    public void setWifiMac(String wifiMac) {
        this.mWifiMac = wifiMac;
    }

    public ColorOshareState getState() {
        return this.mState;
    }

    public void setState(ColorOshareState state) {
        if (!this.mState.equals(ColorOshareState.TRANSIT_SUCCESS) || !state.equals(ColorOshareState.TRANSIT_FAILED)) {
            this.mState = state;
        }
    }

    public String toString() {
        return "Name:" + this.mName + " Virtual:" + this.mVirtual + " State:" + this.mState;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof ColorOshareDevice)) {
            return false;
        }
        ColorOshareDevice other = (ColorOshareDevice) o;
        if (!compare(this.mName, other.getName()) || !compare(this.mBleMac, other.getBleMac()) || !compare(this.mWifiMac, other.getWifiMac())) {
            return false;
        }
        return true;
    }

    private boolean compare(String aString, String bString) {
        if (TextUtils.isEmpty(aString) && TextUtils.isEmpty(bString)) {
            return true;
        }
        if (!TextUtils.isEmpty(aString)) {
            return aString.equals(bString);
        }
        return false;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mBleMac);
        dest.writeString(this.mWifiMac);
        dest.writeSerializable(this.mState);
        dest.writeInt(this.mVirtual);
        dest.writeParcelable(this.mBluetootchDevice, flags);
        dest.writeInt(this.mProgress);
        dest.writeString(this.mRemainTime);
        dest.writeParcelable(this.mHeadIcon, flags);
        dest.writeInt(this.mSucceedNum);
        dest.writeInt(this.mTotalNum);
        dest.writeLong(this.mLastFoundTime);
        dest.writeInt(this.mVender);
    }
}
