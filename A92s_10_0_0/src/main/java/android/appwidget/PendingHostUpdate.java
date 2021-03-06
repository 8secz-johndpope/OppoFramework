package android.appwidget;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.RemoteViews;

public class PendingHostUpdate implements Parcelable {
    public static final Parcelable.Creator<PendingHostUpdate> CREATOR = new Parcelable.Creator<PendingHostUpdate>() {
        /* class android.appwidget.PendingHostUpdate.AnonymousClass1 */

        @Override // android.os.Parcelable.Creator
        public PendingHostUpdate createFromParcel(Parcel parcel) {
            return new PendingHostUpdate(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public PendingHostUpdate[] newArray(int size) {
            return new PendingHostUpdate[size];
        }
    };
    static final int TYPE_PROVIDER_CHANGED = 1;
    static final int TYPE_VIEWS_UPDATE = 0;
    static final int TYPE_VIEW_DATA_CHANGED = 2;
    final int appWidgetId;
    final int type;
    int viewId;
    RemoteViews views;
    AppWidgetProviderInfo widgetInfo;

    public static PendingHostUpdate updateAppWidget(int appWidgetId2, RemoteViews views2) {
        PendingHostUpdate update = new PendingHostUpdate(appWidgetId2, 0);
        update.views = views2;
        return update;
    }

    public static PendingHostUpdate providerChanged(int appWidgetId2, AppWidgetProviderInfo info) {
        PendingHostUpdate update = new PendingHostUpdate(appWidgetId2, 1);
        update.widgetInfo = info;
        return update;
    }

    public static PendingHostUpdate viewDataChanged(int appWidgetId2, int viewId2) {
        PendingHostUpdate update = new PendingHostUpdate(appWidgetId2, 2);
        update.viewId = viewId2;
        return update;
    }

    private PendingHostUpdate(int appWidgetId2, int type2) {
        this.appWidgetId = appWidgetId2;
        this.type = type2;
    }

    private PendingHostUpdate(Parcel in) {
        this.appWidgetId = in.readInt();
        this.type = in.readInt();
        int i = this.type;
        if (i != 0) {
            if (i != 1) {
                if (i == 2) {
                    this.viewId = in.readInt();
                }
            } else if (in.readInt() != 0) {
                this.widgetInfo = new AppWidgetProviderInfo(in);
            }
        } else if (in.readInt() != 0) {
            this.views = new RemoteViews(in);
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.appWidgetId);
        dest.writeInt(this.type);
        int i = this.type;
        if (i == 0) {
            writeNullParcelable(this.views, dest, flags);
        } else if (i == 1) {
            writeNullParcelable(this.widgetInfo, dest, flags);
        } else if (i == 2) {
            dest.writeInt(this.viewId);
        }
    }

    private void writeNullParcelable(Parcelable p, Parcel dest, int flags) {
        if (p != null) {
            dest.writeInt(1);
            p.writeToParcel(dest, flags);
            return;
        }
        dest.writeInt(0);
    }
}
