import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { RefereeComponent } from '../list/referee.component';
import { RefereeDetailComponent } from '../detail/referee-detail.component';
import { RefereeUpdateComponent } from '../update/referee-update.component';
import { RefereeRoutingResolveService } from './referee-routing-resolve.service';

const refereeRoute: Routes = [
  {
    path: '',
    component: RefereeComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RefereeDetailComponent,
    resolve: {
      referee: RefereeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RefereeUpdateComponent,
    resolve: {
      referee: RefereeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RefereeUpdateComponent,
    resolve: {
      referee: RefereeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(refereeRoute)],
  exports: [RouterModule],
})
export class RefereeRoutingModule {}
