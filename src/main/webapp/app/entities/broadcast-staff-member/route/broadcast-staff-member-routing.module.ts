import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BroadcastStaffMemberComponent } from '../list/broadcast-staff-member.component';
import { BroadcastStaffMemberDetailComponent } from '../detail/broadcast-staff-member-detail.component';
import { BroadcastStaffMemberUpdateComponent } from '../update/broadcast-staff-member-update.component';
import { BroadcastStaffMemberRoutingResolveService } from './broadcast-staff-member-routing-resolve.service';

const broadcastStaffMemberRoute: Routes = [
  {
    path: '',
    component: BroadcastStaffMemberComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BroadcastStaffMemberDetailComponent,
    resolve: {
      broadcastStaffMember: BroadcastStaffMemberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BroadcastStaffMemberUpdateComponent,
    resolve: {
      broadcastStaffMember: BroadcastStaffMemberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BroadcastStaffMemberUpdateComponent,
    resolve: {
      broadcastStaffMember: BroadcastStaffMemberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(broadcastStaffMemberRoute)],
  exports: [RouterModule],
})
export class BroadcastStaffMemberRoutingModule {}
