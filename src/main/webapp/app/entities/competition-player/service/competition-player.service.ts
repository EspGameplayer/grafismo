import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompetitionPlayer, getCompetitionPlayerIdentifier } from '../competition-player.model';

export type EntityResponseType = HttpResponse<ICompetitionPlayer>;
export type EntityArrayResponseType = HttpResponse<ICompetitionPlayer[]>;

@Injectable({ providedIn: 'root' })
export class CompetitionPlayerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/competition-players');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(competitionPlayer: ICompetitionPlayer): Observable<EntityResponseType> {
    return this.http.post<ICompetitionPlayer>(this.resourceUrl, competitionPlayer, { observe: 'response' });
  }

  update(competitionPlayer: ICompetitionPlayer): Observable<EntityResponseType> {
    return this.http.put<ICompetitionPlayer>(
      `${this.resourceUrl}/${getCompetitionPlayerIdentifier(competitionPlayer) as number}`,
      competitionPlayer,
      { observe: 'response' }
    );
  }

  partialUpdate(competitionPlayer: ICompetitionPlayer): Observable<EntityResponseType> {
    return this.http.patch<ICompetitionPlayer>(
      `${this.resourceUrl}/${getCompetitionPlayerIdentifier(competitionPlayer) as number}`,
      competitionPlayer,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompetitionPlayer>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompetitionPlayer[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCompetitionPlayerToCollectionIfMissing(
    competitionPlayerCollection: ICompetitionPlayer[],
    ...competitionPlayersToCheck: (ICompetitionPlayer | null | undefined)[]
  ): ICompetitionPlayer[] {
    const competitionPlayers: ICompetitionPlayer[] = competitionPlayersToCheck.filter(isPresent);
    if (competitionPlayers.length > 0) {
      const competitionPlayerCollectionIdentifiers = competitionPlayerCollection.map(
        competitionPlayerItem => getCompetitionPlayerIdentifier(competitionPlayerItem)!
      );
      const competitionPlayersToAdd = competitionPlayers.filter(competitionPlayerItem => {
        const competitionPlayerIdentifier = getCompetitionPlayerIdentifier(competitionPlayerItem);
        if (competitionPlayerIdentifier == null || competitionPlayerCollectionIdentifiers.includes(competitionPlayerIdentifier)) {
          return false;
        }
        competitionPlayerCollectionIdentifiers.push(competitionPlayerIdentifier);
        return true;
      });
      return [...competitionPlayersToAdd, ...competitionPlayerCollection];
    }
    return competitionPlayerCollection;
  }
}
