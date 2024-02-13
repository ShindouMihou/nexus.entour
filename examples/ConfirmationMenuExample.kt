@file:Suppress("LocalVariableName")
package pw.mihou.entour.test

import org.javacord.api.interaction.Interaction
import pw.mihou.entour.react.components.ConfirmationMenu
import pw.mihou.entour.react.components.ConfirmationMenuState
import pw.mihou.entour.react.components.createConfirmationMenuState
import pw.mihou.nexus.features.command.facade.NexusCommandEvent
import pw.mihou.nexus.features.command.facade.NexusHandler
import pw.mihou.reakt.adapters.R
import pw.mihou.reakt.elements.Embed
import pw.mihou.reakt.elements.PrimaryButton
import pw.mihou.reakt.elements.SecondaryButton
import kotlin.time.Duration.Companion.minutes

class ConfirmationMenuExample: NexusHandler {
    val name = "buy"
    val description = "This does something really risky!"
    override fun onEvent(event: NexusCommandEvent) {
        event.interaction.R(false) {
            val confirmationStatusDelegate = createConfirmationMenuState
            val confirmationStatus by confirmationStatusDelegate

            render {
                when (confirmationStatus) {
                    ConfirmationMenuState.PENDING ->  ConfirmationMenu(event.user, confirmationStatusDelegate) {
                        // Time limit defines how much time before the confirmation menu actually expires and is set to cancelled.
                        // By default, it is never, which means it will never set to cancel, and that isn't recommended.
                        timeLimit = 1.minutes

                        // This defines the message to be sent to ask for confirmation of the user. You can theoretically add buttons and
                        // other elements here as this is just asking for a React.Component, but it is recommended to just define the message
                        // itself here.
                        Message {
                            Embed {
                                Body {
                                    this append p("Are you sure you want to proceed with doing this action. Please note that this is an " +
                                            "irreversible action.")
                                }
                                Color(java.awt.Color.YELLOW)
                            }
                        }

                        // When defining the buttons, remember to always use the action parameter as it is the main thing that
                        // keeps the Confirmation Menu working.
                        ConfirmationButton { action -> PrimaryButton(label = "Confirm") { action(it) } }
                        CancelButton { action -> SecondaryButton(label = "Cancel") { action(it) } }
                    }
                    ConfirmationMenuState.CANCELLED -> {
                        Embed {
                            Body {
                                this append p("You have opt-ed to cancel. Nothing has been changed and no action has been performed.")
                            }
                            Color(java.awt.Color.RED)
                        }
                    }
                    ConfirmationMenuState.CONFIRMED -> {
                        Embed {
                            Body {
                                this append p("You have confirmed to perform the action. We are now making irreversible changes, this may take some time.")
                            }
                            Color(java.awt.Color.GREEN)
                        }
                    }
                }
            }
        }
    }
}