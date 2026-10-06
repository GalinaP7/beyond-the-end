# Beyond the End — Development

Technical notes for developing and contributing to Beyond the End.

---

## 🛠 Technology

Beyond the End is currently built with:

| Technology | Version |
|---|---|
| Minecraft | 26.2 |
| Java | 25 |
| Fabric Loader | 0.19.3+ |
| Fabric API | 0.154.2+26.2 |
| Gradle | 9.7.1 |

Beyond the End currently targets **Fabric**.

---

## 📁 Project Structure

The main systems are located under:

```text
src/main/java/com/lithiyana/storysystem/
├── StorySystem.java
├── StoryPointManager.java
├── RankManager.java
├── Title.java
├── TitleManager.java
└── ModItems.java
```

Client-only features are kept separately:

```text
src/client/java/com/lithiyana/storysystem/
└── mixin/
    └── PlayerNameTagMixin.java
```

Resources such as textures, models, translations, and Fabric configuration are stored in:

```text
src/main/resources/
```

---

## ✦ Core Systems

### Story Points

`StoryPointManager` manages:

- lifetime Story Points
- Token milestone history
- persistent player progression
- loading and saving progression data

Token milestone history is stored separately from SP so previously rewarded milestones cannot normally be claimed again.

### ⚔ Ranks

`RankManager` determines a player's rank from their lifetime Story Points.

Ranks are calculated from SP rather than stored as separate player progression.

### ⚔ Titles

`Title` defines available titles.

`TitleManager` manages:

- unlocked titles
- equipped titles
- title persistence

Players may unlock multiple titles but equip only one at a time.

### 🪙 Story Tokens

Story Tokens are registered as physical Minecraft items.

They are awarded automatically when players cross new 10-SP milestones and can also be given manually by administrators.

---

## 💾 Persistence

Player progression is saved outside the world save in:

```text
config/storysystem/
```

Story Point data tracks both lifetime SP and previously rewarded Token milestones.

Title data stores unlocked and equipped titles.

---

## 🖥 Client & Server

Most Beyond the End progression logic runs on the **server**.

Client-side code is used for features that require rendering, such as custom player nametags.

The current nametag system uses a client-side mixin targeting Minecraft's player rendering system.

Server progression data will need to be synchronized to clients before ranks, SP, and equipped titles can be rendered correctly above players.

---

## 🔨 Building

From the project directory:

```powershell
.\gradlew build
```

A successful build creates the mod JAR in:

```text
build/libs/
```

The normal mod file will look similar to:

```text
storysystem-1.0.0.jar
```

Do not install the `-sources.jar` file as the mod.

---

## 🧪 Development Workflow

```text
Code
 ↓
Build
 ↓
Test in Minecraft
 ↓
Update Documentation
 ↓
Commit
```

Minecraft testing is part of development. A successful Gradle build confirms that the code compiles, but gameplay behavior should still be tested in-game.

---

## 🚧 Current Development

The core progression backend is implemented:

- ✅ Story Points
- ✅ Persistent SP storage
- ✅ Story Tokens
- ✅ Token milestone tracking
- ✅ Ranks
- ✅ Rank-up announcements
- ✅ Title unlocking and equipping
- 🚧 Player nametag rendering
- 📋 Server-to-client progression synchronization
- 📋 Quests and achievements
- 📋 Professions and community systems

See the main [README](../README.md) for the broader project roadmap.
