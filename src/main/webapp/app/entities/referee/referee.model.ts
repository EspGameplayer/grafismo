import { IPerson } from 'app/entities/person/person.model';
import { ILocalAssociationProvince } from 'app/entities/local-association-province/local-association-province.model';
import { IMatch } from 'app/entities/match/match.model';

export interface IReferee {
  id?: number;
  person?: IPerson;
  association?: ILocalAssociationProvince | null;
  matches?: IMatch[] | null;
}

export class Referee implements IReferee {
  constructor(
    public id?: number,
    public person?: IPerson,
    public association?: ILocalAssociationProvince | null,
    public matches?: IMatch[] | null
  ) {}
}

export function getRefereeIdentifier(referee: IReferee): number | undefined {
  return referee.id;
}
