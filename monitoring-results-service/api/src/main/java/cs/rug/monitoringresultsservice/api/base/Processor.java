package cs.rug.monitoringresultsservice.api.base;

public interface Processor<T extends ProcessorRequest, R extends ProcessorResponse> {

    R process(T request);
}
