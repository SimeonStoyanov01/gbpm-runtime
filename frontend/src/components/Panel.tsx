import type { ReactNode } from 'react';

type PanelProps = {
  title: string;
  action?: ReactNode;
  children: ReactNode;
};

export function Panel({ title, action, children }: PanelProps) {
  return (
    <div className="panel">
      <div className="panel-header">
        <h3>{title}</h3>
        {action}
      </div>
      <div className="panel-body">{children}</div>
    </div>
  );
}
