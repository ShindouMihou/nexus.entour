@file:Suppress("LocalVariableName", "FunctionName")

package pw.mihou.entour.react.hooks

import org.javacord.api.entity.user.User
import org.javacord.api.event.interaction.ButtonClickEvent
import pw.mihou.entour.react.utils.ack
import pw.mihou.entour.react.utils.launch
import pw.mihou.nexus.configuration.modules.Cancellable
import pw.mihou.nexus.features.react.React
import pw.mihou.nexus.features.react.ReactComponent
import pw.mihou.nexus.features.react.elements.Embed
import pw.mihou.nexus.features.react.elements.PrimaryButton
import pw.mihou.nexus.features.react.elements.SecondaryButton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

fun React.useConfirmationMenu(invoker: User, builder: ConfirmationMenuBuilder.() -> Unit): ConfirmationMenu {
    var job: Cancellable? = null
    val config = ConfirmationMenuBuilder()
    builder(config)

    val state = writable(ConfirmationMenuState.PENDING)
    val component: ReactComponent = {
        config.message(this)
        config.confirmationButton(this) {
            it.ack(invoker) {
                state.set(ConfirmationMenuState.CONFIRMED)
            }
        }
        config.cancelButton(this) {
            it.ack(invoker) {
                state.set(ConfirmationMenuState.CANCELLED)
            }
        }
    }

    onRender {
        job?.cancel(true)
        if (state.get() == ConfirmationMenuState.PENDING && config.timeLimit != null) {
            job = launch.scheduler.launch(config.timeLimit!!.inWholeMilliseconds) {
                state.set(ConfirmationMenuState.CANCELLED)
                job = null
            }
        }
    }

    return ConfirmationMenu(state, component)
}

enum class ConfirmationMenuState {
    CONFIRMED, PENDING, CANCELLED
}
data class ConfirmationMenu(val state: React.Writable<ConfirmationMenuState>, val component: ReactComponent)

typealias ConfirmationMenuAction = (ev: ButtonClickEvent) -> Unit
typealias ConfirmationMenuButton = React.Component.(action: ConfirmationMenuAction) -> Unit
class ConfirmationMenuBuilder internal constructor() {
    internal var message: ReactComponent = {}
    internal var confirmationButton: ConfirmationMenuButton = { action ->
        PrimaryButton(label = "Confirm") { action(it) }
    }
    internal var cancelButton: ConfirmationMenuButton = { action ->
        SecondaryButton(label = "Cancel") { action(it) }
    }

    /**
     * Defines the time limit of no action before the state is changed to [ConfirmationMenuState.CANCELLED].
     */
    var timeLimit: Duration? = 1.minutes

    /**
     * Defines the message to be sent to ask for confirmation from the user, this can be
     * an [Embed], a Text or even Buttons as long as it's a proper [React.Component].
     *
     * @param component the component to render during [ConfirmationMenuState.PENDING] state.
     */
    fun Message(component: ReactComponent) {
        message = component
    }

    /**
     * Defines the button to use for the confirmation button. Remember to use the action provided as a
     * parameter for the button to function properly.
     * @param component the component to render.
     */
    fun ConfirmationButton(component: ConfirmationMenuButton) {
        confirmationButton = component
    }

    /**
     * Defines the button to use for the cancel button. Remember to use the action provided as a
     * parameter for the button to function properly.
     * @param component the component to render.
     */
    fun CancelButton(component: ConfirmationMenuButton) {
        cancelButton = component
    }
}