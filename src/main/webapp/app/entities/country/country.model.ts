export interface ICountry {
  id?: number;
  name?: string;
  abb?: string;
  flagContentType?: string | null;
  flag?: string | null;
}

export class Country implements ICountry {
  constructor(
    public id?: number,
    public name?: string,
    public abb?: string,
    public flagContentType?: string | null,
    public flag?: string | null
  ) {}
}

export function getCountryIdentifier(country: ICountry): number | undefined {
  return country.id;
}
