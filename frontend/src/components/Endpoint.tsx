import type { ReactNode } from 'react';

type EndpointProps = {
  children: ReactNode;
};

export function Endpoint({ children }: EndpointProps) {
  return <span className="endpoint">{children}</span>;
}
