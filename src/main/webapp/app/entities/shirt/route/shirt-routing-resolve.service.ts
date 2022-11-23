import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IShirt, Shirt } from '../shirt.model';
import { ShirtService } from '../service/shirt.service';

@Injectable({ providedIn: 'root' })
export class ShirtRoutingResolveService implements Resolve<IShirt> {
  constructor(protected service: ShirtService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IShirt> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((shirt: HttpResponse<Shirt>) => {
          if (shirt.body) {
            return of(shirt.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Shirt());
  }
}
