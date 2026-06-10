package cs.rug.camunda8integration.api.base;

public interface Processor<R extends ProcessorResponse, I extends ProcessorRequest> {
    R process(I request);
}
