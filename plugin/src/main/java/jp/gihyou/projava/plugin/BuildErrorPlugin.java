package jp.gihyou.projava.plugin;

public interface BuildErrorPlugin {
    /** 数字が大きいほど優先。例: 100 = 最重要、10 = 中、1 = 補助 */
    default int priority() { return 50; }

    boolean canHandle(String errorMessage);

    /** 詳細な提案（構造化） */
    Suggestion getSuggestion(String errorMessage);
}
