import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
//セーブデータの入出力クラス
class SaveData {
    private static final String FILE_PATH = "./SaveData.txt";
    private static final int LITTLE_PRIM = 29363;
    private static final int LARGE_PRIM = 858563;
    private static final String REGEX = ",";

    public static final int UNKNOWN = -1;
    public static final int NUMBER_OF_SAVEDATA = 5;

    public static int[] loadFromFile() {
        int[] data = new int[5];
        try (FileReader fileReader = new FileReader(FILE_PATH);
            BufferedReader bufferedReader = new BufferedReader(fileReader);){
            
            String line;

            // ファイルから一行ずつ読み込む
            line = bufferedReader.readLine();
            String[] splitted = line.split(REGEX);
            line = bufferedReader.readLine();
            String[] hashes = line.split(REGEX);
            if(splitted.length != NUMBER_OF_SAVEDATA || hashes.length != NUMBER_OF_SAVEDATA) {
                throw new IOException("The savedata is broken!!\nPlese ivoke \"SaveData::saveData(int[])\"");
            }
            for(int i = 0; i < NUMBER_OF_SAVEDATA; i++) {
                if(hash(splitted[i]).equals(hashes[i])) {
                    data[i] = Integer.parseInt(splitted[i]);
                } else {
                    data[i] = -2;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data; // StringBuilderをStringに変換して返す
    }

    public static void saveData(int[] data) {
        try(FileWriter fileWriter = new FileWriter(FILE_PATH, false);  // ファイル上書きモード
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);) {
            if(data.length != NUMBER_OF_SAVEDATA) {
                throw new IOException(String.format("Too few/many savedata!! Please make sure that you should save %d files.", NUMBER_OF_SAVEDATA));
            }
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < NUMBER_OF_SAVEDATA - 1; i++) {
                sb.append(data[i] + REGEX);
            }
            sb.append(Integer.toString(data[NUMBER_OF_SAVEDATA-1])+"\n");
            for(int i = 0; i < NUMBER_OF_SAVEDATA - 1; i++) {
                sb.append(hash(data[i])+REGEX);
            }
            sb.append(hash(data[NUMBER_OF_SAVEDATA-1]));

            // saveDataをファイルに書き込む
            bufferedWriter.write(sb.toString());
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static String hash(int key) {
        return hash(Integer.toString(key));
    }

    private static String hash(String key) {
        long hash = 287629;
        for(int i = 0; i < key.length(); i++) {
            hash = ((hash * LITTLE_PRIM) + (int)key.charAt(i)) % LARGE_PRIM;
        }
        return Long.toString(hash);
    }
}
