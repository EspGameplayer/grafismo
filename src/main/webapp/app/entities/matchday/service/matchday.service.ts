import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMatchday, getMatchdayIdentifier } from '../matchday.model';

export type EntityResponseType = HttpResponse<IMatchday>;
export type EntityArrayResponseType = HttpResponse<IMatchday[]>;

@Injectable({ providedIn: 'root' })
export class MatchdayService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/matchdays');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(matchday: IMatchday): Observable<EntityResponseType> {
    return this.http.post<IMatchday>(this.resourceUrl, matchday, { observe: 'response' });
  }

  update(matchday: IMatchday): Observable<EntityResponseType> {
    return this.http.put<IMatchday>(`${this.resourceUrl}/${getMatchdayIdentifier(matchday) as number}`, matchday, { observe: 'response' });
  }

  partialUpdate(matchday: IMatchday): Observable<EntityResponseType> {
    return this.http.patch<IMatchday>(`${this.resourceUrl}/${getMatchdayIdentifier(matchday) as number}`, matchday, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMatchday>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMatchday[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMatchdayToCollectionIfMissing(matchdayCollection: IMatchday[], ...matchdaysToCheck: (IMatchday | null | undefined)[]): IMatchday[] {
    const matchdays: IMatchday[] = matchdaysToCheck.filter(isPresent);
    if (matchdays.length > 0) {
      const matchdayCollectionIdentifiers = matchdayCollection.map(matchdayItem => getMatchdayIdentifier(matchdayItem)!);
      const matchdaysToAdd = matchdays.filter(matchdayItem => {
        const matchdayIdentifier = getMatchdayIdentifier(matchdayItem);
        if (matchdayIdentifier == null || matchdayCollectionIdentifiers.includes(matchdayIdentifier)) {
          return false;
        }
        matchdayCollectionIdentifiers.push(matchdayIdentifier);
        return true;
      });
      return [...matchdaysToAdd, ...matchdayCollection];
    }
    return matchdayCollection;
  }
}
