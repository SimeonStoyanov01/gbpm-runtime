type StatusBadgeProps = {
  value?: string;
};

export function StatusBadge({ value }: StatusBadgeProps) {
  const status = value || 'UNKNOWN';
  const tone = status === 'VIOLATED'
    ? 'danger'
    : status === 'WITHIN_TARGET' || status === 'DEPLOYED' || status === 'STARTED'
      ? 'success'
      : status === 'CALCULATED'
        ? 'live'
        : 'neutral';

  return <span className={`badge ${tone}`}>{status}</span>;
}
