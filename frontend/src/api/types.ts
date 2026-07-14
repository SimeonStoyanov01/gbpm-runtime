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

export type ActiveUserTask = {
  userTaskKey: string;
  name?: string;
  bpmnElementId?: string;
  state: string;
  assignee?: string;
  decisionVariable?: string;
};

export type FindActiveUserTasksResponse = {
  userTasks: ActiveUserTask[];
};

export type KeiAnnotation = {
  id: string;
  unit?: string;
  targetValue?: string;
  icon?: string;
};

export type ElementKeiAnnotations = {
  bpmnElementId: string;
  elementName?: string;
  elementType?: string;
  keiAnnotations: KeiAnnotation[];
};

export type FindProcessKeisResponse = {
  elementKeiAnnotations: ElementKeiAnnotations[];
};

export type MonitoringRecord = {
  calculationEventId?: string;
  evaluationEventId?: string;
  engineType?: string;
  calculatorId?: string;
  calculationMethod?: string;
  referenceSetId?: string;
  processDefinitionKey?: number;
  bpmnProcessId?: string;
  processInstanceKey?: number;
  elementInstanceKey?: number;
  bpmnElementId?: string;
  elementName?: string;
  keiId?: string;
  calculatedValue?: number;
  calculatedUnit?: string;
  targetValue?: number;
  difference?: number;
  evaluationStatus?: string;
  calculatedAt?: string;
  evaluatedAt?: string;
};

export type ProcessInstanceDetails = {
  processDefinitionKey?: number;
  bpmnProcessId?: string;
  resourceName?: string;
  processInstanceKey?: number;
  bpmnXml?: string;
  records: MonitoringRecord[];
  violations: MonitoringRecord[];
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
