package cs.rug.monitoringresultsservice.api.operations.findprocessmodel;

import cs.rug.monitoringresultsservice.api.base.Processor;
import cs.rug.monitoringresultsservice.api.operations.registerprocessmodel.ProcessModelElement;

import java.util.List;

public interface FindProcessModelOperation extends Processor<Long, List<ProcessModelElement>> {
}
