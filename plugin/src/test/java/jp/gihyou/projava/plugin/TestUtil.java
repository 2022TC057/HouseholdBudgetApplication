package jp.gihyou.projava.plugin;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class TestUtil {
    private TestUtil() {}

    /** 例: readRes("logs/source_target_old_ja.txt") */
    public static String readRes(String relPath) {
        try {
            var url = TestUtil.class.getClassLoader().getResource(relPath);
            if (url == null) throw new IllegalArgumentException("resource not found: " + relPath);
            return Files.readString(Path.of(url.toURI()), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("read failed: " + relPath, e);
        }
    }
}
