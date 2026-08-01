package cs.rug.camunda8integration.api.base;

@FunctionalInterface
public interface Processor<I, O> {
    O process(I input);
}
