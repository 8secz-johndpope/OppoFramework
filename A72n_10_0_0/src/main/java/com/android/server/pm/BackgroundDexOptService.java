package com.android.server.pm;

import android.app.ActivityThread;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.common.OppoFeatureCache;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.os.Environment;
import android.os.OppoThermalManager;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.storage.StorageManager;
import android.util.ArraySet;
import android.util.Log;
import android.util.StatsLog;
import com.android.internal.util.ArrayUtils;
import com.android.server.LocalServices;
import com.android.server.PinnerService;
import com.android.server.job.controllers.JobStatus;
import com.android.server.oppo.TemperatureProvider;
import com.android.server.pm.dex.DexManager;
import com.android.server.pm.dex.DexoptOptions;
import com.android.server.usb.descriptors.UsbTerminalTypes;
import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class BackgroundDexOptService extends JobService {
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final long IDLE_OPTIMIZATION_PERIOD;
    private static final int JOB_IDLE_OPTIMIZE = 800;
    private static final int JOB_POST_BOOT_UPDATE = 801;
    private static final int LOW_THRESHOLD_MULTIPLIER_FOR_DOWNGRADE = 2;
    private static final int OPTIMIZE_ABORT_BY_JOB_SCHEDULER = 2;
    private static final int OPTIMIZE_ABORT_NO_SPACE_LEFT = 3;
    private static final int OPTIMIZE_CONTINUE = 1;
    private static final int OPTIMIZE_PROCESSED = 0;
    private static final String TAG = "BackgroundDexOptService";
    private static long abortIdleDexoptCounter = 0;
    private static final long mDowngradeUnusedAppsThresholdInMillis = getDowngradeUnusedAppsThresholdInMillis();
    private static boolean mUserUnlockedOnce = false;
    private static ComponentName sDexoptServiceName = new ComponentName(PackageManagerService.PLATFORM_PACKAGE_NAME, BackgroundDexOptService.class.getName());
    static final ArraySet<String> sFailedPackageNamesPrimary = new ArraySet<>();
    static final ArraySet<String> sFailedPackageNamesSecondary = new ArraySet<>();
    private static Context sysContext = null;
    private final AtomicBoolean mAbortIdleOptimization = new AtomicBoolean(false);
    private final AtomicBoolean mAbortPostBootUpdate = new AtomicBoolean(false);
    private final File mDataDir = Environment.getDataDirectory();
    private final AtomicBoolean mExitPostBootUpdate = new AtomicBoolean(false);

    static /* synthetic */ long access$408() {
        long j = abortIdleDexoptCounter;
        abortIdleDexoptCounter = 1 + j;
        return j;
    }

    static {
        long j;
        if (DEBUG) {
            j = TimeUnit.MINUTES.toMillis(1);
        } else {
            j = TimeUnit.DAYS.toMillis(1);
        }
        IDLE_OPTIMIZATION_PERIOD = j;
    }

    public static void schedule(Context context) {
        if (!isBackgroundDexoptDisabled()) {
            sysContext = context;
            final JobScheduler js = (JobScheduler) context.getSystemService("jobscheduler");
            IntentFilter userFilter = new IntentFilter();
            userFilter.addAction("android.intent.action.USER_UNLOCKED");
            context.registerReceiverAsUser(new BroadcastReceiver() {
                /* class com.android.server.pm.BackgroundDexOptService.AnonymousClass1 */

                public void onReceive(Context context, Intent intent) {
                    if ("android.intent.action.USER_UNLOCKED".equals(intent.getAction())) {
                        Log.i(BackgroundDexOptService.TAG, "Jobs scheduled JOB_POST_BOOT_UPDATE after user unlock.");
                        js.schedule(new JobInfo.Builder(BackgroundDexOptService.JOB_POST_BOOT_UPDATE, BackgroundDexOptService.sDexoptServiceName).setMinimumLatency(TimeUnit.MINUTES.toMillis(1)).setOverrideDeadline(BackgroundDexOptService.IDLE_OPTIMIZATION_PERIOD).setRequiresDeviceIdle(true).build());
                    }
                }
            }, UserHandle.CURRENT, userFilter, null, null);
            js.schedule(new JobInfo.Builder(JOB_IDLE_OPTIMIZE, sDexoptServiceName).setRequiresDeviceIdle(true).setRequiresCharging(true).setPeriodic(IDLE_OPTIMIZATION_PERIOD).build());
            if (PackageManagerService.DEBUG_DEXOPT) {
                Log.i(TAG, "Jobs scheduled");
            }
        }
    }

    public static void notifyPackageChanged(String packageName) {
        synchronized (sFailedPackageNamesPrimary) {
            sFailedPackageNamesPrimary.remove(packageName);
        }
        synchronized (sFailedPackageNamesSecondary) {
            sFailedPackageNamesSecondary.remove(packageName);
        }
    }

    private int getBatteryLevel() {
        Intent intent = registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        int level = intent.getIntExtra("level", -1);
        int scale = intent.getIntExtra("scale", -1);
        if (!intent.getBooleanExtra("present", true)) {
            return 100;
        }
        if (level < 0 || scale <= 0) {
            return 0;
        }
        return (level * 100) / scale;
    }

    private long getLowStorageThreshold(Context context) {
        long lowThreshold = StorageManager.from(context).getStorageLowBytes(this.mDataDir);
        if (lowThreshold == 0) {
            Log.e(TAG, "Invalid low storage threshold");
        }
        return lowThreshold;
    }

    private boolean runPostBootUpdate(final JobParameters jobParams, final PackageManagerService pm, final ArraySet<String> pkgs) {
        if (this.mExitPostBootUpdate.get()) {
            return false;
        }
        new Thread("BackgroundDexOptService_PostBootUpdate") {
            /* class com.android.server.pm.BackgroundDexOptService.AnonymousClass2 */

            public void run() {
                BackgroundDexOptService.this.postBootUpdate(jobParams, pm, pkgs);
            }
        }.start();
        return true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void postBootUpdate(JobParameters jobParams, PackageManagerService pm, ArraySet<String> pkgs) {
        int lowBatteryThreshold = getResources().getInteger(17694829);
        long lowThreshold = getLowStorageThreshold(this);
        this.mAbortPostBootUpdate.set(false);
        ArraySet<String> updatedPackages = new ArraySet<>();
        Iterator<String> it = pkgs.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String pkg = it.next();
            if (!this.mAbortPostBootUpdate.get()) {
                if (this.mExitPostBootUpdate.get() || getBatteryLevel() < lowBatteryThreshold) {
                    break;
                }
                long usableSpace = this.mDataDir.getUsableSpace();
                if (usableSpace < lowThreshold) {
                    Log.w(TAG, "Aborting background dex opt job due to low storage: " + usableSpace);
                    break;
                }
                if (PackageManagerService.DEBUG_DEXOPT) {
                    Log.i(TAG, "Updating package " + pkg);
                }
                if (pm.performDexOptWithStatus(new DexoptOptions(pkg, 1, 4)) == 1) {
                    updatedPackages.add(pkg);
                }
            } else {
                return;
            }
        }
        notifyPinService(updatedPackages);
        jobFinished(jobParams, false);
    }

    public static void setUserUnlockedOnce() {
        mUserUnlockedOnce = true;
    }

    private boolean runIdleOptimization(final JobParameters jobParams, final PackageManagerService pm, final ArraySet<String> pkgs) {
        new Thread("BackgroundDexOptService_IdleOptimization") {
            /* class com.android.server.pm.BackgroundDexOptService.AnonymousClass3 */

            public void run() {
                Log.i(BackgroundDexOptService.TAG, "BackgroundDexOptService_IdleOptimization begin:");
                boolean abortBgDexopt = false;
                boolean deviceUprading = pm.isDeviceUpgrading();
                Log.w(BackgroundDexOptService.TAG, "Idle optimizations mUserUnlockedOnce" + BackgroundDexOptService.mUserUnlockedOnce + " upgrading: " + deviceUprading);
                if (!BackgroundDexOptService.mUserUnlockedOnce && deviceUprading && BackgroundDexOptService.abortIdleDexoptCounter < 2) {
                    abortBgDexopt = true;
                    BackgroundDexOptService.access$408();
                }
                if (abortBgDexopt) {
                    Log.w(BackgroundDexOptService.TAG, "Idle optimizations aborted because of device not unlocked once");
                    BackgroundDexOptService.this.jobFinished(jobParams, false);
                    return;
                }
                BackgroundDexOptService backgroundDexOptService = BackgroundDexOptService.this;
                if (backgroundDexOptService.idleOptimization(pm, pkgs, backgroundDexOptService) != 2) {
                    Log.w(BackgroundDexOptService.TAG, "Idle optimizations aborted because of space constraints.");
                    Log.i(BackgroundDexOptService.TAG, "BackgroundDexOptService_IdleOptimization end as normal.");
                    BackgroundDexOptService.this.jobFinished(jobParams, false);
                }
            }
        }.start();
        return true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int idleOptimization(PackageManagerService pm, ArraySet<String> pkgs, Context context) {
        Log.i(TAG, "Performing idle optimizations");
        this.mExitPostBootUpdate.set(true);
        this.mAbortIdleOptimization.set(false);
        long lowStorageThreshold = getLowStorageThreshold(context);
        int result = optimizePackages(pm, pkgs, lowStorageThreshold, true);
        if (result == 2 || !supportSecondaryDex()) {
            return result;
        }
        int result2 = reconcileSecondaryDexFiles(pm.getDexManager());
        if (result2 == 2) {
            return result2;
        }
        return optimizePackages(pm, pkgs, lowStorageThreshold, false);
    }

    private long getDirectorySize(File f) {
        long size = 0;
        if (!f.isDirectory()) {
            return f.length();
        }
        for (File file : f.listFiles()) {
            size += getDirectorySize(file);
        }
        return size;
    }

    private long getPackageSize(PackageManagerService pm, String pkg) {
        PackageInfo info = pm.getPackageInfo(pkg, 0, 0);
        if (info == null || info.applicationInfo == null) {
            return 0;
        }
        File path = Paths.get(info.applicationInfo.sourceDir, new String[0]).toFile();
        if (path.isFile()) {
            path = path.getParentFile();
        }
        long size = 0 + getDirectorySize(path);
        if (ArrayUtils.isEmpty(info.applicationInfo.splitSourceDirs)) {
            return size;
        }
        long size2 = size;
        for (String splitSourceDir : info.applicationInfo.splitSourceDirs) {
            File path2 = Paths.get(splitSourceDir, new String[0]).toFile();
            if (path2.isFile()) {
                path2 = path2.getParentFile();
            }
            size2 += getDirectorySize(path2);
        }
        return size2;
    }

    private int optimizePackages(PackageManagerService pm, ArraySet<String> pkgs, long lowStorageThreshold, boolean isForPrimaryDex) {
        boolean dex_opt_performed;
        ArraySet<String> updatedPackages = new ArraySet<>();
        Set<String> unusedPackages = pm.getUnusedPackages(mDowngradeUnusedAppsThresholdInMillis);
        Log.d(TAG, "Unsused Packages " + String.join(",", unusedPackages));
        boolean shouldDowngrade = shouldDowngrade(2 * lowStorageThreshold);
        Log.d(TAG, "Should Downgrade " + shouldDowngrade);
        Log.i(TAG, "BackgroundDexOptService_IdleOptimization begin:");
        Iterator<String> it = pkgs.iterator();
        while (it.hasNext()) {
            String pkg = it.next();
            int abort_code = abortIdleOptimizations(lowStorageThreshold);
            if (abort_code == 2) {
                Log.i(TAG, "BackgroundDexOptService_IdleOptimization abort for quit idle.");
                return abort_code;
            } else if (!abortIdleOptimizationsByThermal()) {
                if (unusedPackages.contains(pkg) && shouldDowngrade) {
                    dex_opt_performed = downgradePackage(pm, pkg, isForPrimaryDex);
                } else if (abort_code != 3) {
                    dex_opt_performed = optimizePackage(pm, pkg, isForPrimaryDex);
                }
                if (dex_opt_performed) {
                    updatedPackages.add(pkg);
                }
            }
        }
        notifyPinService(updatedPackages);
        Log.i(TAG, "BackgroundDexOptService_IdleOptimization end as normal.");
        return 0;
    }

    private boolean downgradePackage(PackageManagerService pm, String pkg, boolean isForPrimaryDex) {
        Log.d(TAG, "Downgrading " + pkg);
        boolean dex_opt_performed = false;
        long package_size_before = getPackageSize(pm, pkg);
        if (!isForPrimaryDex) {
            dex_opt_performed = performDexOptSecondary(pm, pkg, 5, 548);
        } else if (!pm.canHaveOatDir(pkg)) {
            pm.deleteOatArtifactsOfPackage(pkg);
        } else {
            dex_opt_performed = performDexOptPrimary(pm, pkg, 5, 548);
        }
        if (dex_opt_performed) {
            StatsLog.write(128, pkg, package_size_before, getPackageSize(pm, pkg), false);
        }
        return dex_opt_performed;
    }

    private boolean supportSecondaryDex() {
        return SystemProperties.getBoolean("dalvik.vm.dexopt.secondary", false);
    }

    private int reconcileSecondaryDexFiles(DexManager dm) {
        for (String p : dm.getAllPackagesWithSecondaryDexFiles()) {
            if (this.mAbortIdleOptimization.get()) {
                return 2;
            }
            dm.reconcileSecondaryDexFiles(p);
        }
        return 0;
    }

    private boolean optimizePackage(PackageManagerService pm, String pkg, boolean isForPrimaryDex) {
        int reason = 3;
        int dexoptFlags = UsbTerminalTypes.TERMINAL_IN_MIC_ARRAY;
        if (pm.inCptWhiteList(CompatibilityHelper.FORCE_DEXOPT_IN_SPEED, pkg)) {
            reason = 7;
        }
        if (OppoFeatureCache.get(IColorDexMetadataManager.DEFAULT).isNeedToDexOptForce(pkg)) {
            dexoptFlags = 517 | 2;
        }
        if (isForPrimaryDex) {
            return performDexOptPrimary(pm, pkg, reason, dexoptFlags);
        }
        return performDexOptSecondary(pm, pkg, reason, dexoptFlags);
    }

    private boolean performDexOptPrimary(PackageManagerService pm, String pkg, int reason, int dexoptFlags) {
        if (trackPerformDexOpt(pkg, false, new Supplier(pkg, reason, dexoptFlags) {
            /* class com.android.server.pm.$$Lambda$BackgroundDexOptService$KiE2NsUPOYmoSDt9BwEQICZw */
            private final /* synthetic */ String f$1;
            private final /* synthetic */ int f$2;
            private final /* synthetic */ int f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.util.function.Supplier
            public final Object get() {
                return Integer.valueOf(PackageManagerService.this.performDexOptWithStatus(new DexoptOptions(this.f$1, this.f$2, this.f$3)));
            }
        }) == 1) {
            return true;
        }
        return false;
    }

    private boolean performDexOptSecondary(PackageManagerService pm, String pkg, int reason, int dexoptFlags) {
        if (trackPerformDexOpt(pkg, true, new Supplier(new DexoptOptions(pkg, reason, dexoptFlags | 8)) {
            /* class com.android.server.pm.$$Lambda$BackgroundDexOptService$TAsfDUuoxt92xKFoSCfpMUmY2Es */
            private final /* synthetic */ DexoptOptions f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.util.function.Supplier
            public final Object get() {
                return BackgroundDexOptService.lambda$performDexOptSecondary$1(PackageManagerService.this, this.f$1);
            }
        }) == 1) {
            return true;
        }
        return false;
    }

    static /* synthetic */ Integer lambda$performDexOptSecondary$1(PackageManagerService pm, DexoptOptions dexoptOptions) {
        return Integer.valueOf(pm.performDexOpt(dexoptOptions) ? 1 : -1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0015, code lost:
        r1 = r6.get().intValue();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0020, code lost:
        if (r1 == -1) goto L_0x002b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0022, code lost:
        monitor-enter(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r0.remove(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0026, code lost:
        monitor-exit(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x002b, code lost:
        return r1;
     */
    private int trackPerformDexOpt(String pkg, boolean isForPrimaryDex, Supplier<Integer> performDexOptWrapper) {
        ArraySet<String> sFailedPackageNames = isForPrimaryDex ? sFailedPackageNamesPrimary : sFailedPackageNamesSecondary;
        synchronized (sFailedPackageNames) {
            if (sFailedPackageNames.contains(pkg)) {
                return 0;
            }
            sFailedPackageNames.add(pkg);
        }
    }

    private boolean abortIdleOptimizationsByThermal() {
        Context context;
        Intent batteryIntent;
        int temp;
        int mPreHeatThreshold = OppoThermalManager.mPreHeatDexOatThreshold;
        boolean skip = false;
        if (!ActivityThread.inCptWhiteList((int) CompatibilityHelper.BGOPT_THERMAL, "BGOPT_THERMAL") || (context = sysContext) == null || (batteryIntent = context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"))) == null || (temp = batteryIntent.getIntExtra("temperature", -1)) <= -1) {
            return false;
        }
        if (temp >= mPreHeatThreshold) {
            skip = true;
        }
        if (PackageManagerService.DEBUG_DEXOPT) {
            Log.w(TAG, "Aborting " + skip + " temp: " + temp + " : " + mPreHeatThreshold);
        }
        return skip;
    }

    private int abortIdleOptimizations(long lowStorageThreshold) {
        if (this.mAbortIdleOptimization.get()) {
            Log.i(TAG, "BackgroundDexOptService_IdleOptimization abort for quit idle.");
            return 2;
        }
        long usableSpace = this.mDataDir.getUsableSpace();
        if (usableSpace >= lowStorageThreshold) {
            return 1;
        }
        Log.w(TAG, "Aborting background dex opt job due to low storage: " + usableSpace);
        return 3;
    }

    private boolean shouldDowngrade(long lowStorageThresholdForDowngrade) {
        if (this.mDataDir.getUsableSpace() < lowStorageThresholdForDowngrade) {
            return true;
        }
        return false;
    }

    public static boolean runIdleOptimizationsNow(PackageManagerService pm, Context context, List<String> packageNames) {
        ArraySet<String> packagesToOptimize;
        BackgroundDexOptService bdos = new BackgroundDexOptService();
        if (packageNames == null) {
            packagesToOptimize = pm.getOptimizablePackages();
        } else {
            packagesToOptimize = new ArraySet<>(packageNames);
        }
        return bdos.idleOptimization(pm, packagesToOptimize, context) == 0;
    }

    /* JADX INFO: Multiple debug info for r1v5 boolean: [D('result' boolean), D('skipPost' boolean)] */
    public boolean onStartJob(JobParameters params) {
        if (PackageManagerService.DEBUG_DEXOPT) {
            Log.i(TAG, "onStartJob");
        }
        PackageManagerService pm = (PackageManagerService) ServiceManager.getService(Settings.ATTR_PACKAGE);
        if (pm.isStorageLow()) {
            if (PackageManagerService.DEBUG_DEXOPT) {
                Log.i(TAG, "Low storage, skipping this run");
            }
            return false;
        }
        ArraySet<String> pkgs = pm.getOptimizablePackages();
        if (pkgs.isEmpty()) {
            if (PackageManagerService.DEBUG_DEXOPT) {
                Log.i(TAG, "No packages to optimize");
            }
            return false;
        } else if (params.getJobId() != JOB_POST_BOOT_UPDATE) {
            return runIdleOptimization(params, pm, pkgs);
        } else {
            if (pm.inPmsWhiteList(CompatibilityHelper.RUN_SCORE_BLACK_LIST, TemperatureProvider.SWITCH_ON, new ArrayList<>(Arrays.asList(TemperatureProvider.SWITCH_ON)))) {
                return false;
            }
            return runPostBootUpdate(params, pm, pkgs);
        }
    }

    public boolean onStopJob(JobParameters params) {
        if (PackageManagerService.DEBUG_DEXOPT) {
            Log.i(TAG, "onStopJob");
        }
        if (params.getJobId() == JOB_POST_BOOT_UPDATE) {
            this.mAbortPostBootUpdate.set(true);
            return false;
        }
        this.mAbortIdleOptimization.set(true);
        return true;
    }

    private void notifyPinService(ArraySet<String> updatedPackages) {
        PinnerService pinnerService = (PinnerService) LocalServices.getService(PinnerService.class);
        if (pinnerService != null) {
            Log.i(TAG, "Pinning optimized code " + updatedPackages);
            pinnerService.update(updatedPackages, false);
        }
    }

    private static long getDowngradeUnusedAppsThresholdInMillis() {
        String sysPropValue = SystemProperties.get("pm.dexopt.downgrade_after_inactive_days");
        if (sysPropValue != null && !sysPropValue.isEmpty()) {
            return TimeUnit.DAYS.toMillis(Long.parseLong(sysPropValue));
        }
        Log.w(TAG, "SysProp pm.dexopt.downgrade_after_inactive_days not set");
        return JobStatus.NO_LATEST_RUNTIME;
    }

    private static boolean isBackgroundDexoptDisabled() {
        return SystemProperties.getBoolean("pm.dexopt.disable_bg_dexopt", false);
    }
}
