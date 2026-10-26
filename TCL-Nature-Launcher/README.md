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

## Build APK (GitHub Actions — easiest)
1. Create a private GitHub repository; upload all the files from this ZIP (including `.github/workflows`).
2. Open **Actions → Build Android TV APK → Run workflow**.
3. After a successful run, download the `NatureTVHome-v0.1.0-debug-apk` artifact.
4. Extract `app-debug.apk`. This debug APK is signed by the build system for sideload testing (not a production release).

## Local build
Install JDK 17, Android SDK API 35, Android build-tools 35, and Gradle 8.9. Run `gradle assembleDebug`. Output: `app/build/outputs/apk/debug/app-debug.apk`.

## TCL safe installation
1. Record your previous HOME launcher in case you need to switch back. **Do not disable/uninstall CleanYourTV yet.**
2. Enable Developer Options and USB/Wireless debugging.
3. `adb install -r app-debug.apk`
4. Launch via the TV's Apps menu before selecting a default HOME.
5. If supported, select Nature TV Home when Android asks for your HOME launcher.
6. Test Home, remote, weather, standby/wake, reboot, and rollback before relying on it.

Rollback: choose the original launcher in Android default-app settings (if available), or uninstall this app with `adb uninstall com.vj.tvlauncher`; no CleanYourTV debloat settings are modified.

## Privacy
Weather queries contact `geocoding-api.open-meteo.com` and `api.open-meteo.com`. City is manually entered; no GPS / precise location permission. No telemetry or advertising library.

## Changelog
- **0.1.0** — Initial independent Android TV HOME app, static landscape background, clock/date, dock, manual city, Open-Meteo fetch and offline fallback, basic settings and tests.

## Testing / feedback
See `TESTING_AND_FEEDBACK.md`; record model, Android version, steps, expected, actual, screenshot/logcat, severity and retest result. No test passes are asserted until a build and device test are performed.
