import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Started NeuronNetwork...");
        NeuronNetwork network = new NeuronNetwork(10);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String[] command;

        while (true) {
            System.out.print("$: ");
            command = reader.readLine().split(" ");
            try {
                switch (command[0]) {
                    case "learn":
                        var neuron = network.getNeuron(command[1]);
                        System.out.println("Start learning the neuron " + neuron.getName() + ":");
                        study(neuron, reader);
                        System.out.println("The learning stopped");
                        break;
                    case "what":
                        var res = network.recognize(ImageIO.read(new File(command[1])));
                        System.out.println(res);
                        break;
                    case "debug":
                        var str = network.check(ImageIO.read(new File(command[1])));
                        System.out.println(str);
                        break;
                    case "help":
                        System.out.println("learn *neuron's name* -- to learn a neuron\n" +
                                "what *file's name* -- what is number on the picture\n" +
                                "debug *file's name* -- to debugging");
                        break;
                    case "exit":
                        return;
                }
            } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                System.out.println("Wrong command. Try to repeat. Write 'help'");
            }
        }
    }

    private static void study(Neuron neuron, BufferedReader reader) throws IOException {
        String str;
        int[][] input = new int[3][5];

        while (true) {
            System.out.print("~" + neuron.getName() + "-$: ");
            str = reader.readLine();
            if (str.equals("exit"))
                return;

            if (str.equals("info")) {
                out(neuron.getWeight());
                continue;
            }


            try {
                NeuronNetwork.convert(ImageIO.read(new File(str)), input);

                var isTrue = neuron.recognize(input);
                System.out.println(isTrue + " " + neuron.getSum());
                System.out.print("Agree? (Y/n): ");
                var a = reader.readLine().equals("n");

                if (a) {
                    if (!isTrue)
                        neuron.inc(input);
                    else
                        neuron.dec(input);
                }
            } catch (IOException e) {
                System.out.println("This file doesn't exist");
            }
        }
    }

    private static void out(int[][] matrix) {
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 3; x++) {
                System.out.print(matrix[x][y] + " ");
            }
            System.out.println();
        }
    }
}
