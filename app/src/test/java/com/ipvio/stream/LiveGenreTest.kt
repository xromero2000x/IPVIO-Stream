package com.ipvio.stream

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/** T-026: the automatic genre categories in Live TV can be switched off. */
class LiveGenreTest {
    private fun ch(id: String, title: String, group: String) =
        Item(id, Kind.LIVE, title, null, "http://h/live/u/p/$id.ts", null, group)

    private val live = listOf(
        ch("1", "FR: TF1 FHD", "FR 🎬 GENERALE FHD"),
        ch("2", "FR: BFM TV", "FR 🎬 INFORMATION"),
        ch("3", "FR: GULLI", "FR 🎬 JEUNESSE")
    )

    @Test fun shown_off_returnsNoGenreCategories() {
        assertTrue(LiveGenre.shown(live, on = false).isEmpty())
    }

    @Test fun shown_on_returnsDetectedGenres() {
        val g = LiveGenre.shown(live, on = true)
        assertEquals(listOf("2"), g["GEN:news"]?.map { it.id })
    }

    @Test fun providerRows_keepOrderAndSkipHiddenAndRepeats() {
        val groups = live.groupBy { it.group }
        val names = groups.keys.toList()
        val rows = LiveGenre.providerRows(listOf(names[2]) + names + listOf("gone"), setOf(names[1]), groups)
        assertEquals(listOf(names[2], names[0]), rows.keys.toList())
        assertEquals(listOf("3"), rows[names[2]]?.map { it.id })
    }

    @Test fun providerRows_leaveOutAdultCategories() {
        val groups = (live + ch("9", "XXX 1", "FR XXX ADULTES")).groupBy { it.group }
        assertEquals(3, LiveGenre.providerRows(groups.keys.toList(), emptySet(), groups).size)
    }

    @Test fun reset_liveTabClearsGenreSwitch() {
        assertTrue("live_genres" in SettingsResetLogic.generalKeys(SettingsResetLogic.LIVE))
    }
}
