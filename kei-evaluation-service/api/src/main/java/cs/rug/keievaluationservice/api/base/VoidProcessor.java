package cs.rug.keievaluationservice.api.base;

@FunctionalInterface
public interface VoidProcessor<I> {
    void process(I input);
}
