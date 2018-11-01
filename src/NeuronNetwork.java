import java.awt.image.BufferedImage;
import java.io.IOException;

public class NeuronNetwork {
    private Neuron[] neurons;

    public NeuronNetwork(int size) throws IOException {
        neurons = new Neuron[size];
        for (var i = 0; i < size; i++) {
            neurons[i] = new Neuron(3, 5, Integer.toString(i));
        }
    }

    public Neuron[] getNeurons() {
        return neurons;
    }

    public String check(BufferedImage image) {
        StringBuilder sb = new StringBuilder("Sums: ");
        int[][] input = new int[3][5];
        NeuronNetwork.convert(image, input);
        for (var n : neurons) {
            sb.append(n.getName()).append(": ").append(n.check(input)).append("\n");
        }

        return sb.toString();
    }

    public String recognize(BufferedImage image) {
        int[][] input = new int[3][5];

        convert(image, input);

        int max = 0;
        Neuron n = null;
        for (var neuron : neurons) {
            if (neuron.recognize(input)) {
                if (neuron.getSum() > max){
                    max = neuron.getSum();
                    n = neuron;
                }
            }
        }

        if (n != null)
            return n.getName() + ", Sum = " + n.getSum();
        return null;
    }

    public static void study(BufferedImage image, Neuron neuron, boolean isTrue) throws IOException {
        int[][] input = new int[3][5];

        convert(image, input);
        neuron.study(input, isTrue);
    }

    public static void convert(BufferedImage image, int[][] input) {
        for (int y = 0; y <= 4; y++) {
            for (int x = 0; x <= 2; x++) {
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
}
