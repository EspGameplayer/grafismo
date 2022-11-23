import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICallup } from '../callup.model';
import { CallupService } from '../service/callup.service';

@Component({
  templateUrl: './callup-delete-dialog.component.html',
})
export class CallupDeleteDialogComponent {
  callup?: ICallup;

  constructor(protected callupService: CallupService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.callupService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
