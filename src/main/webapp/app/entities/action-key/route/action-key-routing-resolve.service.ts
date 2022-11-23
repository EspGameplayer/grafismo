import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IActionKey, ActionKey } from '../action-key.model';
import { ActionKeyService } from '../service/action-key.service';

@Injectable({ providedIn: 'root' })
export class ActionKeyRoutingResolveService implements Resolve<IActionKey> {
  constructor(protected service: ActionKeyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IActionKey> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((actionKey: HttpResponse<ActionKey>) => {
          if (actionKey.body) {
            return of(actionKey.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ActionKey());
  }
}
