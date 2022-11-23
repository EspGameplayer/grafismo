import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ActionKeyComponent } from '../list/action-key.component';
import { ActionKeyDetailComponent } from '../detail/action-key-detail.component';
import { ActionKeyUpdateComponent } from '../update/action-key-update.component';
import { ActionKeyRoutingResolveService } from './action-key-routing-resolve.service';

const actionKeyRoute: Routes = [
  {
    path: '',
    component: ActionKeyComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ActionKeyDetailComponent,
    resolve: {
      actionKey: ActionKeyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ActionKeyUpdateComponent,
    resolve: {
      actionKey: ActionKeyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ActionKeyUpdateComponent,
    resolve: {
      actionKey: ActionKeyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(actionKeyRoute)],
  exports: [RouterModule],
})
export class ActionKeyRoutingModule {}
