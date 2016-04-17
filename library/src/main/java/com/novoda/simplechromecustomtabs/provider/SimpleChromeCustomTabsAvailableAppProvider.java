package com.novoda.simplechromecustomtabs.provider;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SimpleChromeCustomTabsAvailableAppProvider implements AvailableAppProvider {

    private final BestPackageFinder bestPackageFinder;
    private final Executor executor;

    SimpleChromeCustomTabsAvailableAppProvider(BestPackageFinder bestPackageFinder, Executor executor) {
        this.bestPackageFinder = bestPackageFinder;
        this.executor = executor;
    }

    public static AvailableAppProvider newInstance() {
        BestPackageFinder bestPackageFinder = BestPackageFinder.newInstance();
        Executor executor = Executors.newSingleThreadExecutor();
        return new SimpleChromeCustomTabsAvailableAppProvider(bestPackageFinder, executor);
    }

    @Override
    @WorkerThread
    public void findBestPackage(@NonNull final PackageFoundCallback packageFoundCallback) {
        Runnable findBestPackageTask = findBestPackageTask(packageFoundCallback);
        executor.execute(findBestPackageTask);
    }

    private Runnable findBestPackageTask(final PackageFoundCallback packageFoundCallback) {
        return new Runnable() {
            @Override
            public void run() {
                String packageName = bestPackageFinder.findBestPackage();
                if (TextUtils.isEmpty(packageName)) {
                    packageFoundCallback.onPackageNotFound();
                } else {
                    packageFoundCallback.onPackageFound(packageName);
                }
            }
        };
    }

    public interface PackageFoundCallback {
        void onPackageFound(String packageName);

        void onPackageNotFound();
    }

}
