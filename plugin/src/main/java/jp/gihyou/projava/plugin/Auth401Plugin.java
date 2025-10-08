package jp.gihyou.projava.plugin;

import java.util.regex.Pattern;

public class Auth401Plugin implements BuildErrorPlugin {
    private static final Pattern P = Pattern.compile(
            "status code:\\s*401|Unauthorized|authentication failed|requires authentication",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    @Override public int priority() { return 90; } // 高め

    @Override public boolean canHandle(String msg) {
        return msg != null && P.matcher(msg).find();
    }

    @Override public Suggestion getSuggestion(String msg) {
        return new Suggestion(
                "auth-401",
                "依存取得の認証エラー（401 Unauthorized）",
                "認証情報が不足/無効の可能性があります。GitHub Packages 等ではトークン要件に注意。",
                new String[]{
                        "~/.m2/settings.xml に <servers> を設定（<id> は pom の repository の id と一致）",
                        "GitHub Packages: Personal Access Token に read:packages 権限を付与し <password> へ",
                        "リポジトリURL/権限/期限切れトークンを確認"
                }
        );
    }
}

