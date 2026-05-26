package cs.rug.gbpmruntime.common.api.base;

public interface VoidProcessor<I extends ProcessorRequest> {
    void process(I request);
}
