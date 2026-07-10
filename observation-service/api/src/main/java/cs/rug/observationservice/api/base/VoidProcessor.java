package cs.rug.observationservice.api.base;

public interface VoidProcessor<I extends ProcessorRequest> {
    void process(I request);
}
