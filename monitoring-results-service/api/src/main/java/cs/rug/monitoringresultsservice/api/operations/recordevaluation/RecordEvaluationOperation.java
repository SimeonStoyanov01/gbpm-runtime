package cs.rug.monitoringresultsservice.api.operations.recordevaluation;

import cs.rug.monitoringresultsservice.api.base.VoidProcessor;
import cs.rug.monitoringresultsservice.api.events.evaluationcompleted.KeiEvaluationCompletedEvent;

public interface RecordEvaluationOperation extends VoidProcessor<KeiEvaluationCompletedEvent> {
}
