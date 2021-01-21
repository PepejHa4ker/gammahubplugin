package com.gamma.hub.model;

import com.pepej.papi.config.ConfigFactory;
import com.pepej.papi.config.configurate.ConfigurationNode;
import lombok.Data;
import lombok.Getter;


import java.io.File;
import java.util.HashSet;
import java.util.Set;

@Data
public class Head {

    @Getter
    private static final Set<Head> heads = new HashSet<>();
    private final String name;
    private final String texture;


    public static void load(File file) {
        ConfigurationNode config = ConfigFactory.gson().load(file);
        for (ConfigurationNode node : config.getChildrenList()) {
            Head head = new Head(node.getNode("name").getString(), node.getNode("texture").getString());
            heads.add(head);
        }
    }


    public static Head findByName(String name) {
        return heads.stream().filter(head -> head.getName().equalsIgnoreCase(name)).findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
