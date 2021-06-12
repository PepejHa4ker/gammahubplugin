package com.gamma.hub.model;

import com.google.common.collect.Lists;
import com.pepej.papi.config.ConfigFactory;
import com.pepej.papi.config.ConfigurationNode;
import com.pepej.papi.config.objectmapping.ConfigSerializable;
import com.pepej.papi.config.objectmapping.meta.Setting;
import com.pepej.papi.random.Weighted;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.checkerframework.checker.index.qual.NonNegative;

import java.io.File;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigSerializable
public class DonatInfo {

    @Setting String name;
    @Setting Material material;
    @Setting Type type;
    @Setting String[] description;

    @Getter
    private static final List<DonatInfo> donats = Lists.newArrayList();

    public DonatInfo(String name, Material material, Type type, String[] description) {
        this.name = name;
        this.material = material;
        this.type = type;
        this.description = description;
    }

    @SneakyThrows
    public static void load(File file) {
        ConfigurationNode config = ConfigFactory.gson().load(file);
        final List<DonatInfo> donatInfos = config.getList(DonatInfo.class);
        if (donatInfos == null) {
            return;
        }
        donats.addAll(donatInfos);
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
