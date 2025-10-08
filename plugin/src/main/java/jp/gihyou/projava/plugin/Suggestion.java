package jp.gihyou.projava.plugin;

public record Suggestion(
        String id,        // "auth-401" など
        String title,     // 見出し
        String detail,    // 詳細説明
        String[] steps    // 手順（行ごとに）
) {}
