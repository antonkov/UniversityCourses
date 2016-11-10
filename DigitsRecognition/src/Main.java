import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

/**
 * Created by antonkov on 12/20/2014.
 */
public class Main {
    final String trainLabelsFname = "train-labels.idx1-ubyte";
    final String trainImagesFname = "train-images.idx3-ubyte";
    final String testLabelsFname = "t10k-labels.idx1-ubyte";
    final String testImagesFname = "t10k-images.idx3-ubyte";
    final int imgHeight = 28;
    final int imgWeight = 28;
    final int imgSize = imgHeight * imgWeight;
    final int countDigits = 10;

    PrintWriter out = new PrintWriter(System.out);

    byte[] getBytes(byte[] bytes, int st, int en) {
        byte[] res = new byte[en - st];
        for (int i = 0; i < res.length; i++)
            res[i] = bytes[st + i];
        return res;
    }

    int getInt(byte[] data, int st) {
        return java.nio.ByteBuffer.wrap(getBytes(data, st, st + 4)).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    final int maxCount = 60 * 1000;

    int[] readLabels(String filename) throws IOException {
        Path path = Paths.get(filename);
        byte[] data = Files.readAllBytes(path);
        int st = 8;
        int cnt = getInt(data, 4);
        cnt = Math.min(cnt, maxCount); // TODO: delete
        int[] labels = new int[cnt];
        for (int i = 0; i < cnt; ++i) {
            labels[i] = data[st++];
        }
        return labels;
    }

    byte[][][] readImages(String filename) throws IOException {
        Path path = Paths.get(filename);
        byte[] data = Files.readAllBytes(path);
        int cnt = getInt(data, 4), h = getInt(data, 8), w = getInt(data, 12);
        cnt = Math.min(cnt, maxCount); // TODO: delete
        int st = 16;
        byte[][][] result = new byte[cnt][][];
        for (int i = 0; i < cnt; i++) {
            result[i] = new byte[h][w];
            for (int j = 0; j < h; j++)
                for (int k = 0; k < w; k++)
                    result[i][j][k] = data[st++];
        }
        return result;
    }

    class Image {
        byte[][] data;
        int label;

        public Image(byte[][] data, int label) {
            this.data = data;
            this.label = label;
        }

        @Override
        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append("-----------" + label + "----------------\n");
            for (byte[] b : data) {
                for (byte bb : b) {
                    s.append((bb != 0) ? "#" : ".");
                }
                s.append("\n");
            }
            return s.toString();
        }
    }

    public static BufferedImage getGrayscale(byte[][] data, int h, int w) {
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        int[] nBits = {8};
        ColorModel cm = new ComponentColorModel(cs, nBits, false, true,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        SampleModel sm = cm.createCompatibleSampleModel(w, h);
        byte[] tmp = new byte[h * w];
        for (int i = 0; i < h; i++)
            for (int j = 0; j < w; j++)
                tmp[i * w + j] = data[i][j];
        DataBufferByte db = new DataBufferByte(tmp, h * w);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);
        BufferedImage result = new BufferedImage(cm, raster, false, null);

        return result;
    }

    static final Random rng = new Random();

    class Network {
        class Edge {
            int a, b;
            double w;
            Edge backEdge;

            public Edge(int a, int b, double w) {
                this.a = a;
                this.b = b;
                this.w = w;
            }

            void changeW(double newW) {
                w = newW;
                backEdge.w = newW;
            }
        }

        class Node {
            double val, err, notActivatedVal;
            ArrayList<Edge> forward, backward;

            public Node() {
                forward = new ArrayList<Edge>();
                backward = new ArrayList<Edge>();
            }
        }

        Node[] graph;

        void addEdge(int a, int b, double w) {
            Edge e = new Edge(a, b, w);
            Edge backE = new Edge(b, a, w);
            e.backEdge = backE;
            backE.backEdge = e;

            graph[a].forward.add(e);
            graph[b].backward.add(backE);
        }

        double learnSpeed;
        int countLayers;
        int[] layerSize;
        int[] layerStart;
        int beforeLayersSize;
        int sizeTotal;

        double activationFunc(double z) {
            return 1.0 / (1 + Math.exp(-z));
        }

        double activationFuncDeriv(double z) {
            double funcVal = activationFunc(z);
            return funcVal * (1 - funcVal);
        }


        double randomWeight(double cnt) {
            return (rng.nextDouble() * 2 - 1) / (2 * cnt);
        }

        public Network() {
            learnSpeed = 0.1;

            countLayers = 2;
            beforeLayersSize = countLayers - 1;
            layerSize = new int[countLayers];
            layerStart = new int[countLayers];
            layerSize[0] = imgSize;
            //layerSize[1] = 50;
            layerSize[1] = countDigits;
            //layerSize[1] = 500;
            //layerSize[2] = countDigits;

            layerStart[0] = beforeLayersSize;
            for (int i = 1; i < countLayers; i++)
                layerStart[i] = layerStart[i - 1] + layerSize[i - 1];

            sizeTotal = countLayers - 1;
            for (int size : layerSize)
                sizeTotal += size;

            graph = new Node[sizeTotal];
            for (int i = 0; i < graph.length; i++) {
                graph[i] = new Node();
            }

            for (int layer = 0; layer < countLayers - 1; layer++) {
                for (int j = 0; j < layerSize[layer + 1]; j++) {
                    addEdge(layer, layerStart[layer + 1] + j, randomWeight(layerSize[layer]));
                }
                for (int i = 0; i < layerSize[layer]; i++) {
                    for (int j = 0; j < layerSize[layer + 1]; j++) {
                        addEdge(layerStart[layer] + i, layerStart[layer + 1] + j, randomWeight(layerSize[layer]));
                    }
                }
            }
        }

        void goForward(int v, boolean[] was) {
            if (was[v])
                return;
            Node node = graph[v];
            if (!node.backward.isEmpty()) {
                node.notActivatedVal = 0;
                for (Edge e : node.backward) {
                    goForward(e.b, was);
                    node.notActivatedVal += e.w * graph[e.b].val;
                }
                node.val = activationFunc(node.notActivatedVal);
            }
            was[v] = true;
        }

        void goBackward(int v, boolean[] was) {
            if (was[v])
                return;
            Node node = graph[v];
            if (!node.forward.isEmpty()) {
                node.err = 0;
                for (Edge e : node.forward) {
                    goBackward(e.b, was);
                    Node forwardNode = graph[e.b];
                    node.err += forwardNode.err * activationFuncDeriv(forwardNode.notActivatedVal) * e.w;
                    e.changeW(e.w - learnSpeed * forwardNode.err * activationFuncDeriv(forwardNode.notActivatedVal) * node.val);
                }
            }
            was[v] = true;
        }

        void fillInput(Image img) {
            int pFill = 0;
            for (int i = 0; i < beforeLayersSize; i++)
                graph[pFill++].val = -1;
            for (byte[] row : img.data) {
                for (byte x : row) {
                    graph[pFill++].val = (x & 0xFF) / 255.0;
                }
            }
        }

        double learn(Image img) {
            fillInput(img);

            boolean[] was = new boolean[sizeTotal];
            for (int i = 0; i < layerSize[countLayers - 1]; ++i)
                goForward(layerStart[countLayers - 1] + i, was);

            double[] should = new double[layerSize[countLayers - 1]];
            Arrays.fill(should, 0);
            should[img.label] = 1;
            double Qi = 0;
            for (int i = 0; i < layerSize[countLayers - 1]; i++) {
                double err = graph[layerStart[countLayers - 1] + i].val - should[i];
                graph[layerStart[countLayers - 1] + i].err = err;
                Qi += err * err;
            }

            was = new boolean[sizeTotal];
            for (int i = 0; i < layerStart[1]; i++)
                goBackward(i, was);
            return Qi;
        }

        int recognize(Image img) {
            fillInput(img);

            int pCheck = sizeTotal - countDigits;
            int best = 0;
            double bestVal = Double.NEGATIVE_INFINITY;
            boolean[] was = new boolean[sizeTotal];
            for (int i = 0; i < countDigits; ++i) {
                goForward(pCheck, was);
                if (graph[pCheck].val > bestVal) {
                    bestVal = graph[pCheck].val;
                    best = i;
                }
                pCheck++;
            }
            return best;
        }
    }

    BufferedImage imageToDraw;

    void run() throws IOException {
        int[] trainLabels = readLabels(trainLabelsFname);
        int[] testLabels = readLabels(testLabelsFname);
        byte[][][] trainImages = readImages(trainImagesFname);
        byte[][][] testImages = readImages(testImagesFname);
        ArrayList<Image> train = new ArrayList<Image>(), test = new ArrayList<Image>();
        for (int i = 0; i < trainLabels.length; i++)
            train.add(new Image(trainImages[i], trainLabels[i]));
        for (int i = 0; i < testLabels.length; i++)
            test.add(new Image(testImages[i], testLabels[i]));

        int debCnt = 0;
        Network network = new Network();
        int countTrainPasses = 1;
        for (int trainPass = 0; trainPass < countTrainPasses; trainPass++) {
            Collections.shuffle(train);
            for (Image img : train) {
                double qi = network.learn(img);
                debCnt++;
                if (debCnt % 100 == 0)
                    System.out.println(debCnt + " : " + qi);
            }
        }

        int result = 0;
        for (Image img : test) {
            int networkRes = network.recognize(img);
            if (img.label == networkRes) {
                result++;
            } else {
                if (imageToDraw == null)
                    imageToDraw = getGrayscale(img.data, img.data.length, img.data[0].length);
            }
        }
        out.println(result * 100.0 / test.size() + "%");
        out.flush();
    }

    void drawImage() {
        JFrame frame = new JFrame("digit");
        frame.setVisible(true);
        frame.add(new JLabel(new ImageIcon(imageToDraw.getScaledInstance(400, 400, 0))));

        frame.pack();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void main(String[] args) {
        try {
            long before = System.currentTimeMillis();

            Main main = new Main();
            main.run();
            main.drawImage();

            System.out.println(System.currentTimeMillis() - before);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
