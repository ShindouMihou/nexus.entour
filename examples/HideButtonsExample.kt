import pw.mihou.entour.react.hooks.useHideButtons
import pw.mihou.nexus.features.command.facade.NexusCommandEvent
import pw.mihou.nexus.features.command.facade.NexusHandler
import pw.mihou.nexus.features.react.elements.Embed
import pw.mihou.nexus.features.react.elements.PrimaryButton
import kotlin.time.Duration.Companion.minutes

class HideButtonsExample: NexusHandler {
    override fun onEvent(event: NexusCommandEvent) {
        event.interaction.R {
            val hideButtons by useHideButtons(after = 5.minutes)
            var clicks by writable(0)

            render {
                Embed {
                    Body(spaced = true) {
                        this append p("This is a example of using the `useHideButtons` hook which will hide the buttons of " +
                                "this component after 5 minutes from the last click of the button " +
                                "(or last re-render).")
                        this append p("People have clicked the button here $clicks times.")
                    }
                }
                if (!hideButtons) {
                    PrimaryButton(label = "Click here!") {
                        clicks++
                    }
                }
            }
        }
    }
}