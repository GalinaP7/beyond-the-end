# Beyond the End — Installation Guide

Beyond the End is currently developed for **Minecraft 26.2 using Fabric**.

---

# Requirements

| Requirement | Version |
|---|---|
| Minecraft | 26.2 |
| Mod Loader | Fabric |
| Fabric Loader | 0.19.3+ |
| Fabric API | 0.154.2+26.2 |
| Beyond the End | Current development build |

Beyond the End currently supports **Fabric**. Forge and NeoForge are not supported.

---

# 🎮 Player Installation

To join a server running Beyond the End, you will need a compatible Fabric profile with Beyond the End installed.

## 1. Create or Select a Fabric Profile

Using a Minecraft launcher or mod manager such as CurseForge, create or select a profile using:

```text
Minecraft 26.2
Fabric
```

---

## 2. Install Fabric API

Install **Fabric API** for Minecraft 26.2.

If you are using CurseForge, you can install it through the profile's **Add Content** option.

---

## 3. Get Beyond the End

Obtain the current Beyond the End `.jar` file.

During development, the file may have a name similar to:

```text
storysystem-1.0.2.jar
```

> **Do not extract the `.jar` file.**
>
> If the mod was provided inside a ZIP file, extract the **ZIP**, then locate the `.jar` inside it.

---

## 4. Add the Mod

In CurseForge:

```text
My Modpacks
    ↓
Select your profile
    ↓
⋮
    ↓
Open Folder
```

Open the `mods` folder and place the Beyond the End `.jar` inside.

Your folder should look roughly like:

```text
your-profile/
└── mods/
    ├── fabric-api-....jar
    ├── storysystem-1.0.2.jar
    └── other-mods....
```

---

## 5. Launch Minecraft

Completely restart Minecraft if it is already running.

Launch your Fabric profile and join the server running Beyond the End.

That's it!

---

# ⭐ SIMPLE — Player Installation

If you just want to get the mod installed, follow the pictures below.

## Step 1 — Open Your CurseForge Profile

In CurseForge, click your Minecraft profile.

Then click the **three dots (⋮)**:

![Open the CurseForge profile menu](images/installation/01-profile-menu.png)

---

## Step 2 — Open the Profile Folder

Click **Open Folder**.

![Click Open Folder in CurseForge](images/installation/02-open-folder.png)

---

## Step 3 — Open the Mods Folder

Open the:

```text
mods
```

folder.

![Open the mods folder](images/installation/03-mods-folder.png)

It should look something like this, along with any other mods you already have:

![Example mods folder](images/installation/04-mods-folder-example.png)

---

## Step 4 — Get the Mod File

You will be given the current Beyond the End mod.

If it was given to you inside a **ZIP file**, right-click the ZIP and select:

**Extract All...**

![Right-click the ZIP and select Extract All](images/installation/05-extract-all.png)

Extract it to any location:

![Choose where to extract the ZIP](images/installation/06-extract-location.png)

Once it opens, find the Beyond the End `.jar` file:

![Find the Beyond the End JAR](images/installation/07-find-jar.png)

> **Important:** Extract the ZIP, not the `.jar`.
>
> The `.jar` is the actual mod file.

---

## Step 5 — Put It in Your Mods Folder

Drag the Beyond the End `.jar` into your `mods` folder:

![Drag the Beyond the End JAR into the mods folder](images/installation/08-drag-jar.png)

**THAT'S IT!**

---

## Step 6 — Play!

Go back to CurseForge and click **Play**:

![Launch the Minecraft profile](images/installation/09-play.png)

You can now join the server running Beyond the End.

---

# 🖥 Server Installation

To install Beyond the End on a compatible Fabric server:

1. Stop the server.
2. Make sure the server is running the supported Minecraft version with Fabric.
3. Install the compatible version of Fabric API.
4. Place the Beyond the End `.jar` inside the server's `mods` folder.
5. Start the server.
6. Check the server console to make sure the mod loaded successfully.

A typical server setup will look roughly like:

```text
server/
├── mods/
│   ├── fabric-api-....jar
│   └── storysystem-1.0.2.jar
├── config/
├── world/
└── ...
```

Always stop the server before replacing or updating the mod.

---

# 🔄 Updating Beyond the End

## Players

When you receive a new version of Beyond the End:

1. Close Minecraft.
2. Open your profile's `mods` folder.
3. Remove the old Beyond the End `.jar`.
4. Add the new `.jar`.
5. Launch Minecraft again.

Do not keep multiple versions of Beyond the End in the same `mods` folder.

## Server Owners

When updating Beyond the End on a server:

1. Stop the server.
2. Back up the world.
3. Remove the old Beyond the End `.jar`.
4. Add the new `.jar`.
5. Start the server.
6. Check the console to make sure the mod loaded successfully.
