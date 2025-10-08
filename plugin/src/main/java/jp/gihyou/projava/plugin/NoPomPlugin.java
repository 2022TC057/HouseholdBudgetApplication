package jp.gihyou.projava.plugin;

import java.util.regex.Pattern;

public class NoPomPlugin implements BuildErrorPlugin {
    private static final Pattern P = Pattern.compile(
            "there is no POM in this directory"
                    + "|MissingProjectException"
                    + "|The goal you specified requires a project to execute",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    @Override public int priority() { return 95; } // 最初に気づきたい系なので高め

    @Override public boolean canHandle(String msg) {
        return msg != null && P.matcher(msg).find();
    }

    @Override public Suggestion getSuggestion(String msg) {
        return new Suggestion(
                "no-pom",
                "Maven が pom.xml を見つけられていません",
                "実行ディレクトリがプロジェクト直下でない、または pom.xml の場所指定が必要です。",
                new String[] {
                        "pom.xml の存在するディレクトリへ移動して実行（cd path/to/project）",
                        "または: mvn -f path/to/pom.xml <goal> のように -f で明示指定",
                        "IntelliJ は右側の Maven タブから実行して、実行ディレクトリの取り違えを防止"
                }
        );
    }
}
