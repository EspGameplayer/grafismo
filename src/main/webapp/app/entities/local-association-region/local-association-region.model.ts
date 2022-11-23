import { IAssociation } from 'app/entities/association/association.model';

export interface ILocalAssociationRegion {
  id?: number;
  name?: string;
  association?: IAssociation | null;
}

export class LocalAssociationRegion implements ILocalAssociationRegion {
  constructor(public id?: number, public name?: string, public association?: IAssociation | null) {}
}

export function getLocalAssociationRegionIdentifier(localAssociationRegion: ILocalAssociationRegion): number | undefined {
  return localAssociationRegion.id;
}
