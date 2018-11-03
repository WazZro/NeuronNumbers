import java.awt.image.BufferedImage;
import java.io.IOException;

public class NeuronNetwork {
    private Neuron[] neurons;
    private int resX;
    private int resY;

    public NeuronNetwork(int size) throws IOException {
        neurons = new Neuron[size];
        for (var i = 0; i < size; i++) {
            neurons[i] = new Neuron(3, 5, Integer.toString(i));
        }
    }

    public NeuronNetwork(int size, int resX, int resY) throws IOException {
        neurons = new Neuron[size];
        this.resX = resX;
        this.resY = resY;
        for (var i = 0; i < size; i++) {
            neurons[i] = new Neuron(resX, resY, Integer.toString(i));
        }
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public String check(BufferedImage image) {
        StringBuilder sb = new StringBuilder("Sums: ");
        int[][] input = new int[resX][resY];
        NeuronNetwork.convert(image, input);
        for (var n : neurons) {
            sb.append(n.getName()).append(": ").append(n.check(input)).append("\n");
        }

        return sb.toString();
    }

    public String recognize(BufferedImage image) {
        int[][] input = new int[resX][resY];

        convert(image, input);

        double max = 0;
        double current = 0;
        Neuron n = null;
        for (var neuron : neurons) {
            current = neuron.recognize(input);
            if (current > max) {
                n = neuron;
                max = current;
            }
//            if (neuron.recognize(input)) {
//                if (neuron.getSum() > max) {
//                    max = neuron.getSum();
//                    n = neuron;
//                }
//            }
        }

        if (n != null)
            return "It is " + n.getName() + " number";
        return null;
    }

    public static void study(int resX, int resY, BufferedImage image, Neuron neuron, boolean isTrue) throws IOException {
        int[][] input = new int[resX][resY];

        convert(image, input);
        neuron.study(input, isTrue);
    }

    public static void convert(BufferedImage image, int[][] input) {
        for (int y = 0; y < input[0].length; y++) {
            for (int x = 0; x < input.length; x++) {
                int n = image.getRGB(x, y);
                if (n == -1)
                    n = 0;
                else
                    n = 1;

                input[x][y] = n;
            }
        }
    }

    public Neuron getNeuron(String name) {
        for (var n : neurons) {
            if (name.equals(n.getName()))
                return n;
        }

        return null;
    }

    public int getResX() {
        return resX;
    }

    public int getResY() {
        return resY;
    }
}
