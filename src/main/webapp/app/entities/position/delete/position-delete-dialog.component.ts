import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPosition } from '../position.model';
import { PositionService } from '../service/position.service';

@Component({
  templateUrl: './position-delete-dialog.component.html',
})
export class PositionDeleteDialogComponent {
  position?: IPosition;

  constructor(protected positionService: PositionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.positionService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
