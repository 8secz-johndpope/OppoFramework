package android.telephony;

import android.annotation.UnsupportedAppUsage;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.SystemProperties;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SignalStrength extends OppoSignalStrength implements Parcelable {
    @UnsupportedAppUsage
    public static final Parcelable.Creator<SignalStrength> CREATOR = new Parcelable.Creator() {
        /* class android.telephony.SignalStrength.AnonymousClass1 */

        @Override // android.os.Parcelable.Creator
        public SignalStrength createFromParcel(Parcel in) {
            return SignalStrength.makeSignalStrength(in);
        }

        @Override // android.os.Parcelable.Creator
        public SignalStrength[] newArray(int size) {
            return new SignalStrength[size];
        }
    };
    private static final boolean DBG = false;
    public static final int INVALID = Integer.MAX_VALUE;
    private static final String LOG_TAG = "SignalStrength";
    private static final int LTE_RSRP_THRESHOLDS_NUM = 4;
    private static final String MEASUREMENT_TYPE_RSCP = "rscp";
    @UnsupportedAppUsage
    public static final int NUM_SIGNAL_STRENGTH_BINS = 5;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static final int SIGNAL_STRENGTH_GOOD = 3;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static final int SIGNAL_STRENGTH_GREAT = 4;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static final int SIGNAL_STRENGTH_MODERATE = 2;
    public static final String[] SIGNAL_STRENGTH_NAMES = {"none", "poor", "moderate", "good", "great"};
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static final int SIGNAL_STRENGTH_NONE_OR_UNKNOWN = 0;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static final int SIGNAL_STRENGTH_POOR = 1;
    private static final int WCDMA_RSCP_THRESHOLDS_NUM = 4;
    CellSignalStrengthCdma mCdma;
    CellSignalStrengthGsm mGsm;
    protected CellSignalStrengthLte mLte;
    protected CellSignalStrengthNr mNr;
    CellSignalStrengthTdscdma mTdscdma;
    CellSignalStrengthWcdma mWcdma;

    @UnsupportedAppUsage
    public static SignalStrength newFromBundle(Bundle m) {
        SignalStrength ret = new SignalStrength();
        ret.setFromNotifierBundle(m);
        return ret;
    }

    @UnsupportedAppUsage
    public SignalStrength() {
        this(new CellSignalStrengthCdma(), new CellSignalStrengthGsm(), new CellSignalStrengthWcdma(), new CellSignalStrengthTdscdma(), new CellSignalStrengthLte(), new CellSignalStrengthNr());
    }

    public SignalStrength(CellSignalStrengthCdma cdma, CellSignalStrengthGsm gsm, CellSignalStrengthWcdma wcdma, CellSignalStrengthTdscdma tdscdma, CellSignalStrengthLte lte, CellSignalStrengthNr nr) {
        this.mCdma = cdma;
        this.mGsm = gsm;
        this.mWcdma = wcdma;
        this.mTdscdma = tdscdma;
        this.mLte = lte;
        this.mNr = nr;
        this.mOEMLevel_0 = 0;
        this.mOEMLevel_1 = 0;
    }

    public SignalStrength(android.hardware.radio.V1_0.SignalStrength signalStrength) {
        this(new CellSignalStrengthCdma(signalStrength.cdma, signalStrength.evdo), new CellSignalStrengthGsm(signalStrength.gw), new CellSignalStrengthWcdma(), new CellSignalStrengthTdscdma(signalStrength.tdScdma), new CellSignalStrengthLte(signalStrength.lte), new CellSignalStrengthNr());
        this.mOEMLevel_0 = 0;
        this.mOEMLevel_1 = 0;
    }

    public SignalStrength(android.hardware.radio.V1_2.SignalStrength signalStrength) {
        this(new CellSignalStrengthCdma(signalStrength.cdma, signalStrength.evdo), new CellSignalStrengthGsm(signalStrength.gsm), new CellSignalStrengthWcdma(signalStrength.wcdma), new CellSignalStrengthTdscdma(signalStrength.tdScdma), new CellSignalStrengthLte(signalStrength.lte), new CellSignalStrengthNr());
    }

    public SignalStrength(android.hardware.radio.V1_4.SignalStrength signalStrength) {
        this(new CellSignalStrengthCdma(signalStrength.cdma, signalStrength.evdo), new CellSignalStrengthGsm(signalStrength.gsm), new CellSignalStrengthWcdma(signalStrength.wcdma), new CellSignalStrengthTdscdma(signalStrength.tdscdma), new CellSignalStrengthLte(signalStrength.lte), new CellSignalStrengthNr(signalStrength.nr));
    }

    private CellSignalStrength getPrimary() {
        if (this.mLte.isValid()) {
            return this.mLte;
        }
        if (this.mCdma.isValid()) {
            return this.mCdma;
        }
        if (this.mTdscdma.isValid()) {
            return this.mTdscdma;
        }
        if (this.mWcdma.isValid()) {
            return this.mWcdma;
        }
        if (this.mGsm.isValid()) {
            return this.mGsm;
        }
        if (this.mNr.isValid()) {
            return this.mNr;
        }
        return this.mLte;
    }

    public List<CellSignalStrength> getCellSignalStrengths() {
        return getCellSignalStrengths(CellSignalStrength.class);
    }

    public <T extends CellSignalStrength> List<T> getCellSignalStrengths(Class<T> clazz) {
        List<T> cssList = new ArrayList<>(2);
        if (this.mLte.isValid() && clazz.isAssignableFrom(CellSignalStrengthLte.class)) {
            cssList.add(this.mLte);
        }
        if (this.mCdma.isValid() && clazz.isAssignableFrom(CellSignalStrengthCdma.class)) {
            cssList.add(this.mCdma);
        }
        if (this.mTdscdma.isValid() && clazz.isAssignableFrom(CellSignalStrengthTdscdma.class)) {
            cssList.add(this.mTdscdma);
        }
        if (this.mWcdma.isValid() && clazz.isAssignableFrom(CellSignalStrengthWcdma.class)) {
            cssList.add(this.mWcdma);
        }
        if (this.mGsm.isValid() && clazz.isAssignableFrom(CellSignalStrengthGsm.class)) {
            cssList.add(this.mGsm);
        }
        if (this.mNr.isValid() && clazz.isAssignableFrom(CellSignalStrengthNr.class)) {
            cssList.add(this.mNr);
        }
        return cssList;
    }

    public void updateLevel(PersistableBundle cc, ServiceState ss) {
        this.mCdma.updateLevel(cc, ss);
        this.mGsm.updateLevel(cc, ss);
        this.mWcdma.updateLevel(cc, ss);
        this.mTdscdma.updateLevel(cc, ss);
        this.mLte.updateLevel(cc, ss);
        this.mNr.updateLevel(cc, ss);
    }

    @UnsupportedAppUsage
    public SignalStrength(SignalStrength s) {
        copyFrom(s);
    }

    @UnsupportedAppUsage
    public void copyFrom(SignalStrength s) {
        this.mCdma = new CellSignalStrengthCdma(s.mCdma);
        this.mGsm = new CellSignalStrengthGsm(s.mGsm);
        this.mWcdma = new CellSignalStrengthWcdma(s.mWcdma);
        this.mTdscdma = new CellSignalStrengthTdscdma(s.mTdscdma);
        this.mLte = new CellSignalStrengthLte(s.mLte);
        this.mNr = new CellSignalStrengthNr(s.mNr);
        this.mOEMLevel_0 = s.mOEMLevel_0;
        this.mOEMLevel_1 = s.mOEMLevel_1;
    }

    @UnsupportedAppUsage
    public SignalStrength(Parcel in) {
        this.mCdma = (CellSignalStrengthCdma) in.readParcelable(CellSignalStrengthCdma.class.getClassLoader());
        this.mGsm = (CellSignalStrengthGsm) in.readParcelable(CellSignalStrengthGsm.class.getClassLoader());
        this.mWcdma = (CellSignalStrengthWcdma) in.readParcelable(CellSignalStrengthWcdma.class.getClassLoader());
        this.mTdscdma = (CellSignalStrengthTdscdma) in.readParcelable(CellSignalStrengthTdscdma.class.getClassLoader());
        this.mLte = (CellSignalStrengthLte) in.readParcelable(CellSignalStrengthLte.class.getClassLoader());
        this.mNr = (CellSignalStrengthNr) in.readParcelable(CellSignalStrengthLte.class.getClassLoader());
        this.mOEMLevel_0 = in.readInt();
        this.mOEMLevel_1 = in.readInt();
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.mCdma, flags);
        out.writeParcelable(this.mGsm, flags);
        out.writeParcelable(this.mWcdma, flags);
        out.writeParcelable(this.mTdscdma, flags);
        out.writeParcelable(this.mLte, flags);
        out.writeParcelable(this.mNr, flags);
        out.writeInt(this.mOEMLevel_0);
        out.writeInt(this.mOEMLevel_1);
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Deprecated
    public int getGsmSignalStrength() {
        return this.mGsm.getAsuLevel();
    }

    @Deprecated
    public int getGsmBitErrorRate() {
        return this.mGsm.getBitErrorRate();
    }

    @Deprecated
    public int getCdmaDbm() {
        return this.mCdma.getCdmaDbm();
    }

    @Deprecated
    public int getCdmaEcio() {
        return this.mCdma.getCdmaEcio();
    }

    @Deprecated
    public int getEvdoDbm() {
        return this.mCdma.getEvdoDbm();
    }

    @Deprecated
    public int getEvdoEcio() {
        return this.mCdma.getEvdoEcio();
    }

    @Deprecated
    public int getEvdoSnr() {
        return this.mCdma.getEvdoSnr();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getLteSignalStrength() {
        return this.mLte.getRssi();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getLteRsrp() {
        return this.mLte.getRsrp();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getLteRsrq() {
        return this.mLte.getRsrq();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getLteRssnr() {
        return this.mLte.getRssnr();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getLteCqi() {
        return this.mLte.getCqi();
    }

    public int getLevel() {
        int level = getPrimary().getLevel();
        if (level >= 0 && level <= 4) {
            return getPrimary().getLevel();
        }
        loge("Invalid Level " + level + ", this=" + this);
        return 0;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getAsuLevel() {
        return getPrimary().getAsuLevel();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getDbm() {
        return getPrimary().getDbm();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getGsmDbm() {
        return this.mGsm.getDbm();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getGsmLevel() {
        return this.mGsm.getLevel();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getGsmAsuLevel() {
        return this.mGsm.getAsuLevel();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getCdmaLevel() {
        return this.mCdma.getLevel();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getCdmaAsuLevel() {
        return this.mCdma.getAsuLevel();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getEvdoLevel() {
        return this.mCdma.getEvdoLevel();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getEvdoAsuLevel() {
        return this.mCdma.getEvdoAsuLevel();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getLteDbm() {
        return this.mLte.getRsrp();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getLteLevel() {
        return this.mLte.getLevel();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getLteAsuLevel() {
        return this.mLte.getAsuLevel();
    }

    @Deprecated
    public boolean isGsm() {
        return !(getPrimary() instanceof CellSignalStrengthCdma);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getTdScdmaDbm() {
        return this.mTdscdma.getRscp();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getTdScdmaLevel() {
        return this.mTdscdma.getLevel();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public int getTdScdmaAsuLevel() {
        return this.mTdscdma.getAsuLevel();
    }

    @Deprecated
    public int getWcdmaRscp() {
        return this.mWcdma.getRscp();
    }

    @Deprecated
    public int getWcdmaAsuLevel() {
        return this.mWcdma.getAsuLevel();
    }

    @Deprecated
    public int getWcdmaDbm() {
        return this.mWcdma.getDbm();
    }

    @Deprecated
    public int getWcdmaLevel() {
        return this.mWcdma.getLevel();
    }

    public int hashCode() {
        return Objects.hash(this.mCdma, this.mGsm, this.mWcdma, this.mTdscdma, this.mLte, this.mNr);
    }

    public boolean equals(Object o) {
        if (!(o instanceof SignalStrength)) {
            return false;
        }
        SignalStrength s = (SignalStrength) o;
        if (!this.mCdma.equals(s.mCdma) || !this.mGsm.equals(s.mGsm) || !this.mWcdma.equals(s.mWcdma) || !this.mTdscdma.equals(s.mTdscdma) || !this.mLte.equals(s.mLte) || !this.mNr.equals(s.mNr)) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "SignalStrength:{" + "mCdma=" + this.mCdma + ",mGsm=" + this.mGsm + ",mWcdma=" + this.mWcdma + ",mTdscdma=" + this.mTdscdma + ",mLte=" + this.mLte + ",mNr=" + this.mNr + ",primary=" + getPrimary().getClass().getSimpleName() + "}";
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    private void setFromNotifierBundle(Bundle m) {
        this.mCdma = (CellSignalStrengthCdma) m.getParcelable("Cdma");
        this.mGsm = (CellSignalStrengthGsm) m.getParcelable("Gsm");
        this.mWcdma = (CellSignalStrengthWcdma) m.getParcelable("Wcdma");
        this.mTdscdma = (CellSignalStrengthTdscdma) m.getParcelable("Tdscdma");
        this.mLte = (CellSignalStrengthLte) m.getParcelable("Lte");
        this.mNr = (CellSignalStrengthNr) m.getParcelable("Nr");
        this.mOEMLevel_0 = m.getInt("OEMLevel_0");
        this.mOEMLevel_1 = m.getInt("OEMLevel_1");
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    @Deprecated
    public void fillInNotifierBundle(Bundle m) {
        m.putParcelable("Cdma", this.mCdma);
        m.putParcelable("Gsm", this.mGsm);
        m.putParcelable("Wcdma", this.mWcdma);
        m.putParcelable("Tdscdma", this.mTdscdma);
        m.putParcelable("Lte", this.mLte);
        m.putParcelable("Nr", this.mNr);
        m.putInt("OEMLevel_0", this.mOEMLevel_0);
        m.putInt("OEMLevel_1", this.mOEMLevel_1);
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }

    private static void loge(String s) {
        Rlog.e(LOG_TAG, s);
    }

    /* access modifiers changed from: private */
    public static SignalStrength makeSignalStrength(Parcel in) {
        if (!SystemProperties.get("ro.vendor.mtk_telephony_add_on_policy", WifiEnterpriseConfig.ENGINE_DISABLE).equals(WifiEnterpriseConfig.ENGINE_DISABLE)) {
            return new SignalStrength(in);
        }
        try {
            Constructor clazzConstructfunc = Class.forName("mediatek.telephony.MtkSignalStrength").getConstructor(Parcel.class);
            clazzConstructfunc.setAccessible(true);
            return (SignalStrength) clazzConstructfunc.newInstance(in);
        } catch (InstantiationException e) {
            e.printStackTrace();
            Rlog.e(LOG_TAG, "MtkSignalStrength InstantiationException! Used AOSP instead!");
            return new SignalStrength(in);
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
            Rlog.e(LOG_TAG, "MtkSignalStrength InvocationTargetException! Used AOSP instead!");
            return new SignalStrength(in);
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
            Rlog.e(LOG_TAG, "MtkSignalStrength IllegalAccessException! Used AOSP instead!");
            return new SignalStrength(in);
        } catch (Exception e4) {
            return new SignalStrength(in);
        }
    }

    @Override // android.telephony.OppoSignalStrength
    public int[] getColorOSLevel() {
        return new int[]{this.mOEMLevel_0, this.mOEMLevel_1};
    }
}
