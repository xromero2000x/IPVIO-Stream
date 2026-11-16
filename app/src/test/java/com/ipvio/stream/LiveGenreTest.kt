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

    @Test fun reset_liveTabClearsGenreSwitch() {
        assertTrue("live_genres" in SettingsResetLogic.generalKeys(SettingsResetLogic.LIVE))
    }
}
