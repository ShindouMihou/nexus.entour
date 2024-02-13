@file:Suppress("LocalVariableName", "FunctionName", "Unused")

package pw.mihou.entour.react.components

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.javacord.api.entity.user.User
import org.javacord.api.event.interaction.ButtonClickEvent
import pw.mihou.entour.react.utils.ack
import pw.mihou.reakt.ReactDocument
import pw.mihou.reakt.Reakt
import pw.mihou.reakt.elements.PrimaryButton
import pw.mihou.reakt.elements.SecondaryButton
import pw.mihou.reakt.utils.coroutine
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

typealias ConfirmationMenuConstructor = ConfirmationMenuBuilder.() -> Unit
val Reakt.createConfirmationMenuState get() = writable(ConfirmationMenuState.PENDING)
fun Reakt.Document.ConfirmationMenu(invoker: User, state: Reakt.Writable<ConfirmationMenuState>, builder: ConfirmationMenuConstructor) =
    @Suppress("NAME_SHADOWING")
    component("pw.mihou.entour.react.ConfirmationMenu") {
        val invoker = ensureProp<User>("invoker")
        val state = writableProp<ConfirmationMenuState>("state")
        val builder = ensureProp<ConfirmationMenuConstructor>("builder")

        val config = ConfirmationMenuBuilder()
        builder(config)

        var job by session.writable<Job?>("job", null)
        onAfterMount {
            job?.cancel()
            if (state.get() == ConfirmationMenuState.PENDING && config.timeLimit != null) {
                job = coroutine {
                    delay(config.timeLimit!!.inWholeMilliseconds + 250) // potential lag delay (250ms)
                    state set ConfirmationMenuState.CANCELLED
                    job = null
                }
            }
        }

        render {
            config.message(this)
            config.confirmationButton(this) {
                it.ack(invoker) {
                    state set ConfirmationMenuState.CONFIRMED
                }
            }
            config.cancelButton(this) {
                it.ack(invoker) {
                    state set ConfirmationMenuState.CANCELLED
                }
            }
        }
    }("invoker" to invoker, "state" to state, "builder" to builder)

enum class ConfirmationMenuState {
    CONFIRMED, PENDING, CANCELLED
}
typealias ConfirmationMenuAction = (ev: ButtonClickEvent) -> Unit
typealias ConfirmationMenuButton = Reakt.Document.(action: ConfirmationMenuAction) -> Unit
class ConfirmationMenuBuilder internal constructor() {
    internal var message: ReactDocument = {}
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
     * an Embed, a Text or even Buttons as long as it's a proper [ReactDocument].
     *
     * @param component the component to render during [ConfirmationMenuState.PENDING] state.
     */
    fun Message(component: ReactDocument) {
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