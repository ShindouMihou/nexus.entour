package pw.mihou.entour.react.hooks

import pw.mihou.entour.react.utils.launch
import pw.mihou.nexus.configuration.modules.Cancellable
import pw.mihou.nexus.features.react.React
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * [useHideButtons] is a hook that will give you a little [React.Writable] that will
 * turn into [true] after the given [after] timestamp. This will extend the time by another 5
 * minutes when re-rendering happens, for example, when a button click causes a re-render then
 * it will reset the clock.
 *
 * This is useful for cases where you want to remove the buttons of a response after
 * a given set of time.
 *
 * @param after the amount of time before hiding the buttons, defaults to 10 minutes.
 * @return a [React.Writable] that will change to [true] after the given time.
 */
fun React.useHideButtons(after: Duration = 5.minutes): React.Writable<Boolean> {
    val hideButtons = writable(false)
    var job : Cancellable? =  null
    onRender {
        job?.cancel(true)
        job = launch.scheduler.launch(after.inWholeMilliseconds)  {
            hideButtons.set(false)
        }
    }
    return hideButtons
}