package cs.rug.monitoringresultsservice.api.base;

@FunctionalInterface
public interface Processor<I, O> {
    O process(I input);
}
