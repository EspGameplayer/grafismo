import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITeamStaffMember } from '../team-staff-member.model';
import { TeamStaffMemberService } from '../service/team-staff-member.service';

@Component({
  templateUrl: './team-staff-member-delete-dialog.component.html',
})
export class TeamStaffMemberDeleteDialogComponent {
  teamStaffMember?: ITeamStaffMember;

  constructor(protected teamStaffMemberService: TeamStaffMemberService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.teamStaffMemberService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
