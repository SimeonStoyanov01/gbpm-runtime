import { queryString, requestJson } from './http';
import type {
  ActiveViolationFilters,
  MonitoringRecord,
  MonitoringRecordFilters,
  ProcessInstanceDetails,
} from './types';

export async function findMonitoringRecords(
  filters: MonitoringRecordFilters = {},
): Promise<MonitoringRecord[]> {
  return requestJson<MonitoringRecord[]>(`/api/monitoring/records${queryString(filters)}`);
}

export async function findActiveViolations(
  filters: ActiveViolationFilters = {},
): Promise<MonitoringRecord[]> {
  return requestJson<MonitoringRecord[]>(`/api/monitoring/violations/active${queryString(filters)}`);
}

export async function findProcessInstanceDetails(
  processInstanceKey: number,
): Promise<ProcessInstanceDetails> {
  return requestJson<ProcessInstanceDetails>(`/api/monitoring/process-instances/${processInstanceKey}`);
}
