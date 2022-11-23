import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CallupComponent } from '../list/callup.component';
import { CallupDetailComponent } from '../detail/callup-detail.component';
import { CallupUpdateComponent } from '../update/callup-update.component';
import { CallupRoutingResolveService } from './callup-routing-resolve.service';

const callupRoute: Routes = [
  {
    path: '',
    component: CallupComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CallupDetailComponent,
    resolve: {
      callup: CallupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CallupUpdateComponent,
    resolve: {
      callup: CallupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CallupUpdateComponent,
    resolve: {
      callup: CallupRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(callupRoute)],
  exports: [RouterModule],
})
export class CallupRoutingModule {}
