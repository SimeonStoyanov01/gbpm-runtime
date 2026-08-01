import type { MonitoringRecord } from '../../api/types';
import { EmptyState } from '../../components/EmptyState';
import { Panel } from '../../components/Panel';
import { formatDateTime, formatMeasurement, formatVariance } from '../../utils/format';

type LiveEventsProps = {
  calculations: MonitoringRecord[];
  evaluations: MonitoringRecord[];
  violations: MonitoringRecord[];
  socketStatus: string;
};

export function LiveEvents({ calculations, evaluations, violations, socketStatus }: LiveEventsProps) {
  return (
    <section id="events">
      <div className="section-heading">
        <h2 className="section-title">Live Message Stream</h2>
        <span className={`badge ${socketStatus === 'connected' ? 'live' : 'neutral'}`}>Socket {socketStatus}</span>
      </div>
      <div className="grid-3 event-grid">
        <Panel title="Calculation Events" action={<span className="badge live">/topic/monitoring/calculations</span>}>
          <div className="event-feed">
            {calculations.length === 0 ? (
              <EmptyState>No targetless calculation messages received in this browser session.</EmptyState>
            ) : (
              calculations.slice(0, 6).map((record) => (
                <div className="event-card highlight" key={`${record.calculationEventId}-${record.keiId}`}>
                  <span className="badge live">Calculation</span>
                  <div>
                    <strong>{record.elementName || record.bpmnElementId || '-'}</strong>
                    <br />
                    <span>KEI: {record.keiId || '-'}</span>
                    <br />
                    <span className="mono">element instance {record.elementInstanceKey || '-'}</span>
                  </div>
                  <div className="event-value">
                    <strong>{formatMeasurement(record.calculatedValue, record.calculatedUnit)}</strong>
                    <span className="event-time">{formatDateTime(record.calculatedAt)}</span>
                  </div>
                </div>
              ))
            )}
          </div>
        </Panel>

        <Panel title="Evaluation Events" action={<span className="badge live">/topic/monitoring/evaluations</span>}>
          <div className="event-feed">
            {evaluations.length === 0 ? (
              <EmptyState>No evaluation messages received in this browser session.</EmptyState>
            ) : (
              evaluations.slice(0, 6).map((record) => (
                <div className="event-card highlight" key={`${record.evaluationEventId}-${record.keiId}`}>
                  <span className={`badge ${record.evaluationStatus === 'VIOLATED' ? 'danger' : 'success'}`}>Evaluation</span>
                  <div>
                    <strong>{record.elementName || record.bpmnElementId || '-'}</strong>
                    <br />
                    <span>KEI: {record.keiId || '-'}</span>
                    <br />
                    <span className="mono">element instance {record.elementInstanceKey || '-'}</span>
                  </div>
                  <div className="event-value">
                    <strong>{formatMeasurement(record.calculatedValue, record.calculatedUnit)}</strong>
                    <span className={`badge ${record.evaluationStatus === 'VIOLATED' ? 'danger' : 'success'}`}>{record.evaluationStatus || '-'}</span>
                    <span className="event-time">{formatDateTime(record.evaluatedAt)}</span>
                  </div>
                </div>
              ))
            )}
          </div>
        </Panel>

        <Panel title="Violation Events" action={<span className="badge live">/topic/monitoring/violations</span>}>
          <div className="event-feed">
            {violations.length === 0 ? (
              <EmptyState>No violation messages received in this browser session.</EmptyState>
            ) : (
              violations.slice(0, 6).map((violation) => (
                <div className="event-card highlight" key={violation.evaluationEventId}>
                  <span className="badge danger">Violation</span>
                  <div>
                    <strong>{violation.elementName || violation.bpmnElementId || '-'}</strong>
                    <br />
                    <span>KEI: {violation.keiId || '-'}</span>
                    <br />
                    <span className="mono">element instance {violation.elementInstanceKey || '-'}</span>
                  </div>
                  <div className="event-value">
                    <strong>{formatVariance(violation.difference, violation.calculatedUnit)}</strong>
                    <span className="event-time">{formatDateTime(violation.evaluatedAt)}</span>
                  </div>
                </div>
              ))
            )}
          </div>
        </Panel>
      </div>

    </section>
  );
}
