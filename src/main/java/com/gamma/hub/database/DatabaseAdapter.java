package com.gamma.hub.database;

import com.gamma.hub.GammaHubPlugin;
import com.gamma.hub.model.DonatInfo;
import com.pepej.papi.checker.checker.nullness.qual.NonNull;
import com.pepej.papi.utils.UndashedUuids;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseAdapter {
    private DatabaseAdapter() {}
    public static final DatabaseAdapter INSTANCE = new DatabaseAdapter();


    @Synchronized
    @SneakyThrows
    public void insertGiftData(@NonNull final Player collector, @NonNull final long time, final DonatInfo.Type type ) {
        @Cleanup final Connection connection = GammaHubPlugin.getInstance().getDatabaseManager().getConnection();
        final PreparedStatement st = connection.prepareStatement("INSERT IGNORE INTO hub (id, time, type) VALUES (?, ?, ?);");
                st.setString(1, UndashedUuids.toString(collector.getUniqueId()));
        st.setLong(2, time);
        st.setString(3, type.name());

        st.execute();

    }
    @Synchronized
    @SneakyThrows
    public void updateGiftData(@NonNull final Player collector, @NonNull final long time,  final DonatInfo.Type type ) {
        @Cleanup final Connection connection =GammaHubPlugin.getInstance().getDatabaseManager().getConnection();
        final PreparedStatement st = connection.prepareStatement("UPDATE IGNORE hub SET time = ? WHERE id = ? AND type = ?;");
        st.setLong(1, time);
        st.setString(2, UndashedUuids.toString(collector.getUniqueId()));
        st.setString(3, type.name());
        st.execute();

    }
    @Synchronized
    @SneakyThrows
    public boolean userExists(@NonNull final Player collector, final DonatInfo.Type type ) {
        @Cleanup final Connection connection =GammaHubPlugin.getInstance().getDatabaseManager().getConnection();
        final PreparedStatement st = connection.prepareStatement("SELECT * FROM hub WHERE id = ? AND type = ?;");
        st.setString(1, UndashedUuids.toString(collector.getUniqueId()));
        st.setString(2, type.name());
        ResultSet rs = st.executeQuery();
        return rs.next();

    }
    @Synchronized
    @SneakyThrows
    public long getLastTakeDate(@NonNull final Player collector, final DonatInfo.Type type ) {
        @Cleanup final Connection connection = GammaHubPlugin.getInstance().getDatabaseManager().getConnection();
        final PreparedStatement st = connection.prepareStatement("SELECT * FROM hub WHERE id = ? AND type = ?;");
        st.setString(1, UndashedUuids.toString(collector.getUniqueId()));
        st.setString(2, type.name());
        ResultSet rs = st.executeQuery();
        return rs.getLong("time");


    }

}
