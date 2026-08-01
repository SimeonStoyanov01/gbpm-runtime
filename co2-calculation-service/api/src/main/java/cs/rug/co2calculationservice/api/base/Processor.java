package cs.rug.co2calculationservice.api.base;

@FunctionalInterface
public interface Processor<I, O> {
    O process(I input);
}
