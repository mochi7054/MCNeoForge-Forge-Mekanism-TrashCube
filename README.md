# Mekanism TrashCube (ごみキューブ)

A Mekanism addon mod that introduces a tierless **Trash Cube (ごみキューブ)** block designed to instantly void/delete items, fluids, chemicals (gas, infusion, pigment, slurry), and energy connection inputs.

Mekanismの各種搬入（アイテム、液体、気体/各種化学物質、エネルギー）を即時に消滅させるティアなしのブロック「ごみキューブ」を追加するアドオンModです。

---

## Features (機能特徴)

- **1-Slot Unlimited Inventory (1スロット・無限スタック入力)**
  - Reverted from grid layouts to a single inventory slot centered in the GUI.
  - Custom capacity slot implementation overrides standard limits (`Integer.MAX_VALUE`), allowing pipeline automation and player drop actions to dump massive item counts in a single operation.
  - GUI incorporates a centered face emoticon (`> <`) and helpful directive translation ("Throw it here!" / "ここに投げて！") right above the slot.
  - GUIのスロットは中央に1つのみ配置され、アイテム制限（通常64個）を無効化（最大 `Integer.MAX_VALUE` 個）して一度に大量のアイテムを投入可能です。スロットの上部にはかわいい顔文字（`> <`）とメッセージ（「ここに投げて！」）が表示されます。

- **Instant Voiding (即時消滅)**
  - All inserted contents (items, fluids, chemicals, and energy) are instantly deleted on insertion (`onContentsChanged` trigger) rather than waiting for server ticks, preventing ghost/invisible items in GUI slots.
  - 投入されたアイテム、液体、気体、エネルギーはサーバースレッドの次のティック同期を待たず、スロットに搬入された瞬間に即時消滅します（GUIでの不可視ゴーストアイテムの発生を防ぎます）。

- **Bypasses Caching - Strict Side Config (搬入設定の完全遵守)**
  - Integrates directly with Mekanism's capability system. Side configuration settings (`INPUT` / `NONE`) are strictly respected, ensuring that connected automation pipes and cables will not import items if the side config is set to `NONE`.
  - MekanismのSide Configuration（搬入設定）に完全対応。搬入設定が `NONE` になっている面からは、パイプやケーブルなどを繋いでもアイテムや液体等は一切搬入されません。

- **Tab Features (タブ機能)**
  - **Security Tab (セキュリティー設定)**: Standard Mekanism protection.
  - **Redstone Tab (レッドストーン制御)**: Standard Mekanism redstone signal behavior.
  - **Upgrade Tab (アップグレードスロット)**: Supports **Anchor Upgrade only** (アンカーアップグレードのみ対応) to keep the chunk loaded.
  - **Side Config Tab (搬入面設定)**: Allows setting side configurations for Items, Fluids, Chemicals, and Energy to **Input** or **None**.

- **Advancement (進捗)**
  - Includes a custom advancement **"Trash Cubed" (ごみの3乗)** tab, unlocked as soon as the player obtains a Trash Cube in their inventory.
  - ごみキューブを取得すると、進捗「**ごみの3乗**」が解除されます（進捗タブ自体のツールチップ名はクリエイティブタブと同様に「Mekanism: TrashCube」となります）。

---

## Crafting Recipe (レシピ)

Crafted using standard Mekanism logistical components:
Mekanismの基本的な搬送用資材や鋼鉄ケーシングを用いてクラフト可能です。

| Row (行) | Left (左) | Center (中央) | Right (右) |
| :--- | :--- | :--- | :--- |
| **Top (上段)** | Refined Glowstone Ingot<br>(精製グローストーンインゴット) | Basic Logistical Transporter<br>(基本物流トランスポーター) | Refined Glowstone Ingot<br>(精製グローストーンインゴット) |
| **Middle (中段)** | Basic Mechanical Pipe<br>(基本メカニカルパイプ) | Steel Casing<br>(鋼鉄ケーシング) | Basic Pressurized Tube<br>(基本加圧チューブ) |
| **Bottom (下段)** | Refined Glowstone Ingot<br>(精製グローストーンインゴット) | Basic Universal Cable<br>(基本ユニバーサルパイプ) | Refined Glowstone Ingot<br>(精製グローストーンインゴット) |

### Recipe Pattern Map
```
[ G ] [ T ] [ G ]
[ M ] [ C ] [ P ]
[ G ] [ U ] [ G ]
```
- **G**: Refined Glowstone Ingot (`mekanism:ingot_refined_glowstone`)
- **T**: Basic Logistical Transporter (`mekanism:basic_logistical_transporter`)
- **M**: Basic Mechanical Pipe (`mekanism:basic_mechanical_pipe`)
- **C**: Steel Casing (`mekanism:steel_casing`)
- **P**: Basic Pressurized Tube (`mekanism:basic_pressurized_tube`)
- **U**: Basic Universal Cable (`mekanism:basic_universal_cable`)

---

## Supported Versions (対応環境)

This repository contains two branches corresponding to different Minecraft mod loaders and versions:
本Modはモッドローダーおよびバージョン別に2つのブランチに分かれて管理・提供されています。

1. **Minecraft 1.20.1 Forge** - Branch: [`1.20.1-forge`](https://github.com/mochi7054/MCNeoForge-Forge-Mekanism-TrashCube/tree/1.20.1-forge)
2. **Minecraft 1.21.1 NeoForge** - Branch: [`1.21.1-neoforge`](https://github.com/mochi7054/MCNeoForge-Forge-Mekanism-TrashCube/tree/1.21.1-neoforge)

---

## Building from Source (ビルド方法)

To build the mod `.jar` file from source, clone the appropriate branch and execute the Gradle build command:
ソースコードからModの `.jar` ファイルをコンパイルする場合は、該当するブランチをチェックアウトし、以下のコマンドを実行します。

```bash
# Clone the repository
git clone https://github.com/mochi7054/MCNeoForge-Forge-Mekanism-TrashCube.git
cd MCNeoForge-Forge-Mekanism-TrashCube

# Checkout your target branch (Forge or NeoForge)
git checkout 1.20.1-forge  # Or: git checkout 1.21.1-neoforge

# Clean compile and build
./gradlew clean build --no-configuration-cache
```

The compiled jar file will be located at:
ビルド成功後、ビルド済みのjarファイルは以下のパスに生成されます。
- `build/libs/mekanismtrashcube-[version]-[loader].jar`

---

## License (ライセンス)

This project is licensed under the **MIT License**.
本プロジェクトは **MITライセンス** の下で提供されています。
