package jp.gihyou.projava.plugin;

import java.util.regex.Pattern;

public class CompilerOptionTooOldPlugin implements BuildErrorPlugin {
    private static final Pattern P = Pattern.compile(
            "ソース.?オプション\\s*\\d+.*?8以降を使用してください"
                    + "|ターゲット.?オプション\\s*\\d+.*?8以降を使用してください"
                    + "|Source option\\s*\\d+\\s*is no longer supported\\.\\s*Use\\s*\\d+\\s*or later"
                    + "|Target option\\s*\\d+\\s*is no longer supported\\.\\s*Use\\s*\\d+\\s*or later",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    @Override public int priority() { return 70; }

    @Override public boolean canHandle(String msg) { return msg != null && P.matcher(msg).find(); }

    @Override public Suggestion getSuggestion(String msg) {
        return new Suggestion(
                "compiler-too-old",
                "コンパイラオプションが古すぎます",
                "maven-compiler-plugin の source/target（または release）が古すぎます。",
                new String[]{
                        "pom.xml の <maven.compiler.source>/<maven.compiler.target> を 8 以上に設定",
                        "または <maven-compiler-plugin> の <release>8</release> を設定",
                        "一時対応: mvn -Dmaven.compiler.release=8 compile"
                }
        );
    }
}

