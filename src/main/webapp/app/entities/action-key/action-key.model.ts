export interface IActionKey {
  id?: number;
  action?: string | null;
  keys?: string | null;
}

export class ActionKey implements IActionKey {
  constructor(public id?: number, public action?: string | null, public keys?: string | null) {}
}

export function getActionKeyIdentifier(actionKey: IActionKey): number | undefined {
  return actionKey.id;
}
