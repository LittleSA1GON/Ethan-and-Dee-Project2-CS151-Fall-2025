package game.gamemanager;

import java.util.List;
import java.util.ArrayList;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FileManager {
    private static final Path TXT_DIR = Paths.get("txtfiles");
    private static final Path USERS_FILE = TXT_DIR.resolve("user_accounts.txt");
    private static final Path HIGHSCORES_FILE = TXT_DIR.resolve("high_scores.txt");
    private static final Path BLACKJACK_FILE = TXT_DIR.resolve("blackjack.txt");
    private static final SecureRandom RNG = new SecureRandom();

    public static void init() throws IOException {
        if (!Files.exists(TXT_DIR)) {
            Files.createDirectories(TXT_DIR);
        }
        if (!Files.exists(USERS_FILE)) {
            Files.createFile(USERS_FILE);
        }
        if (!Files.exists(HIGHSCORES_FILE)) {
            Files.createFile(HIGHSCORES_FILE);
        }
        if (!Files.exists(BLACKJACK_FILE)) {
            Files.createFile(BLACKJACK_FILE);
        }
    }

    public static void initGlobalScoreboard() throws IOException {
        init();

        if (!Files.exists(HIGHSCORES_FILE) || Files.size(HIGHSCORES_FILE) == 0) {
            List<String> defaultLines = new ArrayList<>();

            StringBuilder blackjackLine = new StringBuilder("blackjack");
            StringBuilder snakeLine = new StringBuilder("snake");

            for (int i = 1; i <= 5; i++) {
                blackjackLine.append(":").append("User").append(i).append("+1000");
                snakeLine.append(":").append("User").append(i).append("+1000");
            }

            defaultLines.add(blackjackLine.toString());
            defaultLines.add(snakeLine.toString());

            Files.write(HIGHSCORES_FILE, defaultLines, StandardCharsets.UTF_8);
        }
    }

    
    public static boolean saveUserAccount(String username, String password) throws IOException, NoSuchAlgorithmException {
        init();
        String hash = bytesToHex(sha256(password.getBytes(StandardCharsets.UTF_8)));
        List<String> lines = Files.readAllLines(USERS_FILE, StandardCharsets.UTF_8);

        boolean replaced = false;
        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i).trim();

            if (line.startsWith(username + ":")) {
                lines.set(i, username + ":" + hash);
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            lines.add(username + ":" + hash);
        }
        Files.write(USERS_FILE, lines, StandardCharsets.UTF_8);
        return true;
    }

    public static boolean verifyUserAccount(String username, String password) throws IOException, NoSuchAlgorithmException {
        init();
        if (!Files.exists(USERS_FILE)){
            return false;
        }
        List<String> lines = Files.readAllLines(USERS_FILE, StandardCharsets.UTF_8);
        String targetHash = bytesToHex(sha256(password.getBytes(StandardCharsets.UTF_8)));

        for (String line : lines) {
            String[] parts = line.split(":", 2);
            if (parts.length == 2 && parts[0].equals(username)) {
                return parts[1].trim().equalsIgnoreCase(targetHash);
            }
        }
        return false;
    }

    public static void saveHighScore(String username, int score) throws IOException {
        init();
        String entry = username + ":" + score + System.lineSeparator();
        Files.write(HIGHSCORES_FILE, entry.getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }

    public static List<String> getHighScores() throws IOException {
        init();
        if (!Files.exists(HIGHSCORES_FILE)) return new ArrayList<>();
        return Files.readAllLines(HIGHSCORES_FILE, StandardCharsets.UTF_8);
    }

    public static void initUserHighScores(String username) throws IOException {
        initGlobalScoreboard();
        updateGlobalScore(username, 1000, "snake");
        updateGlobalScore(username, 1000, "blackjack");
    }

    public static void saveBlackjackSave(String username, String plainJson, String password) throws Exception {
        init();
        byte[] keyBytes = sha256(password.getBytes(StandardCharsets.UTF_8));
        SecretKeySpec key = new SecretKeySpec(keyBytes, 0, 32, "AES");

        byte[] iv = new byte[16];
        RNG.nextBytes(iv);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
        byte[] cipherBytes = cipher.doFinal(plainJson.getBytes(StandardCharsets.UTF_8));

        String encoded = Base64.getEncoder().encodeToString(iv) + ":" + Base64.getEncoder().encodeToString(cipherBytes);

        List<String> lines = Files.readAllLines(BLACKJACK_FILE, StandardCharsets.UTF_8);
        lines.removeIf(line -> line.startsWith(username + ":"));
        lines.add(username + ":" + encoded);

        Files.write(BLACKJACK_FILE, lines, StandardCharsets.UTF_8);
    }

    public static String loadBlackjackSave(String username, String password) throws Exception {
        init();
        if (!Files.exists(BLACKJACK_FILE)) return null;
        List<String> lines = Files.readAllLines(BLACKJACK_FILE, StandardCharsets.UTF_8);

        for (String line : lines) {
            if (line.startsWith(username + ":")) {
                String[] parts = line.split(":", 3);
                if (parts.length != 3) throw new IOException("Invalid save file format");

                byte[] iv         = Base64.getDecoder().decode(parts[1]);
                byte[] cipherBytes = Base64.getDecoder().decode(parts[2]);

                byte[] keyBytes = sha256(password.getBytes(StandardCharsets.UTF_8));
                SecretKeySpec key = new SecretKeySpec(keyBytes, 0, 32, "AES");

                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
                byte[] plain = cipher.doFinal(cipherBytes);
                return new String(plain, StandardCharsets.UTF_8);
            }
        }
        return null;
    }

    private static byte[] sha256(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(data);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }
    public static void deleteUserAccount(String username) throws IOException {
        init();
        List<String> lines = Files.readAllLines(USERS_FILE, StandardCharsets.UTF_8);
        lines.removeIf(line -> line.startsWith(username + ":"));
        Files.write(USERS_FILE, lines, StandardCharsets.UTF_8);
    }

    public static void deleteAllBlackjackSaves(String username) throws IOException {
        init();
        if (!Files.exists(BLACKJACK_FILE)) {
            return;
        }
        List<String> lines = Files.readAllLines(BLACKJACK_FILE, StandardCharsets.UTF_8);
        lines.removeIf(line -> line.startsWith(username + ":"));
        Files.write(BLACKJACK_FILE, lines, StandardCharsets.UTF_8);
    }
    public static void updateGlobalScore(String username, int newScore, String game) throws IOException {
        init();
        initGlobalScoreboard();

        List<String> lines = Files.readAllLines(HIGHSCORES_FILE, StandardCharsets.UTF_8);

        int blackjackIndex = -1;
        int snakeIndex = -1;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.toLowerCase().startsWith("blackjack:")) {
                blackjackIndex = i;
            } 
            else if (line.toLowerCase().startsWith("snake:")) {
                snakeIndex = i;
            }
        }

        if (blackjackIndex == -1 || snakeIndex == -1) {
            initGlobalScoreboard();
            lines = Files.readAllLines(HIGHSCORES_FILE, StandardCharsets.UTF_8);
            blackjackIndex = 0;
            snakeIndex = 1;
        }

        int targetIndex = "blackjack".equalsIgnoreCase(game) ? blackjackIndex : snakeIndex;
        String targetLine = lines.get(targetIndex).trim();

        String[] parts = targetLine.split(":");
        String gameName = parts[0];

        List<UserScore> entries = new ArrayList<>();

        for (int i = 1; i < parts.length; i++) {
            String entry = parts[i].trim();
            if (entry.isEmpty()) continue;

            String[] userScoreParts = entry.split("\\+"); // username+score
            if (userScoreParts.length != 2) continue;

            String name = userScoreParts[0];
            int score;
            try {
                score = Integer.parseInt(userScoreParts[1]);
            } 
            catch (NumberFormatException e) {
                continue;
            }

            entries.add(new UserScore(name, score));
        }

        boolean found = false;

        for (UserScore us : entries) {
            if (us.getUsername().equals(username)) {
                if (newScore > us.getScore()) {
                    us.setScore(newScore);
                }
                found = true;
                break;
            }
        }

        if (!found) {
            if (entries.size() < 5) {
                entries.add(new UserScore(username, newScore));
            } 
            else {
                int lowestIndex = 0;
                for (int i = 1; i < entries.size(); i++) {
                    if (entries.get(i).getScore() < entries.get(lowestIndex).getScore()) {
                        lowestIndex = i;
                    }
                }
                if (newScore > entries.get(lowestIndex).getScore()) {
                    entries.set(lowestIndex, new UserScore(username, newScore));
                }
            }
        }

        entries.sort((a, b) -> Integer.compare(b.getScore(), a.getScore()));

        StringBuilder sb = new StringBuilder(gameName);
        int limit = Math.min(5, entries.size());
        for (int i = 0; i < limit; i++) {
            UserScore us = entries.get(i);
            sb.append(":").append(us.getUsername()).append("+").append(us.getScore());
        }

        lines.set(targetIndex, sb.toString());
        Files.write(HIGHSCORES_FILE, lines, StandardCharsets.UTF_8);
    }
}
