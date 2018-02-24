import classifiers.MultinomialBayesTextClassifier;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class TextClassifierRunner {
    public static void main(String[] args) throws Exception {
        TextClassifier textClassifier = new TextClassifier(MultinomialBayesTextClassifier.class);

        int trainCapacity = 9000;
        int testCapacity = 12115 - trainCapacity;
        int trainNoCompCapacity = 18000;
        int testNoCompCapacity = 22686 - trainNoCompCapacity;

        BufferedReader compReader = new BufferedReader(new FileReader(new Resource("politics_comprom").get()));
        BufferedReader noCompReader = new BufferedReader(new FileReader(new Resource("everything_else").get()));

        ArrayList<String> compListTest = new ArrayList<String>(testCapacity);
        ArrayList<String> noCompListTest = new ArrayList<String>(testCapacity);

        for (int i = 0; i < trainNoCompCapacity; i++) {
            textClassifier.addTrainingInstance(noCompReader.readLine(), TextClassifier.Topic.OTHER);
        }

        for (int i = 0; i < trainCapacity; i++) {
            textClassifier.addTrainingInstance(compReader.readLine(), TextClassifier.Topic.POLITICS_COMPROM);
        }

        for (int i = 0; i < testCapacity; i++) {
            compListTest.add(compReader.readLine());
        }

        for (int i = 0; i < testNoCompCapacity; i++) {
            noCompListTest.add(noCompReader.readLine());
        }

        textClassifier.train();

        int rightGuessedComp = 0;
        int rightGuessedNoComp = 0;

        int TP = 0, FP = 0, TN = 0, FN = 0;

        for (int i = 0; i < testCapacity; i++) {
            Map<TextClassifier.Topic, Double> res = textClassifier.classify(compListTest.get(i));

            if (res.get(TextClassifier.Topic.POLITICS_COMPROM) > 0.5) {
                TP++;
                rightGuessedComp++;
            } else {
                FN++;
            }
        }

        for (int i = 0; i < testNoCompCapacity; i++) {
            Map<TextClassifier.Topic, Double> res2 = textClassifier.classify(noCompListTest.get(i));

            if (res2.get(TextClassifier.Topic.POLITICS_COMPROM) < 0.5) {
                TN++;
                rightGuessedNoComp++;
            } else {
                FP++;
            }
        }

        System.out.println(rightGuessedComp / (double) testCapacity);
        System.out.println(rightGuessedNoComp / (double) testNoCompCapacity);
        System.out.println((rightGuessedNoComp + rightGuessedComp) / (double) (testCapacity + testNoCompCapacity));

        double precision = TP / (double) (TP + FP);
        double recall = TP / (double) (TP + FN);
        double F = 2 * (precision * recall / (precision + recall));

        System.out.println("TP == " + TP + ", TN == " + TN + ", FP == " + FP + ", FN == " + FN);
        System.out.println("Precision == " + precision + ", recall == " + recall + ", F == " + F);

        Scanner sc = new Scanner(new FileReader("compromat_11"));

        while (sc.hasNext()) {
            String line = sc.nextLine();
            line = line.substring(12, line.length());
            System.out.println(textClassifier.classify(line) + " " + line);
        }

        System.out.println("-----------------------------------==");

        sc = new Scanner(new FileReader("no_compromat_11"));

        while (sc.hasNext()) {
            String line = sc.nextLine();
            System.out.println(textClassifier.classify(line) + " " + line);
        }

        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println(textClassifier.classify(scanner.nextLine()));
        }
    }
}

class Resource {
    private URL resource;

    Resource(String innerPath) {
        ClassLoader classLoader = getClass().getClassLoader();
        resource = classLoader.getResource(innerPath);
    }

    String get() {
        return resource.getPath();
    }
}
