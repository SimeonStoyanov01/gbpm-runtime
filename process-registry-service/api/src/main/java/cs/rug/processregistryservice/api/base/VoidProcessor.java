package cs.rug.processregistryservice.api.base;

@FunctionalInterface
public interface VoidProcessor<I> {
    void process(I input);
}
