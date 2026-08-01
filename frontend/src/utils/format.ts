import type { MonitoringRecord } from '../api/types';

export function formatDateTime(value?: string): string {
  if (!value) {
    return '-';
  }

  return new Intl.DateTimeFormat(undefined, {
    dateStyle: 'short',
    timeStyle: 'short',
  }).format(new Date(value));
}

export function formatMeasurement(value?: number, unit?: string): string {
  if (value === undefined || value === null) {
    return '-';
  }

  return unit ? `${value} ${unit}` : String(value);
}

export function formatVariance(value?: number, unit?: string): string {
  if (value === undefined || value === null) {
    return '-';
  }

  if (value === 0) {
    return 'At target';
  }

  const measurement = formatMeasurement(Math.abs(value), unit);
  return value > 0 ? `${measurement} above target` : `${measurement} below target`;
}

export function latestByTime<T>(items: T[], timeSelector: (item: T) => string | undefined): T | undefined {
  return [...items].sort((left, right) => {
    const leftTime = timeSelector(left);
    const rightTime = timeSelector(right);
    return Date.parse(rightTime || '0') - Date.parse(leftTime || '0');
  })[0];
}

export function monitoringStatus(record: MonitoringRecord): string {
  if (record.evaluationStatus) {
    return record.evaluationStatus;
  }

  return record.calculationEventId ? 'CALCULATED' : 'UNKNOWN';
}
