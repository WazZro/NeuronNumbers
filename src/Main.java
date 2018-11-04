import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Started NeuronNetwork...");
        NeuronNetwork network = new NeuronNetwork(10, 60, 100);
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
                        //study(neuron, reader);
                        System.out.println("The learning stopped");
                        break;
                    case "what":
                        var res = network.recognize(ImageIO.read(Files.newInputStream(Paths.get(command[1]))));
                        System.out.println(res);
                        break;
                    case "debug":
                        var str = network.check(ImageIO.read(Files.newInputStream(Paths.get(command[1]))));
                        System.out.println(str);
                        break;
                    case "help":
                        System.out.println("learn *neuron's name* -- to learn a neuron\n" +
                                "what *file's name* -- what is number on the picture\n" +
                                "debug *file's name* -- to debugging");
                        break;
                    case "exit":
                        return;
                    default:
                        System.out.println("Wrong command. Try to repeat. Write 'help'");
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Specify command parameters!");
            } catch (NullPointerException e) {
                System.out.println("No files found");
            }
        }
    }

    private static void study(Neuron neuron, BufferedReader reader) throws IOException {
        String str;
        int[][] input = new int[60][100];

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
                NeuronNetwork.convert(ImageIO.read(Files.newInputStream(Paths.get(str))), input);

                var isTrue = neuron.recognize(input);
                System.out.println(isTrue + " " + neuron.getSum());
                System.out.print("Agree? (Y/n): ");
                var a = reader.readLine().equals("n");

            } catch (IOException e) {
                System.out.println("This file is wrong or doesn't exist");
            }


//            try {
//                NeuronNetwork.convert(ImageIO.read(new File(str)), input);
//
//                var isTrue = neuron.recognize(input);
//                System.out.println(isTrue + " " + neuron.getSum());
//                System.out.print("Agree? (Y/n): ");
//                var a = reader.readLine().equals("n");
//
//                if (a)
//                    neuron.study(input, isTrue);
//            } catch (IOException e) {
//                System.out.println("This file doesn't exist");
//            }
        }
    }

    private static void out(int[][] matrix) {
        for (int y = 0; y < matrix[0].length; y++) {
            for (int x = 0; x < matrix.length; x++) {
                System.out.print(matrix[x][y] + " ");
            }
            System.out.println();
        }
    }
}
