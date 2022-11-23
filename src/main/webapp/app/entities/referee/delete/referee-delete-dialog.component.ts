import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReferee } from '../referee.model';
import { RefereeService } from '../service/referee.service';

@Component({
  templateUrl: './referee-delete-dialog.component.html',
})
export class RefereeDeleteDialogComponent {
  referee?: IReferee;

  constructor(protected refereeService: RefereeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.refereeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
