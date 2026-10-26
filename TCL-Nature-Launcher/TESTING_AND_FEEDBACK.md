# Testing and user feedback — Nature TV Home

**Build:** 0.1.0 source alpha. Status: NOT YET COMPILED / NOT DEVICE TESTED.

| ID | Test method | Expected | Status |
|---|---|---|---|
| TC01 | Install debug APK, open launcher | Screen loads | NOT RUN |
| TC02 | Press Left/Right and OK on each tile | Focus visible and app launches | NOT RUN |
| TC03 | Change clock/date and cross midnight | Clock correct | NOT RUN |
| TC04 | Connect internet; select Breda | Temperature/condition show | NOT RUN |
| TC05 | Disconnect after successful weather | Cached stale reading | NOT RUN |
| TC06 | Enter nonexistent city | Graceful error; dock works | NOT RUN |
| TC07 | Reboot TV | HOME selectable and screen responsive | NOT RUN |
| TC08 | Press HOME from YouTube | Launcher appears if firmware supports | NOT RUN |
| TC09 | HDMI 1 | Input settings open or explanatory message | NOT RUN |
| TC10 | Remove Netflix or other configured app | No crash | NOT RUN |
| TC11 | Test on TCL with CleanYourTV enabled | No debloat change and no loop | NOT RUN |
| TC12 | Reassign old launcher / uninstall | Previous launcher restored | NOT RUN |

## Feedback template
- Report ID:
- Version / APK SHA256:
- TCL model / firmware / Android API:
- Related test case:
- Expected behavior:
- Observed behavior:
- Reproduction steps:
- Logcat or screenshot:
- Severity (Blocker/High/Medium/Low):
- Change proposed:
- Retest result/date:

## Android diagnostics
`adb logcat -d -t 500 > nature-tv-logcat.txt`
`adb shell getprop ro.build.version.sdk`
`adb shell cmd package resolve-activity --brief -a android.intent.action.MAIN -c android.intent.category.HOME`

## Next versions
- 0.2.0 — pixel-match approved UI, icon assets, configurable dock and wallpapers.
- 0.3.0 — weather refresh scheduler, forecast summary, city picker and more unit options.
- 0.9.0 — TCL Home/HDMI support, boot/wake profiling, robustness and recovery.
- 1.0.0 — signed release after device acceptance.
