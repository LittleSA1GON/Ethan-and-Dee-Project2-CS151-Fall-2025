package game.gamemanager;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;

public class MusicManager {
    private static MediaPlayer snakePlayer;
    private static MediaPlayer blackjackPlayer;
    private static MediaPlayer gameManagerPlayer;
    private static final String SNAKE_MUSIC = "/game/snake/ytmp3free.cc_duran-duran-invisible-youtubemp3free.org.mp3";
    private static final String BLACKJACK_MUSIC = "/game/blackjack/ytmp3free.cc_balatro-main-theme-youtubemp3free.org.mp3";
    private static final String GAMEMANAGER_MUSIC = "/game/gamemanager/ytmp3free.cc_blue-archive-ost-1-constant-moderato-youtubemp3free.org.mp3";

    public static void playSnakeMusic() {
        try {
            stopSnakeMusic();  
            
            URL resource = MusicManager.class.getResource(SNAKE_MUSIC);
            if (resource == null) {
                System.err.println("Snake music file not found: " + SNAKE_MUSIC);
                return;
            }
            
            Media media = new Media(resource.toExternalForm());
            snakePlayer = new MediaPlayer(media);
            snakePlayer.setCycleCount(MediaPlayer.INDEFINITE);  
            snakePlayer.setVolume(0.2);  
            snakePlayer.play();
        } 
        catch (Exception e) {
            System.err.println("Error loading Snake music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void playBlackjackMusic() {
        try {
            stopBlackjackMusic();  
            
            URL resource = MusicManager.class.getResource(BLACKJACK_MUSIC);
            if (resource == null) {
                System.err.println("Blackjack music file not found: " + BLACKJACK_MUSIC);
                return;
            }
            
            Media media = new Media(resource.toExternalForm());
            blackjackPlayer = new MediaPlayer(media);
            blackjackPlayer.setCycleCount(MediaPlayer.INDEFINITE);  
            blackjackPlayer.setVolume(0.5); 
            blackjackPlayer.play();
        } 
        catch (Exception e) {
            System.err.println("Error loading Blackjack music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void stopSnakeMusic() {
        if (snakePlayer != null) {
            snakePlayer.stop();
            snakePlayer.dispose();
            snakePlayer = null;
        }
    }

    public static void stopBlackjackMusic() {
        if (blackjackPlayer != null) {
            blackjackPlayer.stop();
            blackjackPlayer.dispose();
            blackjackPlayer = null;
        }
    }

    public static void playGameManagerMusic() {
        try {
            stopGameManagerMusic();  
            
            URL resource = MusicManager.class.getResource(GAMEMANAGER_MUSIC);
            if (resource == null) {
                System.err.println("GameManager music file not found: " + GAMEMANAGER_MUSIC);
                return;
            }
            
            Media media = new Media(resource.toExternalForm());
            gameManagerPlayer = new MediaPlayer(media);
            gameManagerPlayer.setCycleCount(MediaPlayer.INDEFINITE);  
            gameManagerPlayer.setVolume(0.5);  
            gameManagerPlayer.play();
        } 
        catch (Exception e) {
            System.err.println("Error loading GameManager music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void stopGameManagerMusic() {
        if (gameManagerPlayer != null) {
            gameManagerPlayer.stop();
            gameManagerPlayer.dispose();
            gameManagerPlayer = null;
        }
    }

    public static void stopAllMusic() {
        stopSnakeMusic();
        stopBlackjackMusic();
        stopGameManagerMusic();
    }

    public static void setSnakeMusicVolume(double volume) {
        if (snakePlayer != null) {
            snakePlayer.setVolume(Math.max(0, Math.min(1, volume)));
        }
    }

    public static void setBlackjackMusicVolume(double volume) {
        if (blackjackPlayer != null) {
            blackjackPlayer.setVolume(Math.max(0, Math.min(1, volume)));
        }
    }

    public static void setGameManagerMusicVolume(double volume) {
        if (gameManagerPlayer != null) {
            gameManagerPlayer.setVolume(Math.max(0, Math.min(1, volume)));
        }
    }

    public static void setAllMusicVolume(double volume) {
        setSnakeMusicVolume(volume);
        setBlackjackMusicVolume(volume);
        setGameManagerMusicVolume(volume);
    }

    public static boolean isSnakeMusicPlaying() {
        return snakePlayer != null && snakePlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public static boolean isBlackjackMusicPlaying() {
        return blackjackPlayer != null && blackjackPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    public static boolean isGameManagerMusicPlaying() {
        return gameManagerPlayer != null && gameManagerPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }
}
