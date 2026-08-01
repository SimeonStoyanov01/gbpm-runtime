package cs.rug.observationservice.api.base;

@FunctionalInterface
public interface VoidProcessor<I> {
    void process(I input);
}
