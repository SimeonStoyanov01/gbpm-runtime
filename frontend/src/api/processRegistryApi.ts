import { requestJson } from './http';
import type {
  DeployProcessResponse,
  FindProcessKeisResponse,
  StartProcessInstanceResponse,
} from './types';

export async function deployProcess(file: File): Promise<DeployProcessResponse> {
  const body = new FormData();
  body.set('resource', file);

  return requestJson<DeployProcessResponse>('/api/process-definitions/deploy', {
    method: 'POST',
    body,
  });
}

export async function startProcessInstance(
  processDefinitionKey: string,
  variables: unknown,
): Promise<StartProcessInstanceResponse> {
  return requestJson<StartProcessInstanceResponse>(
    `/api/process-definitions/${processDefinitionKey}/instances`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(variables),
    },
  );
}

export async function findProcessKeis(processDefinitionKey: string): Promise<FindProcessKeisResponse> {
  return requestJson<FindProcessKeisResponse>(`/api/process-definitions/${processDefinitionKey}/keis`);
}
