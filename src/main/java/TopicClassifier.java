import classifiers.Classifier;

public class TopicClassifier {
    private TextClassifier.Topic topic;
    private Classifier classifier;

    public TopicClassifier(TextClassifier.Topic topic, Class clazz) {
        this.topic = topic;
        try {
            this.classifier = (Classifier) clazz.newInstance();
        } catch (java.lang.InstantiationException e) {
            // todo: handle exception
        } catch (java.lang.IllegalAccessException e) {
            // todo: handle exception
        }
    }

    public TextClassifier.Topic getTopic() {
        return topic;
    }

    public void train() {
        classifier.train();
    }

    public void addTrainingInstance(String text, boolean belongs) {
        classifier.addTrainingInstance(text, belongs);
    }

    public Double classify(String text) {
        return classifier.classify1(text);
    }
}

