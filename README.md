# SimpleRestSpec
個人用のREST APIの管理ツールです。
- context は RESTサービス単位で管理します。
- context の下に そのRESTサービス内のAPIのIN/OUT情報を登録します。
- context単位でRESTのAPI情報を Export/Import できます。

## 事前準備
事前に 姉妹プロジェクトの simplerestspec-ui で npm run build で作成する
dist 以下の成果物を、<br>
本プロジェクトの src/main/resources/static の下にコピーしてください。<br>

なお、static/assets は毎回違うファイル名でビルドされるので、
コピー前に古いファイルを削除しておいてください。

## ビルド方法
mvn package でビルドすると<br>
target/SimpleRestSpec.jar ができます。

## 実行方法
java -jar target/SimpleRestSpec.jar で
http://localhost:8080/SimpleRestSpec を<br>
ブラウザで開くとトップぺージを表示します。

## 起動ポートの変更方法
java -jar -Dserver.port=AAA target/SimpleRestSpec.jar で<br>
起動ポートを AAA に変更することができます。
