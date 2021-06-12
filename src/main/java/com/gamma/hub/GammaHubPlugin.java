package com.gamma.hub;

import com.gamma.hub.commands.GCommands;
import com.gamma.hub.database.DatabaseAdapter;
import com.gamma.hub.events.EventListener;
import com.gamma.hub.managers.UserManager;
import com.gamma.hub.model.DonatInfo;
import com.gamma.hub.model.Head;
import com.gamma.hub.model.ServerInfo;
import com.gamma.hub.npc.NpcManager;
import com.pepej.papi.Papi;
import com.pepej.papi.adventure.platform.bukkit.BukkitAudiences;
import com.pepej.papi.ap.Plugin;
import com.pepej.papi.ap.PluginDependency;
import com.pepej.papi.dependency.Dependency;
import com.pepej.papi.plugin.PapiJavaPlugin;
import com.pepej.papi.services.Services;
import com.pepej.papi.sql.Sql;
import lombok.Getter;

@Dependency("com.zaxxer:HikariCP:3.3.1")
@Plugin(name = "GammaHub", version = "1.0.0", hardDepends = { "papi", "papi-sql"})
@Getter
public class GammaHubPlugin extends PapiJavaPlugin {

    private BukkitAudiences audiences;

    private static GammaHubPlugin instance;

    public static GammaHubPlugin getInstance() {
        return instance;
    }

    private UserManager userManager;



    @Override
    public void onPluginLoad() {
        instance = this;

    }


    @Override
    public void onPluginEnable() {
        provideService(DatabaseAdapter.class, new DatabaseAdapter(getService(Sql.class)));
        Head.load(getBundledFile("heads.json"));
        ServerInfo.load(getBundledFile("servers.json"));
        DonatInfo.load(getBundledFile("donats.json"));
        final EventListener module = new EventListener();
        Papi.server().getPluginManager().registerEvents(module, this);
        bindModule(module);
        bindModule(new GCommands());
        this.userManager = bindModule(new UserManager());
        this.audiences = bind(BukkitAudiences.create(this));
        bindModule(new NpcManager());
        provideService(UserManager.class, userManager);
        provideService(BukkitAudiences.class, audiences);


    }
}
