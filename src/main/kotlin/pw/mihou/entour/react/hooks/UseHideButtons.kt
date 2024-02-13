package pw.mihou.entour.react.hooks

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import pw.mihou.reakt.Reakt
import pw.mihou.reakt.utils.coroutine
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

/**
 * [useHideButtons] is a hook that will give you a little [Reakt.Writable] that will
 * turn into [true] after the given [after] timestamp. This will extend the time by another 5
 * minutes when re-rendering happens, for example, when a button click causes a re-render then
 * it will reset the clock.
 *
 * This is useful for cases where you want to remove the buttons of a response after
 * a given set of time.
 *
 * @param after the amount of time before hiding the buttons, defaults to 10 minutes.
 * @return a [Reakt.Writable] that will change to [true] after the given time.
 */
fun Reakt.useHideButtons(after: Duration = 5.minutes): Reakt.Writable<Boolean> {
    val hideButtons = writable(false)
    var job : Job? =  null
    onUpdate {
        job?.cancel()
        job = coroutine {
            delay(after.inWholeMilliseconds)
            hideButtons set false
            job = null
        }
    }
    return hideButtons
}