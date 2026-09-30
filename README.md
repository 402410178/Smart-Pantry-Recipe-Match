# Smart Pantry Manager

A Java Android application for tracking pantry ingredients and suggesting recipes only when every required ingredient and quantity is available. The app uses a local SQLite database, seeded with 18 recipes on first launch.

## Technology and database choice

- Java Activities with explicit Intents for screen navigation and data passing.
- RecyclerView with custom IngredientAdapter and RecipeAdapter classes.
- SQLite through `SQLiteOpenHelper`, with tables for pantry items, recipes, recipe ingredients, and settings.
- No network, Maps, GPS, or location permissions are used.

SQLite is the simplest defensible option for this single-device assessment: it is built into Android, persists after the app closes, and supports the required create/read/update/delete operations without a hosted service or account.

## Setup checklist (Windows)

1. Install the current stable Android Studio release using the official installer and complete its Setup Wizard.
2. Install a **JDK 17**. Android Gradle Plugin 8.x requires JDK 17 to run. Android Studio's bundled Java may be a different major version, so set the Gradle JDK explicitly in **File > Settings > Build, Execution, Deployment > Build Tools > Gradle**.
3. In **Tools > SDK Manager**, install Android SDK Platform 35 and Android SDK Build-Tools 35.x. In **Tools > Device Manager**, create a Pixel emulator using an API 35 system image. Enable hardware virtualization if the emulator reports that acceleration is unavailable.
4. Install Git for Windows. Open PowerShell and confirm `java -version` reports 17 and `git --version` prints a version.
5. If Git identity is not configured, set the same verified name/email used by your GitHub account:

   ```powershell
   git config --global user.name "YOUR NAME"
   git config --global user.email "YOUR GITHUB-LINKED EMAIL"
   ```

6. Open this project folder in Android Studio. Allow dependency downloads and sync to finish. The app uses Android Gradle Plugin 8.7.3, which requires Gradle 8.9, compile SDK 35, and Java 17.

### One-time Gradle wrapper setup

This source delivery does not contain the Gradle wrapper JAR. Before the first Android Studio sync or command-line build, install Gradle 8.9 once from [Gradle's official releases page](https://gradle.org/releases/), then run the wrapper task from this project folder:

```powershell
& "C:\Gradle\gradle-8.9\bin\gradle.bat" wrapper --gradle-version 8.9 --distribution-type bin
```

Use the location where you extracted Gradle if it differs from `C:\Gradle`. Keep the generated `gradlew`, `gradlew.bat`, and `gradle/wrapper/` files in the repository. Afterward, Android Studio and the command line can use the wrapper and download the pinned Gradle version automatically. Commit the generated wrapper files as an additional genuine setup commit.

### Build and launch

1. Start the API 35 emulator in Device Manager.
2. In Android Studio, choose the `app` run configuration and click **Run**.
3. From PowerShell, after generating the wrapper, run:

   ```powershell
   .\gradlew.bat testDebugUnitTest
   .\gradlew.bat assembleDebug
   ```

4. The installable debug APK is created at `app\build\outputs\apk\debug\app-debug.apk`. Do not include `build/`, `.gradle/`, `local.properties`, or the APK in the submission source ZIP.

## App walkthrough

- **Pantry:** add an ingredient; tap a row to edit it; long-press a row and confirm to delete it. The list reloads from SQLite when the screen resumes.
- **Suggested Recipes:** shows only recipes whose complete ingredient list and required quantities match the pantry. A missing item or insufficient quantity excludes the recipe. An empty-state message explains what to do.
- **Recipe Detail:** select a suggestion to view ingredients and preparation steps.
- **Settings:** the expiring-soon preference is stored in SQLite. Expiry dates/alerts themselves are not implemented; the setting is a persisted example preference.

The matcher folds simple singular/plural variants and converts kg to g, litres to ml, and tablespoons to teaspoons. It deliberately rejects different measurement dimensions (for example, grams cannot satisfy millilitres). It is a small deterministic rule set rather than natural-language understanding.

## Development stages and commit plan

The current repository contains ten real, sequential commits. Each corresponds to a distinct code or documentation milestone. They were created during this build session on 30 September 2026; they must not be described as work spread across earlier days. Read `SUBMISSION_GUIDE.md` for the matching stage-by-stage checks, GitHub instructions, demonstration script, report outline, screenshot list, and packaging checklist.

## Known verification boundary

Five plain-Java matcher smoke scenarios have been compiled and passed in the supplied workspace. The JUnit regression suite is included in `app/src/test`, but the full Android Gradle build and emulator flows have not been run here because Android SDK/Studio and Gradle are not installed in this workspace. Run the listed Gradle commands and complete the emulator manual checklist before claiming those checks passed.

## Useful official references

- Android Developers, [Install Android Studio](https://developer.android.com/studio/install).
- Android Developers, [About the Android Gradle Plugin and Gradle compatibility](https://developer.android.com/build/releases/about-agp).
- Android Developers, [Java versions in Android builds](https://developer.android.com/build/jdks).
- Android Developers, [SQLiteOpenHelper API reference](https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper).
- Android Developers, [Create dynamic lists with RecyclerView](https://developer.android.com/develop/ui/views/layout/recyclerview).
- GitHub Docs, [Adding locally hosted code to GitHub](https://docs.github.com/en/migrations/importing-source-code/using-the-command-line-to-import-source-code/adding-locally-hosted-code-to-github).
