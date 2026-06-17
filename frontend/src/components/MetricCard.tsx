type MetricCardProps = {
  label: string;
  value: string | number;
  sub?: string;
};

export function MetricCard({ label, value, sub }: MetricCardProps) {
  return (
    <div className="card">
      <div className="label">{label}</div>
      <div className="value">{value}</div>
      {sub && <div className="sub">{sub}</div>}
    </div>
  );
}
