import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TeamStaffMemberComponent } from './list/team-staff-member.component';
import { TeamStaffMemberDetailComponent } from './detail/team-staff-member-detail.component';
import { TeamStaffMemberUpdateComponent } from './update/team-staff-member-update.component';
import { TeamStaffMemberDeleteDialogComponent } from './delete/team-staff-member-delete-dialog.component';
import { TeamStaffMemberRoutingModule } from './route/team-staff-member-routing.module';

@NgModule({
  imports: [SharedModule, TeamStaffMemberRoutingModule],
  declarations: [
    TeamStaffMemberComponent,
    TeamStaffMemberDetailComponent,
    TeamStaffMemberUpdateComponent,
    TeamStaffMemberDeleteDialogComponent,
  ],
  entryComponents: [TeamStaffMemberDeleteDialogComponent],
})
export class TeamStaffMemberModule {}
