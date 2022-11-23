import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InjuryComponent } from '../list/injury.component';
import { InjuryDetailComponent } from '../detail/injury-detail.component';
import { InjuryUpdateComponent } from '../update/injury-update.component';
import { InjuryRoutingResolveService } from './injury-routing-resolve.service';

const injuryRoute: Routes = [
  {
    path: '',
    component: InjuryComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InjuryDetailComponent,
    resolve: {
      injury: InjuryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InjuryUpdateComponent,
    resolve: {
      injury: InjuryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InjuryUpdateComponent,
    resolve: {
      injury: InjuryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(injuryRoute)],
  exports: [RouterModule],
})
export class InjuryRoutingModule {}
