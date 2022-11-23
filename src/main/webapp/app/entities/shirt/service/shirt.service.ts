import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IShirt, getShirtIdentifier } from '../shirt.model';

export type EntityResponseType = HttpResponse<IShirt>;
export type EntityArrayResponseType = HttpResponse<IShirt[]>;

@Injectable({ providedIn: 'root' })
export class ShirtService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/shirts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(shirt: IShirt): Observable<EntityResponseType> {
    return this.http.post<IShirt>(this.resourceUrl, shirt, { observe: 'response' });
  }

  update(shirt: IShirt): Observable<EntityResponseType> {
    return this.http.put<IShirt>(`${this.resourceUrl}/${getShirtIdentifier(shirt) as number}`, shirt, { observe: 'response' });
  }

  partialUpdate(shirt: IShirt): Observable<EntityResponseType> {
    return this.http.patch<IShirt>(`${this.resourceUrl}/${getShirtIdentifier(shirt) as number}`, shirt, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IShirt>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IShirt[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addShirtToCollectionIfMissing(shirtCollection: IShirt[], ...shirtsToCheck: (IShirt | null | undefined)[]): IShirt[] {
    const shirts: IShirt[] = shirtsToCheck.filter(isPresent);
    if (shirts.length > 0) {
      const shirtCollectionIdentifiers = shirtCollection.map(shirtItem => getShirtIdentifier(shirtItem)!);
      const shirtsToAdd = shirts.filter(shirtItem => {
        const shirtIdentifier = getShirtIdentifier(shirtItem);
        if (shirtIdentifier == null || shirtCollectionIdentifiers.includes(shirtIdentifier)) {
          return false;
        }
        shirtCollectionIdentifiers.push(shirtIdentifier);
        return true;
      });
      return [...shirtsToAdd, ...shirtCollection];
    }
    return shirtCollection;
  }
}
