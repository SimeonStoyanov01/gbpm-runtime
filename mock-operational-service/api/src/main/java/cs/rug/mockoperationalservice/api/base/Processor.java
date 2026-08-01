package cs.rug.mockoperationalservice.api.base;

@FunctionalInterface
public interface Processor<I, O> {
    O process(I input);
}
