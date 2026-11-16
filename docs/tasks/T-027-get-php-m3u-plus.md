# T-027 get.php lists: request type=m3u_plus

## Goal

User report (16 Nov 2026): Live TV shows only "Alle Sender" and "Sonstige"; the provider's categories are missing.
The playlist address ends in `get.php?username=…&password=…&type=m3u`.

## Findings

- Xtream panels deliver `type=m3u` without attributes: no `group-title`, no `tvg-logo`, no `tvg-id`.
  `M3uParser` then puts every entry into "Sonstige". Only `type=m3u_plus` carries the categories.
- `PlaylistStore.downloadList` requested the address exactly as entered.
- Before T-026 the automatic genre categories hid the problem in Live TV.

## Change

- `XtreamLogic.kt`: new pure function `plusUrl(url)`. For a get.php address with user and password it turns `type=m3u`
  into `type=m3u_plus` and adds the type when it is missing. Other addresses and other types are returned unchanged.
- `PlaylistStore.kt` (`downloadList`): the request uses `plusUrl(u)`. The hash in the `.info` file is that of the requested
  address, so a raw copy or a partial download made with `type=m3u` is not reused.
- `XtreamLogicTest.kt`: three tests.

## Not changed

The saved playlist address, cache file names (still derived from the saved address), title ids (the stream addresses are
the same in both list types), the fallback to `player_api.php` when get.php fails.

## Risks

- A provider that answers `m3u` but refuses `m3u_plus`: the existing fallback to `player_api.php` takes over (it delivers categories as well).
- An already loaded playlist keeps its parsed copy until the next refresh.

## Acceptance criteria

1. Add or refresh a playlist `…/get.php?username=…&password=…&type=m3u`: Live TV, movies and series show the provider's categories.
2. A playlist entered as `type=m3u_plus` loads as before.
3. A plain `.m3u` address loads as before.

## Test results

`./gradlew testDebugUnitTest assembleDebug` run by the assistant on 16 Nov 2026: build successful, 408 unit tests green. Not tested on a device or against a real provider.

## Status

Implemented, waiting for the device test.
