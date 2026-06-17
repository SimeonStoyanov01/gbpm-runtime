import { MetricCard } from '../../components/MetricCard';
import { Panel } from '../../components/Panel';
import { StatusBadge } from '../../components/StatusBadge';
import type { MonitoringRecord, ThresholdViolation } from '../../api/types';
import { formatDateTime, formatMeasurement, latestByTime } from '../../utils/format';
import { LiveResults } from './LiveResults';

type DashboardProps = {
  records: MonitoringRecord[];
  violations: ThresholdViolation[];
  socketStatus: string;
};

export function Dashboard({ records, violations, socketStatus }: DashboardProps) {
  const latestRecord = latestByTime(records, (record) => record.evaluatedAt || record.calculatedAt);
  const violatedRecords = records.filter((record) => record.evaluationStatus === 'VIOLATED');

  return (
    <section id="dashboard">
      <h2 className="section-title">Dashboard</h2>
      <div className="grid-5">
        <MetricCard label="Total Monitoring Records" value={records.length} sub="GET :8094 /api/monitoring/records" />
        <MetricCard label="Total Violations" value={violatedRecords.length} sub="Derived from monitoring state" />
        <MetricCard label="Active Violations" value={violations.length} sub="GET :8094 /api/monitoring/violations/active" />
        <MetricCard label="Latest Process Instance" value={latestRecord?.processInstanceKey || '-'} sub="Latest persisted result" />
        <MetricCard
          label="Latest Calculated KEI"
          value={latestRecord ? formatMeasurement(latestRecord.calculatedValue, latestRecord.calculatedUnit) : '-'}
          sub={latestRecord?.keiId || 'No calculations yet'}
        />
      </div>

      <Panel title="Live Results" action={<span className={`badge ${socketStatus === 'connected' ? 'live' : 'neutral'}`}>Socket {socketStatus}</span>}>
        <LiveResults records={records} />
      </Panel>

      <div className="grid-2" style={{ marginTop: 18 }}>
        <Panel title="Latest Monitoring Records" action={<span className="badge live">Live updated</span>}>
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Process</th>
                  <th>Instance Key</th>
                  <th>Activity</th>
                  <th>KEI</th>
                  <th>Calculated</th>
                  <th>Target</th>
                  <th>Difference</th>
                  <th>Status</th>
                  <th>Calculated At</th>
                </tr>
              </thead>
              <tbody>
                {records.slice(0, 5).map((record) => (
                  <tr key={`${record.calculationEventId}-${record.evaluationEventId}`} className={record.evaluationStatus === 'VIOLATED' ? 'violation-row' : undefined}>
                    <td>{record.bpmnProcessId || '-'}</td>
                    <td className="mono">{record.processInstanceKey || '-'}</td>
                    <td>{record.bpmnElementId || '-'}</td>
                    <td>{record.keiId || '-'}</td>
                    <td>{formatMeasurement(record.calculatedValue, record.calculatedUnit)}</td>
                    <td>{formatMeasurement(record.targetValue, record.calculatedUnit)}</td>
                    <td>{record.difference ?? '-'}</td>
                    <td><StatusBadge value={record.evaluationStatus} /></td>
                    <td>{formatDateTime(record.calculatedAt)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Panel>

        <Panel title="Active Violations" action={<span className="badge danger">Critical</span>}>
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>Process</th>
                  <th>Activity</th>
                  <th>Emission Type</th>
                  <th>Calculated</th>
                  <th>Target</th>
                  <th>Difference</th>
                  <th>Status</th>
                  <th>Occurred At</th>
                </tr>
              </thead>
              <tbody>
                {violations.slice(0, 5).map((violation) => (
                  <tr key={violation.eventId} className="violation-row">
                    <td>{violation.bpmnProcessId || '-'}</td>
                    <td>{violation.serviceTaskId || '-'}</td>
                    <td>{violation.emissionType || '-'}</td>
                    <td>{violation.calculatedValue ?? '-'}</td>
                    <td>{violation.targetValue ?? '-'}</td>
                    <td>{violation.difference ?? '-'}</td>
                    <td><StatusBadge value={violation.status} /></td>
                    <td>{formatDateTime(violation.occurredAt)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Panel>
      </div>
    </section>
  );
}
