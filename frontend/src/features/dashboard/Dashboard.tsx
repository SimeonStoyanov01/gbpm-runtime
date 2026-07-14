import { useState } from 'react';
import type { MonitoringRecord } from '../../api/types';
import { MetricCard } from '../../components/MetricCard';
import { MonitoringRecordDialog } from '../../components/MonitoringRecordDialog';
import { Panel } from '../../components/Panel';
import { StatusBadge } from '../../components/StatusBadge';
import { formatDateTime, formatMeasurement, formatVariance, latestByTime, monitoringStatus } from '../../utils/format';

type DashboardProps = {
  records: MonitoringRecord[];
  violations: MonitoringRecord[];
  onOpenInstance: (processInstanceKey: number) => void;
};

export function Dashboard({ records, violations, onOpenInstance }: DashboardProps) {
  const [selectedRecord, setSelectedRecord] = useState<MonitoringRecord>();
  const latestRecord = latestByTime(records, (record) => record.evaluatedAt || record.calculatedAt);
  const violatedRecords = records.filter((record) => record.evaluationStatus === 'VIOLATED');

  return (
    <section id="dashboard">
      <h2 className="section-title">Dashboard</h2>
      <div className="grid-5">
        <MetricCard label="Monitoring Records" value={records.length} sub="Persisted KEI results" />
        <MetricCard label="Violated Evaluations" value={violatedRecords.length} sub="Results outside their targets" />
        <MetricCard label="Active Violations" value={violations.length} sub="Queryable violation projection" />
        <MetricCard
          label="Latest Process Instance"
          value={latestRecord?.bpmnProcessId || '-'}
          sub={latestRecord?.processInstanceKey ? `Instance ${latestRecord.processInstanceKey}` : 'No process results'}
          onClick={latestRecord?.processInstanceKey ? () => onOpenInstance(latestRecord.processInstanceKey!) : undefined}
        />
        <MetricCard
          label="Latest Calculated KEI"
          value={latestRecord ? formatMeasurement(latestRecord.calculatedValue, latestRecord.calculatedUnit) : '-'}
          sub={latestRecord?.keiId || 'No calculations yet'}
        />
      </div>

      <div className="dashboard-results">
        <Panel title="Latest Monitoring Records" action={<a className="panel-link" href="#records">View all records</a>}>
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
                  <th>Updated At</th>
                </tr>
              </thead>
              <tbody>
                {records.slice(0, 5).map((record) => (
                  <tr key={`${record.calculationEventId}-${record.evaluationEventId}`} className={record.evaluationStatus === 'VIOLATED' ? 'violation-row' : undefined}>
                    <td>
                      <button className="record-link" type="button" onClick={() => setSelectedRecord(record)}>
                        {record.elementName || record.bpmnElementId || '-'}
                      </button>
                    </td>
                    <td className="mono">{record.elementInstanceKey || '-'}</td>
                    <td>{record.keiId || '-'}</td>
                    <td>{formatMeasurement(record.calculatedValue, record.calculatedUnit)}</td>
                    <td>{formatMeasurement(record.targetValue, record.calculatedUnit)}</td>
                    <td>{formatVariance(record.difference, record.calculatedUnit)}</td>
                    <td><StatusBadge value={monitoringStatus(record)} /></td>
                    <td>{formatDateTime(record.evaluatedAt || record.calculatedAt)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Panel>

        <Panel title="Active Violations" action={<a className="panel-link" href="#violations">View all violations</a>}>
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
                  <th>Occurred At</th>
                </tr>
              </thead>
              <tbody>
                {violations.slice(0, 5).map((violation) => (
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
                    <td>{formatDateTime(violation.evaluatedAt)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Panel>
      </div>

      <MonitoringRecordDialog record={selectedRecord} onClose={() => setSelectedRecord(undefined)} />
    </section>
  );
}
