package jp.gihyou.projava.plugin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompilerOptionTooOldPluginTest {
    @Test
    void positive_detectsJapaneseMsg() {
        var log = TestUtil.readRes("logs/source_target_old_ja.txt");
        var p = new CompilerOptionTooOldPlugin();
        assertTrue(p.canHandle(log));

        var s = p.getSuggestion(log);
        assertNotNull(s);
        assertTrue(s.id().equals("compiler-too-old") || s.title().contains("コンパイラ"));
        assertTrue(String.join("\n", s.steps()).toLowerCase().contains("maven-compiler-plugin"));
    }

    @Test
    void negative_ignoresSuccess() {
        assertFalse(new CompilerOptionTooOldPlugin().canHandle("BUILD SUCCESS"));
    }
}

