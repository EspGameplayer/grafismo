export interface ISponsor {
  id?: number;
  name?: string;
  logoContentType?: string | null;
  logo?: string | null;
}

export class Sponsor implements ISponsor {
  constructor(public id?: number, public name?: string, public logoContentType?: string | null, public logo?: string | null) {}
}

export function getSponsorIdentifier(sponsor: ISponsor): number | undefined {
  return sponsor.id;
}
