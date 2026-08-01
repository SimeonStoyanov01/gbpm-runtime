package cs.rug.co2calculationservice.api.base;

@FunctionalInterface
public interface VoidProcessor<I> {
    void process(I input);
}
