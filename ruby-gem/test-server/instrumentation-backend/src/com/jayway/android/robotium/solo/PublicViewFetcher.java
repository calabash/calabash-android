package com.jayway.android.robotium.solo;

import android.app.Activity;
import android.app.Instrumentation;

public class PublicViewFetcher extends ViewFetcher {

    public PublicViewFetcher(Instrumentation instrumentation, Activity activity) {
        super(new ActivityUtils(instrumentation, activity, new Sleeper()));
    }
}
