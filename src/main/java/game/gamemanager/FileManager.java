package game.gamemanager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.ArrayList;
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
        if (!Files.exists(USERS_FILE)) return false;
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
        init();
        StringBuilder sb = new StringBuilder();
        sb.append(username);
        for (int i = 0; i < 10; i++) {
            sb.append(":1000");
        }
        sb.append(System.lineSeparator());
        Files.write(HIGHSCORES_FILE, sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
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

    public static void updateAllBlackjackScores(String username, int newScore) throws IOException {
        init();
        List<String> lines = Files.readAllLines(HIGHSCORES_FILE, StandardCharsets.UTF_8);

        for (int i = 0; i < lines.size(); i++) {
            String line  = lines.get(i);
            String[] parts = line.split(":", -1);

            if (parts.length > 0 && parts[0].equals(username)) {

                while (parts.length < 11) {
                    line += ":1000";
                    parts = line.split(":", -1);
                }

                int lowestScore = Integer.parseInt(parts[10]);

                if (newScore > lowestScore) {
                    parts[10] = parts[9];
                    parts[9]  = parts[8];
                    parts[8]  = parts[7];
                    parts[7]  = parts[6];
                    parts[6]  = String.valueOf(newScore);

                    line = String.join(":", parts);
                    lines.set(i, line);
                    Files.write(HIGHSCORES_FILE, lines, StandardCharsets.UTF_8);
                }
                return;
            }
        }


        StringBuilder sb = new StringBuilder(username);
        for (int i = 0; i < 5; i++) sb.append(":1000");    
        sb.append(":").append(newScore);                 
        for (int i = 0; i < 4; i++) sb.append(":1000");      
        sb.append(System.lineSeparator());
        Files.write(HIGHSCORES_FILE, sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }

    public static void updateBlackjackScore(String username, int scoreIndex, int score)
            throws IOException {
        init();
        if (scoreIndex < 0 || scoreIndex > 4) {
            throw new IllegalArgumentException("scoreIndex must be 0–4");
        }

        List<String> lines = Files.readAllLines(HIGHSCORES_FILE, StandardCharsets.UTF_8);
        for (int i = 0; i < lines.size(); i++) {
            String line  = lines.get(i);
            String[] parts = line.split(":", -1);

            if (parts.length > 0 && parts[0].equals(username)) {
                while (parts.length < 11) {
                    line += ":1000";
                    parts = line.split(":", -1);
                }

                parts[6 + scoreIndex] = String.valueOf(score);
                line = String.join(":", parts);
                lines.set(i, line);
                Files.write(HIGHSCORES_FILE, lines, StandardCharsets.UTF_8);
                return;
            }
        }

        StringBuilder sb = new StringBuilder(username);
        for (int i = 0; i < 5; i++) {
            sb.append(":1000"); 
        }
        for (int i = 0; i < 5; i++) {
            if (i == scoreIndex) {
                sb.append(":").append(score);
            } else {
                sb.append(":1000");
            }
        }
        sb.append(System.lineSeparator());
        Files.write(HIGHSCORES_FILE, sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }
    public static void updateAllSnakeScores(String username, int[] snakeScores) throws IOException {
        if (snakeScores == null || snakeScores.length != 5) {
            throw new IllegalArgumentException("snakeScores must be an array of 5 integers");
        }

        init();
        List<String> lines = Files.readAllLines(HIGHSCORES_FILE, StandardCharsets.UTF_8);

        for (int i = 0; i < lines.size(); i++) {
            String line  = lines.get(i);
            String[] parts = line.split(":", -1);

            if (parts.length > 0 && parts[0].equals(username)) {

                while (parts.length < 11) {
                    line += ":1000";
                    parts = line.split(":", -1);
                }

                int lowestExistingScore = Integer.parseInt(parts[5]);

                boolean shouldUpdate = false;
                for (int score : snakeScores) {
                    if (score > lowestExistingScore) {
                        shouldUpdate = true;
                        break;
                    }
                }

                if (shouldUpdate) {
                    for (int j = 0; j < 5; j++) {
                        parts[j + 1] = String.valueOf(snakeScores[j]);
                    }
                    line = String.join(":", parts);
                    lines.set(i, line);
                    Files.write(HIGHSCORES_FILE, lines, StandardCharsets.UTF_8);
                }
                return;
            }
        }

        StringBuilder sb = new StringBuilder(username);
        for (int j = 0; j < 5; j++) {
            sb.append(":").append(snakeScores[j]);
        }
        for (int j = 0; j < 5; j++) {
            sb.append(":1000"); 
        }
        sb.append(System.lineSeparator());
        Files.write(HIGHSCORES_FILE, sb.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
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
}
