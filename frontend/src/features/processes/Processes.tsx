import { useCallback, useEffect, useState } from 'react';
import { completeUserTask, findActiveUserTasks } from '../../api/camunda8IntegrationApi';
import { deployProcess, findProcessKeis, startProcessInstance } from '../../api/processRegistryApi';
import type {
  ActiveUserTask,
  DeployProcessResponse,
  ElementKeiAnnotations,
  FindProcessKeisResponse,
  StartProcessInstanceResponse,
} from '../../api/types';
import { Endpoint } from '../../components/Endpoint';
import { Panel } from '../../components/Panel';
import { StatusBadge } from '../../components/StatusBadge';

export function Processes() {
  const [deployment, setDeployment] = useState<DeployProcessResponse>();
  const [processDefinitionKey, setProcessDefinitionKey] = useState('');
  const [variablesJson, setVariablesJson] = useState('{}');
  const [startedInstance, setStartedInstance] = useState<StartProcessInstanceResponse>();
  const [keiResponse, setKeiResponse] = useState<FindProcessKeisResponse>();
  const [activeUserTasks, setActiveUserTasks] = useState<ActiveUserTask[]>([]);
  const [completingUserTaskKey, setCompletingUserTaskKey] = useState<string>();
  const [userTaskError, setUserTaskError] = useState<string>();
  const [error, setError] = useState<string>();
  const [busy, setBusy] = useState(false);

  const refreshActiveUserTasks = useCallback(async (processInstanceKey: string) => {
    try {
      const response = await findActiveUserTasks(processInstanceKey);
      setActiveUserTasks(response.userTasks);
      setUserTaskError(undefined);
    } catch (exception) {
      setUserTaskError(exception instanceof Error ? exception.message : 'Failed to load active user tasks.');
    }
  }, []);

  useEffect(() => {
    const processInstanceKey = startedInstance?.processInstanceKey;
    if (!processInstanceKey) {
      setActiveUserTasks([]);
      return;
    }

    void refreshActiveUserTasks(processInstanceKey);
    const refreshTimer = window.setInterval(
      () => void refreshActiveUserTasks(processInstanceKey),
      2000,
    );

    return () => window.clearInterval(refreshTimer);
  }, [refreshActiveUserTasks, startedInstance?.processInstanceKey]);

  async function handleDeploy(file?: File) {
    if (!file) {
      setError('Select a BPMN file first.');
      return;
    }

    setBusy(true);
    setError(undefined);
    try {
      const response = await deployProcess(file);
      setDeployment(response);
      setProcessDefinitionKey(response.processDefinitionKey);
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'Deployment failed.');
    } finally {
      setBusy(false);
    }
  }

  async function handleStart() {
    setBusy(true);
    setError(undefined);
    try {
      const parsed = JSON.parse(variablesJson) as Record<string, unknown>;
      setStartedInstance(await startProcessInstance(processDefinitionKey, parsed));
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'Process start failed.');
    } finally {
      setBusy(false);
    }
  }

  async function handleFindKeis() {
    if (!processDefinitionKey) {
      setError('Process definition key is required.');
      return;
    }

    setBusy(true);
    setError(undefined);
    try {
      setKeiResponse(await findProcessKeis(processDefinitionKey));
    } catch (exception) {
      setError(exception instanceof Error ? exception.message : 'KEI lookup failed.');
    } finally {
      setBusy(false);
    }
  }

  async function handleCompleteUserTask(task: ActiveUserTask, decision?: boolean) {
    setCompletingUserTaskKey(task.userTaskKey);
    setUserTaskError(undefined);
    try {
      const variables = task.decisionVariable ? { [task.decisionVariable]: decision } : {};
      await completeUserTask(task.userTaskKey, variables);
      setActiveUserTasks((current) => current.filter((item) => item.userTaskKey !== task.userTaskKey));
    } catch (exception) {
      setUserTaskError(exception instanceof Error ? exception.message : 'Failed to complete user task.');
    } finally {
      setCompletingUserTaskKey(undefined);
    }
  }

  return (
    <section id="processes">
      <h2 className="section-title">Processes</h2>
      {error && <p className="error">{error}</p>}

      <div className="grid-2">
        <Panel title="Deploy BPMN" action={<Endpoint>POST :8080 /api/process-definitions/deploy</Endpoint>}>
          <DeployForm disabled={busy} onDeploy={handleDeploy} />
          {deployment && (
            <OperationSummary
              title="Deployed process"
              status={deployment.status}
              fields={[
                ['Process', deployment.bpmnProcessId],
                ['Version', deployment.version],
                ['Definition key', deployment.processDefinitionKey, true],
                ['Deployment key', deployment.deploymentKey, true],
                ['BPMN resource', deployment.resourceName],
              ]}
            />
          )}
          <div className="button-row">
            <button className="secondary" disabled={!deployment || busy} onClick={handleFindKeis}>View KEIs</button>
            <button className="secondary" disabled={!deployment || busy} onClick={() => setProcessDefinitionKey(deployment?.processDefinitionKey || '')}>
              Use key for start form
            </button>
          </div>
        </Panel>

        <Panel title="Start Process Instance" action={<Endpoint>POST :8080 /api/process-definitions/{'{key}'}/instances</Endpoint>}>
          <div className="form-grid">
            <div>
              <label>Process Definition Key</label>
              <input value={processDefinitionKey} onChange={(event) => setProcessDefinitionKey(event.target.value)} />
            </div>
            <div>
              <label>Variables JSON</label>
              <textarea value={variablesJson} onChange={(event) => setVariablesJson(event.target.value)} />
            </div>
            <button disabled={!processDefinitionKey || busy} onClick={handleStart}>Start process instance</button>
          </div>
          {startedInstance && (
            <>
              <OperationSummary
                title="Started process instance"
                status={startedInstance.status}
                fields={[
                  ['Process', startedInstance.bpmnProcessId],
                  ['Version', startedInstance.version],
                  ['Definition key', startedInstance.processDefinitionKey, true],
                  ['Instance key', startedInstance.processInstanceKey, true],
                ]}
              />

              <div className="user-task-section">
                <div className="user-task-heading">
                  <h4>Active User Tasks</h4>
                  <Endpoint>GET :8090 /api/v1/camunda8/process-instances/{'{key}'}/user-tasks</Endpoint>
                </div>
                {userTaskError && <p className="error">{userTaskError}</p>}
                {activeUserTasks.length === 0 ? (
                  <p className="hint">No active user tasks for this instance.</p>
                ) : (
                  <div className="user-task-list">
                    {activeUserTasks.map((task) => (
                      <div className="user-task-row" key={task.userTaskKey}>
                        <span>
                          <strong>{task.name || 'User task'}</strong>
                          <span className="mono">{task.bpmnElementId || task.userTaskKey}</span>
                        </span>
                        <div className="user-task-actions">
                          {task.decisionVariable && (
                            <button
                              className="secondary"
                              disabled={completingUserTaskKey === task.userTaskKey}
                              onClick={() => handleCompleteUserTask(task, false)}
                            >
                              No
                            </button>
                          )}
                          <button
                            disabled={completingUserTaskKey === task.userTaskKey}
                            onClick={() => handleCompleteUserTask(task, true)}
                          >
                            {task.decisionVariable ? 'Yes' : 'Complete'}
                          </button>
                        </div>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            </>
          )}
        </Panel>
      </div>

      <Panel title="KEI Annotations" action={<Endpoint>GET :8080 /api/process-definitions/{'{key}'}/keis</Endpoint>}>
        <div className="button-row" style={{ marginTop: 0, marginBottom: 16 }}>
          <button className="secondary" disabled={!processDefinitionKey || busy} onClick={handleFindKeis}>Refresh KEIs</button>
        </div>
        <KeiTable elements={keiResponse?.elementKeiAnnotations || []} />
      </Panel>
    </section>
  );
}

type DeployFormProps = {
  disabled: boolean;
  onDeploy: (file?: File) => void;
};

function DeployForm({ disabled, onDeploy }: DeployFormProps) {
  const [file, setFile] = useState<File>();

  return (
    <div className="form-grid">
      <div>
        <label>BPMN file</label>
        <input type="file" accept=".bpmn,.xml" onChange={(event) => setFile(event.target.files?.[0])} />
      </div>
      <button disabled={disabled} onClick={() => onDeploy(file)}>Deploy BPMN file</button>
    </div>
  );
}

function KeiTable({ elements }: { elements: ElementKeiAnnotations[] }) {
  return (
    <div className="table-wrap">
      <table>
        <thead>
          <tr>
            <th>Task</th>
            <th>Type</th>
            <th>KEI ID</th>
            <th>Unit</th>
            <th>Target Value</th>
            <th>Icon</th>
          </tr>
        </thead>
        <tbody>
          {elements.flatMap((element) =>
            element.keiAnnotations.map((kei) => (
              <tr key={`${element.bpmnElementId}-${kei.id}`}>
                <td>
                  <strong>{element.elementName || 'Unnamed BPMN element'}</strong>
                  <span className="table-secondary mono">{element.bpmnElementId}</span>
                </td>
                <td>{element.elementType || '-'}</td>
                <td>{kei.id}</td>
                <td>{kei.unit}</td>
                <td>{kei.targetValue || <span className="badge neutral">No target</span>}</td>
                <td>{kei.icon || '-'}</td>
              </tr>
            )),
          )}
        </tbody>
      </table>
    </div>
  );
}

type OperationSummaryField = [label: string, value: string | number, mono?: boolean];

type OperationSummaryProps = {
  title: string;
  status: string;
  fields: OperationSummaryField[];
};

function OperationSummary({ title, status, fields }: OperationSummaryProps) {
  return (
    <section className="operation-summary">
      <header>
        <h4>{title}</h4>
        <StatusBadge value={status} />
      </header>
      <dl>
        {fields.map(([label, value, mono]) => (
          <div key={label}>
            <dt>{label}</dt>
            <dd className={mono ? 'mono' : undefined}>{value}</dd>
          </div>
        ))}
      </dl>
    </section>
  );
}
