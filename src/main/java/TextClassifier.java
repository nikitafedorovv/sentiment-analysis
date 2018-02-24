import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextClassifier {
    private List<TopicClassifier> topicClassifiers;

    public TextClassifier(Class clazz) {
        topicClassifiers = new ArrayList<TopicClassifier>(Topic.values().length);

        for (Topic topic : Topic.values()) {
            topicClassifiers.add(new TopicClassifier(topic, clazz));
        }
    }

    public Map<Topic, Double> classify(String text) {
        Map<Topic, Double> result = new HashMap<Topic, Double>(Topic.values().length);

        for (TopicClassifier topicClassifier : topicClassifiers) {
            result.put(topicClassifier.getTopic(), topicClassifier.classify(text));
        }

        return result;
    }

    public void addTrainingInstance(String text, Topic topic) {
        for (TopicClassifier topicClassifier : topicClassifiers) {
            if (topicClassifier.getTopic() != topic) {
                topicClassifier.addTrainingInstance(text, false);
            } else {
                topicClassifier.addTrainingInstance(text, true);
            }
        }
    }

    public void train() {
        for (TopicClassifier topicClassifier : topicClassifiers) {
            topicClassifier.train();
        }
    }

    enum Topic {
        /*COOKING,*/
        POLITICS_COMPROM/*,
        SPORT,
        IT,
        ART*/,
        OTHER
    }
}


