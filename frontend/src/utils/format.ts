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

export function latestByTime<T>(items: T[], timeSelector: (item: T) => string | undefined): T | undefined {
  return [...items].sort((left, right) => {
    const leftTime = timeSelector(left);
    const rightTime = timeSelector(right);
    return Date.parse(rightTime || '0') - Date.parse(leftTime || '0');
  })[0];
}
