package cs.rug.monitoringresultsservice.api.operations.recordcalculation;

import cs.rug.monitoringresultsservice.api.base.VoidProcessor;
import cs.rug.monitoringresultsservice.api.events.calculationcompleted.KeiCalculationCompletedEvent;

public interface RecordCalculationOperation extends VoidProcessor<KeiCalculationCompletedEvent> {
}
