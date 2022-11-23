import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BroadcastStaffMemberComponent } from './list/broadcast-staff-member.component';
import { BroadcastStaffMemberDetailComponent } from './detail/broadcast-staff-member-detail.component';
import { BroadcastStaffMemberUpdateComponent } from './update/broadcast-staff-member-update.component';
import { BroadcastStaffMemberDeleteDialogComponent } from './delete/broadcast-staff-member-delete-dialog.component';
import { BroadcastStaffMemberRoutingModule } from './route/broadcast-staff-member-routing.module';

@NgModule({
  imports: [SharedModule, BroadcastStaffMemberRoutingModule],
  declarations: [
    BroadcastStaffMemberComponent,
    BroadcastStaffMemberDetailComponent,
    BroadcastStaffMemberUpdateComponent,
    BroadcastStaffMemberDeleteDialogComponent,
  ],
  entryComponents: [BroadcastStaffMemberDeleteDialogComponent],
})
export class BroadcastStaffMemberModule {}
