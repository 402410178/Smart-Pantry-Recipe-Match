# Smart Pantry Manager: build, evidence, and submission guide

Use this alongside `README.md` and the assignment PDF. Replace any bracketed personal details with accurate information. Do not claim tests, screenshots, dates, or development events that did not happen.

## 1. Ten genuine development stages

These are the ten existing commit subjects, in repository order. Each stage has a suggested check that you should actually perform in Android Studio. The repository history records all ten commits on **30 September 2026**, during one build session; it does not establish work over 3-5 days. The assignment explicitly says history is checked against the video and report timeline. Do not backdate commits or narrate this as a multi-day chronology. For a late submission, explain the real timeline and follow the institution's late-submission process.

| # | Commit message | Files and implementation | Check to perform and record honestly |
|---|---|---|---|
| 1 | `Scaffold Java Android application and configure build` | Gradle settings, app module, manifest, theme; Java package and five declared Activities. | Open the root folder in Android Studio; confirm Gradle sync and launcher manifest configuration. |
| 2 | `Add SQLite schema and seed 18 starter recipes` | `Ingredient.java`, `Recipe.java`, `PantryDatabase.java`; schema and initial recipe data. | First launch should create the database and show an empty pantry; inspect that the recipe table contains 18 entries using Android Studio's App Inspection/Database Inspector if available. |
| 3 | `Implement strict ingredient and quantity matching` | `RecipeMatcher.java`; name normalization, mass/volume/count compatibility, quantity comparison. | Run matcher regression tests. Verify a missing ingredient and an insufficient quantity each exclude a recipe. |
| 4 | `Add RecyclerView adapters and shared screen navigation` | `IngredientAdapter.java`, `RecipeAdapter.java`, `Ui.java`; list binding and bottom navigation. | Add enough pantry rows to scroll; confirm rows render and the navigation buttons open the expected screens. |
| 5 | `Build pantry list and validated ingredient CRUD flow` | `PantryActivity.java`, `IngredientFormActivity.java`; create, read, update, delete and form validation. | Create one item, edit its name/amount, delete it after confirmation; try blank name/unit, invalid number, zero and negative quantity. |
| 6 | `Show only recipes that pass full pantry matching` | `SuggestedRecipesActivity.java`; strict filter and zero-match feedback. | Add all ingredients for a known recipe at sufficient amounts, verify it appears, reduce/remove one, verify it disappears. |
| 7 | `Add recipe detail view with ingredients and method` | `RecipeDetailActivity.java`; selected recipe data passed in an Intent. | Open a matched recipe and verify its required ingredients and method are visible. |
| 8 | `Add persistent preferences screen` | `SettingsActivity.java`; SQLite-backed preference toggle. | Toggle the setting, close/reopen the app, and verify the selected state remains. |
| 9 | `Add matcher regression tests and ignore local build output` | JUnit tests under `app/src/test`, JUnit dependency, `.gitignore`. | Run `.\gradlew.bat testDebugUnitTest`; review the result and fix failures before recording a pass. Five matcher smoke scenarios passed in the preparation workspace; that is not an Android emulator test. |
| 10 | `Enforce SQLite relations and document submission workflow` | `PantryDatabase.java` enables SQLite foreign-key constraints; `README.md` and `SUBMISSION_GUIDE.md` document setup, tests, GitHub, video/report and packaging. | Read every setup instruction against the installed tool versions; check the guide against the supplied PDF. |

These commits are genuine code/documentation increments, but since they were all made in one session they may not satisfy the rubric's expectation of commits spread across development. They cannot honestly be represented as such. Ask the lecturer how to submit late and explain the actual circumstances rather than manufacturing evidence.

## 2. Setup and debugging checklist

1. Install Android Studio and finish the Setup Wizard.
2. Install JDK 17 and set it as Android Studio's **Gradle JDK**. In PowerShell, check `java -version`; a Java 26 installation is not the target configured for this Android Gradle Plugin.
3. In SDK Manager, install **Android SDK Platform 35** and **Build-Tools 35.x**. Note the installed SDK location.
4. Create a Pixel/API 35 AVD in Device Manager. Start it before clicking Run. If it cannot boot or is very slow, enable VT-x/AMD-V virtualization and restart.
5. Install Git for Windows and verify `git --version`. Configure the Git name/email that is linked to your GitHub account.
6. Follow README's one-time Gradle 8.9 wrapper bootstrap. Do not commit machine-specific `local.properties`.
7. Open the project root (the folder containing `settings.gradle`) in Android Studio, allow Gradle sync/downloads, and inspect the Build output for errors before running.
8. Run `testDebugUnitTest`, then `assembleDebug`, then launch on the emulator.
9. In the emulator, perform all CRUD, validation, navigation, strict-match, zero-match, recipe-detail, and persistence checks listed below. Record actual outcomes and any remaining defect.

Useful debug order: (a) confirm Gradle JDK 17; (b) confirm SDK Platform 35 is installed; (c) allow repository dependency downloads; (d) read the first meaningful Build error; (e) use Logcat for runtime crashes; (f) clear app data only when intentionally retesting first-run database seeding. Clearing app data deletes the pantry.

## 3. GitHub repository and push

The assignment asks for a **public** repository. If you choose a private repository, add the lecturer/marker, **Royal Goronga**, as a collaborator using the GitHub account/username supplied by the lecturer or submission portal. The marker is a collaborator, not the commit author. Do not publish passwords, API keys, `local.properties`, student data, or emulator/build output.

1. Sign in to GitHub and create an empty repository named `Smart-Pantry-Manager`. Choose **Public** if permitted. Do not initialize it with a README, license, or `.gitignore`; this local repository already has its first commit.
2. In the project folder, inspect `git status` and `git log --oneline --decorate -10`. Confirm identity and all ten messages.
3. Rename the local branch and add your own repository URL:

   ```powershell
   git branch -M main
   git remote add origin https://github.com/YOUR-USERNAME/Smart-Pantry-Manager.git
   git remote -v
   git push -u origin main
   ```

4. Authenticate in the browser prompt or Git Credential Manager; never put a token directly in a command or source file.
5. Refresh GitHub and verify the files and all ten commits are visible. Add the working repository link to the report. If the marker needs access, test the link in a signed-out/private browser window.
6. After generating the Gradle wrapper and running the actual Android checks, commit the wrapper and any real fixes with descriptive messages, then push again. This creates additional honest commits.

If the institution has a policy about AI assistance or authorship, follow it. Read and understand every class; do not submit a claim that all implementation was independently written if that is not true. Be ready to explain and modify the app yourself.

## 4. Emulator test checklist

Capture real emulator screenshots as you perform these steps. Record pass/fail and notes in your report; do not use mockups.

1. **First launch / seed:** app opens without a crash and pantry shows its empty-state/list area; recipe database contains 18 seeded recipes.
2. **Create / read:** add `tomato`, quantity `3`, unit `count`; return to Pantry and confirm it appears.
3. **Validation:** attempt empty name, empty unit, non-number quantity, `0`, and a negative amount. Each must be rejected with a visible field error.
4. **Update:** tap the tomato row, change quantity to `4`, save, and verify the updated value.
5. **Delete:** long-press a row, cancel once and confirm it remains; long-press again and confirm deletion.
6. **Strict match positive:** create all of one known recipe's ingredients at equal or greater amounts, e.g. 200 g pasta, 3 tomatoes, 2 cloves garlic, 1 tbsp olive oil. Open suggestions and verify Tomato Pasta appears.
7. **Strict match negative:** delete garlic or reduce it below 2 cloves; revisit suggestions and verify Tomato Pasta is absent. This is the key rubric demonstration.
8. **Normalization:** test `tomatoes` against recipe `tomato`, and `1 kg` against a `100 g` recipe requirement (entering less than required must still fail). Confirm metric conversion does not permit grams to satisfy millilitres.
9. **Zero matches:** clear/delete ingredients and verify the friendly no-match text appears instead of a blank screen.
10. **Detail and navigation:** open a suggestion; verify ingredients and method; navigate Pantry/Recipes/Settings.
11. **Persistence:** close the app from Recents (do not clear storage), reopen it, and verify the pantry and Settings preference remain.
12. **Stability:** repeat navigation and CRUD without a crash. Check Logcat if a failure occurs.

The `RecipeMatcherTest` JUnit suite covers complete match, missing ingredient, insufficient amount, plural/metric normalization, and incompatible units. Use the emulator for database persistence and UI behavior; unit tests do not prove those flows.

## 5. Five-to-seven-minute narrated demonstration script

Target about **6:20** total. Narrate in your own words and point at actual source. Voice is required; face is optional. A suggested 1080p recording is good only if text remains readable. Keep the final H.264 MP4 inside the ZIP and check ZIP size is below 50 MB.

### 0:00-1:00 — GitHub history

“This is my Smart Pantry Manager repository. The history has ten sequential commits for setup, SQLite and seeded recipes, strict matching, RecyclerView adapters and navigation, pantry CRUD, recipe suggestions and detail, settings, tests, and documentation. These commits were all made in one session on 30 September 2026, so I am not presenting them as a multi-day history. I will continue committing any further work honestly.”

Show the actual GitHub commit list and click into representative commits. Do not imply the history spans days or dates it does not.

### 1:00-3:25 — Live app and CRUD

“The Pantry screen reads ingredients from SQLite. I will add [ingredient], quantity [amount] [unit], then show it in the list. I can tap it to edit and long-press to delete after confirmation. The form rejects a blank name and a quantity that is not greater than zero. The bottom navigation opens recipe suggestions and Settings.”

Perform create, read, update, delete, and one visible validation failure. Then show the persisted preference if timing permits.

“Now I will demonstrate strict matching. I have the full set of ingredients for [recipe], so it appears here. When I remove [one required item] / reduce it below the required amount, the recipe disappears. The app does not show a partial match. When nothing qualifies, it explains that I need to add ingredients.”

Demonstrate the positive and negative match live. Open a recipe detail and show the method. Close and reopen the app and show the pantry record remains.

### 3:25-5:45 — Explain three concepts using your code

Choose three you understand. Point at the actual files and code while speaking.

1. **Strict matcher — `RecipeMatcher.java`:** “The suggestions screen loops through recipes. For each recipe, the matcher checks every required ingredient. It normalizes common singular/plural forms, converts compatible units to a common base, compares available and required amounts, and returns false immediately if an ingredient is missing, below the required amount, or measured in an incompatible dimension. Only a complete pass reaches the suggestion list.”
2. **Database — `PantryDatabase.java`:** “`SQLiteOpenHelper` creates the local tables the first time the app opens. Pantry CRUD methods use SQLite queries and writes. The app reloads the pantry from the database when the screen resumes, so records remain after closing the app. The recipe catalogue is inserted at database creation.”
3. **Adapter and Intent — `IngredientAdapter.java` / `RecipeAdapter.java` and `PantryActivity.java`:** “RecyclerView asks the custom adapter to create and bind rows. A row click starts the edit Activity with an explicit Intent carrying the ingredient ID and values. When the form saves, it updates SQLite and finishes; the Pantry screen refreshes from the database.”

If you choose lifecycle instead, explain the actual callbacks visible in `PantryActivity` and `SuggestedRecipesActivity`: `onCreate` sets up the helper and `onResume` reloads/rebuilds visible UI when returning to a screen.

### 5:45-6:20 — Database justification and close

“I chose SQLite with `SQLiteOpenHelper` because this app is local, uses a small structured dataset, and must demonstrate persistent CRUD. It runs on-device without a server, cloud account, network connection, or API secret. The schema separates pantry items, recipes, recipe requirements, and settings. Thank you.”

Adjust timing after a practice recording; final duration must be between 5 and 7 minutes. Keep natural narration throughout.

## 6. Written report plan (follow this order)

Prepare a Word document, then export one PDF. Apply **Times New Roman 12 pt and 1.5 line spacing throughout**; use the institution's supplied cover page and include a signed declaration of originality. Use Harvard in-text citations and a consistent reference list. Add page numbers and update the table of contents after final edits.

1. **Cover page:** use supplied Richfield form. Title: *Smart Pantry Manager*. Enter your own full name, verified ITS number, module, date and signature; do not use the lecturer/marker's name as the student author or invent missing details.
2. **Table of contents.**
3. **Introduction:** food waste/problem, intended user, aim, scope and key strict-matching promise. Cite any factual claim about food waste.
4. **System design:** include a hand-drawn or clean diagram based on the actual navigation and a data model. Suggested screen flow: `PantryActivity -> IngredientFormActivity`, `PantryActivity -> SuggestedRecipesActivity -> RecipeDetailActivity`, and shared navigation to `SettingsActivity`. Suggested data model: `pantry(id, name, quantity, unit)`, `recipes(id, name, description, steps)`, `recipe_ingredients(recipe_id, name, quantity, unit)`, and `app_settings(setting_key, setting_value)`. Explain recipe_ingredients belongs to one recipe. Use a simple ER diagram and screen-flow diagram; ensure they match the delivered code.
5. **Screenshots of every screen and core function:** place genuine emulator captures with numbered figure captions (checklist below).
6. **Key code snippets (3-5):** keep each short and readable, with class/file name, what it does and why. Choose (a) `RecipeMatcher.canMake` complete ingredient and quantity checks; (b) SQLite create/update/delete or schema; (c) `SuggestedRecipesActivity` filtering; (d) Intent extras opening edit/detail; (e) adapter bind/list refresh. Cite Android documentation where relevant. Do not paste entire classes.
7. **Challenges and solutions (2-3 real cases):** write only what you actually encountered. Possible prompts, only if true: matching `tomato`/`tomatoes`; quantity and unit conversion; refreshing data after returning from an edit form; selecting a compatible JDK/Gradle version. For each: symptom, cause, change made, evidence the fix worked.
8. **Conclusion and reflection:** summarize achieved scope and test evidence, what you learned about Activities, Intents, adapters, SQLite and strict matching, limitations (no expiry dates or real expiry notifications; basic name normalization; local-only storage), and realistic next improvements.
9. **References:** Harvard-style entries for sources actually consulted; include Android Developers documentation used for Activities, Intents, RecyclerView and SQLiteOpenHelper, plus any other real sources. Example pattern: `Organisation (year or n.d.) Title. Available at: URL (Accessed: 30 September 2026).` Follow your institution's Harvard guide if its punctuation differs.

### Screenshot checklist (capture from the running app)

- Pantry list with at least two saved ingredients.
- Empty pantry / no-match feedback state.
- Add Ingredient form before entry.
- Validation error for blank name and invalid/zero quantity (one or more images if all states cannot fit clearly).
- Pantry list immediately after successful creation.
- Edit Ingredient populated form and the updated pantry value after save.
- Delete confirmation dialog, then list after deletion.
- Suggested Recipes with at least one full strict match.
- Suggested Recipes after removing one required ingredient or lowering quantity, proving that recipe is gone.
- Recipe Detail showing complete ingredient list and preparation steps.
- Settings screen with the preference value; optionally show it retained after reopening.
- Optional database evidence: Database Inspector showing `pantry`, `recipes` (18 rows), `recipe_ingredients`, and `app_settings`.

Caption each figure with what it proves, e.g. “Figure 6: Tomato Pasta is excluded after garlic is removed, demonstrating that partial matches are not suggested.” Use your own app screenshots, no mockups or stock imagery. Crop only irrelevant emulator chrome; preserve readable app content.

### Suggested Harvard references to verify and cite

- Android Developers (n.d.) *SQLiteOpenHelper*. Available at: https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper (Accessed: 30 September 2026).
- Android Developers (n.d.) *Create dynamic lists with RecyclerView*. Available at: https://developer.android.com/develop/ui/views/layout/recyclerview (Accessed: 30 September 2026).
- Android Developers (n.d.) *Intents and intent filters*. Available at: https://developer.android.com/guide/components/intents-filters (Accessed: 30 September 2026).
- Android Developers (n.d.) *The activity lifecycle*. Available at: https://developer.android.com/guide/components/activities/activity-lifecycle (Accessed: 30 September 2026).
- Include only pages, tutorials or sources you actually read, and cite them where used in the report.

## 7. Packaging and final submission checklist

Name the ZIP exactly `Studentnumber_Surname_MobileAppDev700_Assignment.zip`. It must contain one complete Android Studio project folder (source/configuration; exclude `.gradle/` and every `build/` folder), the compressed 5-7 minute MP4 directly inside the ZIP, and `Studentnumber_Surname_MobileAppDev700_Assignment.docx` (or one PDF report if accepted by your portal). The report must contain a working GitHub link. Keep the complete ZIP **under 50 MB**.

- [ ] Android project opens, syncs, and builds on the target setup; screenshots prove tested behavior.
- [ ] Ten real commits are pushed and visible; repository is public or marker has access.
- [ ] README and complete source are included; no generated build/cache or machine-specific files.
- [ ] Video is narrated, 5-7 minutes, clear, H.264 MP4, and stored inside the ZIP.
- [ ] Report uses required cover page, TOC, required sections/order, screenshots/captions, 3-5 snippets, real challenges, conclusion, Harvard references, and signed originality declaration.
- [ ] ZIP has the exact required filename and is less than 50 MB; open the ZIP once to verify its contents.
- [ ] Upload the one ZIP to Moodle and confirm the portal shows a successful submission. Keep a backup copy.

The assignment allows late submissions under the institution's late policy. If late, state the actual reason and timing in the permitted submission/communication channel; do not alter Git timestamps or fabricate a staged multi-day history.
