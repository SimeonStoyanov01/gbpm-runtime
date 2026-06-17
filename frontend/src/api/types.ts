export type DeployProcessResponse = {
  deploymentKey: string;
  processDefinitionKey: string;
  bpmnProcessId: string;
  version: number;
  resourceName: string;
  tenantId?: string;
  status: string;
};

export type StartProcessInstanceResponse = {
  processDefinitionKey: string;
  bpmnProcessId: string;
  version: number;
  processInstanceKey: string;
  status: string;
};

export type KeiAnnotation = {
  id: string;
  unit: string;
  targetValue: string;
  icon?: string;
};

export type ElementKeiAnnotations = {
  bpmnElementId: string;
  bpmn4esKeiAnnotations: KeiAnnotation[];
};

export type FindProcessKeisResponse = {
  processDefinitionKey: number;
  bpmn4esElementKeiAnnotations: ElementKeiAnnotations[];
};

export type MonitoringRecord = {
  calculationEventId?: string;
  evaluationEventId?: string;
  calculationRequestId?: string;
  observationId?: string;
  sourceEventId?: string;
  processDefinitionKey?: number;
  bpmnProcessId?: string;
  processInstanceKey?: number;
  elementInstanceKey?: number;
  bpmnElementId?: string;
  keiId?: string;
  calculatedValue?: number;
  calculatedUnit?: string;
  targetValue?: number;
  difference?: number;
  evaluationStatus?: string;
  calculatedAt?: string;
  evaluatedAt?: string;
};

export type ThresholdViolation = {
  eventId?: string;
  processDefinitionKey?: number;
  bpmnProcessId?: string;
  serviceTaskId?: string;
  processInstanceKey?: number;
  emissionType?: string;
  calculatedValue?: number;
  targetValue?: number;
  difference?: number;
  status?: string;
  occurredAt?: string;
};

export type MonitoringRecordFilters = {
  processInstanceKey?: string;
  processDefinitionKey?: string;
  bpmnProcessId?: string;
  evaluationStatus?: string;
};

export type ActiveViolationFilters = {
  processDefinitionKey?: string;
  bpmnProcessId?: string;
};
