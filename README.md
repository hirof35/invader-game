# Galactic War (Java Space Invaders)

Java Swingを使用して開発された、シンプルながら拡張性の高い縦スクロール型シューティングゲームです。

## 概要
本作は、往年のアーケードゲーム『スペースインベーダー』の基本メカニクスをベースに、Javaの標準ライブラリのみで構築されています。オブジェクトの衝突判定、リアルタイムの入力制御、およびゲームループの実装といった、ゲーム開発の基礎を網羅しています。

## 特徴
*   **低遅延な操作性**: `KeyListener` のフラグ管理により、スムーズな左右移動と射撃を実現。
*   **構造的設計**: プレイヤー、敵（エイリアン）、弾丸の各エンティティをリスト管理し、効率的な当たり判定（Intersection）を実装。
*   **動的難易度**: 敵が壁に到達するたびに降下速度が上がり、プレイヤーに緊張感を与えます。
*   **レトロなUI**: シンプルな2Dグラフィックスと、スコア・ライフ表示機能を搭載。

## 操作方法
| キー | アクション |
| :--- | :--- |
| **← / →** | プレイヤーの移動 |
| **SPACE** | 弾を発射 |
| **R** | ゲームオーバー/クリア後のリスタート |

## システム構成
*   **言語**: Java 8以上
*   **ライブラリ**: `javax.swing`, `java.awt` (外部依存関係なし)
*   **解像度**: 500x550 (固定)
*   **フレームレート**: 約60 FPS (Timer 16ms)

## 導入手順
1.  **リポジトリをクローン**
    ```bash
    git clone [https://github.com/yourusername/GalacticWar.git](https://github.com/yourusername/GalacticWar.git)
    ```
2.  **コンパイル**
    ```bash
    javac galacticWar/GalacticWar.java
    ```
3.  **実行**
    ```bash
    java galacticWar.GalacticWar
    ```

## 今後の拡張予定 (Roadmap)
*   [ ] **サウンドの実装**: `javax.sound.sampled` を用いたSE（射撃音、爆発音）の追加。
*   [ ] **エンティティの抽象化**: `GameObject` クラスを定義し、ポリモーフィズムによる描画・更新処理の共通化（構造の最適化）。
*   [ ] **敵パターンの多様化**: 弾を撃たない偵察機や、耐久力のある大型機の導入。
*   [ ] **エフェクトの強化**: 敵撃破時のパーティクル・エフェクトの実装。

## ライセンス
[MIT License](LICENSE)

---


<img width="623" height="720" alt="スクリーンショット 2026-05-10 112214" src="https://github.com/user-attachments/assets/7f39b8eb-75f3-4d9b-a227-8582533dc029" />
