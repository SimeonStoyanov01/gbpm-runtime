import { requestJson, requestVoid } from './http';
import type { FindActiveUserTasksResponse } from './types';

export async function findActiveUserTasks(
  processInstanceKey: string,
): Promise<FindActiveUserTasksResponse> {
  return requestJson<FindActiveUserTasksResponse>(
    `/api/v1/camunda8/process-instances/${processInstanceKey}/user-tasks`,
  );
}

export async function completeUserTask(
  userTaskKey: string,
  variables: Record<string, unknown>,
): Promise<void> {
  return requestVoid(`/api/v1/camunda8/user-tasks/${userTaskKey}/completion`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify(variables),
  });
}
