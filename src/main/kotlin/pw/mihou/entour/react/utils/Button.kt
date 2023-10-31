package pw.mihou.entour.react.utils

import org.javacord.api.entity.user.User
import org.javacord.api.event.interaction.ButtonClickEvent

internal inline fun ButtonClickEvent.ack(invoker: User?, continuation: () -> Unit) {
    this.buttonInteraction.acknowledge()
    if (invoker != null && this.buttonInteraction.user.id != invoker.id) {
        return
    }
    continuation()
}