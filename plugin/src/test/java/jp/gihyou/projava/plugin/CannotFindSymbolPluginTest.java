package jp.gihyou.projava.plugin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CannotFindSymbolPluginTest {
    @Test
    void positive_detectsCannotFind() {
        var log = TestUtil.readRes("logs/cannot_find_symbol.txt");
        var p = new CannotFindSymbolPlugin();
        assertTrue(p.canHandle(log));

        var s = p.getSuggestion(log);
        assertNotNull(s);
        var t = (s.title() + " " + s.id()).toLowerCase();
        assertTrue(t.contains("cannot-find-symbol") || t.contains("シンボル"));
    }

    @Test
    void negative_ignoresSuccess() {
        assertFalse(new CannotFindSymbolPlugin().canHandle("BUILD SUCCESS"));
    }
}
