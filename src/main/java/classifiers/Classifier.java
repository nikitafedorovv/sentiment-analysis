package classifiers;

public interface Classifier {

    void addTrainingInstance(String text, boolean belongs);

    void train();

    Double classify1(String text);

    enum BinaryClazz {
        NOT_BELONGS,
        BELONGS
    }
}
