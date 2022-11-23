import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMatchday } from '../matchday.model';
import { MatchdayService } from '../service/matchday.service';

@Component({
  templateUrl: './matchday-delete-dialog.component.html',
})
export class MatchdayDeleteDialogComponent {
  matchday?: IMatchday;

  constructor(protected matchdayService: MatchdayService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.matchdayService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
