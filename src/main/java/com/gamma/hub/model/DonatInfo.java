package com.gamma.hub.model;

import com.pepej.papi.checker.checker.index.qual.NonNegative;
import com.pepej.papi.config.ConfigFactory;
import com.pepej.papi.config.configurate.ConfigurationNode;
import com.pepej.papi.google.common.collect.Lists;
import com.pepej.papi.random.Weighted;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Material;

import java.io.File;
import java.util.List;

@Data
public class DonatInfo {

    private final String name;
    private final Material material;
    private final String[] description;
    private final Type type;

    @Getter
    private static final List<DonatInfo> donats = Lists.newArrayList();

    public DonatInfo(String name, Material material, String[] description, Type type) {
        this.name = name;
        this.material = material;
        this.description = description;
        this.type = type;
    }

    public static void load(File file) {
        ConfigurationNode config = ConfigFactory.gson().load(file);
        config.getChildrenList().forEach(server -> {
            DonatInfo loaded = new DonatInfo(
                    server.getNode("name").getString(),
                    Material.valueOf(server.getNode("material").getString()),
                    server.getNode("description").getChildrenList()
                            .stream()
                            .map(ConfigurationNode::getString)
                            .toArray(String[]::new),
                    Type.valueOf(server.getNode("type").getString()));


            donats.add(loaded);

        });
    }

        public enum Type implements Weighted {
            VIP {
                @Override
                public @NonNegative double getWeight() {
                    return 2;
                }
            },
            PREMIUM {
                @Override
                public @NonNegative double getWeight() {
                    return 3;
                }
            },
            GRAND {
                @Override
                public @NonNegative double getWeight() {
                    return 4;
                }
            },
            DEFAULT {
                @Override
                public @NonNegative double getWeight() {
                    return 1;
                }
            },
            STAFF {
                @Override
                public @NonNegative double getWeight() {
                    return 5;
                }
            };
        }
    }
