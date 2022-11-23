import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TeamStaffMemberComponent } from '../list/team-staff-member.component';
import { TeamStaffMemberDetailComponent } from '../detail/team-staff-member-detail.component';
import { TeamStaffMemberUpdateComponent } from '../update/team-staff-member-update.component';
import { TeamStaffMemberRoutingResolveService } from './team-staff-member-routing-resolve.service';

const teamStaffMemberRoute: Routes = [
  {
    path: '',
    component: TeamStaffMemberComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TeamStaffMemberDetailComponent,
    resolve: {
      teamStaffMember: TeamStaffMemberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TeamStaffMemberUpdateComponent,
    resolve: {
      teamStaffMember: TeamStaffMemberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TeamStaffMemberUpdateComponent,
    resolve: {
      teamStaffMember: TeamStaffMemberRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(teamStaffMemberRoute)],
  exports: [RouterModule],
})
export class TeamStaffMemberRoutingModule {}
