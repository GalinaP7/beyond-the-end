# Beyond the End — Command Reference

Commands currently available in Beyond the End.

---

# 🎮 Player Commands

## `/sp`

Displays your current Story Points and rank.

```text
/sp
```

Example:

```text
✦ STORY BOARD ✦
⭐ Story Points: 150
⚔ Rank: Wanderer
```

---

## `/titles`

Displays your equipped title and all titles you have unlocked.

```text
/titles
```

---

## `/titles equip <title>`

Equips one of your unlocked titles.

```text
/titles equip frogleaper
```

You can only equip titles you have unlocked. Equipping a new title replaces your currently equipped title.

---

## `/titles clear`

Unequips your current title.

```text
/titles clear
```

The title remains unlocked.

---

# ⚙️ Administrator Commands

These commands require administrator permissions.

## Story Points

### `/sp add <player> <amount>`

Awards Story Points to a player.

```text
/sp add <player> 15
```

This is the normal way to award SP. Any newly reached Token milestones are automatically rewarded, and reaching a new rank triggers a rank-up announcement.

### `/sp remove <player> <amount>`

Removes Story Points from a player.

```text
/sp remove <player> 10
```

This does not reset previously rewarded Token milestones.

### `/sp set <player> <amount>`

Sets a player's Story Points to an exact value.

```text
/sp set <player> 150
```

This does not automatically award Story Tokens or reset Token milestone history.

---

## 🪙 Story Tokens

### `/token give <player> <amount>`

Gives physical Story Tokens directly to a player.

```text
/token give <player> 5
```

This does not change the player's Story Points or Token milestone history.

### `/token resetmilestones <player>`

Resets a player's Token milestone history.

```text
/token resetmilestones <player>
```

> ⚠️ **Use with caution.** Previously reached Token milestones may become eligible for rewards again.

### `/token syncmilestones <player>`

Synchronizes a player's Token milestone history with their current Story Points.

```text
/token syncmilestones <player>
```

This does not give physical Tokens.

---

## ⚔ Titles

### `/titles give <player> <title>`

Unlocks a title for a player.

```text
/titles give <player> frogleaper
```

The title is unlocked but not automatically equipped.

### `/titles revoke <player> <title>`

Removes an unlocked title from a player.

```text
/titles revoke <player> frogleaper
```

If the title is currently equipped, it is also unequipped.

---

# Quick Reference

| Command | Access | Purpose |
|---|---|---|
| `/sp` | Player | View Story Points and rank |
| `/titles` | Player | View your titles |
| `/titles equip <title>` | Player | Equip an unlocked title |
| `/titles clear` | Player | Unequip your title |
| `/sp add <player> <amount>` | Admin | Award SP |
| `/sp remove <player> <amount>` | Admin | Remove SP |
| `/sp set <player> <amount>` | Admin | Set SP |
| `/token give <player> <amount>` | Admin | Give Story Tokens |
| `/token resetmilestones <player>` | Admin | Reset Token milestone history |
| `/token syncmilestones <player>` | Admin | Sync Token milestone history |
| `/titles give <player> <title>` | Admin | Unlock a title |
| `/titles revoke <player> <title>` | Admin | Revoke a title |

---

`<player>`, `<amount>`, and `<title>` represent values you replace when running the command. Do not include the `< >` brackets.
