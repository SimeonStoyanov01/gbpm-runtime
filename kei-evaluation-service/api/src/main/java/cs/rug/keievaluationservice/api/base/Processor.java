package cs.rug.keievaluationservice.api.base;

public interface Processor<T extends ProcessorRequest, R extends ProcessorResponse> {

    R process(T request);
}
