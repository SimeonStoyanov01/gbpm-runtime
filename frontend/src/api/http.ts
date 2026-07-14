export async function requestJson<T>(url: string, init?: RequestInit): Promise<T> {
  const response = await fetch(url, init);
  await requireSuccessfulResponse(response);
  return response.json() as Promise<T>;
}

export async function requestVoid(url: string, init?: RequestInit): Promise<void> {
  const response = await fetch(url, init);
  await requireSuccessfulResponse(response);
}

export function queryString(params: Record<string, string | undefined>): string {
  const searchParams = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value && value.trim()) {
      searchParams.set(key, value.trim());
    }
  });

  const serialized = searchParams.toString();
  return serialized ? `?${serialized}` : '';
}

async function requireSuccessfulResponse(response: Response): Promise<void> {
  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || `${response.status} ${response.statusText}`);
  }
}
