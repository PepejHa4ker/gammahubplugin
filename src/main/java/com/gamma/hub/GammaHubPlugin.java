package com.gamma.hub;

import com.pepej.papi.ap.Plugin;
import com.pepej.papi.ap.PluginDependency;
import com.pepej.papi.plugin.PapiJavaPlugin;

@Plugin(name = "GammaHub", version = "1.0.0", depends = @PluginDependency("papi"))
public class GammaHubPlugin extends PapiJavaPlugin {
    @Override
    protected void onPluginLoad() {

    }

    @Override
    protected void onPluginDisable() {
    }

    @Override
    protected void onPluginEnable() {
    }
}
