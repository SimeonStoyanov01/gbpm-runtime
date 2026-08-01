package cs.rug.camunda8integration.api.base;

@FunctionalInterface
public interface VoidProcessor<I> {
    void process(I input);
}
