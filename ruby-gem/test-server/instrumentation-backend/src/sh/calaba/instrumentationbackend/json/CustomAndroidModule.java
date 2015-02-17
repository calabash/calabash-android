package sh.calaba.instrumentationbackend.json;

import android.content.ComponentName;
import android.content.Intent;

import sh.calaba.instrumentationbackend.intenthook.IntentHook;
import sh.calaba.instrumentationbackend.json.requests.IntentHookRequest;
import sh.calaba.org.codehaus.jackson.Version;
import sh.calaba.org.codehaus.jackson.map.module.SimpleModule;

public class CustomAndroidModule extends SimpleModule {
    public CustomAndroidModule() {
        super("CustomAndroidModule", new Version(0, 1, 0, null));
        addSerializer(Intent.class, new IntentSerializer());
        addDeserializer(Intent.class, new IntentDeserializer());
        addSerializer(ComponentName.class, new ComponentNameSerializer());
        addDeserializer(ComponentName.class, new ComponentNameDeserializer());
        addDeserializer(IntentHookRequest.class, new IntentHookRequestDeserializer());
    }
}
