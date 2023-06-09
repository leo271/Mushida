import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
// import java.awt.Point;
// 元々は描画クラスだったが実質的なゲームマネージャ
class DrawCanvas extends JPanel implements KeyListener{
    // 今回出題する単語を管理するクラス
    WordProcessor wp = new WordProcessor();
    // それぞれ音とラベルを管理するクラス
    Sound sound = new Sound();
    Labels labels = new Labels(this);
    // 時間に関する変数
    long mistaken_timer = 0;
    long correct_timer = 0;
    long timeLimit;
    float default_alpha = 0.2f;
    // 時計のパラメータ
    // Point clock_pos = new Point(680, 280);
    // int clock_rad = 80;

    // 正解・不正解時に画面の色が変わるエフェクトの効果時間(ms)
    final int effectDelay = 300;
    // 制限時間（ms)
    final long duration = 25_000;
    FadingEffect fade = null;
    // 得点と終了フラグ
    int score;
    int calc_score(int str_len) {
        return (str_len*100 + str_len*str_len*10)*2;
    }
    boolean isEnd = false;

    DrawCanvas() {
        super();
        wp.loadData();
        timeLimit = System.currentTimeMillis() + duration;
        sound.startMain();
        labels.updateText(wp.str);
    }

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        labels.setLocation();
        // 制限時間に到達したかしていないかでそれぞれ操作
        long lastTime = timeLimit - System.currentTimeMillis();
        if(lastTime > 0) {
            // g.setColor(new Color(0.4f, 0.4f, 0.2f, 0.5f));
            // g.drawOval(clock_pos.x - clock_rad, clock_pos.y - clock_rad, clock_rad*2, clock_rad*2);
            // g.drawLine(clock_pos.x, clock_pos.y, (int)(clock_pos.x - clock_rad * 0.9 * Math.sin((double)lastTime / duration * 2 * Math.PI)), (int)(clock_pos.y - clock_rad * 0.9 * Math.cos((double)lastTime / duration * 2 * Math.PI)));
            
            labels.updateTime(timeLimit);
        } else if(!isEnd){ 
            isEnd = true;
            sound.startEnding();
            labels.updateTime(System.currentTimeMillis());
            int[] highscores = SaveData.loadFromFile();
            int idx = 0;
            while(idx < SaveData.NUMBER_OF_SAVEDATA && score < highscores[idx]){idx++;}
            int current = -1;
            if(idx != SaveData.NUMBER_OF_SAVEDATA) {
                for(int i = SaveData.NUMBER_OF_SAVEDATA-1; i > idx; i--) {
                    highscores[i] = highscores[i-1];
                }
                highscores[idx] = score;
                current = idx;
                SaveData.saveData(highscores);
            }
            labels.loadHighScores(highscores, current);
            labels.switchEnd();
        }

        //正解・不正解時のエフェクト
        if(mistaken_timer > System.currentTimeMillis()) {
            g.setColor(new Color(0.5f, 100f/255, 61f/255, (float)(mistaken_timer-System.currentTimeMillis())/effectDelay*default_alpha));
            g.fillRect(0, 0, 800, 600);
        }
        g.setColor(Color.BLACK);

        // 得点確定時の赤いエフェクト
        if(fade != null) {
            fade.update();
        }
	}

    public void keyPressedInner(char c) {
        // これは再開時のエンターでミスにならないようにするため
        if(c =='\n') return;
        // 入力が正しいかどうか判定
        boolean correct = wp.inputKey(c);
        if(correct) {
            sound.type();
            labels.changeBlack(wp);
        } else {
            mistaken_timer = System.currentTimeMillis() + effectDelay;
            timeLimit -= 100;
        }
        //一単語打ち終わったとき
        if(wp.isDone()) {
            //スコア計算
            int obtained = calc_score(wp.str.length());
            score += obtained;
            labels.setScore(score);
            // 時間系変数の操作
            correct_timer = System.currentTimeMillis() + effectDelay;
            mistaken_timer = 0;
            timeLimit += wp.str.length() * 100;
            fade = new FadingEffect(String.format("+%d", obtained), this, 300, 50, 100, 0);
            // 新しい単語に更新
            wp.another();
            labels.updateText(wp.str);
            
            sound.procceeds();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // もしキーが押されていたらキーに反応させない
        if(isEnd) return;
        keyPressedInner(e.getKeyChar());
    }

    // 再開処理はごちゃごちゃを避けるために別の関数に書いている。
    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_ENTER && isEnd) {
            // 終了フラグを変更
            isEnd = false;
            sound.startMain();
            // 新しい単語に更新
            wp.another();
            // ラベルをリセット
            labels.switchEnd();
            labels.updateText(wp.str);
            labels.setScore(0);

            timeLimit = System.currentTimeMillis() + duration;
            score = 0;
            fade = null;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        
    }
}