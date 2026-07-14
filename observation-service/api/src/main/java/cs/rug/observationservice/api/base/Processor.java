package cs.rug.observationservice.api.base;

@FunctionalInterface
public interface Processor<I, O> {
    O process(I input);
}
