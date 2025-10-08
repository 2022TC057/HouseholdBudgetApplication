package jp.gihyou.projava.plugin;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Auth401PluginTest {
    @Test
    void positive_detects401() {
        var log = TestUtil.readRes("logs/auth_401.txt");
        var p = new Auth401Plugin();
        assertTrue(p.canHandle(log));

        var s = p.getSuggestion(log);
        assertNotNull(s);
        assertTrue(s.id().equals("auth-401") || s.title().contains("認証"));
    }

    @Test
    void negative_ignoresSuccess() {
        assertFalse(new Auth401Plugin().canHandle("BUILD SUCCESS"));
    }
}

