import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILineup, getLineupIdentifier } from '../lineup.model';

export type EntityResponseType = HttpResponse<ILineup>;
export type EntityArrayResponseType = HttpResponse<ILineup[]>;

@Injectable({ providedIn: 'root' })
export class LineupService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/lineups');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(lineup: ILineup): Observable<EntityResponseType> {
    return this.http.post<ILineup>(this.resourceUrl, lineup, { observe: 'response' });
  }

  update(lineup: ILineup): Observable<EntityResponseType> {
    return this.http.put<ILineup>(`${this.resourceUrl}/${getLineupIdentifier(lineup) as number}`, lineup, { observe: 'response' });
  }

  partialUpdate(lineup: ILineup): Observable<EntityResponseType> {
    return this.http.patch<ILineup>(`${this.resourceUrl}/${getLineupIdentifier(lineup) as number}`, lineup, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILineup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILineup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLineupToCollectionIfMissing(lineupCollection: ILineup[], ...lineupsToCheck: (ILineup | null | undefined)[]): ILineup[] {
    const lineups: ILineup[] = lineupsToCheck.filter(isPresent);
    if (lineups.length > 0) {
      const lineupCollectionIdentifiers = lineupCollection.map(lineupItem => getLineupIdentifier(lineupItem)!);
      const lineupsToAdd = lineups.filter(lineupItem => {
        const lineupIdentifier = getLineupIdentifier(lineupItem);
        if (lineupIdentifier == null || lineupCollectionIdentifiers.includes(lineupIdentifier)) {
          return false;
        }
        lineupCollectionIdentifiers.push(lineupIdentifier);
        return true;
      });
      return [...lineupsToAdd, ...lineupCollection];
    }
    return lineupCollection;
  }
}
