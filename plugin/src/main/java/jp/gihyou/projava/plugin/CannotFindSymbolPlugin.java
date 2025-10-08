package jp.gihyou.projava.plugin;

import java.util.regex.Pattern;

public class CannotFindSymbolPlugin implements BuildErrorPlugin {
    private static final Pattern P = Pattern.compile(
            "cannot find symbol"
                    + "|シンボルを見つけられません",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL
    );

    @Override public int priority() { return 60; }

    @Override public boolean canHandle(String msg) {
        return msg != null && P.matcher(msg).find();
    }

    @Override public Suggestion getSuggestion(String msg) {
        return new Suggestion(
                "cannot-find-symbol",
                "コンパイル時に参照シンボルを解決できません",
                "依存不足・import漏れ・クラス/パッケージ名の誤り・生成コード未出力などが典型です。",
                new String[] {
                        "必要な依存が pom.xml に入っているか（scope:test 限定になっていないか）",
                        "import の漏れ/誤り、クラス名やパッケージ名の綴りを確認",
                        "Java 9+ の場合は module-info.java の exports/opens を確認",
                        "lombok / annotationProcessor 等の生成コードが有効か（IDE設定・compiler設定）"
                }
        );
    }
}