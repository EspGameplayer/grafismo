import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILineup } from '../lineup.model';
import { LineupService } from '../service/lineup.service';

@Component({
  templateUrl: './lineup-delete-dialog.component.html',
})
export class LineupDeleteDialogComponent {
  lineup?: ILineup;

  constructor(protected lineupService: LineupService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.lineupService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
