package cs.rug.keievaluationservice.api.base;

@FunctionalInterface
public interface Processor<I, O> {
    O process(I input);
}
