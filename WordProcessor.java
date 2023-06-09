import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

public class WordProcessor {
    String str;
    int index;
    static String[] words = new String[]{"preparing..."};
    int words_idx;
    static Random rnd = new Random(System.currentTimeMillis());

    public WordProcessor() {
        index = 0;
        words_idx = 0;
        str = "preparing..";
        words = null;
    }

    public void shuffle(String[] strs) {
        final int TRIAL = 10000;
        for(int i = 0; i < TRIAL; i++) {
            int x = rnd.nextInt(words.length);
            int y = rnd.nextInt(words.length);
            String buf = strs[x];
            strs[x] = strs[y];
            strs[y] = buf;
        }
    }

    public void loadData() {
        String path = "./words.txt";
        try(FileInputStream stream = new FileInputStream(path)) {
            // ファイルのデータをすべて読み込む
            byte[] bytes = stream.readAllBytes();
            // データを文字列として格納
            String allString = new String(bytes);
            words = allString.split(",");
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
        shuffle(words);
        another();
    }

    public boolean inputKey(char c) {
        if(str.charAt(index) == c) {
            index++;
            return true;
        } else {
            return false;
        }
    }

    public String getEnded() {
        if(str==null) {
            return "";
        }
        return str.substring(0, index);
    }

    public String getYet() {
        return str.substring(index, str.length());
    }

    public boolean isDone() {
        return str.length() == index;
    }

    public void another() {
        if(words == null) {
            return;
        }
        words_idx = (words_idx+1)%words.length;
        str = words[words_idx];
        index = 0;
    }
}
