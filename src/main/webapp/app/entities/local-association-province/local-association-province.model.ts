import { ILocalAssociationRegion } from 'app/entities/local-association-region/local-association-region.model';

export interface ILocalAssociationProvince {
  id?: number;
  name?: string;
  association?: ILocalAssociationRegion | null;
}

export class LocalAssociationProvince implements ILocalAssociationProvince {
  constructor(public id?: number, public name?: string, public association?: ILocalAssociationRegion | null) {}
}

export function getLocalAssociationProvinceIdentifier(localAssociationProvince: ILocalAssociationProvince): number | undefined {
  return localAssociationProvince.id;
}
