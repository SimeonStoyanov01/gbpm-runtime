import type { ReactNode } from 'react';

const sections = [
  ['dashboard', 'Dashboard'],
  ['processes', 'Processes'],
  ['records', 'Monitoring Records'],
  ['violations', 'Active Violations'],
  ['events', 'Live Events'],
];

type LayoutProps = {
  children: ReactNode;
};

export function Layout({ children }: LayoutProps) {
  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <h1>Runtime</h1>
          <p>Runtime Environmental Monitoring</p>
        </div>
        <nav>
          {sections.map(([id, label], index) => (
            <a key={id} className={index === 0 ? 'active' : undefined} href={`#${id}`}>
              {label}
            </a>
          ))}
        </nav>
      </aside>
      <main>
        <header className="page-header">
          <div>
            <h2>Monitoring-oriented BPMN runtime prototype</h2>
            <p>
              Backend-aligned MVP interface for deploying BPMN models, starting process instances,
              inspecting KEI annotations, and monitoring calculation, evaluation, and violation events.
            </p>
          </div>
          <div className="chip">
            <span className="dot" />
            REST initial state + STOMP live updates
          </div>
        </header>
        {children}
      </main>
    </div>
  );
}
