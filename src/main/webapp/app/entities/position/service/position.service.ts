import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPosition, getPositionIdentifier } from '../position.model';

export type EntityResponseType = HttpResponse<IPosition>;
export type EntityArrayResponseType = HttpResponse<IPosition[]>;

@Injectable({ providedIn: 'root' })
export class PositionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/positions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(position: IPosition): Observable<EntityResponseType> {
    return this.http.post<IPosition>(this.resourceUrl, position, { observe: 'response' });
  }

  update(position: IPosition): Observable<EntityResponseType> {
    return this.http.put<IPosition>(`${this.resourceUrl}/${getPositionIdentifier(position) as number}`, position, { observe: 'response' });
  }

  partialUpdate(position: IPosition): Observable<EntityResponseType> {
    return this.http.patch<IPosition>(`${this.resourceUrl}/${getPositionIdentifier(position) as number}`, position, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPosition>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPosition[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPositionToCollectionIfMissing(positionCollection: IPosition[], ...positionsToCheck: (IPosition | null | undefined)[]): IPosition[] {
    const positions: IPosition[] = positionsToCheck.filter(isPresent);
    if (positions.length > 0) {
      const positionCollectionIdentifiers = positionCollection.map(positionItem => getPositionIdentifier(positionItem)!);
      const positionsToAdd = positions.filter(positionItem => {
        const positionIdentifier = getPositionIdentifier(positionItem);
        if (positionIdentifier == null || positionCollectionIdentifiers.includes(positionIdentifier)) {
          return false;
        }
        positionCollectionIdentifiers.push(positionIdentifier);
        return true;
      });
      return [...positionsToAdd, ...positionCollection];
    }
    return positionCollection;
  }
}
