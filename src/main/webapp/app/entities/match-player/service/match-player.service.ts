import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMatchPlayer, getMatchPlayerIdentifier } from '../match-player.model';

export type EntityResponseType = HttpResponse<IMatchPlayer>;
export type EntityArrayResponseType = HttpResponse<IMatchPlayer[]>;

@Injectable({ providedIn: 'root' })
export class MatchPlayerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/match-players');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(matchPlayer: IMatchPlayer): Observable<EntityResponseType> {
    return this.http.post<IMatchPlayer>(this.resourceUrl, matchPlayer, { observe: 'response' });
  }

  update(matchPlayer: IMatchPlayer): Observable<EntityResponseType> {
    return this.http.put<IMatchPlayer>(`${this.resourceUrl}/${getMatchPlayerIdentifier(matchPlayer) as number}`, matchPlayer, {
      observe: 'response',
    });
  }

  partialUpdate(matchPlayer: IMatchPlayer): Observable<EntityResponseType> {
    return this.http.patch<IMatchPlayer>(`${this.resourceUrl}/${getMatchPlayerIdentifier(matchPlayer) as number}`, matchPlayer, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMatchPlayer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMatchPlayer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMatchPlayerToCollectionIfMissing(
    matchPlayerCollection: IMatchPlayer[],
    ...matchPlayersToCheck: (IMatchPlayer | null | undefined)[]
  ): IMatchPlayer[] {
    const matchPlayers: IMatchPlayer[] = matchPlayersToCheck.filter(isPresent);
    if (matchPlayers.length > 0) {
      const matchPlayerCollectionIdentifiers = matchPlayerCollection.map(matchPlayerItem => getMatchPlayerIdentifier(matchPlayerItem)!);
      const matchPlayersToAdd = matchPlayers.filter(matchPlayerItem => {
        const matchPlayerIdentifier = getMatchPlayerIdentifier(matchPlayerItem);
        if (matchPlayerIdentifier == null || matchPlayerCollectionIdentifiers.includes(matchPlayerIdentifier)) {
          return false;
        }
        matchPlayerCollectionIdentifiers.push(matchPlayerIdentifier);
        return true;
      });
      return [...matchPlayersToAdd, ...matchPlayerCollection];
    }
    return matchPlayerCollection;
  }
}
