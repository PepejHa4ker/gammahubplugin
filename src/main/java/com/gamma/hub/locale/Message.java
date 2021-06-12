package com.gamma.hub.locale;

import com.gamma.hub.GammaHubPlugin;
import com.pepej.papi.adventure.text.Component;
import com.pepej.papi.adventure.text.ComponentLike;
import com.pepej.papi.adventure.text.TextComponent;
import com.pepej.papi.adventure.text.event.ClickEvent;
import com.pepej.papi.adventure.text.event.HoverEvent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.pepej.papi.adventure.text.Component.*;
import static com.pepej.papi.adventure.text.format.NamedTextColor.*;

public interface Message {


    Component OPEN_BRACKET = text('(');
    Component CLOSE_BRACKET = text(')');
    Component FULL_STOP = text('.');

    Component PREFIX_COMPONENT = text()
            .color(GRAY)
            .append(text('['))
            .append(text("Gamma", AQUA))
            .append(text(']'))
            .build();

    static TextComponent prefixed(ComponentLike component) {
        return text()
                .append(PREFIX_COMPONENT)
                .append(space())
                .append(component)
                .build();
    }

    Args1<Boolean> PLAYERS_TOGGLE = state -> prefixed(text()
            .color(GRAY)
            .append(text("Вы успешно")
                    .append(space())
                    .append(state ? text("скрыли", RED) : text("показали", GREEN))
            )
            .append(space())
            .append(text("всех игроков"))
            .append(space())
            .append(OPEN_BRACKET
                    .append(text("Клик"))
                    .append(CLOSE_BRACKET)
            .clickEvent(ClickEvent.runCommand("/hideplayers")))
            .hoverEvent(HoverEvent.showText(text("Кликните, чтобы", AQUA)
                    .append(space())
                    .append(state ? text("скрыть", RED) : text("показать", GREEN))
                    .append(space())
                    .append(text("всех игроков"))

            )));
    Args1<Player> JOIN = player -> prefixed(text()
            .color(GRAY)
            .append(text("Добро пожаловать на наш сервер,"))
            .append(space())
            .append(text(player.getName(), GREEN))
            .hoverEvent(HoverEvent.showText(text("Рады тебя видеть!", AQUA)))

    );

    Args1<Boolean> STACKING_TOGGLE = state -> prefixed(text()
            .color(GRAY)
            .append(text("Вы успешно")
                    .append(space())
                    .append(state ? text("включили", GREEN) : text("выключили", RED))
            )
            .append(space())
            .append(text("стакинг"))
            .append(space())
            .append(text("(Клик)")
                    .hoverEvent(HoverEvent.showText(text("Кликните, чтобы", AQUA)
                            .append(space())
                            .append(state ? text("включить", GREEN) : text("выключить", RED))
                            .append(space())
                            .append(text("стакинг"))

                    ))
                    .clickEvent(ClickEvent.runCommand("/stacking")))
    );

    Args1<String> NO_PERMISSION_MESSAGE = permission -> prefixed(text()
            .color(RED)
            .append(text("У Вас недостаточно прав"))
            .hoverEvent(HoverEvent.showText(text(permission, AQUA)))
    );

    Args0 BONUS_COOLDOWN_MESSAGE = () -> prefixed(text()
            .color(RED)
            .append(text("Бонус ещё не созрел"))
    );

    @FunctionalInterface
    interface Args0 {
        Component build();

        default void send(CommandSender sender) {
            GammaHubPlugin.getInstance().getAudiences().sender(sender).sendMessage(build());
        }
    }

    @FunctionalInterface
    interface Args1<A0> {
        Component build(A0 arg0);

        default void send(CommandSender sender, A0 arg0) {
            GammaHubPlugin.getInstance().getAudiences().sender(sender).sendMessage(build(arg0));
        }
    }

    @FunctionalInterface
    interface Args2<A0, A1> {
        Component build(A0 arg0, A1 arg1);

        default void send(CommandSender sender, A0 arg0, A1 arg1) {
            GammaHubPlugin.getInstance().getAudiences().sender(sender).sendMessage(build(arg0, arg1));
        }
    }

    @FunctionalInterface
    interface Args3<A0, A1, A2> {
        Component build(A0 arg0, A1 arg1, A2 arg2);

        default void send(CommandSender sender, A0 arg0, A1 arg1, A2 arg2) {
            GammaHubPlugin.getInstance().getAudiences().sender(sender).sendMessage(build(arg0, arg1, arg2));
        }
    }

    @FunctionalInterface
    interface Args4<A0, A1, A2, A3> {
        Component build(A0 arg0, A1 arg1, A2 arg2, A3 arg3);

        default void send(CommandSender sender, A0 arg0, A1 arg1, A2 arg2, A3 arg3) {
            GammaHubPlugin.getInstance().getAudiences().sender(sender).sendMessage(build(arg0, arg1, arg2, arg3));
        }
    }

    @FunctionalInterface
    interface Args5<A0, A1, A2, A3, A4> {
        Component build(A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4);

        default void send(CommandSender sender, A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4) {
            GammaHubPlugin.getInstance().getAudiences().sender(sender).sendMessage(build(arg0, arg1, arg2, arg3, arg4));
        }
    }

    @FunctionalInterface
    interface Args6<A0, A1, A2, A3, A4, A5> {
        Component build(A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5);

        default void send(CommandSender sender, A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5) {
            GammaHubPlugin.getInstance().getAudiences().sender(sender).sendMessage(build(arg0, arg1, arg2, arg3, arg4, arg5));
        }
    }

}
