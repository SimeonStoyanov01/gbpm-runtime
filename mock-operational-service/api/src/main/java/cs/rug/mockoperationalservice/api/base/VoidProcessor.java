package cs.rug.mockoperationalservice.api.base;

@FunctionalInterface
public interface VoidProcessor<I> {
    void process(I input);
}
