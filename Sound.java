import java.io.File;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

public class Sound {
    Clip bgm;
    Clip bgm2;
    Clip end_voice;
    Clip[] typed = new Clip[10];
    int typed_index = 0;
    Clip[] procceeds = new Clip[3];
    int procceeds_index = 0;
    boolean hasStopped = false;

    Sound() {
        bgm = createClip(new File("./BGM.wav"));
        bgm2 = createClip(new File("./BGM2.wav"));
        for(int i = 0; i < typed.length; i++) {
            typed[i] = createClip(new File("./Typed.wav"));
        }
        end_voice = createClip(new File("./EndVoice.wav"));
        for(int i = 0; i < procceeds.length; i++) {
            procceeds[i] = createClip(new File("./Procceeds.wav"));
        }
        FloatControl gainControl = (FloatControl) bgm.getControl(FloatControl.Type.MASTER_GAIN);
        // デシベル単位で音量を指定
        float volume = -12.0f;
        gainControl.setValue(volume);
    }

    void type() {
        play(typed[typed_index]);
        typed_index = (typed_index+1)%typed.length;
    }
    // 待機BGMを止めてメインBGMを流す
    void startMain() {
        play(bgm);
        bgm2.stop();
        bgm2.flush();
        hasStopped = true;
    }
    // SEなので複数のクリップを用意して順繰りに再生する　単語を打ち終わったとき
    void procceeds() {
        play(procceeds[procceeds_index]);
        procceeds_index = (procceeds_index+1)%procceeds.length;
    }

    void startEnding() {
        // 声が流れてからBGMを流すまでラグを作る
        Thread playThread = new Thread(() -> {
            try {
                hasStopped = false;
                play(end_voice);
                bgm.stop();
                // 再生終了まで待機
                Thread.sleep(2000);
                bgm2.flush();
                bgm2.setFramePosition(0);
                if(!hasStopped) {
                    bgm2.loop(Clip.LOOP_CONTINUOUSLY);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        playThread.start();
    }
    // 再生位置0でクリップを再生
    private void play(Clip clip) {
        clip.flush();
        clip.setFramePosition(0);
        clip.start();
    }

    public static Clip createClip(File path) {
		//指定されたURLのオーディオ入力ストリームを取得
		try (AudioInputStream ais = AudioSystem.getAudioInputStream(path)){
			//ファイルの形式取得
			AudioFormat af = ais.getFormat();
			//単一のオーディオ形式を含む指定した情報からデータラインの情報オブジェクトを構築
			DataLine.Info dataLine = new DataLine.Info(Clip.class,af);
			//指定された Line.Info オブジェクトの記述に一致するラインを取得
			Clip c = (Clip)AudioSystem.getLine(dataLine);
			//再生準備完了
			c.open(ais);
			return c;
		} catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
}
