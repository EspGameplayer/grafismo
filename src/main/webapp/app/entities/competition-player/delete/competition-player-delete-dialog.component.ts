import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompetitionPlayer } from '../competition-player.model';
import { CompetitionPlayerService } from '../service/competition-player.service';

@Component({
  templateUrl: './competition-player-delete-dialog.component.html',
})
export class CompetitionPlayerDeleteDialogComponent {
  competitionPlayer?: ICompetitionPlayer;

  constructor(protected competitionPlayerService: CompetitionPlayerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.competitionPlayerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
