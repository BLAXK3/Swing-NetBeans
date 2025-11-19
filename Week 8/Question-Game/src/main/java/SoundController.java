/**
 *
 * @author BLAXK
 */

import java.io.BufferedInputStream;
import java.io.InputStream;
import javazoom.jl.player.Player;

public class SoundController {

    public SoundController(QuestionGame game) {
        this.game = game;
    }
    
    public void close() {
        if (player != null) {
            player.close();
        }
    }
    
    private Player getSoundFile(int selectedSound) {
        switch(selectedSound) {
            case 0 :
                fileName = "Correct.mp3";
                break;
            case 1 :
                fileName = "Wrong.mp3";
                break;
            default :
                fileName = "EndGame.mp3";
        }
        try {
            InputStream inputStream = getClass().getResourceAsStream("/sound/" + fileName);
            BufferedInputStream buffer = new BufferedInputStream(inputStream);
            return new Player(buffer);
        } catch (Exception e) {
            game.callOptionPane(e.toString(), "Error", 0);
            return null;
        }
    }
        
    public void play(int selectSound) {
        close();
        new Thread(() -> {
                try {
                    getSoundFile(selectSound).play();
                } 
                catch (Exception e) {
                    game.callOptionPane(e.toString(), "Error", 0);
                }
        }).start();
    }
    
    private QuestionGame game;
    private Player player;
    private String fileName;
}
