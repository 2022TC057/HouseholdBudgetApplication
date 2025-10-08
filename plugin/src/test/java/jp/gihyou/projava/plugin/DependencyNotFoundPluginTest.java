package jp.gihyou.projava.plugin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DependencyNotFoundPluginTest {
    @Test
    void positive_depMissing() {
        var log = TestUtil.readRes("logs/dep_not_found.txt");
        var p = new DependencyNotFoundPlugin();
        assertTrue(p.canHandle(log));

        var s = p.getSuggestion(log);
        assertNotNull(s);
        var t = (s.title() + " " + s.id()).toLowerCase();
        assertTrue(t.contains("依存") || t.contains("dep-not-found"));
    }

    @Test
    void negative_ignoresSuccess() {
        assertFalse(new DependencyNotFoundPlugin().canHandle("BUILD SUCCESS"));
    }
}
