import type { MonitoringRecord } from '../../api/types';
import { EmptyState } from '../../components/EmptyState';
import { Panel } from '../../components/Panel';
import { formatMeasurement } from '../../utils/format';

type LiveEventsProps = {
  calculations: MonitoringRecord[];
  evaluations: MonitoringRecord[];
  violations: MonitoringRecord[];
};

export function LiveEvents({ calculations, evaluations, violations }: LiveEventsProps) {
  return (
    <section id="events">
      <h2 className="section-title">Live Events</h2>
      <div className="grid-3">
        <Panel title="Calculation Updates" action={<span className="badge live">/topic/monitoring/calculations</span>}>
          <div className="event-feed">
            {calculations.length === 0 ? (
              <EmptyState>No targetless calculation messages received in this browser session.</EmptyState>
            ) : (
              calculations.slice(0, 6).map((record) => (
                <div className="event-card highlight" key={`${record.calculationEventId}-${record.keiId}`}>
                  <span className="badge live">Calculation</span>
                  <div>
                    <strong>{record.keiId || '-'}</strong>
                    <br />
                    <span className="mono">instance {record.processInstanceKey || '-'} · {record.bpmnElementId || '-'}</span>
                  </div>
                  <strong>{formatMeasurement(record.calculatedValue, record.calculatedUnit)}</strong>
                </div>
              ))
            )}
          </div>
        </Panel>

        <Panel title="Evaluation Updates" action={<span className="badge live">/topic/monitoring/evaluations</span>}>
          <div className="event-feed">
            {evaluations.length === 0 ? (
              <EmptyState>No evaluation messages received in this browser session.</EmptyState>
            ) : (
              evaluations.slice(0, 6).map((record) => (
                <div className="event-card highlight" key={`${record.evaluationEventId}-${record.keiId}`}>
                  <span className={`badge ${record.evaluationStatus === 'VIOLATED' ? 'danger' : 'success'}`}>Evaluation</span>
                  <div>
                    <strong>{record.bpmnElementId || '-'}</strong>
                    <br />
                    <span className="mono">
                      {record.keiId || '-'} · {formatMeasurement(record.calculatedValue, record.calculatedUnit)} / target {record.targetValue ?? '-'}
                    </span>
                  </div>
                  <span className={`badge ${record.evaluationStatus === 'VIOLATED' ? 'danger' : 'success'}`}>{record.evaluationStatus || '-'}</span>
                </div>
              ))
            )}
          </div>
        </Panel>

        <Panel title="Violation Updates" action={<span className="badge live">/topic/monitoring/violations</span>}>
          <div className="event-feed">
            {violations.length === 0 ? (
              <EmptyState>No violation messages received in this browser session.</EmptyState>
            ) : (
              violations.slice(0, 6).map((violation) => (
                <div className="event-card highlight" key={violation.evaluationEventId}>
                  <span className="badge danger">Violation</span>
                  <div>
                    <strong>{violation.bpmnElementId || '-'}</strong>
                    <br />
                    <span className="mono">{violation.keiId || '-'} · difference {violation.difference ?? '-'}</span>
                  </div>
                  <span className="badge danger">Active</span>
                </div>
              ))
            )}
          </div>
        </Panel>
      </div>

      <Panel title="Initial state and live-update contract" action={<span className="endpoint">ws://localhost:8094/ws/monitoring</span>}>
        <p className="hint">
          The page first loads persisted monitoring state by REST, then subscribes to STOMP topics for new calculations,
          evaluations, and violations. This avoids missing events that occurred before page load.
        </p>
      </Panel>
    </section>
  );
}
