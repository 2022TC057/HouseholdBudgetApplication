package jp.gihyou.projava.plugin;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception {
        PluginManager manager = new PluginManager();
        manager.registerPlugin(new JavaVersionErrorPlugin());
        manager.registerPlugin(new NoPomPlugin());
        manager.registerPlugin(new CompilerOptionTooOldPlugin());
        manager.registerPlugin(new Auth401Plugin());
        manager.registerPlugin(new DependencyNotFoundPlugin());
        manager.registerPlugin(new CannotFindSymbolPlugin());

        // 入力処理（ここはあなたの現在の仕様に合わせてOK）
        String errorLog;


        if (args.length >= 2 && "--project".equals(args[0])) {
            var dir = Path.of(BuildLogImporter.normalizeDroppedPath(args[1]));
            boolean skipTests = hasFlag(args, "--skipTests");

            System.out.println("[INFO] ビルド対象: " + dir.toAbsolutePath());
            if (!MavenBuildRunner.hasPom(dir)) {
                System.err.println("[ERROR] pom.xml が見つかりません。--project には Maven プロジェクトのルートを指定してください。");
                return;
            }

            try {
                errorLog = MavenBuildRunner.run(
                        dir,
                        List.of("-q", "clean", "package"), // 実行したいゴール
                        skipTests,                         // --skipTests があればテスト省略
                        java.time.Duration.ofMinutes(15)   // タイムアウト
                );
            } catch (Exception e) {
                System.err.println("[ERROR] Maven 実行に失敗: " + e.getMessage());
                return;
            }

        } else if (args.length >= 2 && "--file".equals(args[0])) {
            // 例: mvn -q compile exec:java -Dexec.args="--file C:\logs\err.txt"
            var path = BuildLogImporter.normalizeDroppedPath(args[1]);
            errorLog = Files.readString(Path.of(path), StandardCharsets.UTF_8);

        } else if (args.length >= 1 && "--paste".equals(args[0])) {
            // 例: mvn -q compile exec:java -Dexec.args="--paste"
            System.out.println("エラーログを貼り付けて、Ctrl+Z → Enter（macは Ctrl+D）:");
            errorLog = new String(System.in.readAllBytes(), StandardCharsets.UTF_8);

        } else {
            // 既定: D&D で .txt パス入力（Anaconda Prompt 向け）
            System.out.println("エラーログ .txt をここにドラッグ＆ドロップして Enter:");
            try (var sc = new Scanner(System.in)) {
                String path = BuildLogImporter.normalizeDroppedPath(sc.nextLine());
                errorLog = Files.readString(Path.of(path), StandardCharsets.UTF_8);
            } catch (Exception e) {
                System.err.println("[ERROR] ファイル読み込みに失敗: " + e.getMessage());
                return;
            }
        }

        List<Suggestion> suggestions = manager.analyze(errorLog);
        if (suggestions.isEmpty()) {
            System.out.println("対応する修正提案が見つかりませんでした。");
        } else {
            System.out.println("=== 提案一覧 ===");
            for (var s : suggestions) {
                System.out.println();
                System.out.println("== " + s.title() + " [" + s.id() + "] ==");
                if (s.detail() != null && !s.detail().isBlank()) {
                    System.out.println("説明: " + s.detail());
                }
                if (s.steps() != null && s.steps().length > 0) {
                    System.out.println("対応手順:");
                    for (var step : s.steps()) {
                        System.out.println(" - " + step);
                    }
                }
            }
        }
    }

    private static boolean hasFlag(String[] args, String flag) {
        for (String a : args) {
            if (flag.equals(a)) return true;
        }
        return false;
    }
}