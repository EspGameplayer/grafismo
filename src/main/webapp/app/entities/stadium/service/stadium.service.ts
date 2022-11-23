import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStadium, getStadiumIdentifier } from '../stadium.model';

export type EntityResponseType = HttpResponse<IStadium>;
export type EntityArrayResponseType = HttpResponse<IStadium[]>;

@Injectable({ providedIn: 'root' })
export class StadiumService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/stadiums');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(stadium: IStadium): Observable<EntityResponseType> {
    return this.http.post<IStadium>(this.resourceUrl, stadium, { observe: 'response' });
  }

  update(stadium: IStadium): Observable<EntityResponseType> {
    return this.http.put<IStadium>(`${this.resourceUrl}/${getStadiumIdentifier(stadium) as number}`, stadium, { observe: 'response' });
  }

  partialUpdate(stadium: IStadium): Observable<EntityResponseType> {
    return this.http.patch<IStadium>(`${this.resourceUrl}/${getStadiumIdentifier(stadium) as number}`, stadium, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStadium>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStadium[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStadiumToCollectionIfMissing(stadiumCollection: IStadium[], ...stadiumsToCheck: (IStadium | null | undefined)[]): IStadium[] {
    const stadiums: IStadium[] = stadiumsToCheck.filter(isPresent);
    if (stadiums.length > 0) {
      const stadiumCollectionIdentifiers = stadiumCollection.map(stadiumItem => getStadiumIdentifier(stadiumItem)!);
      const stadiumsToAdd = stadiums.filter(stadiumItem => {
        const stadiumIdentifier = getStadiumIdentifier(stadiumItem);
        if (stadiumIdentifier == null || stadiumCollectionIdentifiers.includes(stadiumIdentifier)) {
          return false;
        }
        stadiumCollectionIdentifiers.push(stadiumIdentifier);
        return true;
      });
      return [...stadiumsToAdd, ...stadiumCollection];
    }
    return stadiumCollection;
  }
}
