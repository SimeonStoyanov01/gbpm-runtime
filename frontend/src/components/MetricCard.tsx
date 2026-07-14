type MetricCardProps = {
  label: string;
  value: string | number;
  sub?: string;
  onClick?: () => void;
};

export function MetricCard({ label, value, sub, onClick }: MetricCardProps) {
  const content = (
    <>
      <div className="label">{label}</div>
      <div className="value">{value}</div>
      {sub && <div className="sub">{sub}</div>}
    </>
  );

  if (onClick) {
    return <button className="card metric-card-action" type="button" onClick={onClick}>{content}</button>;
  }

  return <div className="card">{content}</div>;
}
