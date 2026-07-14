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
  removeMarker: (elementId: string, marker: string) => void;
};

type Overlays = {
  add: (elementId: string, overlay: { position: { top: number; left: number }; html: HTMLElement }) => void;
  clear: () => void;
};

type ElementRegistry = {
  get: (elementId: string) => unknown;
};

export function ProcessDiagramViewer({ bpmnXml, records }: ProcessDiagramViewerProps) {
  const containerRef = useRef<HTMLDivElement>(null);
  const viewerRef = useRef<InstanceType<typeof NavigatedViewer> | null>(null);
  const markedElementIdsRef = useRef<Set<string>>(new Set());
  const [diagramReady, setDiagramReady] = useState(false);
  const [error, setError] = useState<string>();

  useEffect(() => {
    if (!containerRef.current || !bpmnXml?.trim()) {
      return;
    }

    const viewer = new NavigatedViewer({ container: containerRef.current });
    const markedElementIds = markedElementIdsRef.current;
    viewerRef.current = viewer;
    let active = true;
    setDiagramReady(false);
    setError(undefined);

    viewer.importXML(bpmnXml)
      .then(() => {
        if (!active) {
          return;
        }

        const canvas = viewer.get('canvas') as Canvas;
        canvas.zoom('fit-viewport');
        setDiagramReady(true);
      })
      .catch(() => {
        if (active) {
          setError('Diagram cannot be rendered for this instance.');
        }
      });

    return () => {
      active = false;
      viewerRef.current = null;
      markedElementIds.clear();
      viewer.destroy();
    };
  }, [bpmnXml]);

  useEffect(() => {
    const viewer = viewerRef.current;
    if (!viewer || !diagramReady) {
      return;
    }

    const canvas = viewer.get('canvas') as Canvas;
    const overlays = viewer.get('overlays') as Overlays;
    const elementRegistry = viewer.get('elementRegistry') as ElementRegistry;

    overlays.clear();
    markedElementIdsRef.current.forEach((elementId) => {
      canvas.removeMarker(elementId, 'diagram-marker-result');
      canvas.removeMarker(elementId, 'diagram-marker-violated');
      canvas.removeMarker(elementId, 'diagram-marker-calculated');
    });
    markedElementIdsRef.current.clear();

    groupedElementRecords(records).forEach((elementRecords, elementId) => {
      if (!elementRegistry.get(elementId)) {
        return;
      }

      const hasViolation = elementRecords.some((record) => record.evaluationStatus === 'VIOLATED');
      const hasEvaluation = elementRecords.some((record) => record.evaluationStatus === 'WITHIN_TARGET');
      const label = document.createElement('div');
      label.className = hasViolation
        ? 'diagram-result-overlay violated'
        : hasEvaluation
          ? 'diagram-result-overlay'
          : 'diagram-result-overlay calculated';
      label.textContent = elementRecords
        .map(formatDiagramResult)
        .join(' | ');

      overlays.add(elementId, {
        position: { top: -32, left: 0 },
        html: label,
      });

      if (hasViolation) {
        canvas.addMarker(elementId, 'diagram-marker-violated');
      } else if (hasEvaluation) {
        canvas.addMarker(elementId, 'diagram-marker-result');
      } else {
        canvas.addMarker(elementId, 'diagram-marker-calculated');
      }
      markedElementIdsRef.current.add(elementId);
    });
  }, [diagramReady, records]);

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

function formatDiagramResult(record: MonitoringRecord): string {
  const unit = record.calculatedUnit ? ` ${record.calculatedUnit}` : '';
  const result = record.targetValue === undefined || record.targetValue === null
    ? `${record.calculatedValue ?? '-'}${unit}`
    : `${record.calculatedValue ?? '-'} / ${record.targetValue}${unit}`;

  return `${record.keiId || 'KEI'} ${result}`;
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
