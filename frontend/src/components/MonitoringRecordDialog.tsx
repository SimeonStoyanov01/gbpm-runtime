import { useEffect } from 'react';
import type { ReactNode } from 'react';
import type { MonitoringRecord } from '../api/types';
import { formatDateTime, formatMeasurement, formatVariance, monitoringStatus } from '../utils/format';
import { StatusBadge } from './StatusBadge';

type MonitoringRecordDialogProps = {
  record?: MonitoringRecord;
  onClose: () => void;
};

export function MonitoringRecordDialog({ record, onClose }: MonitoringRecordDialogProps) {
  useEffect(() => {
    if (!record) {
      return;
    }

    function closeOnEscape(event: KeyboardEvent) {
      if (event.key === 'Escape') {
        onClose();
      }
    }

    window.addEventListener('keydown', closeOnEscape);
    return () => window.removeEventListener('keydown', closeOnEscape);
  }, [onClose, record]);

  if (!record) {
    return null;
  }

  return (
    <div className="dialog-backdrop" role="presentation" onMouseDown={(event) => {
      if (event.target === event.currentTarget) {
        onClose();
      }
    }}>
      <section className="record-dialog" role="dialog" aria-modal="true" aria-labelledby="record-dialog-title">
        <header>
          <div>
            <span className="label">Monitoring record</span>
            <h3 id="record-dialog-title">{record.elementName || record.bpmnElementId || 'Process activity'}</h3>
          </div>
          <button className="secondary" type="button" onClick={onClose}>Close</button>
        </header>

        <div className="record-dialog-content">
          <section className="record-result-summary">
            <div>
              <span className="label">{record.keiId || 'KEI result'}</span>
              <strong>{formatMeasurement(record.calculatedValue, record.calculatedUnit)}</strong>
              <p>
                {record.targetValue === undefined || record.targetValue === null
                  ? 'No target configured; the calculation was recorded without evaluation.'
                  : `Compared with a target of ${formatMeasurement(record.targetValue, record.calculatedUnit)}.`}
              </p>
            </div>
            <div className="record-result-state">
              <StatusBadge value={monitoringStatus(record)} />
              {record.difference !== undefined && record.difference !== null && (
                <strong>{formatVariance(record.difference, record.calculatedUnit)}</strong>
              )}
            </div>
          </section>

          <RecordSection title="Process context">
            <Detail label="Task" value={record.elementName || '-'} />
            <Detail label="Element instance" value={record.elementInstanceKey} mono />
            <Detail label="BPMN process" value={record.bpmnProcessId} />
            <Detail label="Process instance" value={record.processInstanceKey} mono />
            <Detail label="Calculated at" value={formatDateTime(record.calculatedAt)} />
            <Detail label="Evaluated at" value={formatDateTime(record.evaluatedAt)} />
          </RecordSection>

          <RecordSection title="Calculation provenance">
            <Detail label="Calculator" value={record.calculatorId} />
            <Detail label="Calculation method" value={record.calculationMethod} />
            <Detail label="Reference set" value={record.referenceSetId} />
          </RecordSection>

          {record.resourceBreakdown && record.resourceBreakdown.length > 0 && (
            <section className="record-section">
              <h4>Resource contributions</h4>
              <div className="table-wrap">
                <table className="resource-breakdown-table">
                  <thead>
                    <tr>
                      <th>Resource</th>
                      <th>Emission contribution</th>
                      <th>Share</th>
                    </tr>
                  </thead>
                  <tbody>
                    {record.resourceBreakdown.map((resource, index) => (
                      <tr key={`${resource.resourceName}-${index}`}>
                        <td>{resource.resourceName}</td>
                        <td>{formatMeasurement(resource.emissionValue, resource.unit)}</td>
                        <td>{formatContributionShare(resource.emissionValue, record.calculatedValue)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </section>
          )}

          <details className="technical-details">
            <summary>Technical details</summary>
            <dl className="record-detail-list">
              <Detail label="Process definition" value={record.processDefinitionKey} mono />
              <Detail label="Activity ID" value={record.bpmnElementId} mono />
              <Detail label="Engine" value={record.engineType} />
              <Detail label="Calculation event" value={record.calculationEventId} mono />
              <Detail label="Evaluation event" value={record.evaluationEventId} mono />
            </dl>
          </details>
        </div>
      </section>
    </div>
  );
}

type DetailProps = {
  label: string;
  value?: string | number;
  mono?: boolean;
};

function Detail({ label, value, mono }: DetailProps) {
  return (
    <div>
      <dt>{label}</dt>
      <dd className={mono ? 'mono' : undefined}>{value ?? '-'}</dd>
    </div>
  );
}

type RecordSectionProps = {
  title: string;
  children: ReactNode;
};

function RecordSection({ title, children }: RecordSectionProps) {
  return (
    <section className="record-section">
      <h4>{title}</h4>
      <dl className="record-detail-list">{children}</dl>
    </section>
  );
}

function formatContributionShare(emissionValue: number, calculatedValue?: number): string {
  if (!calculatedValue) {
    return '-';
  }

  return `${((emissionValue / calculatedValue) * 100).toFixed(1)}%`;
}
