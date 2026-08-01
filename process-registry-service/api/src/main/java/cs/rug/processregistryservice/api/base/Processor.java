package cs.rug.processregistryservice.api.base;

@FunctionalInterface
public interface Processor<I, O> {
    O process(I input);
}
