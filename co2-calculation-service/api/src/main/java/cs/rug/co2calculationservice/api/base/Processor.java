package cs.rug.co2calculationservice.api.base;

public interface Processor<R extends ProcessorResponse, I extends ProcessorRequest> {

    R process(I request);
}
