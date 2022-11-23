import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IActionKey } from '../action-key.model';
import { ActionKeyService } from '../service/action-key.service';

@Component({
  templateUrl: './action-key-delete-dialog.component.html',
})
export class ActionKeyDeleteDialogComponent {
  actionKey?: IActionKey;

  constructor(protected actionKeyService: ActionKeyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.actionKeyService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
