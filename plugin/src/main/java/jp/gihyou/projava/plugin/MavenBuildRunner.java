package jp.gihyou.projava.plugin;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class MavenBuildRunner {

    private MavenBuildRunner() {}

    /** プロジェクト直下に pom.xml があるかチェック */
    public static boolean hasPom(Path projectDir) {
        return Files.isDirectory(projectDir) && Files.exists(projectDir.resolve("pom.xml"));
    }

    /**
     * Maven を起動してビルドログを返す。
     * @param projectDir  pom.xml のあるディレクトリ
     * @param goals       例: ["-q","clean","package"] など
     * @param skipTests   true で -DskipTests=true を付与
     * @param timeout     タイムアウト（推奨 10〜15分）
     */
    public static String run(Path projectDir, List<String> goals, boolean skipTests, Duration timeout) throws IOException, InterruptedException {
        if (!hasPom(projectDir)) {
            throw new IllegalArgumentException("指定ディレクトリに pom.xml が見つかりません: " + projectDir);
        }

        // コマンド組み立て（mvn が PATH にある前提）
        List<String> cmd = new ArrayList<>();
        cmd.add(isWindows() ? "mvn.cmd" : "mvn"); // Windowsは mvn.cmd の方が安定
        if (goals != null && !goals.isEmpty()) cmd.addAll(goals);
        if (skipTests) cmd.add("-DskipTests=true");

        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.directory(projectDir.toFile());
        pb.redirectErrorStream(true); // stderr→stdout
        // 文字化け対策：WindowsでもUTF-8で出力するよう Maven に指示（必要に応じて）
        pb.environment().putIfAbsent("MAVEN_OPTS", concatJvmOpts(pb.environment().get("MAVEN_OPTS"), "-Dfile.encoding=UTF-8"));

        Process p = pb.start();

        // 出力読み取り（UTF-8 を第一候補、ダメなら OS 既定）
        Charset cs = StandardCharsets.UTF_8;
        StringBuilder out = new StringBuilder(8 * 1024);
        try (var r = new BufferedReader(new InputStreamReader(p.getInputStream(), cs))) {
            String line;
            long deadlineMs = System.currentTimeMillis() + timeout.toMillis();
            while (true) {
                while (r.ready() && (line = r.readLine()) != null) {
                    out.append(line).append('\n');
                }
                if (p.waitFor(150, TimeUnit.MILLISECONDS)) break; // プロセスが終了したらループ脱出
                if (System.currentTimeMillis() > deadlineMs) {
                    p.destroyForcibly();
                    out.append("\n[ERROR] Timeout: ビルドがタイムアウトしました（").append(timeout).append("）\n");
                    break;
                }
            }
        }

        int exit = p.exitValue();
        out.append("\n[EXIT_CODE] ").append(exit).append('\n');
        return out.toString();
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private static String concatJvmOpts(String base, String extra) {
        if (base == null || base.isBlank()) return extra;
        if (base.contains("-Dfile.encoding")) return base; // 既に指定があれば上書きしない
        return base + " " + extra;
    }
}
