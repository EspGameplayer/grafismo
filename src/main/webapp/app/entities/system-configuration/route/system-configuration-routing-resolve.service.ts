import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISystemConfiguration, SystemConfiguration } from '../system-configuration.model';
import { SystemConfigurationService } from '../service/system-configuration.service';

@Injectable({ providedIn: 'root' })
export class SystemConfigurationRoutingResolveService implements Resolve<ISystemConfiguration> {
  constructor(protected service: SystemConfigurationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISystemConfiguration> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((systemConfiguration: HttpResponse<SystemConfiguration>) => {
          if (systemConfiguration.body) {
            return of(systemConfiguration.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SystemConfiguration());
  }
}
