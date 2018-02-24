package classifiers;

import com.google.inject.internal.util.Join;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.bayes.NaiveBayesMultinomialText;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;

import static classifiers.Classifier.BinaryClazz.*;

public class MultinomialBayesTextClassifier implements Classifier {

    private AbstractClassifier classifier;
    private Instances dataRaw;
    /*private String modelFile;*/

    public MultinomialBayesTextClassifier() {

        classifier = new NaiveBayesMultinomialText();

        /*modelFile = "SomeModel";*/

        ArrayList<Attribute> atts = new ArrayList<Attribute>(2);
        ArrayList<String> classVal = new ArrayList<String>();
        classVal.add(NOT_BELONGS.name());
        classVal.add(BELONGS.name());
        atts.add(new Attribute("content", (ArrayList<String>) null));
        atts.add(new Attribute("@@class@@", classVal));

        dataRaw = new Instances("TrainingInstances", atts, 10);
    }

    public Double classify1(String text) {
        try {

            double[] instanceValue = new double[dataRaw.numAttributes()];
            instanceValue[0] = dataRaw.attribute(0).addStringValue(text);

            Instance toClassify = new DenseInstance(1.0, instanceValue);
            dataRaw.setClassIndex(1);
            toClassify.setDataset(dataRaw);

            double prediction = this.classifier.classifyInstance(toClassify);

            double distribution[] = this.classifier.distributionForInstance(toClassify);

            return distribution[1];

        } catch (Exception e) {
            // todo: handle exception
            return 0d;
        }
    }

    public void train() {
        try {
            classifier.buildClassifier(dataRaw);
        } catch (Exception e) {
            // todo: handle exception
        }
    }

    public void addTrainingInstance(String text, boolean belongs) {
        BinaryClazz clazz = belongs ? BELONGS : NOT_BELONGS;

        double[] instanceValue = new double[dataRaw.numAttributes()];
        instanceValue[0] = dataRaw.attribute(0).addStringValue(Join.join(" ", text.split("\\s+")));
        instanceValue[1] = clazz.ordinal();
        dataRaw.add(new DenseInstance(1.0, instanceValue));
        dataRaw.setClassIndex(1);
    }
    /*
    public void testModel() throws Exception {
        Evaluation eTest = new Evaluation(dataRaw);
        eTest.evaluateModel(classifier, dataRaw);
        String strSummary = eTest.toSummaryString();
        System.out.println(strSummary);
    }

    public void showInstances() {
        System.out.println(dataRaw);
    }

    public Instances getDataRaw() {
        return dataRaw;
    }

    public void saveModel() throws Exception {
        weka.core.SerializationHelper.write(modelFile, classifier);
    }

    public void loadModel(String _modelFile) throws Exception {
        NaiveBayesMultinomialText classifier = (NaiveBayesMultinomialText) weka.core.SerializationHelper.read(_modelFile);
        this.classifier = classifier;
    }
*/
}