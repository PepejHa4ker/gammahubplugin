package com.gamma.hub.database;

import com.pepej.papi.config.objectmapping.ConfigSerializable;
import com.pepej.papi.config.objectmapping.meta.Setting;
import com.pepej.papi.promise.Promise;
import com.pepej.papi.scheduler.Schedulers;
import com.pepej.papi.sql.Sql;
import com.pepej.papi.sql.batch.BatchBuilder;
import com.pepej.papi.sql.plugin.PapiSqlBatchBuilder;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.lang.System.out;


public class DatabaseAdapter {

    private final Sql sql;

    public DatabaseAdapter(Sql sql) {
        this.sql = sql;
    }

    public void insertJoinData(Player user) {
        sql.executeAsync("INSERT INTO t_joins (userId, username, join_ts, ip) VALUES (?, ?, ?, ?);", st -> {
            st.setString(1, user.getUniqueId().toString());
            st.setString(2, user.getName());
            st.setLong(3, System.currentTimeMillis());
            st.setString(4, user.getAddress().getHostString());
        });
    }

    public Promise<List<JoinData>> fetchJoinData(Player player) {
        return sql.queryAsync("SELECT * FROM t_joins WHERE userId = ?", st -> st.setString(1, player.getUniqueId().toString()), rs -> {
            List<JoinData> returnList = new ArrayList<>();
            while (rs.next()) {
                returnList.add(
                        JoinData.builder()
                                .userId(UUID.fromString(rs.getString("userId")))
                                .username(rs.getString("username"))
                                .joinTs(rs.getLong("join_ts"))
                                .ip(rs.getString("ip"))
                                .build()

                );
            }
            return returnList;
        }).thenApplyAsync(Optional::get);

    }

    public Promise<List<ServerJoinData>> fetchServerJoinData(String serverId) {
        return sql.queryAsync("SELECT * FROM t_servers WHERE serverName = ?", st -> st.setString(1, serverId), rs -> {
            List<ServerJoinData> returnList = new ArrayList<>();
            while (rs.next()) {
                returnList.add(
                        ServerJoinData.builder()
                                .userId(UUID.fromString(rs.getString("userId")))
                                .username(rs.getString("username"))
                                .joinTs(rs.getLong("join_ts"))
                                .serverName(rs.getString("serverName"))
                                .build()

                );
            }
            return returnList;
        }).thenApplyAsync(Optional::get);

    }

    public void insertServerData(Player user, String server) {
        sql.executeAsync("INSERT INTO t_servers (userId, username, serverName, join_ts) VALUES (?, ?, ?, ?);", st -> {
            st.setString(1, user.getUniqueId().toString());
            st.setString(2, user.getName());
            st.setString(3, server);
            st.setLong(4, System.currentTimeMillis());

        });
    }


        @Value
        @Builder
        @FieldDefaults(level = AccessLevel.PRIVATE)
        public static class JoinData {
            UUID userId;
            String username;
            long joinTs;
            String ip;
        }

    @Value
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class ServerJoinData {
        UUID userId;
        String username;
        long joinTs;
        String serverName;
    }


    }
