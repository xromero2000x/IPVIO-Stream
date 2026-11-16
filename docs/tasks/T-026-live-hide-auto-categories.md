# T-026 Switch off the automatic genre categories in Live TV

## Goal

User feedback (16 Nov 2026): in Live TV the automatic categories crowd out the provider's own categories
(for example "FR 🎬 GENERALE FHD", "FR 🎬 SPORTS"). The user wants to see only their provider's categories.

## Findings

- `LiveTab.kt` builds the category column as: fixed entries (HOME, GUIDE, SEARCH, MULTI, favorites, lists, NEW, ALL),
  then the six genre categories from `LiveGenre.split`, then the provider categories (`CatOrder.sorted`).
- Genre categories could only be hidden one by one through the category menu (`HiddenCats`).
- Movies and series already have a switch for this (`AppSettings.ipvioFilter`, "Automatische Genres", off by default).

## Change

- `General.kt`: new setting `liveGenres`, preference key `live_genres` in `ipvio_general`, default `false` (decision of the fork owner, 16 Nov 2026: the provider categories show without visiting the settings; visible change for existing users).
- `LiveGenre.kt`: new pure function `shown(live, on)`.
- `LiveTab.kt`: genre categories come from `LiveGenre.shown(live, General.liveGenres)`; a remembered genre category falls back to the Live TV home page when the switch is off. With the switch off the Live TV home page shows one row per provider category (`LiveGenre.providerRows`: column order, no hidden and no adult categories) instead of the genre rows.
- `HomeScreen.kt`: the genre prewarm is skipped while the switch is off.
- `SettingsHub.kt`: toggle "Automatische Kategorien" on the Live TV settings page.
- `SettingsResetLogic.kt`: the Live TV page reset also resets `live_genres`.
- `DictJ.kt`, `DictNl5.kt`: translations of the two new UI strings.
- `LiveGenreTest.kt`: five tests.

## Not changed

Genre detection itself, `HiddenCats`, `CatOrder`, the home screen, movies and series.

## Acceptance criteria

1. Default (switch off): Sport, Filme, Dokumentationen, Kinder, Nachrichten and Musik
   disappear from the Live TV category column; the provider categories follow "Alle Sender" in provider order.
2. Switch on: the genre categories return, Live TV looks as before this task.
3. Live TV page reset turns the switch off again.
4. Switch off while a genre category was the last opened one: Live TV opens on its home page, not on an empty list.

## Test results

`./gradlew testDebugUnitTest assembleDebug` run by the assistant on 16 Nov 2026: build successful, all unit tests green (including `LiveGenreTest`). Not tested on a device.

## Manual test steps

Criteria 1 to 4 above on a Fire TV or Android TV with an M3U playlist.

## Status

Implemented, waiting for build and device test.
