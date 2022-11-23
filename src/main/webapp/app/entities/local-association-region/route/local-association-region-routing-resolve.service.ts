import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILocalAssociationRegion, LocalAssociationRegion } from '../local-association-region.model';
import { LocalAssociationRegionService } from '../service/local-association-region.service';

@Injectable({ providedIn: 'root' })
export class LocalAssociationRegionRoutingResolveService implements Resolve<ILocalAssociationRegion> {
  constructor(protected service: LocalAssociationRegionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILocalAssociationRegion> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((localAssociationRegion: HttpResponse<LocalAssociationRegion>) => {
          if (localAssociationRegion.body) {
            return of(localAssociationRegion.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new LocalAssociationRegion());
  }
}
