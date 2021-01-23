package com.gamma.hub;

import com.gamma.hub.commands.GCommands;
import com.gamma.hub.events.EventListener;
import com.gamma.hub.managers.UserManager;
import com.gamma.hub.model.Head;
import com.gamma.hub.model.ServerInfo;
import com.pepej.papi.adventure.platform.bukkit.BukkitAudiences;
import com.pepej.papi.ap.Plugin;
import com.pepej.papi.ap.PluginDependency;
import com.pepej.papi.plugin.PapiJavaPlugin;
import lombok.Getter;


@Plugin(name = "HiveMCHub", version = "1.0.0", depends = @PluginDependency("papi"))
@Getter
public class GammaHubPlugin extends PapiJavaPlugin {

    private BukkitAudiences audiences;

    private static GammaHubPlugin instance;

    public static GammaHubPlugin getInstance() {
        return instance;
    }

    private UserManager userManager;


    @Override
    protected void onPluginLoad() {
        instance = this;

    }

    @Override
    protected void onPluginDisable() {
    }

    @Override
    protected void onPluginEnable() {
        Head.load(getBundledFile("heads.json"));
        ServerInfo.load(getBundledFile("servers.json"));
        bindModule(new GCommands());
        this.userManager = bindModule(new UserManager());
        this.audiences = bind(BukkitAudiences.create(this));
        bindModule(new EventListener());

    }
}
