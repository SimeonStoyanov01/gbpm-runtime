import { useState } from 'react';
import type { MonitoringRecord, MonitoringRecordFilters } from '../../api/types';
import { Endpoint } from '../../components/Endpoint';
import { EmptyState } from '../../components/EmptyState';
import { Panel } from '../../components/Panel';
import { StatusBadge } from '../../components/StatusBadge';
import { formatDateTime, formatMeasurement } from '../../utils/format';

type MonitoringRecordsProps = {
  records: MonitoringRecord[];
  onFilter: (filters: MonitoringRecordFilters) => void;
};

export function MonitoringRecords({ records, onFilter }: MonitoringRecordsProps) {
  const [filters, setFilters] = useState<MonitoringRecordFilters>({});

  function updateFilter(key: keyof MonitoringRecordFilters, value: string) {
    setFilters((current) => ({ ...current, [key]: value }));
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
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Status</th>
                  <th>Process</th>
                  <th>Instance Key</th>
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
                  <tr key={`${record.calculationEventId}-${record.evaluationEventId}`} className={record.evaluationStatus === 'VIOLATED' ? 'violation-row' : undefined}>
                    <td><StatusBadge value={record.evaluationStatus} /></td>
                    <td>{record.bpmnProcessId || '-'}</td>
                    <td className="mono">{record.processInstanceKey || '-'}</td>
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
        )}
      </Panel>
    </section>
  );
}
