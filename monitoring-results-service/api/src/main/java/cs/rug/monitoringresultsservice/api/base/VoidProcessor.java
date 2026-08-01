package cs.rug.monitoringresultsservice.api.base;

@FunctionalInterface
public interface VoidProcessor<I> {
    void process(I input);
}
