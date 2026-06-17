import { useEffect, useState } from 'react';
import { findActiveViolations, findMonitoringRecords } from './api/monitoringApi';
import type {
  ActiveViolationFilters,
  DeployProcessResponse,
  MonitoringRecord,
  MonitoringRecordFilters,
  ThresholdViolation,
} from './api/types';
import { Layout } from './components/Layout';
import { Dashboard } from './features/dashboard/Dashboard';
import { LiveEvents } from './features/events/LiveEvents';
import { Processes } from './features/processes/Processes';
import { MonitoringRecords } from './features/records/MonitoringRecords';
import { ActiveViolations } from './features/violations/ActiveViolations';
import { connectMonitoringSocket } from './websocket/monitoringSocket';

export function App() {
  const [records, setRecords] = useState<MonitoringRecord[]>([]);
  const [violations, setViolations] = useState<ThresholdViolation[]>([]);
  const [calculationEvents, setCalculationEvents] = useState<MonitoringRecord[]>([]);
  const [evaluationEvents, setEvaluationEvents] = useState<MonitoringRecord[]>([]);
  const [violationEvents, setViolationEvents] = useState<ThresholdViolation[]>([]);
  const [latestDeployment, setLatestDeployment] = useState<DeployProcessResponse>();
  const [error, setError] = useState<string>();

  async function loadRecords(filters?: MonitoringRecordFilters) {
    setRecords(await findMonitoringRecords(filters));
  }

  async function loadViolations(filters?: ActiveViolationFilters) {
    setViolations(await findActiveViolations(filters));
  }

  useEffect(() => {
    Promise.all([loadRecords(), loadViolations()]).catch((exception) => {
      setError(exception instanceof Error ? exception.message : 'Failed to load monitoring state.');
    });
  }, []);

  useEffect(() => {
    const client = connectMonitoringSocket({
      onCalculation: (record) => {
        setCalculationEvents((current) => [record, ...current]);
        setRecords((current) => upsertRecord(current, record));
      },
      onEvaluation: (record) => {
        setEvaluationEvents((current) => [record, ...current]);
        setRecords((current) => upsertRecord(current, record));
      },
      onViolation: (violation) => {
        setViolationEvents((current) => [violation, ...current]);
        setViolations((current) => upsertViolation(current, violation));
      },
    });

    return () => {
      client.deactivate();
    };
  }, []);

  return (
    <Layout>
      {error && <p className="error">{error}</p>}
      {latestDeployment && (
        <p className="hint">
          Latest deployment: <span className="mono">{latestDeployment.processDefinitionKey}</span> · {latestDeployment.bpmnProcessId}
        </p>
      )}
      <Dashboard records={records} violations={violations} />
      <Processes onDeployment={setLatestDeployment} />
      <MonitoringRecords
        records={records}
        onFilter={(filters) => loadRecords(filters).catch((exception) => setError(exception instanceof Error ? exception.message : 'Failed to load records.'))}
      />
      <ActiveViolations
        violations={violations}
        onFilter={(filters) => loadViolations(filters).catch((exception) => setError(exception instanceof Error ? exception.message : 'Failed to load violations.'))}
      />
      <LiveEvents calculations={calculationEvents} evaluations={evaluationEvents} violations={violationEvents} />
    </Layout>
  );
}

function upsertRecord(records: MonitoringRecord[], record: MonitoringRecord): MonitoringRecord[] {
  const key = record.calculationEventId || record.evaluationEventId;
  if (!key) {
    return [record, ...records];
  }

  const index = records.findIndex((existing) =>
    existing.calculationEventId === record.calculationEventId ||
    existing.evaluationEventId === record.evaluationEventId,
  );

  if (index === -1) {
    return [record, ...records];
  }

  return records.map((existing, currentIndex) => currentIndex === index ? { ...existing, ...record } : existing);
}

function upsertViolation(violations: ThresholdViolation[], violation: ThresholdViolation): ThresholdViolation[] {
  const index = violations.findIndex((existing) => existing.eventId === violation.eventId);
  if (index === -1) {
    return [violation, ...violations];
  }

  return violations.map((existing, currentIndex) => currentIndex === index ? violation : existing);
}
