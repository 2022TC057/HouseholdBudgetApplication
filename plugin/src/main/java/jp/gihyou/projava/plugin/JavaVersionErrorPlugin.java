package jp.gihyou.projava.plugin;

import java.util.regex.Pattern;

public class JavaVersionErrorPlugin implements BuildErrorPlugin {
    private static final Pattern P = Pattern.compile(
            // 代表的な文言を多言語で網羅
            "UnsupportedClassVersionError"
                    + "|has been compiled by a more recent version of the Java Runtime"
                    + "|unsupported major\\.minor version"
                    + "|bad class file .* major version \\d+",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    @Override public int priority() { return 80; }

    @Override public boolean canHandle(String msg) {
        return msg != null && P.matcher(msg).find();
    }

    @Override public Suggestion getSuggestion(String msg) {
        return new Suggestion(
                "jdk-mismatch",
                "JDKバージョン不一致（クラスファイルの互換性エラー）",
                "ビルド/実行に用いるJDKのメジャーが一致していない可能性があります。",
                new String[] {
                        "IntelliJ の Project SDK / Language Level を使用ライブラリに合わせる（例: 17）",
                        "pom.xml に <maven.compiler.release> を設定（例: 17）",
                        "依存ライブラリや生成物（.class/.jar）がより新しいJavaでコンパイルされていないか確認",
                        "CLI 実行では java -version / mvn -v のJDKが一致しているか確認"
                }
        );
    }
}

