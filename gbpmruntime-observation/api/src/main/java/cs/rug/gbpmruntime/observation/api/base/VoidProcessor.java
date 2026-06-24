package cs.rug.gbpmruntime.observation.api.base;

public interface VoidProcessor<I extends ProcessorRequest> {
    void process(I request);
}
