export interface IAssociation {
  id?: number;
  name?: string;
}

export class Association implements IAssociation {
  constructor(public id?: number, public name?: string) {}
}

export function getAssociationIdentifier(association: IAssociation): number | undefined {
  return association.id;
}
