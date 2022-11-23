import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICallup, Callup } from '../callup.model';
import { CallupService } from '../service/callup.service';

@Injectable({ providedIn: 'root' })
export class CallupRoutingResolveService implements Resolve<ICallup> {
  constructor(protected service: CallupService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICallup> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((callup: HttpResponse<Callup>) => {
          if (callup.body) {
            return of(callup.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Callup());
  }
}
