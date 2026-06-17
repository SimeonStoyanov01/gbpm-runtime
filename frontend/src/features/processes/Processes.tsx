import { useState } from 'react';
import { deployProcess, findProcessKeis, startProcessInstance } from '../../api/processRegistryApi';
import type {
  DeployProcessResponse,
  ElementKeiAnnotations,
  FindProcessKeisResponse,
  StartProcessInstanceResponse,
} from '../../api/types';
import { Endpoint } from '../../components/Endpoint';
import { Panel } from '../../components/Panel';
import { StatusBadge } from '../../components/StatusBadge';

type ProcessesProps = {
  onDeployment: (deployment: DeployProcessResponse) => void;
};

export function Processes({ onDeployment }: ProcessesProps) {
  const [deployment, setDeployment] = useState<DeployProcessResponse>();
  const [processDefinitionKey, setProcessDefinitionKey] = useState('');
  const [variablesJson, setVariablesJson] = useState('{}');
  const [startedInstance, setStartedInstance] = useState<StartProcessInstanceResponse>();
  const [keiResponse, setKeiResponse] = useState<FindProcessKeisResponse>();
  const [error, setError] = useState<string>();
  const [busy, setBusy] = useState(false);

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
      onDeployment(response);
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

  return (
    <section id="processes">
      <h2 className="section-title">Processes</h2>
      {error && <p className="error">{error}</p>}

      <div className="grid-2">
        <Panel title="Deploy BPMN" action={<Endpoint>POST :8080 /api/process-definitions/deploy</Endpoint>}>
          <DeployForm disabled={busy} onDeploy={handleDeploy} />
          {deployment && (
            <div className="result-box">
              deploymentKey: "{deployment.deploymentKey}"<br />
              processDefinitionKey: "{deployment.processDefinitionKey}"<br />
              bpmnProcessId: "{deployment.bpmnProcessId}"<br />
              version: {deployment.version}<br />
              resourceName: "{deployment.resourceName}"<br />
              status: "{deployment.status}"
            </div>
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
            <div className="result-box">
              processDefinitionKey: "{startedInstance.processDefinitionKey}"<br />
              bpmnProcessId: "{startedInstance.bpmnProcessId}"<br />
              version: {startedInstance.version}<br />
              processInstanceKey: "{startedInstance.processInstanceKey}"<br />
              status: "{startedInstance.status}"
            </div>
          )}
        </Panel>
      </div>

      <Panel title="KEI Annotations" action={<Endpoint>GET :8080 /api/process-definitions/{'{key}'}/keis</Endpoint>}>
        <div className="button-row" style={{ marginTop: 0, marginBottom: 16 }}>
          <button className="secondary" disabled={!processDefinitionKey || busy} onClick={handleFindKeis}>Refresh KEIs</button>
          {keiResponse && <StatusBadge value={`KEY ${keiResponse.processDefinitionKey}`} />}
        </div>
        <KeiTable elements={keiResponse?.bpmn4esElementKeiAnnotations || []} />
        <p className="hint">KEI metadata is read from the process-registry/root backend.</p>
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
            <th>BPMN Element ID</th>
            <th>KEI ID</th>
            <th>Unit</th>
            <th>Target Value</th>
            <th>Icon</th>
          </tr>
        </thead>
        <tbody>
          {elements.flatMap((element) =>
            element.bpmn4esKeiAnnotations.map((kei) => (
              <tr key={`${element.bpmnElementId}-${kei.id}`}>
                <td>{element.bpmnElementId}</td>
                <td>{kei.id}</td>
                <td>{kei.unit}</td>
                <td>{kei.targetValue}</td>
                <td>{kei.icon || '-'}</td>
              </tr>
            )),
          )}
        </tbody>
      </table>
    </div>
  );
}
