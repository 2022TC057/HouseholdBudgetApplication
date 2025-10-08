package jp.gihyou.projava.plugin;

import java.util.regex.Pattern;

public class DependencyNotFoundPlugin implements BuildErrorPlugin {
    private static final Pattern P = Pattern.compile(
            "Could not (find|resolve) artifact"
                    + "|Failure to find .* status code:\\s*404"
                    + "|Could not resolve dependencies for project",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    @Override public int priority() { return 75; }

    @Override public boolean canHandle(String msg) {
        return msg != null && P.matcher(msg).find();
    }

    @Override public Suggestion getSuggestion(String msg) {
        return new Suggestion(
                "dep-not-found",
                "依存アーティファクトが見つかりません（404/解決失敗）",
                "座標の誤り・非中央リポジトリの未設定・一時的なレポ障害などが原因になりがちです。",
                new String[] {
                        "groupId/artifactId/version の綴りと存在するバージョンを確認",
                        "必要なリポジトリを pom.xml の <repositories> に追加（中央以外を使う場合）",
                        "mvn -U clean verify でメタデータ更新を強制",
                        "プロキシ/ミラー/社内レポ設定（~/.m2/settings.xml）を確認"
                }
        );
    }
}
