package cs.rug.co2calculationservice.api.operations.calculateco2;

import cs.rug.co2calculationservice.api.base.VoidProcessor;
import cs.rug.co2calculationservice.api.events.calculationrequested.KeiCalculationRequestedEvent;

public interface CalculateCo2Operation extends VoidProcessor<KeiCalculationRequestedEvent> {
}
