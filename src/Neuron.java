import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Neuron {
    private String name;
    private int[][] mul;
    private int[][] weight;
    private int[][] input;
    private int sum;

    public static final int STUDY_LIMIT = 9;
    public static final int DEFAULT_LIMIT = 9;

    Neuron(int sizeX, int sizeY, String name) throws IOException {
        this.name = name;
        weight = new int[sizeX][sizeY];
        mul = new int[sizeX][sizeY];
        input = new int[sizeX][sizeY];

        load();
    }

    Neuron(int sizeX, int sizeY, int[][] input, String name) {
        weight = new int[sizeX][sizeY];
        mul = new int[sizeX][sizeY];
        this.input = input;
    }

    public int check(int[][] input) {
        this.input = input;
        mulling();
        sum();

        return sum;
    }

    public boolean recognize(int[][] input) {
        this.input = input;
        mulling();
        sum();

        return rez();
    }

    public void study(int[][] input, boolean isTrue) throws IOException {
        this.input = input;
        mulling();
        sum();
    }

    private void mulling() {
        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 4; y++) {
                mul[x][y] = input[x][y] * weight[x][y];
            }
        }
    }

    private void sum() {
        sum = 0;
        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 4; y++) {
                sum += mul[x][y];
            }
        }
    }

    public void inc(int[][] input) throws IOException {
        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 4; y++) {
                weight[x][y] += input[x][y];
            }
        }

        save();
    }

    public void dec(int[][] input) throws IOException {
        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 4; y++) {
                weight[x][y] -= input[x][y];
            }
        }

        save();
    }

    private boolean rez() {
        return sum >= DEFAULT_LIMIT;
    }

    private void save() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(name));
        for (int y = 0; y <= 4; y++) {
            for (int x = 0; x <= 2; x++){
                writer.write(weight[x][y] + " ");
            }
            writer.newLine();
        }

        writer.flush();
    }

    private void load() throws  IOException {
        if (Files.exists(Paths.get(name))) {
            var file = Files.readAllLines(Paths.get(name));

            String[] str;
            int j = 0, k;
            for (var s : file) {
                k = 0;
                str = s.split(" ");
                for (var i : str) {
                    weight[k][j] = Integer.parseInt(i);
                    k++;
                }
                j++;
            }
        } else {
            for (int y = 0; y < 5; y++) {
                for (int x = 0; x < 3; x++) {
                    weight[x][y] = 0;
                }
            }

            save();
        }
    }

    public int[][] getInput() {
        return input;
    }

    public int[][] getMul() {
        return mul;
    }

    public int[][] getWeight() {
        return weight;
    }

    public int getSum() {
        return sum;
    }

    public String getName() {
        return name;
    }
}
