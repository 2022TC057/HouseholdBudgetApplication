package jp.gihyou.projava.plugin;

import java.util.*;
import java.util.stream.Collectors;

public class PluginManager {
    private final List<BuildErrorPlugin> plugins = new ArrayList<>();

    public void registerPlugin(BuildErrorPlugin plugin) { plugins.add(plugin); }

    /** ヒットした提案を優先度順で返す */
    public List<Suggestion> analyze(String errorMessage) {
        return plugins.stream()
                .filter(p -> p.canHandle(errorMessage))
                .sorted(Comparator.comparingInt(BuildErrorPlugin::priority).reversed())
                .map(p -> p.getSuggestion(errorMessage))
                .collect(Collectors.toList());
    }
}
