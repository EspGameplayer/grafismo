import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { LineupComponent } from '../list/lineup.component';
import { LineupDetailComponent } from '../detail/lineup-detail.component';
import { LineupUpdateComponent } from '../update/lineup-update.component';
import { LineupRoutingResolveService } from './lineup-routing-resolve.service';

const lineupRoute: Routes = [
  {
    path: '',
    component: LineupComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: LineupDetailComponent,
    resolve: {
      lineup: LineupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: LineupUpdateComponent,
    resolve: {
      lineup: LineupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: LineupUpdateComponent,
    resolve: {
      lineup: LineupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(lineupRoute)],
  exports: [RouterModule],
})
export class LineupRoutingModule {}
