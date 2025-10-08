package jp.gihyou.projava.plugin;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class BuildLogImporter {
    private BuildLogImporter() {}

    public static String readTxt(Path path) throws Exception {
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    /** Windowsのドラッグ＆ドロップで付く二重引用符を除去 */
    public static String normalizeDroppedPath(String raw) {
        if (raw == null) return null;
        raw = raw.trim();
        if (raw.startsWith("\"") && raw.endsWith("\"") && raw.length() >= 2) {
            return raw.substring(1, raw.length() - 1);
        }
        return raw;
    }
}
