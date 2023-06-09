import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class FadingEffect {
    Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 100);
    int size = 5;
    JLabel[] labels = new JLabel[size];
    //グラデーションの色を格納しておく配列
    int[] Colors = {
        0x40120A, 0x661D10, 0x802414, 0x8C2716, 0xCC3920
    };
    float[] Reds = new float[Colors.length];
    float[] Greens = new float[Colors.length];
    float[] Blues = new float[Colors.length];
    final float default_alpha = 0.3f;

    int start_x; int start_y; int end_x; int end_y;
    // 持続時間
    long duration = 1000;
    long startTime = 0;
    boolean isMoving = false;

    FadingEffect(String text, JPanel jp, int start_x, int start_y, int end_x, int end_y) {
        this.start_x = start_x;
        this.start_y = start_y;
        this.end_x = end_x;
        this.end_y = end_y;
        for(int i = 0; i < size; i++) {
            labels[i] = new JLabel(text);
            labels[i].setForeground(new Color(0.0f, 0.0f, 0.0f, 0.0f));
            labels[i].setLocation((int)((double)i*end_x + (1-(double)i/size)*start_x), (int)((double)i*end_y + (1-(double)i/size)*start_y));
            labels[i].setFont(font);
            jp.add(labels[i]);
            labels[i].getParent().setComponentZOrder(labels[i], 0);
        }
        for(int i = 0; i < Reds.length; i++) {
            Reds[i] = ((Colors[i] & 0xFF0000) >> 16)/ 255.0f;
            Greens[i] = ((Colors[i] & 0x00FF00) >> 8)/ 255.0f;
            Blues[i] = (Colors[i] & 0x0000FF) / 255.0f;
        }
        isMoving = true;
        startTime = System.currentTimeMillis();
    }


    void update() {
        long t = System.currentTimeMillis() - startTime;
        if(!isMoving) return;
        if(t > duration) {
            isMoving = false;
        }
        // ラベルの透明度と位置を添え字によって線形に変更
        for(int i = 0; i < size; i++) {
            labels[i].setForeground(new Color(Reds[i], Greens[i], Blues[i], stepFunc((t-duration/10.0f*i)/duration*4)));
            labels[i].setLocation((int)((double)i*end_x + (1-(double)i/size)*start_x), (int)((double)i*end_y + (1-(double)i/size)*start_y));
        }
    }

    private float stepFunc(float x) {
        if(x < 0 || x > 1) {
            return 0;
        } else {
            return 1 - x;
        }
    }
}
