import { useEffect, useState } from 'react';
import { findActiveViolations, findMonitoringRecords } from './api/monitoringApi';
import type {
  ActiveViolationFilters,
  DeployProcessResponse,
  MonitoringRecord,
  MonitoringRecordFilters,
} from './api/types';
import { Layout } from './components/Layout';
import { Dashboard } from './features/dashboard/Dashboard';
import { LiveEvents } from './features/events/LiveEvents';
import { Processes } from './features/processes/Processes';
import { MonitoringRecords } from './features/records/MonitoringRecords';
import { ActiveViolations } from './features/violations/ActiveViolations';
import { connectMonitoringSocket } from './websocket/monitoringSocket';
import type { MonitoringSocketStatus } from './websocket/monitoringSocket';

export function App() {
  const [records, setRecords] = useState<MonitoringRecord[]>([]);
  const [violations, setViolations] = useState<MonitoringRecord[]>([]);
  const [calculationEvents, setCalculationEvents] = useState<MonitoringRecord[]>([]);
  const [evaluationEvents, setEvaluationEvents] = useState<MonitoringRecord[]>([]);
  const [violationEvents, setViolationEvents] = useState<MonitoringRecord[]>([]);
  const [latestDeployment, setLatestDeployment] = useState<DeployProcessResponse>();
  const [socketStatus, setSocketStatus] = useState<MonitoringSocketStatus>('disconnected');
  const [error, setError] = useState<string>();

  async function loadRecords(filters?: MonitoringRecordFilters) {
    setRecords(sortRecords(await findMonitoringRecords(filters)));
  }

  async function loadViolations(filters?: ActiveViolationFilters) {
    setViolations(sortViolations(await findActiveViolations(filters)));
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
      onStatus: (status) => {
        setSocketStatus(status);
        if (status === 'connected') {
          setError(undefined);
        }
      },
      onError: setError,
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
      <Dashboard records={records} violations={violations} socketStatus={socketStatus} />
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
    return sortRecords([record, ...records]);
  }

  return sortRecords(records.map((existing, currentIndex) => currentIndex === index ? { ...existing, ...record } : existing));
}

function upsertViolation(violations: MonitoringRecord[], violation: MonitoringRecord): MonitoringRecord[] {
  const index = violations.findIndex((existing) => existing.evaluationEventId === violation.evaluationEventId);
  if (index === -1) {
    return sortViolations([violation, ...violations]);
  }

  return sortViolations(violations.map((existing, currentIndex) => currentIndex === index ? violation : existing));
}

function sortRecords(records: MonitoringRecord[]): MonitoringRecord[] {
  return [...records].sort((left, right) => monitoringTime(right) - monitoringTime(left));
}

function sortViolations(violations: MonitoringRecord[]): MonitoringRecord[] {
  return [...violations].sort((left, right) =>
    parseTime(right.evaluatedAt) - parseTime(left.evaluatedAt),
  );
}

function monitoringTime(record: MonitoringRecord): number {
  return parseTime(record.evaluatedAt || record.calculatedAt);
}

function parseTime(value?: string): number {
  const parsed = Date.parse(value || '');
  return Number.isNaN(parsed) ? 0 : parsed;
}
