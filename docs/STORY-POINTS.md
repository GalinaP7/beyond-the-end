# Beyond the End — Story Points & Progression

Story Points form the core progression system of Beyond the End.

Unlike Minecraft experience levels or traditional currency, Story Points represent a player's **permanent lifetime progression** through the world.

They connect several Beyond the End systems:

```text
Activities & Accomplishments
          ↓
     Story Points
          ↓
   ┌──────┴──────┐
   ↓             ↓
Ranks       Story Tokens
```

---

# ⭐ Story Points

Story Points (SP) represent a player's long-term progression.

Players may earn SP for accomplishments such as:

- completing quests;
- contributing to community projects;
- completing achievements;
- participating in events; and
- other meaningful contributions to the world.

The exact ways SP can be earned will expand as Beyond the End develops.

Story Points are **not currency**.

They are not spent when purchasing rewards or using Story Tokens.

For example, if a player earns:

```text
150 SP
```

and later spends all of their Story Tokens, they still have:

```text
150 SP
```

Their Story Points represent what they have accomplished, not what they currently own.

---

# 🪙 Story Tokens

Story Tokens are the physical currency of Beyond the End.

Unlike Story Points, Tokens can be **spent**.

Players automatically earn:

> **1 Story Token for every 10 lifetime Story Points reached.**

Examples:

| Lifetime SP | Tokens Earned |
|---:|---:|
| 10 | 1 |
| 20 | 2 |
| 50 | 5 |
| 100 | 10 |
| 150 | 15 |
| 500 | 50 |

Story Tokens are actual Minecraft items.

They can exist in a player's inventory, be stored in containers, and eventually be used by systems such as shops and rewards.

---

# Token Milestones

Tokens are awarded according to **cumulative Story Point milestones**, not according to the size of an individual SP reward.

For example, suppose a player begins with:

```text
0 SP
```

They receive:

```text
+5 SP
```

Their new total is:

```text
5 SP
```

No Token is awarded yet.

Later, they receive:

```text
+15 SP
```

Their new total becomes:

```text
20 SP
```

The player has now reached both the:

```text
10 SP milestone
20 SP milestone
```

so they are entitled to:

```text
2 Story Tokens
```

The system therefore cares about **which lifetime milestones the player has reached**, rather than whether an individual reward happened to be divisible by 10.

---

# Preventing Duplicate Token Rewards

Beyond the End separately records how many Token milestones have already been paid to each player.

Conceptually, each player has:

```text
Lifetime Story Points
Token milestones already paid
Physical Story Tokens currently owned
```

These represent three different things.

For example:

```text
Lifetime SP:             150
Token milestones paid:   15
Physical Tokens owned:    3
```

This could mean the player earned 15 Tokens through progression and later spent 12 of them.

Their lifetime progression remains:

```text
150 SP
```

---

## Why Milestone History Exists

Without separate milestone history, decreasing and then increasing a player's SP could repeatedly generate Tokens from the same thresholds.

For example:

```text
100 SP
↓
remove 10 SP
↓
90 SP
↓
add 10 SP
↓
100 SP
```

The player should **not** receive the 100-SP Token again if that milestone was already rewarded.

Beyond the End therefore remembers that the milestone has already been paid.

Removing or setting SP does not automatically erase this history.

---

# ⚔ Ranks

Ranks represent major Story Point milestones.

A player's rank is determined automatically from their lifetime SP.

| Story Points | Rank |
|---:|---|
| 0–99 | Unranked |
| 100–299 | Wanderer |
| 300–499 | Adventurer |
| 500–999 | Explorer |
| 1,000–1,999 | Pathfinder |
| 2,000–2,999 | Hero |
| 3,000–4,999 | Champion |
| 5,000–9,999 | Legend |
| 10,000+ | Mythic |

And beyond Mythic?

```text
???
```

Some progression is better left undiscovered.

---

# Rank-Ups

When Story Points are awarded normally and the new total moves a player into another rank, Beyond the End announces the rank-up to the server.

For example:

```text
99 SP
↓
+1 SP
↓
100 SP
↓
Wanderer
```

This creates a shared progression moment rather than making ranks purely private statistics.

---

# ⚔ Titles vs. Ranks

Ranks and Titles are separate systems.

**Ranks** are automatically determined by lifetime Story Points.

**Titles** are individual accomplishments that can be unlocked and equipped.

For example, a player could eventually appear as:

```text
PlayerName [Frogleaper]
Wanderer ✦ 150 SP
```

The player did not choose the rank **Wanderer**. Their Story Point total determined it.

They did choose to equip **Frogleaper** from the titles they had previously unlocked.

A player may own multiple Titles, but can only equip one at a time.

---

# Progression Example

Suppose a player has:

```text
95 SP
9 Token milestones paid
```

They complete an activity worth:

```text
+15 SP
```

Their new Story Point total is:

```text
110 SP
```

Before the reward, they had reached 9 Token milestones.

After the reward, they have reached:

```text
110 / 10 = 11 milestones
```

Therefore:

```text
11 reached
- 9 already paid
----------------
  2 new Tokens
```

The player receives **2 physical Story Tokens**.

They also crossed the 100-SP threshold, so their rank changes from:

```text
Unranked
```

to:

```text
Wanderer
```

and the rank-up is announced to the server.

---

# Administrative Corrections

Beyond the End distinguishes between **normal progression rewards** and **administrative corrections**.

The normal progression command:

```text
/sp add <player> <amount>
```

can award new Tokens and trigger rank-up announcements.

Administrative commands such as:

```text
/sp set <player> <amount>
/sp remove <player> <amount>
```

change the player's SP without automatically granting Tokens.

Additional commands exist for manually repairing or modifying Token milestone history.

See [`COMMANDS.md`](COMMANDS.md) for the complete command reference.

---

# Development Status

The core Story Point, rank, physical Token, and Token milestone systems are implemented.

Future Beyond the End systems will build on this progression foundation, including quests, achievements, professions, community projects, shops, rewards, and other ways to participate in the continuing story of the world.
