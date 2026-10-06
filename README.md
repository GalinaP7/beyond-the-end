# Beyond the End

**A Storybound Minecraft experience.**

You crossed the Nether. You defeated the Ender Dragon. You took to the skies with Elytra. You built your home, raised your beacons, gathered your riches, and conquered nearly everything Minecraft was supposed to offer.

**What now?**

Beyond the End is a multiplayer progression and storytelling mod designed for established Minecraft worlds that have reached the end of vanilla progression. It adds new goals, persistent progression, physical currency, ranks, titles, and a framework for an evolving shared history.

**The End wasn't the finale. It was the prologue.**

> **Development Status:** Beyond the End is currently a work in progress.  
> Features, commands, progression values, and compatibility requirements may change during development.

---

## ✦ Current Features

### ⭐ Story Points

Story Points (SP) represent a player's permanent lifetime progression.

Players can earn SP through server activities such as quests, projects, achievements, events, and other contributions.

Story Points are not spendable currency. Once earned through normal gameplay, they represent the player's long-term progress through Beyond the End.

**Status:** ✅ Implemented

---

### 🪙 Story Tokens

Story Tokens are physical items that act as the spendable currency of Beyond the End.

For every **10 lifetime Story Points**, a player earns **1 Story Token**.

For example:

- 10 SP → 1 Token earned
- 50 SP → 5 Tokens earned
- 150 SP → 15 Tokens earned

Spending Tokens does **not** reduce Story Points.

The system also tracks which SP milestones have already awarded Tokens so that previously earned milestones cannot normally be claimed repeatedly.

**Status:** ✅ Implemented

---

### ⚔ Ranks

Ranks represent major lifetime Story Point milestones.

| Story Points | Rank |
|---:|---|
| 0 | Unranked |
| 100 | Wanderer |
| 300 | Adventurer |
| 500 | Explorer |
| 1,000 | Pathfinder |
| 2,000 | Hero |
| 3,000 | Champion |
| 5,000 | Legend |
| 10,000 | Mythic |
| ??? | ??? |

When a player reaches a new rank through a Story Point reward, the achievement is announced to the server.

**Status:** ✅ Implemented

---

### ⚔ Titles

Titles are collectible accomplishments that players can unlock independently from their rank.

A player may unlock multiple titles, but can only have **one title equipped at a time**.

Players cannot equip titles they have not earned.

Current development titles include:

- Frogleaper
- Cartographer
- Architect
- OSHA Violation
- Professional Menace

Some titles may be hidden until discovered.

**Status:** ✅ Backend implemented  
**Nametag display:** 🚧 In development

---

## 📖 Planned Systems

Beyond the End is intended to grow beyond a progression counter into a framework for continuing the life and history of an established Minecraft world.

Planned systems currently include:

- 📜 Quests and challenges
- 🏆 Achievements
- 🧭 Professions
- 📬 Community suggestions and voting
- 🏗 Community projects
- 🪙 Player shops and Token spending
- ✨ Wish Shrine rewards
- 📚 Server Chronicle and recorded world history
- 🏴 Bounties and Infamy
- ⚔ Expanded titles and progression rewards
- 📖 In-game guides and rule books

These systems are **planned concepts and are not necessarily implemented yet.**

---

## 📜 The Story

### Chapter I — THE END?

*If you are reading this, you have crossed the Nether.*

*You have defeated the Ender Dragon.*

*You have taken to the skies with Elytra.*

*You have built homes, raised beacons, gathered riches, and conquered nearly everything this world was supposed to offer.*

*So there is only one question left.*

***What now?***

*Defeating the Ender Dragon wasn't the story's finale, it was only the beginning.*

*It was the prologue.*

---

## 🎮 Commands

Beyond the End currently includes commands for Story Points, Story Tokens, and Titles.

### Player Commands

```text
/sp

/titles
/titles equip <title>
/titles clear
```

### Administrator Commands

```text
/sp add <player> <amount>
/sp remove <player> <amount>
/sp set <player> <amount>

/token give <player> <amount>
/token resetmilestones <player>
/token syncmilestones <player>

/titles give <player> <title>
/titles revoke <player> <title>
```

A complete explanation of each command will be maintained in [`docs/COMMANDS.md`](docs/COMMANDS.md).

---

## 🛠 Installation

Beyond the End is currently developed for:

- **Minecraft:** 26.2
- **Mod Loader:** Fabric
- **Fabric Loader:** 0.19.3+
- **Fabric API:** 0.154.2+26.2

Beyond the End is currently under active development and is **not yet intended as a stable public release**.

Installation requirements may change as development continues.

A full installation guide will be maintained in [`docs/INSTALLATION.md`](docs/INSTALLATION.md).

---

## 🧩 Compatibility

Beyond the End is currently a **Fabric mod**.

It is not currently available for Forge or NeoForge.

Minecraft updates, Fabric API changes, and changes to Minecraft's internal rendering code may require new versions of Beyond the End. Compatibility information will be documented for each future release.

---

## 🗺 Project Status

| System | Status |
|---|---|
| Story Points | ✅ Implemented |
| Persistent SP storage | ✅ Implemented |
| Physical Story Tokens | ✅ Implemented |
| Automatic Token milestones | ✅ Implemented |
| Ranks | ✅ Implemented |
| Rank-up announcements | ✅ Implemented |
| Unlockable Titles | ✅ Implemented |
| Equipped Title system | ✅ Implemented |
| Persistent Title storage | ✅ Implemented |
| Rank/SP nametag | 🚧 In development |
| Equipped Title nametag | 🚧 Planned |
| Quests | 📋 Planned |
| Achievements | 📋 Planned |
| Professions | 📋 Planned |
| Voting / Suggestions | 📋 Planned |
| Wish Shrine | 📋 Planned |
| Server Chronicle | 📋 Planned |
| Bounties / Infamy | 📋 Planned |
| In-game guide / rules | 📋 Planned |

---

## 📚 Documentation

More detailed documentation is being developed alongside the mod.

Documentation will include:

- [`docs/INSTALLATION.md`](docs/INSTALLATION.md) — installation and compatibility
- [`docs/COMMANDS.md`](docs/COMMANDS.md) — complete command reference
- [`docs/STORY-POINTS.md`](docs/STORY-POINTS.md) — progression, ranks, and Token mechanics
- [`docs/RULES.md`](docs/RULES.md) — gameplay and community rules
- [`docs/LORE.md`](docs/LORE.md) — Beyond the End story and Chronicle
- [`docs/DEVELOPMENT.md`](docs/DEVELOPMENT.md) — technical architecture and development notes

---

## 💻 Development

Beyond the End is being built in Java using Fabric.

The project currently includes server-side progression systems as well as experimental client-side rendering features.

Development follows a simple workflow:

```text
Code
  ↓
Build
  ↓
Test in Minecraft
  ↓
Update documentation
  ↓
Commit to Git
```

---

## ⚠️ Development Notice

Beyond the End is an early work in progress.

The project is currently being developed and tested on a private Minecraft server. Systems may be redesigned, commands may change, save formats may change, and unfinished features may contain bugs.

The repository currently represents active development rather than a finished public release.
