package com.gamma.hub;

import com.gamma.hub.commands.GCommands;
import com.gamma.hub.database.DatabaseManager;
import com.gamma.hub.events.EventListener;
import com.gamma.hub.managers.UserManager;
import com.gamma.hub.model.Head;
import com.gamma.hub.model.ServerInfo;
import com.gamma.hub.npc.NpcManager;
import com.pepej.papi.adventure.platform.bukkit.BukkitAudiences;
import com.pepej.papi.ap.Plugin;
import com.pepej.papi.ap.PluginDependency;
import com.pepej.papi.maven.MavenLibrary;
import com.pepej.papi.plugin.PapiJavaPlugin;
import lombok.Getter;

@MavenLibrary("com.zaxxer:HikariCP:3.3.1")
@Plugin(name = "GammaHub", version = "1.0.0", depends = @PluginDependency("papi"))
@Getter
public class GammaHubPlugin extends PapiJavaPlugin {

    private BukkitAudiences audiences;

    private static GammaHubPlugin instance;

    public static GammaHubPlugin getInstance() {
        return instance;
    }

    private UserManager userManager;

    private DatabaseManager databaseManager;


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
        this.databaseManager = bind(new DatabaseManager());
        this.audiences = bind(BukkitAudiences.create(this));
        bindModule(new EventListener());
        bindModule(new NpcManager());

    }
}
