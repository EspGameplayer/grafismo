export interface IFormation {
  id?: number;
  name?: string;
  distribution?: string | null;
}

export class Formation implements IFormation {
  constructor(public id?: number, public name?: string, public distribution?: string | null) {}
}

export function getFormationIdentifier(formation: IFormation): number | undefined {
  return formation.id;
}
