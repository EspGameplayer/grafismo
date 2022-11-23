import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompetition, getCompetitionIdentifier } from '../competition.model';

export type EntityResponseType = HttpResponse<ICompetition>;
export type EntityArrayResponseType = HttpResponse<ICompetition[]>;

@Injectable({ providedIn: 'root' })
export class CompetitionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/competitions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(competition: ICompetition): Observable<EntityResponseType> {
    return this.http.post<ICompetition>(this.resourceUrl, competition, { observe: 'response' });
  }

  update(competition: ICompetition): Observable<EntityResponseType> {
    return this.http.put<ICompetition>(`${this.resourceUrl}/${getCompetitionIdentifier(competition) as number}`, competition, {
      observe: 'response',
    });
  }

  partialUpdate(competition: ICompetition): Observable<EntityResponseType> {
    return this.http.patch<ICompetition>(`${this.resourceUrl}/${getCompetitionIdentifier(competition) as number}`, competition, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICompetition>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompetition[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCompetitionToCollectionIfMissing(
    competitionCollection: ICompetition[],
    ...competitionsToCheck: (ICompetition | null | undefined)[]
  ): ICompetition[] {
    const competitions: ICompetition[] = competitionsToCheck.filter(isPresent);
    if (competitions.length > 0) {
      const competitionCollectionIdentifiers = competitionCollection.map(competitionItem => getCompetitionIdentifier(competitionItem)!);
      const competitionsToAdd = competitions.filter(competitionItem => {
        const competitionIdentifier = getCompetitionIdentifier(competitionItem);
        if (competitionIdentifier == null || competitionCollectionIdentifiers.includes(competitionIdentifier)) {
          return false;
        }
        competitionCollectionIdentifiers.push(competitionIdentifier);
        return true;
      });
      return [...competitionsToAdd, ...competitionCollection];
    }
    return competitionCollection;
  }
}
