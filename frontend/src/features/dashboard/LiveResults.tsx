import { StatusBadge } from '../../components/StatusBadge';
import type { MonitoringRecord } from '../../api/types';
import { formatDateTime, formatMeasurement } from '../../utils/format';

type LiveResultsProps = {
  records: MonitoringRecord[];
};

export function LiveResults({ records }: LiveResultsProps) {
  const evaluations = records
    .filter((record) => record.evaluationEventId)
    .slice(0, 4);
  const calculationOnly = records
    .filter((record) => record.calculationEventId && !record.evaluationEventId)
    .slice(0, 4);

  return (
    <div className="live-results-grid">
      <section className="live-result-lane">
        <header>
          <h4>Evaluation Results</h4>
          <span className="badge live">with target</span>
        </header>
        {evaluations.length === 0 ? (
          <div className="empty-state compact">No evaluated KEI results yet.</div>
        ) : (
          evaluations.map((record) => (
            <article className="result-card" key={record.evaluationEventId}>
              <div>
                <div className="result-title">
                  <strong>{record.bpmnElementId || '-'}</strong>
                  <StatusBadge value={record.evaluationStatus} />
                </div>
                <p className="mono">{record.bpmnProcessId || '-'} · instance {record.processInstanceKey || '-'}</p>
                <p>{record.keiId || '-'}</p>
              </div>
              <dl className="result-metrics">
                <div>
                  <dt>Calculated</dt>
                  <dd>{formatMeasurement(record.calculatedValue, record.calculatedUnit)}</dd>
                </div>
                <div>
                  <dt>Target</dt>
                  <dd>{formatMeasurement(record.targetValue, record.calculatedUnit)}</dd>
                </div>
                <div>
                  <dt>Difference</dt>
                  <dd>{record.difference ?? '-'}</dd>
                </div>
                <div>
                  <dt>Evaluated</dt>
                  <dd>{formatDateTime(record.evaluatedAt)}</dd>
                </div>
              </dl>
            </article>
          ))
        )}
      </section>

      <section className="live-result-lane">
        <header>
          <h4>Calculation Results</h4>
          <span className="badge neutral">without target</span>
        </header>
        {calculationOnly.length === 0 ? (
          <div className="empty-state compact">No calculation-only KEI results yet.</div>
        ) : (
          calculationOnly.map((record) => (
            <article className="result-card compact" key={record.calculationEventId}>
              <div>
                <div className="result-title">
                  <strong>{record.bpmnElementId || '-'}</strong>
                  <span className="badge live">Calculation</span>
                </div>
                <p className="mono">{record.bpmnProcessId || '-'} · instance {record.processInstanceKey || '-'}</p>
                <p>{record.keiId || '-'}</p>
              </div>
              <dl className="result-metrics two-col">
                <div>
                  <dt>Calculated</dt>
                  <dd>{formatMeasurement(record.calculatedValue, record.calculatedUnit)}</dd>
                </div>
                <div>
                  <dt>Calculated At</dt>
                  <dd>{formatDateTime(record.calculatedAt)}</dd>
                </div>
              </dl>
            </article>
          ))
        )}
      </section>
    </div>
  );
}
