import { useState } from 'react';
import type { ActiveViolationFilters, MonitoringRecord } from '../../api/types';
import { Endpoint } from '../../components/Endpoint';
import { EmptyState } from '../../components/EmptyState';
import { MonitoringRecordDialog } from '../../components/MonitoringRecordDialog';
import { Panel } from '../../components/Panel';
import { StatusBadge } from '../../components/StatusBadge';
import { formatDateTime, formatMeasurement, formatVariance } from '../../utils/format';

type ActiveViolationsProps = {
  violations: MonitoringRecord[];
  onFilter: (filters: ActiveViolationFilters) => void;
};

export function ActiveViolations({ violations, onFilter }: ActiveViolationsProps) {
  const [filters, setFilters] = useState<ActiveViolationFilters>({});
  const [selectedRecord, setSelectedRecord] = useState<MonitoringRecord>();

  function updateFilter(key: keyof ActiveViolationFilters, value: string) {
    setFilters((current) => ({ ...current, [key]: value }));
  }

  return (
    <section id="violations">
      <h2 className="section-title">Active Violations</h2>
      <Panel title="Current threshold violations" action={<Endpoint>GET :8094 /api/monitoring/violations/active</Endpoint>}>
        <div className="filter-bar" style={{ gridTemplateColumns: '1fr 1fr auto' }}>
          <div>
            <label>Process Definition Key</label>
            <input value={filters.processDefinitionKey || ''} onChange={(event) => updateFilter('processDefinitionKey', event.target.value)} />
          </div>
          <div>
            <label>BPMN Process ID</label>
            <input value={filters.bpmnProcessId || ''} onChange={(event) => updateFilter('bpmnProcessId', event.target.value)} />
          </div>
          <button onClick={() => onFilter(filters)}>Apply filters</button>
        </div>

        {violations.length === 0 ? (
          <EmptyState>No active threshold violations.</EmptyState>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Task</th>
                  <th>Element Instance</th>
                  <th>KEI</th>
                  <th>Calculated</th>
                  <th>Target</th>
                  <th>Variance</th>
                  <th>Status</th>
                  <th>Occurred At</th>
                </tr>
              </thead>
              <tbody>
                {violations.map((violation) => (
                  <tr key={violation.evaluationEventId} className="violation-row">
                    <td>
                      <button className="record-link" type="button" onClick={() => setSelectedRecord(violation)}>
                        {violation.elementName || violation.bpmnElementId || '-'}
                      </button>
                    </td>
                    <td className="mono">{violation.elementInstanceKey || '-'}</td>
                    <td>{violation.keiId || '-'}</td>
                    <td>{formatMeasurement(violation.calculatedValue, violation.calculatedUnit)}</td>
                    <td>{formatMeasurement(violation.targetValue, violation.calculatedUnit)}</td>
                    <td>{formatVariance(violation.difference, violation.calculatedUnit)}</td>
                    <td><StatusBadge value={violation.evaluationStatus} /></td>
                    <td>{formatDateTime(violation.evaluatedAt)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </Panel>
      <MonitoringRecordDialog record={selectedRecord} onClose={() => setSelectedRecord(undefined)} />
    </section>
  );
}
