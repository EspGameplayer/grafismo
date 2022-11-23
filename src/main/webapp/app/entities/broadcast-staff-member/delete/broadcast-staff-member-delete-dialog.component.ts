import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBroadcastStaffMember } from '../broadcast-staff-member.model';
import { BroadcastStaffMemberService } from '../service/broadcast-staff-member.service';

@Component({
  templateUrl: './broadcast-staff-member-delete-dialog.component.html',
})
export class BroadcastStaffMemberDeleteDialogComponent {
  broadcastStaffMember?: IBroadcastStaffMember;

  constructor(protected broadcastStaffMemberService: BroadcastStaffMemberService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.broadcastStaffMemberService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
