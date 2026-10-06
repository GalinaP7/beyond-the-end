# Beyond the End — Command Reference

This page documents the commands currently available in Beyond the End.

Commands are divided into **player commands**, which anyone can use, and **administrator commands**, which require appropriate server permissions.

> Beyond the End is currently in development. Commands may change as new systems are added.

---

# Player Commands

## `/sp`

Displays your current Story Point total and rank.

```text
/sp
```

Example output:

```text
✦ STORY BOARD ✦
⭐ Story Points: 150
⚔ Rank: Wanderer
```

Story Points represent permanent lifetime progression and are not spent when Story Tokens are used.

---

## `/titles`

Displays your currently equipped title and all titles you have unlocked.

```text
/titles
```

Example output:

```text
✦ YOUR TITLES ✦
Equipped: [Frogleaper]
Unlocked:
 • Frogleaper
 • Cartographer
```

A player may unlock multiple titles, but only **one title can be equipped at a time**.

---

## `/titles equip <title>`

Equips one of your unlocked titles.

```text
/titles equip frogleaper
```

You cannot equip a title you have not unlocked.

Equipping a new title automatically replaces your previously equipped title.

---

## `/titles clear`

Removes your currently equipped title.

```text
/titles clear
```

This does **not** remove the title from your unlocked titles.

---

# Administrator Commands

The following commands require administrator permissions.

---

## `/sp add <player> <amount>`

Awards Story Points to a player.

```text
/sp add <player> 15
```

This is the normal command for awarding Story Points.

It:

- increases the player's lifetime Story Points;
- checks whether new 10-SP Token milestones were reached;
- awards any newly earned physical Story Tokens;
- updates Token milestone history; and
- announces a rank-up to the server if the player reached a new rank.

### Example

Suppose a player currently has:

```text
5 SP
0 Token milestones paid
```

Running:

```text
/sp add <player> 15
```

brings the player to:

```text
20 SP
```

The player crossed the **10 SP** and **20 SP** milestones, so they receive:

```text
2 Story Tokens
```

---

## `/sp remove <player> <amount>`

Removes Story Points from a player.

```text
/sp remove <player> 10
```

This is primarily an administrative correction tool.

Removing Story Points does **not** lower the player's Token milestone history.

This prevents previously rewarded Story Point thresholds from being repeatedly crossed to generate additional Tokens.

---

## `/sp set <player> <amount>`

Sets a player's Story Point total to an exact value.

```text
/sp set <player> 150
```

This command does **not** automatically award Story Tokens.

It also does not reset previously paid Token milestones.

This makes it useful for administrative corrections and testing without accidentally creating currency.

---

# Story Token Administration

## `/token give <player> <amount>`

Manually gives a player physical Story Tokens.

```text
/token give <player> 5
```

This gives the player **5 real Story Token items**.

It does **not**:

- add Story Points;
- remove Story Points; or
- change Token milestone history.

This can be used for manual rewards, events, testing, or administrative corrections.

---

## `/token resetmilestones <player>`

Resets a player's hidden Token milestone history to zero.

```text
/token resetmilestones <player>
```

> ⚠️ **Use with caution.**

This command is primarily intended for development and testing.

After resetting milestone history, Story Point milestones the player previously received Tokens for can become eligible again.

This means using `/sp add` afterward may award Tokens for previously reached thresholds.

---

## `/token syncmilestones <player>`

Synchronizes a player's Token milestone history with their current Story Point total.

```text
/token syncmilestones <player>
```

Conceptually, this tells Beyond the End:

> Assume this player has already received every Story Token they are entitled to through their current Story Point total.

For example, if a player has:

```text
150 SP
```

syncing their milestones records:

```text
15 Token milestones paid
```

This command does **not** give the player physical Tokens.

It is useful for safely repairing milestone history.

---

# Title Administration

## `/titles give <player> <title>`

Unlocks a title for a player.

```text
/titles give <player> frogleaper
```

The player can then equip the title using:

```text
/titles equip frogleaper
```

Unlocking a title does **not** automatically equip it.

---

## `/titles revoke <player> <title>`

Removes an unlocked title from a player.

```text
/titles revoke <player> frogleaper
```

If the revoked title is currently equipped, it is also unequipped.

The player will no longer be able to equip that title unless it is unlocked again.

---

# Command Summary

| Command | Access | Purpose |
|---|---|---|
| `/sp` | Everyone | View your Story Points and rank |
| `/titles` | Everyone | View unlocked and equipped titles |
| `/titles equip <title>` | Everyone | Equip an unlocked title |
| `/titles clear` | Everyone | Unequip your current title |
| `/sp add <player> <amount>` | Admin | Award SP and eligible Tokens |
| `/sp remove <player> <amount>` | Admin | Remove SP |
| `/sp set <player> <amount>` | Admin | Set exact SP |
| `/token give <player> <amount>` | Admin | Give physical Story Tokens manually |
| `/token resetmilestones <player>` | Admin | Reset Token milestone history |
| `/token syncmilestones <player>` | Admin | Sync milestone history to current SP |
| `/titles give <player> <title>` | Admin | Unlock a title |
| `/titles revoke <player> <title>` | Admin | Revoke a title |

---

# Placeholder Reference

Arguments surrounded by `< >` should be replaced with actual values when running a command.

For example:

```text
<player> = the target player's Minecraft username
<amount> = a number
<title>  = a valid Beyond the End title
```

So:

```text
/sp add <player> 15
```

means:

> Add 15 Story Points to the selected player.

Do not type the `< >` brackets when actually running the command.

---

## Development Status

This command reference describes the currently implemented Beyond the End systems.

Additional commands will be documented here as new systems are developed.
