package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class ClientRequestStats implements Parcelable {
    public static final Parcelable.Creator<ClientRequestStats> CREATOR = new Parcelable.Creator<ClientRequestStats>() {
        /* class android.telephony.ClientRequestStats.AnonymousClass1 */

        @Override // android.os.Parcelable.Creator
        public ClientRequestStats createFromParcel(Parcel in) {
            return new ClientRequestStats(in);
        }

        @Override // android.os.Parcelable.Creator
        public ClientRequestStats[] newArray(int size) {
            return new ClientRequestStats[size];
        }
    };
    private static final int REQUEST_HISTOGRAM_BUCKET_COUNT = 5;
    private String mCallingPackage;
    private long mCompletedRequestsCount = 0;
    private long mCompletedRequestsWakelockTime = 0;
    private long mPendingRequestsCount = 0;
    private long mPendingRequestsWakelockTime = 0;
    private SparseArray<TelephonyHistogram> mRequestHistograms = new SparseArray<>();

    public ClientRequestStats(Parcel in) {
        readFromParcel(in);
    }

    public ClientRequestStats() {
    }

    public ClientRequestStats(ClientRequestStats clientRequestStats) {
        this.mCallingPackage = clientRequestStats.getCallingPackage();
        this.mCompletedRequestsCount = clientRequestStats.getCompletedRequestsCount();
        this.mCompletedRequestsWakelockTime = clientRequestStats.getCompletedRequestsWakelockTime();
        this.mPendingRequestsCount = clientRequestStats.getPendingRequestsCount();
        this.mPendingRequestsWakelockTime = clientRequestStats.getPendingRequestsWakelockTime();
        for (TelephonyHistogram entry : clientRequestStats.getRequestHistograms()) {
            this.mRequestHistograms.put(entry.getId(), entry);
        }
    }

    public String getCallingPackage() {
        return this.mCallingPackage;
    }

    public void setCallingPackage(String mCallingPackage2) {
        this.mCallingPackage = mCallingPackage2;
    }

    public long getCompletedRequestsWakelockTime() {
        return this.mCompletedRequestsWakelockTime;
    }

    public void addCompletedWakelockTime(long completedRequestsWakelockTime) {
        this.mCompletedRequestsWakelockTime += completedRequestsWakelockTime;
    }

    public long getPendingRequestsWakelockTime() {
        return this.mPendingRequestsWakelockTime;
    }

    public void setPendingRequestsWakelockTime(long pendingRequestsWakelockTime) {
        this.mPendingRequestsWakelockTime = pendingRequestsWakelockTime;
    }

    public long getCompletedRequestsCount() {
        return this.mCompletedRequestsCount;
    }

    public void incrementCompletedRequestsCount() {
        this.mCompletedRequestsCount++;
    }

    public long getPendingRequestsCount() {
        return this.mPendingRequestsCount;
    }

    public void setPendingRequestsCount(long pendingRequestsCount) {
        this.mPendingRequestsCount = pendingRequestsCount;
    }

    public List<TelephonyHistogram> getRequestHistograms() {
        List<TelephonyHistogram> list;
        synchronized (this.mRequestHistograms) {
            list = new ArrayList<>(this.mRequestHistograms.size());
            for (int i = 0; i < this.mRequestHistograms.size(); i++) {
                list.add(new TelephonyHistogram(this.mRequestHistograms.valueAt(i)));
            }
        }
        return list;
    }

    public void updateRequestHistograms(int requestId, int time) {
        synchronized (this.mRequestHistograms) {
            TelephonyHistogram entry = this.mRequestHistograms.get(requestId);
            if (entry == null) {
                entry = new TelephonyHistogram(1, requestId, 5);
                this.mRequestHistograms.put(requestId, entry);
            }
            entry.addTimeTaken(time);
        }
    }

    public String toString() {
        return "ClientRequestStats{mCallingPackage='" + this.mCallingPackage + DateFormat.QUOTE + ", mCompletedRequestsWakelockTime=" + this.mCompletedRequestsWakelockTime + ", mCompletedRequestsCount=" + this.mCompletedRequestsCount + ", mPendingRequestsWakelockTime=" + this.mPendingRequestsWakelockTime + ", mPendingRequestsCount=" + this.mPendingRequestsCount + '}';
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public void readFromParcel(Parcel in) {
        this.mCallingPackage = in.readString();
        this.mCompletedRequestsWakelockTime = in.readLong();
        this.mCompletedRequestsCount = in.readLong();
        this.mPendingRequestsWakelockTime = in.readLong();
        this.mPendingRequestsCount = in.readLong();
        ArrayList<TelephonyHistogram> requestHistograms = new ArrayList<>();
        in.readTypedList(requestHistograms, TelephonyHistogram.CREATOR);
        Iterator<TelephonyHistogram> it = requestHistograms.iterator();
        while (it.hasNext()) {
            TelephonyHistogram h = it.next();
            this.mRequestHistograms.put(h.getId(), h);
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mCallingPackage);
        dest.writeLong(this.mCompletedRequestsWakelockTime);
        dest.writeLong(this.mCompletedRequestsCount);
        dest.writeLong(this.mPendingRequestsWakelockTime);
        dest.writeLong(this.mPendingRequestsCount);
        dest.writeTypedList(getRequestHistograms());
    }
}
