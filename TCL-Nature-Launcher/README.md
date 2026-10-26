# Nature TV Home — TCL Google TV launcher
**App version:** 0.1.0 (source alpha) · **26 October 2026**

Native Java Android TV HOME launcher based on the approved *Style B* concept. This is a **new independent implementation inspired by CleanYourTV**, not a copied fork (the upstream repo was unavailable in the build environment). It leaves CleanYourTV and its debloat configuration untouched. The wallpaper is a cropped landscape treatment based on the approved concept render.

## Implemented
- Nature wallpaper, large 24h clock/date and greeting
- Eight-item D-pad focusable dock; launches configured installed apps
- Manual weather city (defaults to Breda); Open-Meteo geocoding and forecast via HTTPS
- Cached weather fallback when offline; weather timeout handling
- Remote-driven settings dialog and list of installed Android TV apps
- Manifest registers both HOME and LEANBACK_LAUNCHER; no root, no analytics

## Known limitations / alpha caveats
- Original dock brand icons replaced with text placeholders (not licensed streaming logos).
- Dock customization, wallpaper change in-app, periodic weather refresh, forecast summary and 12h/°F toggles **not implemented yet**.
- HDMI shortcut uses a generic settings intent which may not exist on a TCL; it reports an error rather than silently failing.
- Live TV package differs across firmware; fallback is to open the installed TV app list.
- Some TCL firmware may not permit third-party default HOME selection.
- No device test or Android compile performed in this environment; treat this as **build candidate source**, not a verified working APK.
- Before wider deployment improve TV focus handling in system dialogs and verify TV package-visibility behavior on actual firmware.

