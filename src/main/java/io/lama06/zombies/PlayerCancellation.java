package io.lama06.zombies;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;

public final class PlayerCancellation {
    public static final Component CANCEL_MESSAGE = Component.text("> Click here to cancel <")
            .color(NamedTextColor.RED)
            .clickEvent(ClickEvent.runCommand("/zombies cancel"));
}
