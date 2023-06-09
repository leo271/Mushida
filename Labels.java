import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.JPanel;

// ラベルの見た目を管理するクラス
class Labels {
    // ラベルのインデックス
    static public final int BLACK = 0;
    static public final int GRAY = 1;
    static public final int TIMELIMIT = 2;
    static public final int SCORE = 3;
    static public final int PRESS_ENTER = 4;
    static public final int HIGHSCORE = 5;
    // ラベルの位置
    private final Point text_preset = new Point(395, 300);
    private Point text_pos;
    private Point score_pos = new Point(300, 100);
    private Point timelimit_pos = new Point(120, 100);
    private Point pressenter_pos = new Point(160, 400);
    private Point highscore_pos = new Point(220, 200);
    // 終了したかどうかのフラグ
    private boolean isEnded = false;
    // 全てのラベルが格納されている配列
    private ArrayList<JLabel> labels;
    // 全体のフォントを作成
    private Font getFont(int size) {
        return new Font(Font.SANS_SERIF, Font.PLAIN, size);
    }

    private final Color text = new Color(0x324851);
    private final Color system = new Color(0x223841);

    // ラベルの初期化
    public Labels(JPanel panel) {
        labels = new ArrayList<>();
        //Black
        labels.add(new JLabel());
        labels.get(BLACK).setFont(getFont(100));
        labels.get(BLACK).setForeground(text);
        panel.add(labels.get(BLACK));
        labels.get(BLACK).getParent().setComponentZOrder(labels.get(BLACK), 0);
        // Gray
        labels.add(new JLabel());
        labels.get(GRAY).setForeground(Color.GRAY);
        labels.get(GRAY).setFont(getFont(100));
        panel.add(labels.get(GRAY));
        //Timelimit
        labels.add(new JLabel());
        labels.get(TIMELIMIT).setForeground(system);
        labels.get(TIMELIMIT).setFont(getFont(50));
        panel.add(labels.get(TIMELIMIT));
        //Score
        labels.add(new JLabel("score:000000"));
        labels.get(SCORE).setForeground(system);
        labels.get(SCORE).setFont(getFont(50));
        panel.add(labels.get(SCORE));
        //Press Enter
        labels.add(new JLabel("[Press Enter]"));
        labels.get(PRESS_ENTER).setForeground(system);
        labels.get(PRESS_ENTER).setVisible(false);
        labels.get(PRESS_ENTER).setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 80));
        panel.add(labels.get(PRESS_ENTER));
        //Highscore
        labels.add(new JLabel());
        labels.get(HIGHSCORE).setForeground(text);
        labels.get(HIGHSCORE).setFont(getFont(30));
        panel.add(labels.get(HIGHSCORE));
    }
    // ラベルの位置を設定（JLabelはループ毎に場所を設定しないとデフォルトの配置に戻される）
    public void setLocation() {
        labels.get(GRAY).setLocation(text_pos.x, text_pos.y);
        labels.get(BLACK).setLocation(text_pos.x, text_pos.y);
        labels.get(SCORE).setLocation(score_pos.x, score_pos.y);
        labels.get(TIMELIMIT).setLocation(timelimit_pos.x, timelimit_pos.y);
        labels.get(PRESS_ENTER).setLocation(pressenter_pos.x, pressenter_pos.y);
        labels.get(HIGHSCORE).setLocation(highscore_pos.x, highscore_pos.y);
    }
    // 中央の単語を新しいものに更新
    public void updateText(String str) {
        labels.get(GRAY).setText(str);
        labels.get(BLACK).setText("");
        Dimension size = labels.get(GRAY).getPreferredSize();
        text_pos = new Point(text_preset.x - size.width/2, text_preset.y - size.height/2);
    }
    // スコアを書式して追加
    public void setScore(int score) {
        labels.get(SCORE).setText(String.format("score:%06d", score));
    }
    // 打った単語を反映
    public void changeBlack(WordProcessor wp) {
        labels.get(BLACK).setText(wp.getEnded());
    }
    // 制限時間を反映
    public void updateTime(long timeLimit) {
        labels.get(TIMELIMIT).setText(String.format("%02.1fs",(timeLimit - System.currentTimeMillis())/1000.0));
    }
    // 終了時のPRESS ENTERの表示切り替え
    public void switchEnd() {
        isEnded = !isEnded;
        labels.get(PRESS_ENTER).setVisible(isEnded);
        labels.get(HIGHSCORE).setVisible(isEnded);
        labels.get(BLACK).setVisible(!isEnded);
        labels.get(GRAY).setVisible(!isEnded);
        labels.get(TIMELIMIT).setVisible(!isEnded);
        score_pos.x = score_pos.x + (isEnded ? -80 : 80);
    }

    private static final String[] PREFIX = {
        "1st", "2nd", "3rd", "4th", "5th"
    };
    int current = -1;
    public void loadHighScores(int[] scores, int current) {
        this.current = current;
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        for(int i = 0; i < scores.length; i++) {
            String comment = "";
            if(current == 0 && i == 0) {
                sb.append("<p style=\"color:#34675C\">");
                comment = "<i> new record!! </i>";
            } else if(i == current) {
                sb.append("<p style=\"color:#34675C;\">");
                comment = "<i> ranked in! </i>";
            } else if(scores[i] == -1) {
                sb.append("<p>" + PREFIX[i] +  ":no data</p>");
                continue;
            }  else if(scores[i] == -2){
                labels.get(HIGHSCORE).setText("Did you fixed the data?😏       ");
                return;
            }else {
                sb.append("<p>");
            }
            sb.append(PREFIX[i] + ":" + scores[i] + comment + "</p>");
        }
        sb.append("</html>");
        labels.get(HIGHSCORE).setText(sb.toString());
    }
}