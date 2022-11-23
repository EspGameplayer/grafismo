import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMatchPlayer } from '../match-player.model';
import { MatchPlayerService } from '../service/match-player.service';

@Component({
  templateUrl: './match-player-delete-dialog.component.html',
})
export class MatchPlayerDeleteDialogComponent {
  matchPlayer?: IMatchPlayer;

  constructor(protected matchPlayerService: MatchPlayerService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.matchPlayerService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
