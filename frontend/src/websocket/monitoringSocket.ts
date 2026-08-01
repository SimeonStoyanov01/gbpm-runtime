import { Client } from '@stomp/stompjs';
import type { MonitoringRecord } from '../api/types';

export type MonitoringSocketHandlers = {
  onCalculation: (record: MonitoringRecord) => void;
  onEvaluation: (record: MonitoringRecord) => void;
  onViolation: (violation: MonitoringRecord) => void;
  onStatus?: (status: MonitoringSocketStatus) => void;
  onError?: (message: string) => void;
};

export type MonitoringSocketStatus = 'connecting' | 'connected' | 'disconnected';

export function connectMonitoringSocket(handlers: MonitoringSocketHandlers): Client {
  const protocol = window.location.protocol === 'https:' ? 'wss' : 'ws';
  const client = new Client({
    brokerURL: `${protocol}://${window.location.hostname}:8094/ws/monitoring`,
    reconnectDelay: 3000,
    connectionTimeout: 5000,
    onWebSocketClose: () => handlers.onStatus?.('disconnected'),
    onWebSocketError: () => {
      handlers.onStatus?.('disconnected');
      handlers.onError?.('Monitoring WebSocket connection failed.');
    },
    onStompError: (frame) => {
      handlers.onStatus?.('disconnected');
      handlers.onError?.(frame.headers.message || 'Monitoring STOMP connection failed.');
    },
    onConnect: () => {
      handlers.onStatus?.('connected');
      client.subscribe('/topic/monitoring/calculations', (message) => {
        handlers.onCalculation(JSON.parse(message.body) as MonitoringRecord);
      });
      client.subscribe('/topic/monitoring/evaluations', (message) => {
        handlers.onEvaluation(JSON.parse(message.body) as MonitoringRecord);
      });
      client.subscribe('/topic/monitoring/violations', (message) => {
        handlers.onViolation(JSON.parse(message.body) as MonitoringRecord);
      });
    },
  });

  handlers.onStatus?.('connecting');
  client.activate();
  return client;
}
