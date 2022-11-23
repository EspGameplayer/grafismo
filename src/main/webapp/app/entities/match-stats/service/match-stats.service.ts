import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMatchStats, getMatchStatsIdentifier } from '../match-stats.model';

export type EntityResponseType = HttpResponse<IMatchStats>;
export type EntityArrayResponseType = HttpResponse<IMatchStats[]>;

@Injectable({ providedIn: 'root' })
export class MatchStatsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/match-stats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(matchStats: IMatchStats): Observable<EntityResponseType> {
    return this.http.post<IMatchStats>(this.resourceUrl, matchStats, { observe: 'response' });
  }

  update(matchStats: IMatchStats): Observable<EntityResponseType> {
    return this.http.put<IMatchStats>(`${this.resourceUrl}/${getMatchStatsIdentifier(matchStats) as number}`, matchStats, {
      observe: 'response',
    });
  }

  partialUpdate(matchStats: IMatchStats): Observable<EntityResponseType> {
    return this.http.patch<IMatchStats>(`${this.resourceUrl}/${getMatchStatsIdentifier(matchStats) as number}`, matchStats, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMatchStats>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMatchStats[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMatchStatsToCollectionIfMissing(
    matchStatsCollection: IMatchStats[],
    ...matchStatsToCheck: (IMatchStats | null | undefined)[]
  ): IMatchStats[] {
    const matchStats: IMatchStats[] = matchStatsToCheck.filter(isPresent);
    if (matchStats.length > 0) {
      const matchStatsCollectionIdentifiers = matchStatsCollection.map(matchStatsItem => getMatchStatsIdentifier(matchStatsItem)!);
      const matchStatsToAdd = matchStats.filter(matchStatsItem => {
        const matchStatsIdentifier = getMatchStatsIdentifier(matchStatsItem);
        if (matchStatsIdentifier == null || matchStatsCollectionIdentifiers.includes(matchStatsIdentifier)) {
          return false;
        }
        matchStatsCollectionIdentifiers.push(matchStatsIdentifier);
        return true;
      });
      return [...matchStatsToAdd, ...matchStatsCollection];
    }
    return matchStatsCollection;
  }
}
