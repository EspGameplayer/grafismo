import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ILocalAssociationProvince, getLocalAssociationProvinceIdentifier } from '../local-association-province.model';

export type EntityResponseType = HttpResponse<ILocalAssociationProvince>;
export type EntityArrayResponseType = HttpResponse<ILocalAssociationProvince[]>;

@Injectable({ providedIn: 'root' })
export class LocalAssociationProvinceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/local-association-provinces');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(localAssociationProvince: ILocalAssociationProvince): Observable<EntityResponseType> {
    return this.http.post<ILocalAssociationProvince>(this.resourceUrl, localAssociationProvince, { observe: 'response' });
  }

  update(localAssociationProvince: ILocalAssociationProvince): Observable<EntityResponseType> {
    return this.http.put<ILocalAssociationProvince>(
      `${this.resourceUrl}/${getLocalAssociationProvinceIdentifier(localAssociationProvince) as number}`,
      localAssociationProvince,
      { observe: 'response' }
    );
  }

  partialUpdate(localAssociationProvince: ILocalAssociationProvince): Observable<EntityResponseType> {
    return this.http.patch<ILocalAssociationProvince>(
      `${this.resourceUrl}/${getLocalAssociationProvinceIdentifier(localAssociationProvince) as number}`,
      localAssociationProvince,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ILocalAssociationProvince>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ILocalAssociationProvince[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addLocalAssociationProvinceToCollectionIfMissing(
    localAssociationProvinceCollection: ILocalAssociationProvince[],
    ...localAssociationProvincesToCheck: (ILocalAssociationProvince | null | undefined)[]
  ): ILocalAssociationProvince[] {
    const localAssociationProvinces: ILocalAssociationProvince[] = localAssociationProvincesToCheck.filter(isPresent);
    if (localAssociationProvinces.length > 0) {
      const localAssociationProvinceCollectionIdentifiers = localAssociationProvinceCollection.map(
        localAssociationProvinceItem => getLocalAssociationProvinceIdentifier(localAssociationProvinceItem)!
      );
      const localAssociationProvincesToAdd = localAssociationProvinces.filter(localAssociationProvinceItem => {
        const localAssociationProvinceIdentifier = getLocalAssociationProvinceIdentifier(localAssociationProvinceItem);
        if (
          localAssociationProvinceIdentifier == null ||
          localAssociationProvinceCollectionIdentifiers.includes(localAssociationProvinceIdentifier)
        ) {
          return false;
        }
        localAssociationProvinceCollectionIdentifiers.push(localAssociationProvinceIdentifier);
        return true;
      });
      return [...localAssociationProvincesToAdd, ...localAssociationProvinceCollection];
    }
    return localAssociationProvinceCollection;
  }
}
