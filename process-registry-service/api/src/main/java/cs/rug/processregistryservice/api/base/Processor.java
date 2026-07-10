package cs.rug.processregistryservice.api.base;

public interface Processor<R extends ProcessorResponse, I extends ProcessorRequest> {
    R process(I request);
}
