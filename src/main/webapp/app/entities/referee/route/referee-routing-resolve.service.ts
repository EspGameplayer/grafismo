import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReferee, Referee } from '../referee.model';
import { RefereeService } from '../service/referee.service';

@Injectable({ providedIn: 'root' })
export class RefereeRoutingResolveService implements Resolve<IReferee> {
  constructor(protected service: RefereeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReferee> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((referee: HttpResponse<Referee>) => {
          if (referee.body) {
            return of(referee.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Referee());
  }
}
