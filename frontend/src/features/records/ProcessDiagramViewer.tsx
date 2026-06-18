import { useEffect, useRef, useState } from 'react';
import NavigatedViewer from 'bpmn-js/lib/NavigatedViewer';
import type { MonitoringRecord } from '../../api/types';
import { EmptyState } from '../../components/EmptyState';
import 'bpmn-js/dist/assets/diagram-js.css';
import 'bpmn-js/dist/assets/bpmn-js.css';
import 'bpmn-js/dist/assets/bpmn-font/css/bpmn-embedded.css';

type ProcessDiagramViewerProps = {
  bpmnXml?: string;
  records: MonitoringRecord[];
};

type Canvas = {
  zoom: (mode: string) => void;
  addMarker: (elementId: string, marker: string) => void;
};

type Overlays = {
  add: (elementId: string, overlay: { position: { top: number; left: number }; html: HTMLElement }) => void;
};

type ElementRegistry = {
  get: (elementId: string) => unknown;
};

export function ProcessDiagramViewer({ bpmnXml, records }: ProcessDiagramViewerProps) {
  const containerRef = useRef<HTMLDivElement>(null);
  const [error, setError] = useState<string>();

  useEffect(() => {
    if (!containerRef.current || !bpmnXml?.trim()) {
      return;
    }

    const viewer = new NavigatedViewer({ container: containerRef.current });
    setError(undefined);

    viewer.importXML(bpmnXml)
      .then(() => {
        const canvas = viewer.get('canvas') as Canvas;
        const overlays = viewer.get('overlays') as Overlays;
        const elementRegistry = viewer.get('elementRegistry') as ElementRegistry;
        canvas.zoom('fit-viewport');

        groupedElementRecords(records).forEach((elementRecords, elementId) => {
          if (!elementRegistry.get(elementId)) {
            return;
          }

          const hasViolation = elementRecords.some((record) => record.evaluationStatus === 'VIOLATED');
          const label = document.createElement('div');
          label.className = hasViolation ? 'diagram-result-overlay violated' : 'diagram-result-overlay';
          label.textContent = elementRecords
            .map((record) => `${record.keiId || 'KEI'} ${record.calculatedValue ?? '-'}`)
            .join(' | ');

          overlays.add(elementId, {
            position: { top: -32, left: 0 },
            html: label,
          });

          canvas.addMarker(elementId, 'diagram-marker-result');
          if (hasViolation) {
            canvas.addMarker(elementId, 'diagram-marker-violated');
          }
        });
      })
      .catch(() => {
        setError('Diagram cannot be rendered for this instance.');
      });

    return () => {
      viewer.destroy();
    };
  }, [bpmnXml, records]);

  if (!bpmnXml?.trim()) {
    return (
      <EmptyState>
        Diagram cannot be rendered for this instance. Redeploy the process to store the BPMN model.
      </EmptyState>
    );
  }

  return (
    <div className="diagram-panel">
      {error && <p className="error">{error}</p>}
      <div ref={containerRef} className="diagram-canvas" />
    </div>
  );
}

function groupedElementRecords(records: MonitoringRecord[]): Map<string, MonitoringRecord[]> {
  const grouped = new Map<string, MonitoringRecord[]>();

  records.forEach((record) => {
    if (!record.bpmnElementId) {
      return;
    }

    grouped.set(record.bpmnElementId, [...(grouped.get(record.bpmnElementId) || []), record]);
  });

  return grouped;
}
