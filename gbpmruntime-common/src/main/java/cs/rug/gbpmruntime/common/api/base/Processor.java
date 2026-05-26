package cs.rug.gbpmruntime.common.api.base;

public interface Processor<R extends ProcessorResponse, I extends ProcessorRequest> {
    R process(I request);
}
