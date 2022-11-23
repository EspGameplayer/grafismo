import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInjury, Injury } from '../injury.model';
import { InjuryService } from '../service/injury.service';

@Injectable({ providedIn: 'root' })
export class InjuryRoutingResolveService implements Resolve<IInjury> {
  constructor(protected service: InjuryService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInjury> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((injury: HttpResponse<Injury>) => {
          if (injury.body) {
            return of(injury.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Injury());
  }
}
