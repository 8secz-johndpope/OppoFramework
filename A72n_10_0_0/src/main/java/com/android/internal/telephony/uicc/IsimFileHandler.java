package com.android.internal.telephony.uicc;

import android.telephony.Rlog;
import com.android.internal.telephony.CommandsInterface;

public class IsimFileHandler extends IccFileHandler implements IccConstants {
    static final String LOG_TAG = "IsimFH";

    public IsimFileHandler(UiccCardApplication app, String aid, CommandsInterface ci) {
        super(app, aid, ci);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.internal.telephony.uicc.IccFileHandler
    public String getEFPath(int efid) {
        if (efid == 28423 || efid == 28425) {
            return "3F007FFF";
        }
        switch (efid) {
            case IccConstants.EF_IMPI /* 28418 */:
            case IccConstants.EF_DOMAIN /* 28419 */:
            case IccConstants.EF_IMPU /* 28420 */:
                return "3F007FFF";
            default:
                return getCommonIccEFPath(efid);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.android.internal.telephony.uicc.IccFileHandler
    public void logd(String msg) {
        Rlog.d(LOG_TAG, msg);
    }

    /* access modifiers changed from: protected */
    @Override // com.android.internal.telephony.uicc.IccFileHandler
    public void loge(String msg) {
        Rlog.e(LOG_TAG, msg);
    }
}
