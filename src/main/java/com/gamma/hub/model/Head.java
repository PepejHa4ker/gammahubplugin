package com.gamma.hub.model;

import com.pepej.papi.config.ConfigFactory;
import com.pepej.papi.config.ConfigurationNode;
import com.pepej.papi.config.objectmapping.ConfigSerializable;
import com.pepej.papi.config.objectmapping.meta.Required;
import com.pepej.papi.config.objectmapping.meta.Setting;
import lombok.*;
import lombok.experimental.FieldDefaults;


import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Head {

    @Getter
    private static final Set<Head> heads = new HashSet<>();
    private final String name;
    private final String texture;


    @SneakyThrows
    public static void load(File file) {
        ConfigurationNode config = ConfigFactory.gson().load(file);
        final List<HeadConfig> headConfigs = config.getList(HeadConfig.class);
        if (headConfigs == null) return;
        for (HeadConfig headConfig : headConfigs) {
            heads.add(new Head(headConfig.getName(), headConfig.getTexture()));
        }
    }


    public static Head findByName(String name) {
        return heads.stream().filter(head -> head.getName().equalsIgnoreCase(name)).findFirst().orElseThrow(IllegalArgumentException::new);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @ConfigSerializable
    public static class HeadConfig {

        @Setting
        @Required
        String name;

        @Setting
        @Required
        String texture;
    }
}
