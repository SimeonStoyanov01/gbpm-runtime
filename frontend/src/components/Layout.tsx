import { useEffect, useState } from 'react';
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
  const [activeSection, setActiveSection] = useState(sections[0][0]);

  useEffect(() => {
    function updateActiveSection() {
      const visibleSection = [...sections]
        .reverse()
        .find(([id]) => (document.getElementById(id)?.getBoundingClientRect().top ?? Infinity) <= 160);
      setActiveSection(visibleSection?.[0] || sections[0][0]);
    }

    updateActiveSection();
    window.addEventListener('scroll', updateActiveSection, { passive: true });
    return () => window.removeEventListener('scroll', updateActiveSection);
  }, []);

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand">
          <h1>Runtime</h1>
          <p>Runtime Environmental Monitoring</p>
        </div>
        <nav>
          {sections.map(([id, label]) => (
            <a
              key={id}
              className={activeSection === id ? 'active' : undefined}
              href={`#${id}`}
              aria-current={activeSection === id ? 'location' : undefined}
            >
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
        </header>
        {children}
      </main>
    </div>
  );
}
