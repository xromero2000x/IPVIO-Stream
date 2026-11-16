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

- `General.kt`: new setting `liveGenres`, preference key `live_genres` in `ipvio_general`, default `true` (existing behaviour kept).
- `LiveGenre.kt`: new pure function `shown(live, on)`.
- `LiveTab.kt`: genre categories come from `LiveGenre.shown(live, General.liveGenres)`.
- `SettingsHub.kt`: toggle "Automatische Kategorien" on the Live TV settings page.
- `SettingsResetLogic.kt`: the Live TV page reset also resets `live_genres`.
- `DictJ.kt`, `DictNl5.kt`: translations of the two new UI strings.
- `LiveGenreTest.kt`: three tests.

## Not changed

Genre detection itself, `HiddenCats`, `CatOrder`, the home screen, movies and series.

## Acceptance criteria

1. Default: Live TV looks as before.
2. Settings > Live TV > "Automatische Kategorien" off: Sport, Filme, Dokumentationen, Kinder, Nachrichten and Musik
   disappear from the Live TV category column; the provider categories follow "Alle Sender" in provider order.
3. Switch on again: the genre categories return.
4. Live TV page reset turns the switch back on.

## Test results

Not run: no Android SDK in the assistant's environment. `LiveGenreTest` must run in CI or Android Studio.

## Manual test steps

Steps 1 to 4 above on a Fire TV or Android TV with an M3U playlist.

## Status

Implemented, waiting for build and device test.
