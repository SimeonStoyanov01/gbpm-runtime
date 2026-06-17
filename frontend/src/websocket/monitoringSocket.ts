import { Client } from '@stomp/stompjs';
import type { MonitoringRecord, ThresholdViolation } from '../api/types';

export type MonitoringSocketHandlers = {
  onCalculation: (record: MonitoringRecord) => void;
  onEvaluation: (record: MonitoringRecord) => void;
  onViolation: (violation: ThresholdViolation) => void;
};

export function connectMonitoringSocket(handlers: MonitoringSocketHandlers): Client {
  const client = new Client({
    brokerURL: `ws://${window.location.hostname}:8094/ws/monitoring`,
    reconnectDelay: 3000,
    onConnect: () => {
      client.subscribe('/topic/monitoring/calculations', (message) => {
        handlers.onCalculation(JSON.parse(message.body) as MonitoringRecord);
      });
      client.subscribe('/topic/monitoring/evaluations', (message) => {
        handlers.onEvaluation(JSON.parse(message.body) as MonitoringRecord);
      });
      client.subscribe('/topic/monitoring/violations', (message) => {
        handlers.onViolation(JSON.parse(message.body) as ThresholdViolation);
      });
    },
  });

  client.activate();
  return client;
}
