import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILocalAssociationProvince, LocalAssociationProvince } from '../local-association-province.model';
import { LocalAssociationProvinceService } from '../service/local-association-province.service';

@Injectable({ providedIn: 'root' })
export class LocalAssociationProvinceRoutingResolveService implements Resolve<ILocalAssociationProvince> {
  constructor(protected service: LocalAssociationProvinceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILocalAssociationProvince> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((localAssociationProvince: HttpResponse<LocalAssociationProvince>) => {
          if (localAssociationProvince.body) {
            return of(localAssociationProvince.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LocalAssociationProvince());
  }
}
