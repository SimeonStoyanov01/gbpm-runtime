package cs.rug.monitoringresultsservice.api.exceptions;

public class ProcessInstanceNotFoundException extends RuntimeException {

    public ProcessInstanceNotFoundException(Long processInstanceKey) {
        super("Process instance not found: " + processInstanceKey);
    }
}
