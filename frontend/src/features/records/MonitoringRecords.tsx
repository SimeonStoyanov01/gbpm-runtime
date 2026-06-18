import { useState } from 'react';
import { findProcessInstanceDetails } from '../../api/monitoringApi';
import type { MonitoringRecord, MonitoringRecordFilters, ProcessInstanceDetails, ThresholdViolation } from '../../api/types';
import { Endpoint } from '../../components/Endpoint';
import { EmptyState } from '../../components/EmptyState';
import { Panel } from '../../components/Panel';
import { StatusBadge } from '../../components/StatusBadge';
import { formatDateTime, formatMeasurement } from '../../utils/format';
import { ProcessDiagramViewer } from './ProcessDiagramViewer';

type MonitoringRecordsProps = {
  records: MonitoringRecord[];
  onFilter: (filters: MonitoringRecordFilters) => void;
};

export function MonitoringRecords({ records, onFilter }: MonitoringRecordsProps) {
  const [filters, setFilters] = useState<MonitoringRecordFilters>({});
  const [expandedInstanceKey, setExpandedInstanceKey] = useState<number>();
  const [detailsByInstance, setDetailsByInstance] = useState<Record<number, ProcessInstanceDetails>>({});
  const [loadingInstanceKey, setLoadingInstanceKey] = useState<number>();
  const [detailError, setDetailError] = useState<string>();

  const groups = groupByProcessInstance(records);

  function updateFilter(key: keyof MonitoringRecordFilters, value: string) {
    setFilters((current) => ({ ...current, [key]: value }));
  }

  async function toggleInstance(processInstanceKey?: number) {
    if (!processInstanceKey) {
      return;
    }

    if (expandedInstanceKey === processInstanceKey) {
      setExpandedInstanceKey(undefined);
      return;
    }

    setExpandedInstanceKey(processInstanceKey);
    setDetailError(undefined);

    if (detailsByInstance[processInstanceKey]) {
      return;
    }

    try {
      setLoadingInstanceKey(processInstanceKey);
      const details = await findProcessInstanceDetails(processInstanceKey);
      setDetailsByInstance((current) => ({ ...current, [processInstanceKey]: details }));
    } catch (exception) {
      setDetailError(exception instanceof Error ? exception.message : 'Failed to load process instance details.');
    } finally {
      setLoadingInstanceKey(undefined);
    }
  }

  return (
    <section id="records">
      <h2 className="section-title">Monitoring Records</h2>
      <Panel title="Persisted calculation and evaluation results" action={<Endpoint>GET :8094 /api/monitoring/records</Endpoint>}>
        <div className="filter-bar">
          <div>
            <label>Process Definition Key</label>
            <input value={filters.processDefinitionKey || ''} onChange={(event) => updateFilter('processDefinitionKey', event.target.value)} />
          </div>
          <div>
            <label>Process Instance Key</label>
            <input value={filters.processInstanceKey || ''} onChange={(event) => updateFilter('processInstanceKey', event.target.value)} />
          </div>
          <div>
            <label>BPMN Process ID</label>
            <input value={filters.bpmnProcessId || ''} onChange={(event) => updateFilter('bpmnProcessId', event.target.value)} />
          </div>
          <div>
            <label>Evaluation Status</label>
            <select value={filters.evaluationStatus || ''} onChange={(event) => updateFilter('evaluationStatus', event.target.value)}>
              <option value="">ALL</option>
              <option value="WITHIN_TARGET">WITHIN_TARGET</option>
              <option value="VIOLATED">VIOLATED</option>
            </select>
          </div>
          <button onClick={() => onFilter(filters)}>Apply filters</button>
        </div>

        {records.length === 0 ? (
          <EmptyState>No monitoring records available yet. Deploy and start a process instance to generate results.</EmptyState>
        ) : (
          <div className="instance-list">
            {groups.map((group) => {
              const details = group.processInstanceKey ? detailsByInstance[group.processInstanceKey] : undefined;
              const expanded = group.processInstanceKey === expandedInstanceKey;

              return (
                <article className="instance-card" key={group.key}>
                  <button className="instance-summary" onClick={() => toggleInstance(group.processInstanceKey)} disabled={!group.processInstanceKey}>
                    <span>
                      <strong>{group.bpmnProcessId || 'Unknown process'}</strong>
                      <span className="mono">{group.processInstanceKey || 'missing instance key'}</span>
                    </span>
                    <span>{group.records.length} result{group.records.length === 1 ? '' : 's'}</span>
                    <StatusBadge value={group.hasViolation ? 'VIOLATED' : group.latestStatus} />
                    <span>{expanded ? 'Collapse' : 'Expand'}</span>
                  </button>

                  {expanded && (
                    <div className="instance-details">
                      {loadingInstanceKey === group.processInstanceKey && <EmptyState>Loading process instance details...</EmptyState>}
                      {detailError && <p className="error">{detailError}</p>}
                      {details && <ProcessInstanceDetailsPanel details={details} />}
                    </div>
                  )}
                </article>
              );
            })}
          </div>
        )}
      </Panel>
    </section>
  );
}

function ProcessInstanceDetailsPanel({ details }: { details: ProcessInstanceDetails }) {
  return (
    <div className="instance-detail-grid">
      <div className="instance-metadata">
        <dl className="metadata-grid">
          <div>
            <dt>Process</dt>
            <dd>{details.bpmnProcessId || '-'}</dd>
          </div>
          <div>
            <dt>Definition Key</dt>
            <dd className="mono">{details.processDefinitionKey || '-'}</dd>
          </div>
          <div>
            <dt>Instance Key</dt>
            <dd className="mono">{details.processInstanceKey || '-'}</dd>
          </div>
          <div>
            <dt>Records</dt>
            <dd>{details.records.length}</dd>
          </div>
        </dl>
      </div>

      <ProcessDiagramViewer bpmnXml={details.bpmnXml} records={details.records} />

      <div>
        <h4>Instance Results</h4>
        <MonitoringRecordTable records={details.records} />
      </div>

      <div>
        <h4>Violations</h4>
        {details.violations.length === 0 ? (
          <EmptyState>No active violations for this instance.</EmptyState>
        ) : (
          <ThresholdViolationTable violations={details.violations} />
        )}
      </div>
    </div>
  );
}

function MonitoringRecordTable({ records }: { records: MonitoringRecord[] }) {
  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Status</th>
            <th>Activity ID</th>
            <th>KEI</th>
            <th>Calculated</th>
            <th>Target</th>
            <th>Difference</th>
            <th>Calculated At</th>
            <th>Evaluated At</th>
          </tr>
        </thead>
        <tbody>
          {records.map((record) => (
            <tr key={monitoringRecordKey(record)} className={record.evaluationStatus === 'VIOLATED' ? 'violation-row' : undefined}>
              <td><StatusBadge value={record.evaluationStatus} /></td>
              <td>{record.bpmnElementId || '-'}</td>
              <td>{record.keiId || '-'}</td>
              <td>{formatMeasurement(record.calculatedValue, record.calculatedUnit)}</td>
              <td>{formatMeasurement(record.targetValue, record.calculatedUnit)}</td>
              <td>{record.difference ?? '-'}</td>
              <td>{formatDateTime(record.calculatedAt)}</td>
              <td>{formatDateTime(record.evaluatedAt)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

function ThresholdViolationTable({ violations }: { violations: ThresholdViolation[] }) {
  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Status</th>
            <th>Activity ID</th>
            <th>Emission Type</th>
            <th>Calculated</th>
            <th>Target</th>
            <th>Difference</th>
            <th>Occurred At</th>
          </tr>
        </thead>
        <tbody>
          {violations.map((violation) => (
            <tr key={violation.eventId || `${violation.processInstanceKey}-${violation.serviceTaskId}`} className="violation-row">
              <td><StatusBadge value={violation.status} /></td>
              <td>{violation.serviceTaskId || '-'}</td>
              <td>{violation.emissionType || '-'}</td>
              <td>{formatMeasurement(violation.calculatedValue)}</td>
              <td>{formatMeasurement(violation.targetValue)}</td>
              <td>{violation.difference ?? '-'}</td>
              <td>{formatDateTime(violation.occurredAt)}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

type ProcessInstanceGroup = {
  key: string;
  processInstanceKey?: number;
  bpmnProcessId?: string;
  latestStatus?: string;
  hasViolation: boolean;
  records: MonitoringRecord[];
};

function groupByProcessInstance(records: MonitoringRecord[]): ProcessInstanceGroup[] {
  const grouped = new Map<string, ProcessInstanceGroup>();

  records.forEach((record) => {
    const key = record.processInstanceKey ? String(record.processInstanceKey) : monitoringRecordKey(record);
    const current = grouped.get(key);
    if (current) {
      current.records.push(record);
      current.hasViolation = current.hasViolation || record.evaluationStatus === 'VIOLATED';
      current.latestStatus = current.latestStatus || record.evaluationStatus;
      return;
    }

    grouped.set(key, {
      key,
      processInstanceKey: record.processInstanceKey,
      bpmnProcessId: record.bpmnProcessId,
      latestStatus: record.evaluationStatus,
      hasViolation: record.evaluationStatus === 'VIOLATED',
      records: [record],
    });
  });

  return [...grouped.values()];
}

function monitoringRecordKey(record: MonitoringRecord): string {
  return record.evaluationEventId || record.calculationEventId || `${record.processInstanceKey}-${record.bpmnElementId}-${record.keiId}`;
}
