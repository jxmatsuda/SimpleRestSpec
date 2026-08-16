# 依頼内容
REST APIの仕様管理システム「SimpleRestSpec」のバックエンド初期実装を行いたいです。
Mavenで作成した空のプロジェクト（`pom.xml` と `src` フォルダが存在する状態）に配置するコード類を一式作成してください。

## システム仕様および環境要件
- **フレームワーク**: Spring Boot 3.x（Jakarta EE / Tomcat 10対応）
- **Javaバージョン**: 17以上
- **データベース**: SQLite3
    - DBファイル格納先: `~/.simpleRestSpec/simplerestspec.db`（起動時にディレクトリが存在しない場合は自動生成すること）
- **ビルド形式**: WAR形式（またはTomcat 10上で動作可能な構成）

## データ構造（エンティティ設計）
以下の3つのエンティティ（テーブル）を作成してください。

1. **ApiSpec**
    - `id`: Long (PK, 自動採番)
    - `name`: String (API名)
    - `path`: String (例: /api/v1/users/{id})
    - `method`: String ('GET' または 'POST')
    - `postType`: String ('JSON' または 'MULTIPART'、POST時のみ使用)
    - `requestParams`: List<RequestParamSpec> ( OneToMany / CascadeType.ALL )
    - `responsePatterns`: List<ResponsePatternSpec> ( OneToMany / CascadeType.ALL )

2. **RequestParamSpec**
    - `paramType`: String ('PATH', 'QUERY', 'BODY', 'FILE')
    - `name`: String (パラメータ名)
    - `dataType`: String (型名: string, integer, file 等)
    - `required`: Boolean (必須フラグ)
    - `description`: String (説明)

3. **ResponsePatternSpec**
    - `id`: Long (PK, 自動採番)
    - `patternName`: String (例: 「正常系」「認証エラー」)
    - `statusCode`: Integer (例: 200, 400)
    - `responseType`: String ('JSON' または 'BINARY')
    - `bodyJson`: String (JSONデータ。長文テキスト保存対応)

## 実装してほしいファイル・クラス
1. `pom.xml`（Spring Boot 3, SQLite JDBC, Hibernate Community Dialects, Lombok 等の必要な依存関係）
2. `src/main/resources/application.yml`（SQLite接続設定、Hibernateの方言設定）
3. 起動時に `~/.simpleRestSpec` ディレクトリを作成する初期化クラス（`CommandLineRunner` 等）
4. JPA Entity クラス群（`ApiSpec`, `RequestParamSpec`, `ResponsePatternSpec`）
5. Repository インターフェース（`ApiSpecRepository`）
6. API仕様情報のCRUD操作を行う REST Controller（`ApiSpecController`）
    - API一覧取得（GET `/api/specs`）
    - API詳細取得（GET `/api/specs/{id}`）
    - API登録（POST `/api/specs`）
    - API更新（PUT `/api/specs/{id}`）
    - API削除（DELETE `/api/specs/{id}`）

## 出力フォーマット
各ファイルの配置パス（例: `src/main/java/com/example/simplerestspec/entity/ApiSpec.java`）を明記した上で、コードを出力してください。