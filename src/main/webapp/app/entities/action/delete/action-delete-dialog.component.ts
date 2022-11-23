import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAction } from '../action.model';
import { ActionService } from '../service/action.service';

@Component({
  templateUrl: './action-delete-dialog.component.html',
})
export class ActionDeleteDialogComponent {
  action?: IAction;

  constructor(protected actionService: ActionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.actionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
