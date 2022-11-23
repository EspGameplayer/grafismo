import dayjs from 'dayjs/esm';
import { ICountry } from 'app/entities/country/country.model';

export interface IPerson {
  id?: number;
  name?: string | null;
  surname1?: string | null;
  surname2?: string | null;
  nickname?: string | null;
  graphicsName?: string;
  callname?: string | null;
  birthdate?: dayjs.Dayjs | null;
  nationality?: ICountry | null;
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public name?: string | null,
    public surname1?: string | null,
    public surname2?: string | null,
    public nickname?: string | null,
    public graphicsName?: string,
    public callname?: string | null,
    public birthdate?: dayjs.Dayjs | null,
    public nationality?: ICountry | null
  ) {}
}

export function getPersonIdentifier(person: IPerson): number | undefined {
  return person.id;
}
