import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Started NeuronNetwork...");
        NeuronNetwork network = new NeuronNetwork(10, 120, 130);
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
                        study(neuron, reader, 0.95);
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
                    case "autolearning":
                        autoLearning(network, Paths.get(command[1]), Integer.parseInt(command[2]), Integer.parseInt(command[3]), Double.parseDouble(command[4]));
                        break;
                    case "exit":
                        return;
                    default:
                        System.out.println("Wrong command. Try to repeat. Write 'help'");
                        break;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("Specify command parameters!");
            } catch (NullPointerException e) {
                System.out.println("No files found");
            }
        }
    }

    private static void study(Neuron neuron, BufferedReader reader, double limit) throws IOException {
        String str;
        int[][] input = new int[neuron.getSizeX()][neuron.getSizeY()];

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

                var isTrue = limit < neuron.recognize(input);
                System.out.println(isTrue + " " + neuron.getSum());
                System.out.print("Agree? (Y/n): ");
                var a = reader.readLine().equals("n");

                if (a)
                    neuron.study(input, !isTrue);
            } catch (IOException e) {
                System.out.println("This file is wrong or doesn't exist");
            }
        }
    }

    private static void autoLearning(NeuronNetwork network, Path path, int maxCycles, int repeat, double limit) throws IOException {
        if (Files.isDirectory(path)) {
            List<Path> files;
            var neurons = network.getNeurons();

            for (int i = 0; i < maxCycles; i++) {
                System.out.println("GENERATION " + i + " -----------------------------------------------------");
                for (int j = 0; j < repeat; j++) {
                    System.out.println("REPEAT " + j + " -----------------------------------------------------");
                    files = getFiles(i, path);

                    for (var n : neurons) {
                        System.out.println("NEURON " + n.getName() + " ------");
                        for (var p : files) {
                            verify(p, n, limit);
                        }
                    }
                }
            }
        }
    }

    private static List<Path> getFiles(int i, Path path) throws IOException {
        final int generation = i;

        return Files
                .list(path)
                .filter(p -> {
                    var str = p.getFileName().getFileName().toString().split("\\.");
                    return Integer.parseInt(str[1]) == generation;
                })
                .collect(Collectors.toList());
    }

    private static void verify(Path path, Neuron neuron, double limit) throws IOException {
        BufferedImage image = ImageIO.read(Files.newInputStream(path));
        int[][] input = new int[neuron.getSizeX()][neuron.getSizeY()];
        NeuronNetwork.convert(image, input);

        var res = limit < neuron.recognize(input);
        System.out.printf("File %s is %s, Neuron %s \n", path.getFileName().toString(), res, neuron.getName());

        if (neuron.getName().equals(path.getFileName().toString().split("\\.")[0])) {
            if (!res)
                neuron.study(input, true);
        } else if (res) {
            neuron.study(input, false);
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
