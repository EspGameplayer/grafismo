import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStadium, Stadium } from '../stadium.model';
import { StadiumService } from '../service/stadium.service';

@Injectable({ providedIn: 'root' })
export class StadiumRoutingResolveService implements Resolve<IStadium> {
  constructor(protected service: StadiumService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStadium> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((stadium: HttpResponse<Stadium>) => {
          if (stadium.body) {
            return of(stadium.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Stadium());
  }
}
